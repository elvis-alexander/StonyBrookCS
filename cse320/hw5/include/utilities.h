#include "lott.h"
#include <fcntl.h>
#include <sys/stat.h>
#include <dirent.h>
#include <fcntl.h>
#include <sys/socket.h>
#include <poll.h>

// @TODO @TODO @TODO, make int for countries in map / reduce
/* refactor code / fix part 3 / run tests, enjoy */

/* used as flags for reducer method */
#define GET_MAX 1
#define GET_MIN 0

typedef struct result_obj {
	int result_i;
	float result_f;
	char* result_str;
}result_obj;

/* used to determine which part of a line I am reading */
enum state {
    time_stamp, // reading time stamp
    ip,         // reading ip
    duration,   // reading duration
    country     // reading country
};

/* represents a map job for a particular file */
typedef struct map_obj {
    float return_val;       // set in map
    pthread_t tid;          // thread id
    char* file_name;        // file name (google_es.csv)
    struct map_obj* next;   // ptr to next in list
    char* map_c_code;
}map_obj;

/* maintains linked list of map() on all files */
typedef struct map_obj_list {
    map_obj* head;          // reference to begin of list
    int size;               // size of linked list
}map_obj_list;

/* represents a thread obj responsible for num_files */
typedef struct thread_obj {
    int num_files;              // num_files responsible for
    map_obj** map_array;        // list of map jobs
    pthread_t tid;              // id of this thread
    int sock_in;
    int sock_out;
}thread_obj;

/* stores an array of threads */
typedef struct thread_array {
    thread_obj** t_list;
    int num_threads;
}thread_array;

/* country list and obj */
typedef struct country_obj {
    char* c_code;
    int occurences;
    struct country_obj* next;
}country_obj;

typedef struct country_obj_list {
    country_obj* head;
    int num_countries;
}country_obj_list;

/* helpers to maintain a country list */
void add_country(country_obj_list* country_list, char* c_code, int occurences);
country_obj* get_country(country_obj_list* country_list, char* c_code);
int already_exists(country_obj_list* country_list, char* c_code, int occurences);
/* num files in DATA_DIR */
int files_count();

/* spawns a new thread per each file in DATA_DIR */
void run_and_create_maps(map_obj_list* map_list, void* map_function);
/* joins all threads spawned in a list of map jobs */
void join_all_maps(map_obj_list* map_list);

/* creates a list of thread nodes */
thread_array* create_thread_array(size_t nthreads);
/* creates and returns a reduce thread */
pthread_t create_reduce_thread(void* func, void* arg);
/* evenly associates file's with threads */
void divide_files(thread_array* arr);
/* runs map_function for each thread */
void run_and_create_threads(thread_array* map_threads, void* map_function);
/* helper to join all threads */
void join_all_threads(thread_array* arr);

/* mappers, used by all part's (wrappers for map jobs per file)*/
result_obj* map_duration(map_obj* curr_map);
result_obj* map_users(map_obj* curr_map);
result_obj* map_country(map_obj* curr_map);

/* mappers for part 1 - desolation */
/* map function for query A/B */
void map_duration_desolation(map_obj* curr_map);
/* map function for query C/D */
void map_users_desolation(map_obj* curr_map);
/* map function for query E */
void map_country_desolation(map_obj* curr_map);

/* mappers for part 2 - desolation */
/* calls map function for all the files in this thread */
void map_battle_threads(thread_obj* t);
void run_mapper_battle(map_obj* curr_map, result_obj* (map_function)(map_obj*), int is_country);

/* mappers for part 3 - desolation */
/* calls map function for all the files in this thread */
void map_threads_fellowship(thread_obj* t);
void run_mapper_fellowship(map_obj* curr_map, result_obj* (map_function)(map_obj*));

/* part 4 */
/* calls map function for all the files in this thread */
void map_threads_towers(thread_obj* t);
void run_mapper_towers(map_obj* curr_map, result_obj* (map_function)(map_obj*));

/* mappers for part 5 - multiplexor */
/* calls map function for all the files in this thread */
void map_threads_multiplexor(thread_obj* t);
void run_mapper_multiplexor(map_obj* curr_map, thread_obj* t, result_obj* (*map_function)(map_obj*));