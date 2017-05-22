#include "lott.h"
#include "utilities.h"

static void* map(void*);
static void* reduce(void*);
/* finds minimum or maximum based on find_max */
map_obj* min_max_reducer(map_obj_list* map_list, int find_max);
/* reducer for country */
map_obj* country_reducer(map_obj_list* map_list);

/* part1 execution flow */
int part1() {
    /* call map, to fill in the values for map_list (linkedlist) */
    map_obj_list* map_list = (map_obj_list*)malloc(sizeof(map_obj_list));
    map_list -> head = NULL;
    map_list -> size = 0;
    /* map each file */
    map(map_list);
    /* reduce results from map */
    reduce(map_list);    
    /* cleanup... */
    free(map_list);
    return 0;
}

/* map function */
static void* map(void* v){
    /* set map_function */
    void* map_function = NULL;
    switch(*QUERY_STRINGS[current_query]) {
        case 'A':
            map_function = map_duration_desolation;
            break;
        case 'B':
            map_function = map_duration_desolation;
            break;
        case 'C':
            map_function = map_users_desolation;
            break;
        case 'D':
            map_function = map_users_desolation;
            break;
        case 'E':
            map_function = map_country_desolation;
            break;
    }
    /* maintain list of map calls per file */
    map_obj_list* map_list = (map_obj_list*)v;
    /* this function spawns a new thread per file and runs specified map_function */
    run_and_create_maps(map_list, map_function);
    /* joins all spawned map thread */
    join_all_maps(map_list);
}

/* mapper for query A/B */
void map_duration_desolation(map_obj* curr_map) {
    result_obj* map_result = map_duration(curr_map);
    curr_map -> return_val = map_result -> result_f;
}

/* mapper for query C/D */
void map_users_desolation(map_obj* curr_map) {
    result_obj* map_result = map_users(curr_map);
    curr_map -> return_val = map_result -> result_f;
}

/* mapper for query E */
void map_country_desolation(map_obj* curr_map) {
    result_obj* result = map_country(curr_map);
    curr_map -> return_val = result -> result_f;
    curr_map -> map_c_code = result -> result_str;
}

/* reduce function */
static void* reduce(void* v){
    map_obj* m = NULL;
    /* set reducer function */
    switch(*QUERY_STRINGS[current_query]) {
        case 'A':
            m = min_max_reducer(v, GET_MAX);            
            break;
        case 'B':
            m = min_max_reducer(v, GET_MIN);
            break;
        case 'C':
            m = min_max_reducer(v, GET_MAX);            
            break;
        case 'D':
            m = min_max_reducer(v, GET_MIN);
            break;
        case 'E':
            m = country_reducer(v);
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

/* reducers for query A/B/C/D */
map_obj* min_max_reducer(map_obj_list* map_list, int find_max) {
    /* empty directory */
    if(map_list -> size == 0)
        return NULL;
    /* find target value (min or max) */
    char* target_file = NULL;
    float target_val;
    map_obj* tmp = map_list -> head;
    while(tmp) {
        // set target file if necessary
        if(
            (target_file == NULL) ||
            ((find_max == GET_MAX) && (tmp -> return_val > target_val)) ||
            ((find_max == GET_MIN) && (tmp -> return_val < target_val)) ||
            ((tmp -> return_val == target_val) && (strcmp(tmp -> file_name, target_file) < 0))
        ) {
            target_val = tmp -> return_val;
            target_file = tmp -> file_name;
        }
        tmp = tmp -> next;
    }
    /* create a copy of the smallest value, to return */
    map_obj* result = (map_obj*)malloc(sizeof(map_obj));
    result -> file_name = target_file;
    result -> return_val = target_val;
    return result;
}

/* reducer on country codes */
map_obj* country_reducer(map_obj_list* map_list) {
    /* iterate on map results */
    country_obj_list* country_list = (country_obj_list *)malloc(sizeof(country_obj_list));
    country_list -> num_countries = 0;
    country_list -> head = NULL;
    /*  */
    map_obj* tmp_map = map_list -> head;
    /*  */
    float max_occur;
    char* max_c_code = NULL;
    /* find max occurence in list returned by map() */
    while(tmp_map) {
        country_obj* c = get_country(country_list, tmp_map -> map_c_code);
        if(c == NULL) {
            char* new_c_code = strdup(tmp_map -> map_c_code);
            if(
                (country_list -> head == NULL) ||
                (tmp_map -> return_val > max_occur) ||
                ((tmp_map -> return_val == max_occur) && (strcmp(new_c_code, max_c_code) < 0))
            ) {
                max_occur = tmp_map -> return_val;
                max_c_code = new_c_code;
            }
            add_country(country_list, new_c_code, tmp_map -> return_val);
            // clean up
            free(tmp_map -> map_c_code);
        } else {
            c -> occurences += tmp_map -> return_val;  // icrement occurences
            if(
                (c -> occurences > max_occur) ||
                ((c -> occurences == max_occur) && (strcmp(c -> c_code, max_c_code) < 0))
            ) {
                max_occur = c -> occurences;
                max_c_code = c -> c_code;
            }
        }
        // iterate
        tmp_map = tmp_map -> next;
    }
    map_obj* new_o = (map_obj *)malloc(sizeof(map_obj));
    new_o -> file_name = strdup(max_c_code);
    new_o -> return_val = max_occur;
    country_obj* obj = country_list -> head;
    while(obj) {
        country_obj* n = obj -> next;
        free(obj -> c_code);
        free(obj);
        obj = n;
    }    
    return new_o;
}