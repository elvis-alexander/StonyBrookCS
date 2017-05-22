#include "lott.h"
#include "utilities.h"


/* task_list: 
    @TODO: use thread safe for local time ands
    @TODO: should sub directories be taken into account d_type, DT_REG vs DT_DIR 
*/
static void* map(void*);
static void* reduce(void*);

#define IS_COUNTRY_MAP 1
#define NOT_COUNTRY_MAP 0


/* finds minimum or maximum based on find_max */
map_obj* min_max_thread_reducer(thread_array* arr, int find_max);
/* find country with highest occurence */
map_obj* country_thread_reducer(thread_array* arr);

/* part2 implementation */
int part2(size_t nthreads) {
    thread_array* map_threads = create_thread_array(nthreads);
    /* call map, to fill in the values for each thread_node */
    map(map_threads);
    /* after joining all map threads run reduce */
    reduce(map_threads);
    /* cleanup... */
    free(map_threads);
    return 0;
}

/* map function */
static void* map(void* v){
    /* set map_function */
	void* map_function = map_battle_threads;
    /* create thread list */
    thread_array* map_threads = (thread_array *)v;
    divide_files(map_threads);
    // this function calls pthread_create for 'map_function' for each thread
    run_and_create_threads(map_threads, map_function);
    /* helper for pthread_join */
    join_all_threads(map_threads);
    return map_threads;
}

/* */
void map_battle_threads(thread_obj* t) {
    result_obj* (*map_function)(map_obj*);
    int is_country = NOT_COUNTRY_MAP;
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
            is_country = IS_COUNTRY_MAP;
            break;
    }
    int curr_file = 0;
    while(curr_file < t -> num_files) {
        run_mapper_battle(*(t -> map_array + curr_file), map_function, is_country);
        ++curr_file;
    }
}

/* mapper for query A/B/C/D/E */
void run_mapper_battle(map_obj* curr_map, result_obj* (map_function)(map_obj*), int is_country) {
    result_obj* map_result = map_function(curr_map);
    curr_map -> return_val = map_result -> result_f;
    if(is_country)
        curr_map -> map_c_code = map_result -> result_str;
}

/* reduce function */
static void* reduce(void* v){
    map_obj* m = NULL;
    /* set reducer function */
    switch(*QUERY_STRINGS[current_query]) {
        case 'A':
            m = min_max_thread_reducer(v, GET_MAX);            
            break;
        case 'B':
            m = min_max_thread_reducer(v, GET_MIN);
            break;
        case 'C':
            m = min_max_thread_reducer(v, GET_MAX);            
            break;
        case 'D':
            m = min_max_thread_reducer(v, GET_MIN);
            break;
        case 'E':
            m = country_thread_reducer(v);
            break;
    }
    /* print results */
    if(m != NULL) {
        printf("Part: %s\nQuery: %s\nResult: %.5g, %s\n",
            PART_STRINGS[current_part], 
            QUERY_STRINGS[current_query],
            m -> return_val,
            m -> file_name
        );
    }
    return NULL;
}

/* */
map_obj* min_max_thread_reducer(thread_array* arr, int find_max) {
	if(arr -> num_threads == 0)
		return 0;
    /* return value's */
    char* target_file = NULL;
	float target_val;
    /* find target value (min or max) */
    thread_obj* thread = NULL;
    int curr_thread = 0;
    int curr_file = 0;
    while(curr_thread < arr -> num_threads) {
    	thread = *(arr -> t_list + curr_thread);
    	curr_file = 0;
    	map_obj* curr_map = NULL;
    	while(curr_file < thread -> num_files) {
    		curr_map = *(thread -> map_array + curr_file);
            // set min/max if current value is greater than previous
            if(
                ((target_file == NULL)) ||
                ((find_max == GET_MAX) && (curr_map -> return_val > target_val)) ||
                ((find_max == GET_MIN) && (curr_map -> return_val < target_val)) ||
                ((curr_map -> return_val == target_val) && (strcmp(curr_map -> file_name, target_file) < 0))
            ) {
                target_val = curr_map -> return_val;
                target_file = curr_map -> file_name;
            }
    		++curr_file;
    	}
    	++curr_thread;
    }
    /* find smallest file (alphabetical order) */
	map_obj* result = (map_obj*)malloc(sizeof(map_obj));
    result -> file_name = target_file;
    result -> return_val = target_val;
    return result;
}


/* */
map_obj* country_thread_reducer(thread_array* arr) {
    if(arr -> num_threads == 0)
        return 0;
    /* */
    country_obj_list* country_list = (country_obj_list *)malloc(sizeof(country_obj_list));
    country_list -> num_countries = 0;
    country_list -> head = NULL;
    /* */
    float max_occur;
    char* max_c_code = NULL;
    /* find target value (min or max) */
    thread_obj* thread = NULL;
    int curr_thread = 0;
    int curr_file = 0;
    while(curr_thread < arr -> num_threads) {
        thread = *(arr -> t_list + curr_thread);
        curr_file = 0;
        map_obj* curr_map = NULL;
        while(curr_file < thread -> num_files) {
            curr_map = *(thread -> map_array + curr_file);
            /***************************************************/
            country_obj* c = get_country(country_list, curr_map -> map_c_code);
            if(c == NULL) {
                char* new_c_code = strdup(curr_map -> map_c_code);
                if((country_list -> head == NULL)) {
                    max_occur = curr_map -> return_val;
                    max_c_code = new_c_code;
                } else if(curr_map -> return_val > max_occur) {
                    max_occur = curr_map -> return_val;
                    max_c_code = new_c_code;
                } else if((curr_map -> return_val == max_occur) && (strcmp(new_c_code, max_c_code) < 0)) {
                    max_occur = curr_map -> return_val;
                    max_c_code = new_c_code;                
                }
                add_country(country_list, new_c_code, curr_map -> return_val);
                // clean up
                free(curr_map -> map_c_code);
            } else {
                c -> occurences += curr_map -> return_val;  // icrement occurences
                if(c -> occurences > max_occur) {
                    max_occur = c -> occurences;
                    max_c_code = c -> c_code;
                } else if((c -> occurences == max_occur) && (strcmp(c -> c_code, max_c_code) < 0)) {
                    max_occur = c -> occurences;///
                    max_c_code = c -> c_code;
                }
            }
            ++curr_file;
        }
        ++curr_thread;
    }
    
    map_obj* result = (map_obj*)malloc(sizeof(map_obj));
    result -> file_name = strdup(max_c_code);
    result -> return_val = max_occur;
    country_obj* obj = country_list -> head;
    while(obj) {
        country_obj* n = obj -> next;
        free(obj -> c_code);
        free(obj);
        obj = n;
    } 
    return result;
}