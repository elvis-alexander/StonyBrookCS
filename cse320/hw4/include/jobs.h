#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>

#define RUNNING_M "Running"
#define STOPPED_M "Stopped"

// this will be the node
typedef struct job_node {
    // reference
    struct job_node* next;     // set up next ref
    struct job_node* prev;     // set up prev ref
    // data fields
    int jid;            // job number (0, 1, ...)
    char* status;       // status
    pid_t pid;          // pid
    char* name;         // name of command
    int exit_stats;     // exit status code
}job_node;

// this will present a linked list
typedef struct jobs_linked_list {
    job_node *head;      // ptr to head node (start)
    job_node *tail;      // ptr to last node (end)
    int num_jobs;        // num of jobs in linked list (traversal)
}jobs_linked_list;


// header files
void construct_list();
void add_last(char* status, pid_t pid, char* name);
// void add_last(int status, pid_t pid, char* name);
job_node* get_job(int id, bool is_pid);
bool remove_job(int id, bool is_pid);
void display_jobs();
void remove_all_jobs();
int return_spid();
bool swap_status(pid_t p);

void swap_all();
void print_process_table();