#include "utilities.h"

/* mapper for query A/B */
result_obj* map_duration(map_obj* curr_map) {
    /* concat directory and file name */
    char path[strlen(curr_map -> file_name) + strlen(DATA_DIR) + 2];    // + 2 (for slash, if necessary and null terminator)
    memcpy(path, DATA_DIR, strlen(DATA_DIR));
    memcpy(path + strlen(DATA_DIR), "/", 1);
    memcpy(path + strlen(DATA_DIR) + 1, curr_map -> file_name, strlen(curr_map -> file_name));
    memcpy(path +strlen(DATA_DIR) + strlen(curr_map -> file_name) + 1, "\0",1);
    /* open file for mapping */
    int fd = open(path, O_RDONLY);
    char* str = (char *)malloc(sizeof(char) * 4001);
    ssize_t size;
    /* initially reading time_stamp */
    enum state my_state = time_stamp;
    float total_duration = 0;
    float num_lines = 0;
    /* this will store duration */
    char val[32];
    int val_index = 0;
    /* parse file for duration of a record */
    while((size = read(fd, str, 4000)) > 0) {
        str[size] = 0;
        int index = 0;
        while(index < size) {
            /* identify which part of the line is currently being parsed */
            if(my_state == time_stamp && str[index] == ',') {
                my_state = ip;
            } else if(my_state == ip && str[index] == ',') {
                my_state = duration;
            } else if(my_state == duration && str[index] != ',') {
                val[val_index++] = str[index];
            } else if(my_state == duration && str[index] == ',') {
                /* detected duration completely */
                val[val_index] = 0;
                val_index = 0;
                total_duration += atoi(val);
                my_state = country;
            } else if(my_state == country && str[index] == '\n') {
                /* finished parsing one row */
                my_state = time_stamp;
                num_lines++;
            }
            ++index;
        }
    }
    free(str);
    close(fd);
    /* set return val (duration) */
    result_obj* map_result = (result_obj *)malloc(sizeof(result_obj));
    map_result -> result_str = strdup(curr_map -> file_name);
    if(num_lines == 0)
        map_result -> result_f  = 0;
    else
        map_result -> result_f = (total_duration/num_lines);
    return map_result;
}

/* mapper for query C/D */
result_obj* map_users(map_obj* curr_map) {
    /* concat directory and file name */
    char path[strlen(curr_map -> file_name) + strlen(DATA_DIR) + 2];    // + 2 (for slash, if necessary and null terminator)
    memcpy(path, DATA_DIR, strlen(DATA_DIR));
    memcpy(path + strlen(DATA_DIR), "/", 1);
    memcpy(path + strlen(DATA_DIR) + 1, curr_map -> file_name, strlen(curr_map -> file_name));
    memcpy(path +strlen(DATA_DIR) + strlen(curr_map -> file_name) + 1, "\0",1);
    /* open file for mapping */
    int fd = open(path, O_RDONLY);
    char* str = (char *)malloc(sizeof(char) * 4001);
    ssize_t size;
    /* initially reading time_stamp */
    enum state my_state = time_stamp;
    /* count lines */
    float num_lines = 0;
    /* used to store time_stamp of a line */
    char val[32];
    int val_index = 0;
    /* used to retrieve year from timestamp */
    const char format[] = "%Y";
    time_t timer;
    struct tm* lt;
    char result[32];
    /* map years to occurences, taking advantage range of unix_timestamp range */
    int min_year = 1901;
    int max_size = 136;
    int map[max_size];
    int i = 0;
    for(i = 0; i < max_size; ++i) {
        map[i] = 0;
    }
    /* parse file for timestamp */
    while((size = read(fd, str, 4000)) > 0) {
        str[size] = 0;
        int index = 0;
        while(index < size) {
            if(my_state == time_stamp && str[index] != ',') {
                /* read time stamp */
                val[val_index++] = str[index];
            } else if(my_state == time_stamp && str[index] == ',') {
                /* detect timestamp */
                val[val_index] = 0;
                val_index = 0;
                /* use localtime and strftime to retrieve year */
                timer = atoi(val);
                /* refactor */
                lt = localtime(&timer);
                strftime(result, sizeof(result), format, lt);
                int year = atoi(result);
                /* get year_node if already exists in list */
                map[year - min_year] = map[year - min_year] + 1;
                /* refactor */
                /* set state to next */
                my_state = ip;
            } else if(my_state == ip && str[index] == '\n') {
                my_state = time_stamp;
                ++num_lines;
            }
            ++index;
        }
    }   
    float count = 0;
    for(i = 0; i < max_size; ++i) {
        if(map[i] > 0)
            ++count;
    }
    /* clean up */
    close(fd);
    free(str);
    /* set return val (duration) */
    result_obj* map_result = (result_obj *)malloc(sizeof(result_obj));
    map_result -> result_str = strdup(curr_map -> file_name);
    if(count == 0)
        map_result -> result_f  = 0;
    else
        map_result -> result_f = (num_lines/count);
    return map_result;
}


result_obj* map_country(map_obj* curr_map) {
    /* concat directory and file name */
    char path[strlen(curr_map -> file_name) + strlen(DATA_DIR) + 2];    // + 2 (for slash, if necessary and null terminator)
    memcpy(path, DATA_DIR, strlen(DATA_DIR));
    memcpy(path + strlen(DATA_DIR), "/", 1);
    memcpy(path + strlen(DATA_DIR) + 1, curr_map -> file_name, strlen(curr_map -> file_name));
    memcpy(path +strlen(DATA_DIR) + strlen(curr_map -> file_name) + 1, "\0",1);
    /* open file for mapping */
    int fd = open(path, O_RDONLY);
    char* str = (char *)malloc(sizeof(char) * 4001);
    ssize_t size;
    /* initially reading time_stamp */
    enum state my_state = time_stamp;
    float num_lines = 0;
    /* this will store duration */
    char val[32];
    int val_index = 0;
    /* keep track of distinct country_codes */
    country_obj_list* country_list = (country_obj_list *)malloc(sizeof(country_obj_list));
    country_list -> num_countries = 0;
    country_list -> head = NULL;
    float max_occur;
    /* */
    char* max_country_code = NULL;
    /* parse file for duration of a record */
    while((size = read(fd, str, 4000)) > 0) {
        /* set null terminator */
        str[size] = 0;
        /* parse buffer, for country code */
        int index = 0;
        while(index < size) {
            /* identify which part of the line is currently being parsed */
            if(my_state == time_stamp && str[index] == ',') {
                my_state = ip;
            } else if(my_state == ip && str[index] == ',') {
                my_state = duration;
            } else if(my_state == duration && str[index] == ',') {
                my_state = country;
            } else if(my_state == country && str[index] != '\n') {
                 val[val_index++] = str[index];
            } else if(my_state == country && str[index] == '\n') {
                /* detected country code completely */
                val[val_index] = 0;
                val_index = 0;
                /* store new countries in list and find max occurence */
                country_obj* c = get_country(country_list, val);
                if(c == NULL) {
                    char* new_country = strdup(val);
                    if(country_list -> head == NULL) {
                        max_occur = 1;
                        max_country_code = new_country;
                    } else if((max_occur == 1) && (strcmp(new_country, max_country_code) < 0)) {
                        max_country_code = new_country;
                    }
                    add_country(country_list, new_country, 1); // append to head
                } else {
                    c -> occurences++;  // increment occurences
                    if(c -> occurences > max_occur) {
                        max_occur = c -> occurences;
                        max_country_code = c -> c_code;
                    } else if(c -> occurences == max_occur && strcmp(c -> c_code, max_country_code) < 0) {
                        max_country_code = c -> c_code;
                    }
                }
                /* finished parsing one row */
                my_state = time_stamp;
                ++num_lines;
            }
            ++index;
        }
    }
    free(str);
    close(fd);
    /* set return val (duration) */
    result_obj* map_result = (result_obj *)malloc(sizeof(result_obj));
    map_result -> result_f = (max_occur);
    map_result -> result_str =  strdup(max_country_code);
    /* clean up */
    country_obj* tmp = country_list -> head;    
    while(tmp) {
        country_obj* t = tmp -> next;
        free(tmp -> c_code);
        free(tmp);
        tmp = t;
    }
    return map_result;
}

/* spawns a new thread per each file in DATA_DIR */
void run_and_create_maps(map_obj_list* map_list, void* map_function) {
    map_obj* prev = NULL;
    /* open directory */
    DIR* dir_desc = opendir(DATA_DIR);
    struct dirent* entry = NULL;
    /* create a new thread per file */
    while((entry = readdir(dir_desc)) != NULL) {
        if((strcmp(entry -> d_name, ".")) == 0 || (strcmp(entry -> d_name, "..")) == 0)
            continue;
        /* contruct new map object */
        map_obj* m = (map_obj*)malloc(sizeof(map_obj));
        m -> file_name = strdup(entry -> d_name);
        /* run thread for file */
        pthread_t new_thread;
        pthread_create(&new_thread, NULL, map_function, m);
        /* set the name of the map thread */
        char* name = (char *)malloc(16);
        sprintf(name, "map %d", map_list -> size);
        pthread_setname_np(new_thread, name);
        m -> tid = new_thread;
        /* append at the end of list */
        if(prev == NULL) {
            /* head node */
            map_list -> head = m;
            prev = m;
        } else {
            /* append to end */
            prev -> next = m;
            prev = m;
        }
        map_list -> size++;
    }
    /* set end of list */
    prev -> next = NULL;
    closedir(dir_desc);
}

/* joins all threads spawned in a list of map jobs */
void join_all_maps(map_obj_list* map_list) {
    /* join on all threads in list */
    map_obj* tmp = map_list -> head;
    while(tmp) {
        pthread_join(tmp -> tid, NULL);
        tmp = tmp -> next;
    }
}

/*creates a structure that will hold a list of thread nodes, where every node will hold files */
thread_array* create_thread_array(size_t nthreads) {
    thread_array* arr = (thread_array*)malloc(sizeof(thread_array));
    arr -> num_threads = nthreads;
    arr -> t_list = (thread_obj**)malloc(sizeof(thread_obj *) * arr -> num_threads);
    return arr;
}

/* creates annd returns a reduce thread */
pthread_t create_reduce_thread(void* func, void* arg) {    
    pthread_t reduce_thread;
    pthread_create(&reduce_thread, NULL, func, arg);
    char* reduce_name = (char *)malloc(16);
    sprintf(reduce_name, "reduce");        
    pthread_setname_np(reduce_thread, reduce_name);
    return reduce_thread;
}

/* divides files evenly into seperate threads */
void divide_files(thread_array* arr) {
    /* calculate number of files per thread */
    int total_files = files_count();
    int files_per_thread = total_files / (arr -> num_threads);
    /* distributed evenly amongst all threads */
    int left_over = total_files % arr -> num_threads;
    /* set file_names for each thread */
    DIR* dir_desc = opendir(DATA_DIR);
    struct dirent* entry = NULL;
    /* */
    int curr_thread = 0;
    int curr_file = 0;
    /* set list of file names and num_files for each thread */
    while(curr_thread < arr -> num_threads) {
        /* new thread_node with correct num files */        
        thread_obj* new_obj = (thread_obj*)malloc(sizeof(thread_obj));
        new_obj -> num_files = files_per_thread;
        /* increment count, on additional left over files */
        if(left_over > 0) {
            new_obj -> num_files++;
            --left_over;
        }
        /* set map_list for thread_node */
        new_obj -> map_array = (map_obj **)malloc(sizeof(map_obj *) * (new_obj -> num_files));
        curr_file = 0;
        while(curr_file < (new_obj -> num_files)) {
            entry = readdir(dir_desc);
            if((strcmp(entry -> d_name, ".")) == 0 || (strcmp(entry -> d_name, "..")) == 0) {
            }else {
                /* */
                *(new_obj -> map_array + curr_file) = (map_obj*)malloc(sizeof(map_obj));
                (*(new_obj -> map_array + curr_file)) -> file_name = strdup(entry -> d_name);
                ++curr_file;
            }
        }
        *(arr -> t_list + curr_thread) = new_obj;        
        curr_thread += 1;
    }
        closedir(dir_desc);
}

/* runs map_function for each thread */
void run_and_create_threads(thread_array* map_threads, void* map_function) {
    int curr_thread = 0;
    while(curr_thread < map_threads -> num_threads) {
        pthread_t new_thread;
        pthread_create(&new_thread, NULL, map_function, *(map_threads -> t_list + curr_thread));
        /* set names of threads */
        char* name = (char *)malloc(16);
        sprintf(name, "map %d", curr_thread);
        pthread_setname_np(new_thread, name);
        (*(map_threads -> t_list + curr_thread)) -> tid = new_thread;
        curr_thread += 1;
    }
}

/* joins a list of threads on calling function */
void join_all_threads(thread_array* arr) {
    int curr_thread = 0;
    while(curr_thread < arr -> num_threads) {
        pthread_join((*(arr -> t_list + curr_thread)) -> tid, NULL);
        curr_thread += 1;
    }    
}

/* everything below is a(n) helper function */
/* returns country in list with c_code */
country_obj* get_country(country_obj_list* country_list, char* c_code) {
    if(country_list -> num_countries == 0)
        return NULL;
    country_obj* tmp = country_list -> head;
    while(tmp) {
        if(strcmp(tmp -> c_code, c_code) == 0)
            return tmp;
        tmp = tmp -> next;
    }
    return NULL;
}

/* adds a country to the list */
void add_country(country_obj_list* country_list, char* c_code, int occurences) {
    country_obj* new_c = (country_obj*)malloc(sizeof(country_obj));
    new_c -> c_code = c_code;
    new_c -> occurences = occurences;
    new_c -> next = NULL;
    if(country_list -> num_countries)
        new_c -> next = country_list -> head;
    country_list -> head = new_c;
    country_list -> num_countries++;
}

/* returns true if coutry already exists */
int already_exists(country_obj_list* country_list, char* c_code, int occurences) {
    if(!country_list)
        return 0;
    country_obj* tmp = country_list -> head;
    while(tmp) {
        if(strcmp(tmp -> c_code, c_code) == 0 && tmp -> occurences == occurences)
            return 1;
        tmp = tmp -> next;
    }
    return 0;
}

// /* returns the number of files in the DATA_DIR directory */
int files_count() {
    DIR* dir_desc = opendir(DATA_DIR);
    struct dirent* entry = NULL;
    int count = 0;
    while((entry = readdir(dir_desc)) != NULL) {
        if((strcmp(entry -> d_name, ".")) == 0 || (strcmp(entry -> d_name, "..")) == 0)
            continue;
        ++count;
    }
    closedir(dir_desc);
    return count;
}