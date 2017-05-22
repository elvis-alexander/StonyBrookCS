#include "lott.h"
#include "utilities.h"

static void* map(void*);
static void* reduce(void*);

// reducers
/* finds minimum or maximum based on find_max */
map_obj* min_max_multiplexor_reducer(thread_array* arr, struct pollfd* my_fds, int find_max);
/* finds country with highest occurence */
map_obj* country_multiplexor_reducer(thread_array* arr, struct pollfd* my_fds);
/* prints results */
void part5_print_result(void* v);
/* this is what will be printed */
float* part5_end_result = NULL;
char* part5_end_file = NULL;

/* part5 implementation */
int part5(size_t nthreads) {
    /* call map, to fill in the values for each thread_node */
    thread_array* map_threads = create_thread_array(nthreads);
    /* map function, will be incharge */
    map(map_threads);
    // push, cancel, join, pop
    pthread_t reduce_thread = create_reduce_thread(reduce, map_threads);
    /* push printing function */
    pthread_cleanup_push(part5_print_result, NULL);
    /* join all map threads */
    join_all_threads(map_threads);
    /* allow reduce to reduce all files from fd's */
    usleep(1000000);
    /* cancel and print after reduce finishes running */
    pthread_cancel(reduce_thread);
    pthread_cleanup_pop(1);
    /* cleanup... */
    free(map_threads);
    return 0;
}

/* map function */
static void* map(void* v){
    /* set map_function */
    void* map_function = map_threads_multiplexor;
    /* create thread list */
    thread_array* arr = (thread_array *)v;
    divide_files(arr);
    // /* ready for thread :) */
    int curr_thread = 0;
    while(curr_thread < arr -> num_threads) {
        thread_obj* curr = *(arr -> t_list + curr_thread);
        int fd[2];
        socketpair(PF_UNIX, SOCK_STREAM, 0, fd);
        curr -> sock_out = fd[0];   // where i write to
        curr -> sock_in = fd[1];    // where i read from
        pthread_t new_thread;
        pthread_create(&new_thread, NULL, map_function, *(arr -> t_list + curr_thread));
        /* set names of threads */
        char* name = (char *)malloc(16);
        sprintf(name, "map %d", curr_thread);
        pthread_setname_np(new_thread, name);
        /* set thread_id */
        (*(arr -> t_list + curr_thread)) -> tid = new_thread;
        curr_thread += 1;
    }
    return arr;
}

/* calls map function for all the files in this thread */
void map_threads_multiplexor(thread_obj* t) {
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
        run_mapper_multiplexor(*(t -> map_array + curr_file), t, map_function);
        ++curr_file;
    }
}

/* write to socket after a map job is done */
void run_mapper_multiplexor(map_obj* curr_map, thread_obj* t, result_obj* (*map_function)(map_obj*)) {
    result_obj* map_result = map_function(curr_map);
    dprintf(t -> sock_out, "%f,%s\n", map_result -> result_f, map_result -> result_str);
    usleep(500000);
}

/* reduce function */
static void* reduce(void* v){
    thread_array* arr = (thread_array*)v;
    /* */
    int size = arr -> num_threads;
    struct pollfd* my_fds = (struct pollfd*)malloc(sizeof(struct pollfd) * size);
    int i = 0;
    for(i = 0; i < size; ++i) {
        thread_obj* curr_obj = *(arr -> t_list + i);
        my_fds[i].fd = curr_obj -> sock_in;
        my_fds[i].events = POLLIN;
    }
    /* set reducer function */
    switch(*QUERY_STRINGS[current_query]) {
        case 'A':
            min_max_multiplexor_reducer(arr, my_fds, GET_MAX);            
            break;
        case 'B':
            min_max_multiplexor_reducer(arr, my_fds, GET_MIN);
            break;
        case 'C':
            min_max_multiplexor_reducer(arr, my_fds, GET_MAX);            
            break;
        case 'D':
            min_max_multiplexor_reducer(arr, my_fds, GET_MIN);
            break;
        case 'E':
            country_multiplexor_reducer(arr, my_fds);
            break;
    }
    return NULL;
}

/* finds minimum or maximum based on find_max */
map_obj* min_max_multiplexor_reducer(thread_array* arr, struct pollfd* my_fds, int find_max) {
    /*  */
    int i = 0;
    int size = arr -> num_threads;
    while(1) {
        if(poll(my_fds, size, 0) == 0)
            continue;
        /* there is/are fd's to be served */
        pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);
        i = 0;
        for(i = 0; i < size; ++i) {
            if(my_fds[i].revents == POLLIN) {
                char buff[128];
                int len = read(my_fds[i].fd, &buff, 128);
                buff[len] = '\0';
                /* read and calculate, min or max */
                char* file_name = (char*)malloc(120);
                float val;
                sscanf(buff, "%f,%s", &val, file_name);
                if(part5_end_result == NULL) {
                    part5_end_result = (float *)malloc(sizeof(float));
                    *part5_end_result = val;
                    part5_end_file = file_name;
                } else if((find_max == GET_MAX && val > *part5_end_result) || (find_max == GET_MIN && val < *part5_end_result)) {
                    *part5_end_result = val;
                    part5_end_file = file_name;
                } else if((val == *part5_end_result) && (strcmp(file_name, part5_end_file) < 0)) {
                    *part5_end_result = val;
                    part5_end_file = file_name;
                }
                memset(buff, '\0', 128);
            }
        }
        pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
    }
    return NULL;
}

/* finds country with highest occurence */
map_obj* country_multiplexor_reducer(thread_array* arr, struct pollfd* my_fds) {
    country_obj_list* country_list = (country_obj_list *)malloc(sizeof(country_obj_list));
    country_list -> num_countries = 0;
    country_list -> head = NULL;
    part5_end_result = (float *)malloc(sizeof(float));
    float max_occur = -1;
    char* max_c_code = NULL;
    int i = 0;
    int size = arr -> num_threads;
    while(1) {
        if(poll(my_fds, size, 0) == 0)
            continue;
        pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);
        i = 0;
        for(i = 0; i < size; ++i) {
            if(my_fds[i].revents == POLLIN) {
                char buff[128];
                int len = read(my_fds[i].fd, &buff, 128);
                buff[len] = '\0';
                /* read and calculate, min or max */
                char* c_code = (char*)malloc(120);
                float val;
                sscanf(buff, "%f,%s", &val, c_code);
                ////////////////////////////////////////////////////
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
                part5_end_file = strdup(max_c_code);
                *part5_end_result = max_occur;
                ////////////////////////////////////////////////////
                memset(buff, '\0', 128);
                pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
            }
        }
    }
    return NULL;
}

/* flushed results */
void part5_print_result(void* v) {
    /* print results */
    if(part5_end_result && part5_end_file) 
        printf("Part: %s\nQuery: %s\nResult: %.5g, %s\n",
            PART_STRINGS[current_part], 
            QUERY_STRINGS[current_query],
            *part5_end_result,
            part5_end_file);
}