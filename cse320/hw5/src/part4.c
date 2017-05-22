#include "lott.h"
#include "utilities.h"

static void* map(void*);
static void* reduce(void*);

/* finds minimum or maximum based on find_max */
map_obj* min_max_towers_reducer(int find_max);
map_obj* country_towers_reducer();
void part4_results(void* v);

/* this is what will be printed */
float* end_result_tower = NULL;
char* end_file_tower = NULL;

// used to perform writers preference
int writecnt = 0;
pthread_mutex_t y;
pthread_mutex_t wsem;
pthread_mutex_t rsem;

/* shared buffer to store writers ouput */
char shared_buff[4096];
int buff_index = 0;

int part4(size_t nthreads){
    shared_buff[0] = '\0';
    /* call map, to fill in the values for each thread_node */
    thread_array* map_threads = create_thread_array(nthreads);
    /* map function, will be incharge */
    map(map_threads);
    /* create a reduce thread and runs */
    pthread_t reduce_thread = create_reduce_thread(reduce, map_threads);
    pthread_cleanup_push(part4_results, NULL);
    /* join all map threads, this is wrapper for pthread_join() */
    join_all_threads(map_threads);
    // allow reduce() to reduce all files
    usleep(1000000);
    usleep(1000000);
    usleep(1000000);
    pthread_cancel(reduce_thread);
    pthread_cleanup_pop(1);
    free(map_threads);
    return 0;
}

static void* map(void* v){
    /* set map_function */
    void* map_function = map_threads_towers;
    /* create thread list */
    thread_array* map_threads = (thread_array *)v;
    /* this helper will assign files evenly for each thread */
    divide_files(map_threads);
    /* ready for thread  */
    run_and_create_threads(map_threads, map_function);
    return map_threads;
}

/* */
void map_threads_towers(thread_obj* t) {
    result_obj* (*map_function)(map_obj*);
    switch(*QUERY_STRINGS[current_query]) {
        case 'A':
            map_function = map_duration;
            break;
        case 'B':
            map_function = map_duration;
            break;
        case 'C':
            map_function = map_users;
            break;
        case 'D':
            map_function = map_users;
            break;
        case 'E':
            map_function = map_country;
    }
    int curr_file = 0;
    while(curr_file < t -> num_files) {
        run_mapper_towers(*(t -> map_array + curr_file), map_function);
        ++curr_file;
    }
}

void run_mapper_towers(map_obj* curr_map, result_obj* (map_function)(map_obj*)) {
    result_obj* map_result = map_function(curr_map);
    ++writecnt;
    pthread_mutex_lock(&y);
    if(writecnt == 1)
        pthread_mutex_lock(&rsem);
    pthread_mutex_unlock(&y);
    pthread_mutex_lock(&wsem);
    // writing()
    sprintf(&shared_buff[buff_index], "%f,%s\n", map_result -> result_f, map_result -> result_str);
    buff_index += strlen(&shared_buff[buff_index]);
    shared_buff[buff_index+1] = '\0';
    //
    pthread_mutex_unlock(&wsem);
    pthread_mutex_lock(&y);
    --writecnt;
    if(writecnt == 0)
        pthread_mutex_unlock(&rsem);
    pthread_mutex_unlock(&y);
}

/* */
static void* reduce(void* v){
    /* set reducer function */
    switch(*QUERY_STRINGS[current_query]) {
        case 'A':
            min_max_towers_reducer(GET_MAX);            
            break;
        case 'B':
            min_max_towers_reducer(GET_MIN);            
            break;
        case 'C':
            min_max_towers_reducer(GET_MAX);            
            break;
        case 'D':
            min_max_towers_reducer(GET_MIN);            
            break;
        case 'E':
            country_towers_reducer();
            break;
    }
    return NULL;
}

size_t up_to_new_line(char* s) {
    size_t len = 0;
    while((*s != '\n')) {
        ++len;
        ++s;
    }
    return len;
}

/* finds minimum or maximum based on find_max */
map_obj* min_max_towers_reducer(int find_max) {
    while(1) {
        pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);
        // pthread_mutex_lock(&rsem);
        pthread_mutex_lock(&wsem);
        int index = 0;
        char* file_name = (char*)malloc(120);
        float val;
        while(sscanf(&shared_buff[index], "%f,%s", &val, file_name) != EOF) {
            if(end_result_tower == NULL) {
                end_result_tower = (float *)malloc(sizeof(float));
                *end_result_tower = val;
                end_file_tower = file_name;
            } else if((find_max == GET_MAX && val > *end_result_tower) || (find_max == GET_MIN && val < *end_result_tower)) {
                *end_result_tower = val;
                end_file_tower = file_name;
            } else if((val == *end_result_tower) && (strcmp(file_name, end_file_tower) < 0)) {
                *end_result_tower = val;
                end_file_tower = file_name;
            }
            file_name = (char*)malloc(120);
            index += up_to_new_line(&shared_buff[index]) + 1;//strlen(&shared_buff[index]);
        }
        buff_index = 0;
        // pthread_mutex_unlock(&rsem);
        pthread_mutex_unlock(&wsem);
        pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
        usleep(1000000);
    }
    return NULL;
}

map_obj* country_towers_reducer() {
    end_result_tower = (float *)malloc(sizeof(float));       
    /* list is used to mapping countries to occurences */
    country_obj_list* country_list = (country_obj_list *)malloc(sizeof(country_obj_list));
    country_list -> num_countries = 0;
    country_list -> head = NULL;
    float max_occur = -1;
    char* max_c_code = NULL;
    while(1) {
        pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);
        // pthread_mutex_lock(&rsem);
        pthread_mutex_lock(&wsem);
        int index = 0;
        char* c_code = (char*)malloc(120);
        float val;
        while(sscanf(&shared_buff[index], "%f,%s", &val, c_code) != EOF) {
            country_obj* c = get_country(country_list, c_code);
            if(c == NULL) {
                char* new_c_code = strdup(c_code);
                if((country_list -> head == NULL)) {
                    max_occur = val;
                    max_c_code = new_c_code;
                } else if(val > max_occur) {
                    max_occur = val;
                    max_c_code = new_c_code;
                } else if((val == max_occur) && (strcmp(new_c_code, max_c_code) < 0)) {
                    max_occur = val;
                    max_c_code = new_c_code;                
                }
                add_country(country_list, new_c_code, val);
            } else {
                c -> occurences += val;  // icrement occurences
                if(c -> occurences > max_occur) {
                    max_occur = c -> occurences;
                    max_c_code = c -> c_code;
                } else if((c -> occurences == max_occur) && (strcmp(c -> c_code, max_c_code) < 0)) {
                    max_c_code = c -> c_code;
                }
            }
            c_code = (char*)malloc(120);
            index += up_to_new_line(&shared_buff[index]) + 1;//strlen(&shared_buff[index]);
            if(country_list -> num_countries != 0) {
                end_file_tower = strdup(max_c_code);
                *end_result_tower = max_occur;
            }
        }
        buff_index = 0;
        // pthread_mutex_unlock(&rsem);
        pthread_mutex_unlock(&wsem);
        pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
        usleep(1000000);
    }
    return NULL;
}

/* flushed results :) */
void part4_results(void* v) {
    if(end_result_tower && end_file_tower) 
        printf("Part: %s\nQuery: %s\nResult: %.5g, %s\n",
            PART_STRINGS[current_part], 
            QUERY_STRINGS[current_query],
            *end_result_tower,
            end_file_tower
        );

}

