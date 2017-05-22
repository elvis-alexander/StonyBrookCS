#include "lott.h"
#include "utilities.h"

static void* map(void*);
static void* reduce(void*);

// reducers
/* finds minimum or maximum based on find_max */
map_obj* min_max_fellowship_reducer(int find_max);
/* finds country with highest occurence */
map_obj* country_fellowship_reducer();

/* prints results */
void print_result(void* v);
/* global fp and mutex */
pthread_mutex_t file_mutex;
pthread_mutex_t readers_preference_mutex;
int readcnt = 0;
/* this is what will be printed */
float* end_result = NULL;
char* end_file = NULL;
/* */

/* part3 implementation */
int part3(size_t nthreads) {
    /* open the file for writing/reading */
    FILE* fp = fopen("mapred.tmp", "w+");
    fclose(fp);
    /* call map, to fill in the values for each thread_node */
    thread_array* map_threads = create_thread_array(nthreads);
    /* map function, will be incharge */
    map(map_threads);
    /* create a reduce thread and run it with map_threads as an argument*/
    pthread_t reduce_thread = create_reduce_thread(reduce, map_threads);
    pthread_cleanup_push(print_result, NULL);
    /* join all map threads, this is wrapper for pthread_join() */
    join_all_threads(map_threads);
    // allow reduce() to reduce all files
    usleep(1000000);
    usleep(1000000);
    usleep(1000000);
    pthread_cancel(reduce_thread);
    pthread_cleanup_pop(1);
    free(map_threads);
    unlink("mapred.tmp");
    return 0;
}

/* map function */
static void* map(void* v){
    /* set map_function */
    void* map_function = map_threads_fellowship;
    /* create thread list */
    thread_array* map_threads = (thread_array *)v;
    /* this helper will assign files evenly for each thread */
    divide_files(map_threads);
    /* ready for thread  */
    run_and_create_threads(map_threads, map_function);
    return map_threads;
}

/* calls map function for all the files in this thread */
void map_threads_fellowship(thread_obj* t) {
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
            break;
    }
    int curr_file = 0;
    while(curr_file < t -> num_files) {
        run_mapper_fellowship(*(t -> map_array + curr_file), map_function);
        ++curr_file;
    }
}

// /* mapper for query A/B/C/D/E */
void run_mapper_fellowship(map_obj* curr_map, result_obj* (map_function)(map_obj*)) {
    /* write to file safely */
    result_obj* map_result = map_function(curr_map);
    pthread_mutex_lock(&file_mutex);
    /* set return val (duration) */
    FILE* fp = fopen("mapred.tmp", "a");
    fprintf(fp, "%f,%s\n", map_result -> result_f, map_result -> result_str);
    fclose(fp);
    pthread_mutex_unlock(&file_mutex);
    /* end of writing safely */
}

/* reduce function */
static void* reduce(void* v){
    /* set reducer function */
    switch(*QUERY_STRINGS[current_query]) {
        case 'A':
            min_max_fellowship_reducer(GET_MAX);            
            break;
        case 'B':
            min_max_fellowship_reducer(GET_MIN);
            break;
        case 'C':
            min_max_fellowship_reducer(GET_MAX);            
            break;
        case 'D':
            min_max_fellowship_reducer(GET_MIN);
            break;
        case 'E':
            country_fellowship_reducer();
            break;
    }
    return NULL;
}

/* finds minimum or maximum based on find_max */
map_obj* min_max_fellowship_reducer(int find_max) {
    FILE* f = fopen("mapred.tmp", "r");
    while(1) {
        /* read and calculate, min or max */
        pthread_mutex_lock(&readers_preference_mutex);
        ++readcnt;
        if(readcnt == 1)
            pthread_mutex_lock(&file_mutex);
        pthread_mutex_unlock(&readers_preference_mutex);
        pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);
        const size_t line_size = 256;
        char* line = (char *)malloc(line_size);
        // *end_result = -1;
        while(fgets(line, line_size, f) != NULL) {
            char* file_name = (char*)malloc(120);
            float val;
            sscanf(line, "%f,%s", &val, file_name);
            if(end_result == NULL) {
                end_result = (float *)malloc(sizeof(float));
                *end_result = val;
                end_file = file_name;
            } else if((find_max == GET_MAX && val > *end_result) || (find_max == GET_MIN && val < *end_result)) {
                *end_result = val;
                end_file = file_name;
            } else if((val == *end_result) && (strcmp(file_name, end_file) < 0)) {
                *end_result = val;
                end_file = file_name;
            }
        }
        free(line);
        pthread_mutex_lock(&readers_preference_mutex);
        --readcnt;
        if(readcnt == 0)
            pthread_mutex_unlock(&file_mutex);
        pthread_mutex_unlock(&readers_preference_mutex);
        pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
        usleep(1000000);
    }
    fclose(f);
    return NULL;
}

/* finds country with highest occurence */
map_obj* country_fellowship_reducer() {
    end_result = (float *)malloc(sizeof(float));       
    /* */
    country_obj_list* country_list = (country_obj_list *)malloc(sizeof(country_obj_list));
    country_list -> num_countries = 0;
    country_list -> head = NULL;           
    FILE* f = fopen("mapred.tmp", "r");
    /* */
    float max_occur = -1;
    char* max_c_code = NULL;
    while(1) {
        /* read and calculate, min or max */
        pthread_mutex_lock(&readers_preference_mutex);
        ++readcnt;
        if(readcnt == 1)
            pthread_mutex_lock(&file_mutex);
        pthread_mutex_unlock(&readers_preference_mutex);
        pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);
        /* */
        const size_t line_size = 256;
        char* line = (char *)malloc(line_size);
        while(fgets(line, line_size, f) != NULL) {
            char* c_code = (char*)malloc(120);
            float val;
            sscanf(line, "%f,%s", &val, c_code);  
            // *******************************************
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
        }
        free(line);
        if(country_list -> num_countries != 0) {
            end_file = strdup(max_c_code);
            *end_result = max_occur;
        }
        pthread_mutex_lock(&readers_preference_mutex);
        --readcnt;
        if(readcnt == 0)
            pthread_mutex_unlock(&file_mutex);
        pthread_mutex_unlock(&readers_preference_mutex);
        pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
        usleep(1000000);
    }
    return NULL;
}

/* flushed results */
void print_result(void* v) {
    /* print results */
    if(end_result && end_file) 
        printf("Part: %s\nQuery: %s\nResult: %.5g, %s\n",
            PART_STRINGS[current_part], 
            QUERY_STRINGS[current_query],
            *end_result,
            end_file
        );
}