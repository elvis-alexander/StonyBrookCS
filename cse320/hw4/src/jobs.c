#include "jobs.h"

// globals :) (not evil)
// pointer to 
jobs_linked_list* jobs = NULL;
// will assign id's to jobs
volatile int global_id = 1;


// constructor for linked list
void construct_list() {
    jobs = (jobs_linked_list*)malloc(sizeof(jobs_linked_list));
    jobs -> head = NULL;
    jobs -> tail = NULL;
    jobs -> num_jobs = 0;
}

// Appends the specified element to the end of this list.
void add_last(char* status, pid_t pid, char* name) {
    // construct new node and set values
    job_node* new_node = (job_node*)malloc(sizeof(job_node));
    new_node -> jid = global_id++;
    new_node -> status = status;
    new_node -> pid = pid;
    new_node -> name = (char*)malloc(strlen(name) + 1);
    strcpy(new_node -> name, name);

    // initialize linkedlist
    if(jobs == NULL)
        construct_list();
    
    // insert into link list
    if(jobs -> head == NULL) {
        // set reference to null
        new_node -> next = NULL;
        new_node -> prev = NULL;
        // set all reference in linked list to new node
        jobs -> head = new_node;
        jobs -> tail = new_node;
    } else {
        // 1 or more elements in linkedlist
        // get reference to tail and
        // append reference to end of list
        job_node* curr_tail = jobs -> tail;
        // set prev to current tail
        new_node -> prev = curr_tail;
        new_node -> next = NULL;
        // set next of tail to new node
        curr_tail -> next = new_node;
        // set new tail
        jobs -> tail = new_node;
    }
    // increment number of jobs
    jobs -> num_jobs = jobs -> num_jobs + 1;
}


// returns a job given a job id or a pid
// searches on pid id is_pid = true
job_node* get_job(int id, bool is_pid) {
    if(jobs == NULL)
        return NULL;
    job_node* curr = jobs -> head;
    while(curr != NULL) {
        int curr_id = curr -> pid;
        if(is_pid == false) {
            curr_id = curr -> jid;
        }
        if(id == curr_id) {
            return curr;        // found job, return
        }
        curr = curr -> next;
    }
    //  did not find job
    return NULL;
}

// removes a job given a job id or a pid
// searches for a pid id is_pid = true
bool remove_job(int id, bool is_pid) {
    // printf("attempting to remove_job\n");
    // printf("remove op: {%d}\n", id);
    if(jobs == NULL || jobs -> num_jobs == 0) {
        // printf("sfish: fg: %d no such job\n", id);  // remove @TODO
        return false;
    }
    // get specified job
    job_node* j = get_job(id, is_pid);
    if(j == NULL)
        return false;     // did not successfuly remove job (doesnt exists)

    // gracedfully remove
    if(j == jobs -> head) {
        if(j -> next == NULL) {
           // only element in the list
            jobs -> head = NULL;
            jobs -> tail = NULL;
        } else {
            // still atleast one element in list
            job_node* next = j -> next;
            next -> prev = NULL;
            jobs -> head = next;
        }
    } else if(j -> next != NULL && j -> prev != NULL) {
        // if removing an element with a next and prev node
        j -> prev -> next = j -> next;
        j -> next -> prev = j -> prev;
    } else if(j == jobs -> tail) {
        // element is tail
        if(j -> prev == NULL) {
            // only element in the list
            jobs -> head = NULL;
            jobs -> tail = NULL;
        } else {
            // still atleast one element in list
            job_node* prev = j -> prev;
            prev -> next = NULL;
            jobs -> tail = prev;
        }
    }
    // clean up
    free(j -> name);
    free(j);
    jobs -> num_jobs = jobs -> num_jobs - 1;
    return true;    // removed gracefully
}

/* print tabular view of jobs */
void display_jobs() {
    // return if size = 0 or unitiailized
    if(jobs == NULL || jobs -> num_jobs == 0) {
        return;
    }
    job_node* curr = jobs -> head;
    while(curr != NULL) {
        // print details in tabular format :)
        printf("[%d]\t%s\t%d\t%s\n", curr -> jid, curr -> status, curr -> pid, curr -> name);
        curr = curr -> next;
        // if(strcmp(curr -> status,STOPPED_M) == 0){
        //     remove_job(curr -> pid,true);
        // }
    }
}


// :) removes all the jobs from the list, gracefully
void remove_all_jobs() {
    if(jobs == NULL) {
        return;
    }
    job_node* curr = jobs -> head;
    while(curr != NULL) {
        job_node* next = curr -> next;
        free(curr -> name);
        free(curr);
        curr = next;
    }
    jobs -> head = NULL;
    jobs -> tail = NULL;
    jobs -> num_jobs = 0;
}


// return -1 if empty/size=0 else returns earliest pid
int return_spid() {
    if(jobs == NULL || jobs -> num_jobs == 0) {
        return -1;
    }
    return jobs -> head -> pid;
}

/* swaps the pid of a process given a pid */
bool swap_status(pid_t p) {
    // search on pid
    job_node* j = get_job(p, true);
    if(j == NULL)
        return false;
    if(strcmp(j -> status, RUNNING_M) == 0)
        j -> status = STOPPED_M;
    else
        j -> status = RUNNING_M;
    return true;
}

void swap_all() {
    if(jobs == NULL)
        return;
    job_node* c = jobs -> head;
    while(c != NULL) {
        swap_status(c -> pid);
        c = c -> next;
    }

}


void print_process_table() {
    printf("PGID\tPID\tTIME\tCMD\n");
    if(jobs == NULL)
        return;
    job_node* c = jobs -> head;
    while(c != NULL) {
        printf("%d\t%d\t00:01\t%s\n", c -> pid, c -> pid, c -> name);
        c = c -> next;
    }
}