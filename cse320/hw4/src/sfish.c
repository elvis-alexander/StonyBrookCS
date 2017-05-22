#include "sfish.h"
#include "jobs.h"

// global non-evil globals (non-threaded perhaps)
pid_t foreground_pid = -1;
char* foreground_command = NULL;
int spid = -1;

int total_num_commands = 0;
int prt_cc = -1;

// child triggered iff
// the child terminated
// the child was stopped by a signal
// the child was resumed by a signal
void child_handler(int s) {
    // printf("entered childhandler\n");
    sigset_t prev_one;
    sigset_t mask_all;
    sigfillset(&mask_all);
    // int status;
    pid_t c = waitpid(-1, NULL, WUNTRACED | WNOHANG);
    // printf("status: %d\n", status);
    if(c > 0) {
        sigprocmask(SIG_BLOCK, &mask_all, &prev_one);
        // printf("will swap\n");
        swap_status(c);
        sigprocmask(SIG_SETMASK, &prev_one, NULL);
    }
}

// ctrl-c
void kill_foreground(int s) {
    // remove job
    if(foreground_pid == -1) return;
    remove_job(foreground_pid, true);
    kill(foreground_pid, SIGINT);
}

// ctrl-z
// change status
void suspend_process(int s) {
    // printf("\nctrl-z stopping...\n");
    if(foreground_pid == -1)
        return; // not foreground has been set
    job_node* j = get_job(foreground_pid, true);
    if(j == NULL) {
        // add last, stopped status
        add_last(STOPPED_M, foreground_pid, foreground_command);
    } 
    j = get_job(foreground_pid, true);
    if(j != NULL)
        printf("[%d] %d stopped by signal 20\n", j -> jid, j -> pid);
    kill(foreground_pid, SIGTSTP);  // maybe insert in if statement (dont think so actually)
    // change status of current foreground process
    // swap_status(foreground_pid);
}

int main(int argc, char** argv) {
    //DO NOT MODIFY THIS. If you do you will get a ZERO.
    rl_catch_signals = 0;
    //This is disable readline's default signal handlers, since you are going
    //to install your own.
    // user command (unparsed)
    char *cmd;

    // store user and host_name data (global)
    user = getenv("USER");
    host_name = (char*)malloc(sizeof(BUFF_SIZE));
    gethostname(host_name, BUFF_SIZE);

    // initialize user and machine to enabled
    user_tag = ENABLED;
    machine_tag = ENABLED;
    
    // add signal handlers
    signal(SIGINT, kill_foreground);
    signal(SIGCHLD, child_handler);
    signal(SIGTSTP, suspend_process);

    // bind hot-keys for part 5
    rl_bind_key(CTRL_H, print_help);    // prints help
    rl_bind_key(CTRL_B, store_spid);    // store pid
    rl_bind_key(CTRL_G, get_spid);      // gets pid
    rl_bind_key(CTRL_P, sfish_info);    // shell info table

    // read line removes the last new line
    while(true) {
        // @TODO change this before submission
        cmd = readline(get_prompt(user_tag, machine_tag));
        // cmd = readline("sfish> ");

        if(cmd == NULL || *cmd == 0)
            continue;
        if ((strcmp(cmd,"quit")==0) || (strcmp(cmd, "q") == 0))
            break;
        ++total_num_commands;
        // split on space, input array
        string_array_node* node = to_array(cmd, " ");
        if(get_builtin(node) != -1) {  
            process_builtin(node);
        } else {
            char* cmd_cpy = (char*)malloc(strlen(cmd) + 1);
            memcpy(cmd_cpy, cmd, strlen(cmd));
            *(cmd_cpy + strlen(cmd)) = '\0';
            process_executable(node, cmd_cpy);
        }
    }
    //Don't forget to free allocated memory, and close file descriptors.
    free(cmd);
    //WE WILL CHECK VALGRIND!
    return EXIT_SUCCESS;
}

/*

    // place a null at &
    *(node -> arr + end_index) = '\0';
    node -> len = node -> len - 1;
    // background process
    pid_t p = Fork();
    if(p == 0) {         
        setpgid(0, 0);
        spawn_process(0, 1, node -> arr);
       // exit(0);
   } else {
       add_last(RUNNING_M, p, *(node -> arr));
   }
*/

// main execution point for executable
void process_executable(string_array_node* node, char* cmd_cpy) {
    // @TODO support bg/fg on redirection commands
    // determine if input/output needed
    bool c = contains_redirection(node);
    // handle redirection
    if(c == true) {

        // Redirection fg vs bg
        int end_index = node -> len - 1;
        bool is_background = false;
        if(strcmp(*(node -> arr + end_index), "&") == 0)
            is_background = true;
        if(is_background == true) {
            ////////////////////////////////
            // place a null at &
            *(node -> arr + end_index) = NULL;
            node -> len = node -> len - 1;
            // background process
            pid_t p = Fork();
            if(p == 0) {         
                setpgid(0, 0);
                process_redirection(node);
                // exit(0);/////####
                return;
           } else {
               add_last(RUNNING_M, p, cmd_cpy);
               return;
           }
            ////////////////////////////////
           return;
        } else {
            // foreground
            // fork and process redirection
            pid_t p = Fork();
            if(p == 0) {
                // parse command and execute.
                setpgid(0, 0);
                process_redirection(node);
                exit(0);    // should not occur
            } else {
                // this will change for jobs
                foreground_pid = p;
                foreground_command = cmd_cpy;
                // wait for any child process to that has stopped
                int sta;
                waitpid(-1, &sta, WUNTRACED);   // return if child has stopped
                prt_cc = WEXITSTATUS(sta);
                return;
            }
        }

    }


    // reg command ~ no output/input or piping redirection
    // validate command
    char* executable = *(node -> arr);
    if(contains_slash(executable) == true) {
        if(file_exists(executable) == false) {
            printf("sfish: %s: No such file or directory\n", executable);
            return;
        }
    } else {
        char* path = get_path(node);
        if(path == NULL) {
            printf("sfish: %s: command not found\n", executable);
            return;
        }
    }

    ////////////******************///////////////
    // determine if this is a regular job or background
    int end_index = node -> len - 1;
    bool is_background = false;
    if(strcmp(*(node -> arr + end_index), "&") == 0)
        is_background = true;
    if(is_background == true) {
        // place a null at &
        *(node -> arr + end_index) = '\0';
        node -> len = node -> len - 1;
        // background process
        pid_t p = Fork();
        if(p == 0) {         
            setpgid(0, 0);
            spawn_process(0, 1, node -> arr);
           // exit(0);
       } else {
           add_last(RUNNING_M, p, cmd_cpy);
       }
    } else {
        // foreground process
        pid_t p = Fork();
        if(p == 0) {
            setpgid(0, 0);
            // returns -1, on error else no return
            spawn_process(0, 1, node -> arr);   // foreground: stdin/stdout
            // printf("eeeeee\n");
            exit(0);
        } else {
            foreground_pid = p;
            foreground_command = cmd_cpy;
            // wait for any child process to that has stopped
            
            int sta;    
            waitpid(-1, &sta, WUNTRACED);   // return if child has stopped
            prt_cc = WEXITSTATUS(sta);
        }

    }
}

/*
    fg JID|PID
    testing sequence for fg
    i).  call process in the background {}
    ii). call fg pid
*/
void fg_builtin(string_array_node* node) {
    if(node -> len < 2)
        return; // no id specified
    int id = get_id(node, 1);
    bool search_type = is_pid_search(node, 1);
    job_node* j = get_job(id, search_type);
    // bool b = remove_job(id, search_type);       // move from bg to fd
    if(j == NULL) {//b == false){
        printf("sfish: fg: %d no such job\n", id);
        return; // @TODO - added this sould test...
    }
    foreground_pid = j -> pid;
    foreground_command = j -> name;
    kill(j -> pid, SIGCONT);
    int sta;
    waitpid(-1, &sta, WUNTRACED);
    prt_cc = WEXITSTATUS(sta);
    // it finished swap status
    if(strcmp(j -> status, RUNNING_M) == 0)
        swap_status(j -> pid);
    // printf("done waiting\n"); // @TODO REMOVE THIS BEFORE SUBMISSION
}

/*
    bg
    if a process is stopped aka ctrl-z (suspend),
    then get the process specified and continue it?
*/
void bg_builtin(string_array_node* node) {
    if(node -> len < 2)
        return; // no id specified
    int id = get_id(node, 1);
    bool search_type = is_pid_search(node, 1);
    job_node* j = get_job(id, search_type);
    if(j == NULL) {
        printf("sfish: bg: %d no such job\n", id);
        return; // @TODO - create test...
    }
    swap_status(j -> pid);
    if(j != NULL)
        printf("[%d] %d %s\n", j -> jid, j -> pid, j -> name);
    kill(j -> pid, SIGCONT);
    // @TODO SET status to running
    // kill(id,SIGCONT);//REMOVE ME AND UNCOMENT REST//////// @TODO
}


// return prompt, modify on parameter
char* get_prompt(int u, int m) {
    char* dir = pwd_builtin();
    char* home = getenv("HOME");

    // set dir to tilda if currently at home directory
    if(strcmp(dir, home) == 0) {
        dir = "~";
    } else {
        // get dir relative to home
        dir = dir + strlen(home);
    }
    char* prompt = (char*)malloc(sizeof(char) * BUFF_SIZE);
    
    char* g = get_git_info();
    if(u == ENABLED && m == ENABLED) {
        if(g == NULL)
            sprintf(prompt, "sfish-%s@%s:[%s]> ", user, host_name, dir);
        else
            sprintf(prompt, "sfish-%s@%s:[%s (%s]> ", user, host_name, dir, g);
    } else if(u == ENABLED && m == DISABLED) {
        if(g == NULL) 
            sprintf(prompt, "sfish-%s:[%s]> ", user, dir);
        else
           sprintf(prompt, "sfish-%s:[%s (%s)]> ", user, dir, g); 
    } else if(u == DISABLED && m == ENABLED) {
        if(g == NULL)
            sprintf(prompt, "sfish-%s:[%s]> ", host_name, dir);
        else
            sprintf(prompt, "sfish-%s:[%s (%s]> ", host_name, dir, g);
    } else if(u == DISABLED && m == DISABLED){
        if(g == NULL)
            sprintf(prompt, "sfish:[%s]> ", dir);
        else
            sprintf(prompt, "sfish:[%s]> ", dir);
    }
    return prompt;
}

// returns array on tokenized of string
// algorithm: find number of strings then re-tokenize and store
string_array_node* to_array(char* s, const char* d) {
    // return value stores array and len
    string_array_node* node = (string_array_node*)malloc(sizeof(string_array_node));
    // result array for string
    char** a = NULL;
    // size of array
    size_t size = 0;
    // temporary token
    char* token = NULL;
    // store a copy of string for re-tokenize
    char* s_cpy_one = (char*)malloc(sizeof(char) * strlen(s) + 1);
    strcpy(s_cpy_one, s);
    char* s_cpy_two = (char*)malloc(sizeof(char) * strlen(s) + 1);
    strcpy(s_cpy_two, s);
    // counter
    int i = 0;

    /* first token */
    if((token = strtok(s_cpy_one, d)) == NULL) {
        /* either empty or all spaces */
        return NULL;
    }
    /* determine number of tokens */
    while(token != NULL) {
        ++size;
        token = strtok(NULL, d);
    }
    // set size of array
    a = (char**)malloc((size + 1) * sizeof(char *));
    *(a + size) = NULL;

    // re-tokenize and store in return value
    token = strtok(s_cpy_two, d);
    int len = strlen(token);
    char* token_cpy = (char*)malloc(sizeof(char) * (len + 1));
    memcpy(token_cpy, token, len);
    *(token_cpy + len) = '\0';
    *(a + i) = token_cpy;
    for(i = 1; i < size; ++i) {
        token = strtok(NULL, d);
        len = strlen(token);
        token_cpy = (char*)malloc(sizeof(char) * (len + 1));
        memcpy(token_cpy, token, len);
        *(token_cpy + len) = '\0';
        *(a + i) = token_cpy;
    }
    /*for(i = 0; i < size; ++i) {
        printf("{ptr, val} : {[%p], [%s]}\n", a+i, *(a+i));
    }*/
    node -> arr = a;
    node -> len = size;
    return node;
}


/* returns true if any of the commands include a '<', '>', or '|' */
bool contains_redirection(string_array_node* node) {
    int i = 0;
    for(i = 0; i < node -> len; ++i) {
        char* curr = (*(node -> arr + i));
        if(strstr(curr, input_redirection) != NULL)
            return true;
        else if(strstr(curr, output_redirection) != NULL)
            return true;
        else if(strstr(curr, pipe_redirection) != NULL)
            return true;
    }
    return false;
}

void display(string_array_node* node) {
    int i = 0;
    for (i = 0; i < node -> len; ++i) {
        printf("i: {%d} val: {%s}\n", i, *(node->arr + i));
    }    
}

void display_split(pipe_node* p) {
    int i = 0;
    int j = 0;
    string_array_node** a = p -> pipe_cmd;
    for(i = 0; i < p -> len; ++i) {
        string_array_node* c = *(a + i);
        for(j = 0; j < c -> len; ++j) {
            printf("j: {%d} val: {%s}\n", j, *(c -> arr + j));
        }
    }
}

// 320 samples
// ls -l > out.txt {files...}
// grep dog < in.txt {dog,dog,dog}
// echo "hello world" > out.txt
// cat < in.txt > out.txt {output of in.txt}
// ls | grep .txt | wc -l {}
// 
// ls | wc -l {8}
// cat < in.txt | grep dog   {dog,dog,dog}
// cat < in.txt | grep dog | wc -l > out.txt (3)
// cat < in.txt | grep dog > o.txt > o2.txt  (dog,dog,dog)
void process_redirection(string_array_node* node) {
    // display(node);
    pipe_node* parsed_command_node = split_array(node);
    // display_split(parsed_command_node);
    string_array_node** parsed_command_arr = parsed_command_node -> pipe_cmd;
    char* file_name = NULL;
    int i = 0;
    int input_fd = STDIN_FILENO;    // init stdin
    // int output_fd = STDOUT_FILENO;  // init stdout
    int pipe_fd[2];
    pid_t pid;
    bool pipe_flag = false;
    // 
    char* prev_cmd = *((*(parsed_command_arr + i)) -> arr);
    string_array_node* prev_args_node = to_array(prev_cmd, " ");
    char** prev_args = prev_args_node -> arr;
    
    for(i = 0; i < parsed_command_node -> len; ++i) {    
        char** curr_cmd_arr = (*(parsed_command_arr + i)) -> arr;
        char* cmd = *curr_cmd_arr;
        if(strcmp(cmd, pipe_redirection) == 0) {
            //////////////VALIDATE EXECUTABLE//////////////
            if(validate_exec(*prev_args) == false)
                exit(0);
            //////////////VALIDATE EXECUTABLE//////////////
            pipe(pipe_fd);
            if((pid = Fork()) == 0) {
                // setpgid(0, 0);
                spawn_process(input_fd, pipe_fd[1], prev_args);
                // printf("done with child\n");
                exit(0);    // should not occur
            } else {
                // no need to write (child will take care of this) 
                close(pipe_fd[1]);
                // set input to write side of process
                input_fd = pipe_fd[0];
                wait(NULL);
                prev_cmd = *((*(parsed_command_arr + i + 1)) -> arr);
                prev_args_node = to_array(prev_cmd, " ");
                prev_args = prev_args_node -> arr;
                pipe_flag = true;
            }
        } else if(strcmp(cmd, input_redirection) == 0) {
            file_name = *((*(parsed_command_arr + i + 1)) -> arr);
            redirect_input(file_name);  // ./foo < in.txt
        } else if(strcmp(cmd, output_redirection) == 0) {
            file_name = *((*(parsed_command_arr + i + 1)) -> arr);
            redirect_output(file_name); // ls -l > out.txt
        }
    }

    // exit, on invalid executable
    if(validate_exec(*prev_args) == false)
        exit(0);
    // execute program :) (@TODO this will change for fg/bg job) 
    if(pipe_flag == true) {
        // prev_args
        pid = Fork();
        if(pid == 0) {
            // setpgid(0, 0);
            spawn_process(input_fd, 1, prev_args); // pipe_fd[0], STDOUT_FILENO
        } else {
            wait(NULL);
        }
    } else {
        spawn_process(0, 1, prev_args);
    }

}

// 
int spawn_process(int input_fd, int output_fd, char** args) {
    if (input_fd != 0) {
        dup2 (input_fd, 0);
        close (input_fd);
    }
    if (output_fd != 1) {
        dup2 (output_fd, 1);
        close (output_fd);
    }
    return execvp(*args, args);
}

// 
pipe_node* split_array(string_array_node* node) {
    // returns an array of 
    int i = 0;
    int num_redirection = num_occurences(node);
    int length = num_redirection * 2 + 2;   // + 1 for null terminator
    int start_index = 0;
    string_array_node** new_array = (string_array_node**)malloc(sizeof(string_array_node*) * (length));

    // -1 for null terminator at end
    for(i = 0; i < length - 1; ++i) {
        char* curr = *(node -> arr + start_index);
        if((strcmp(curr, input_redirection) == 0) || (strcmp(curr, output_redirection) == 0) || (strcmp(curr, pipe_redirection) == 0)) {
            // copy redirection
            *(new_array + i) = (string_array_node*)malloc(sizeof(string_array_node));
            // one for string contents and one for null terminator
            (*(new_array + i)) -> arr = (char**)malloc(sizeof(char*) * 2);
            (*(new_array + i)) -> len = 1;
            array_cpy(node -> arr, start_index, start_index + 1, (*(new_array + i)) -> arr);
            ++start_index;  // next cmd
        } else {
            // gather all string from start up until index of next 
            int end_index = index_of(node, start_index);
            if(end_index == -1) {
                end_index = node -> len;// start_index + 1;
            }
            *(new_array + i) = (string_array_node*)malloc(sizeof(string_array_node));
            // one for string contents and one for null terminator
            (*(new_array + i)) -> arr = (char**)malloc(sizeof(char*) * 2);
            (*(new_array + i)) -> len = 1;
            array_cpy(node -> arr, start_index, end_index, (*(new_array + i)) -> arr);
            start_index = end_index;
        }
    }
    *(new_array + i) = 0;
    pipe_node* new_pipe_node = (pipe_node*)malloc(sizeof(pipe_node));
    new_pipe_node -> pipe_cmd = new_array;
    new_pipe_node -> len = length - 1;  // dont include null
    return new_pipe_node;
}

/* */
void array_cpy(char** src, int src_start, int src_end, char** dst) {
    int i = src_start;
    int new_len = 0;
    for(i = src_start; i < src_end; ++i) {
        new_len += strlen(*(src + i));
        ++new_len;  // for space /(slash) '\0'
    }
    char* concat_str = (char*)malloc(new_len);
    char* tmp = concat_str;
    for(i = src_start; i < src_end; ++i) {
        char* s_cpy = *(src + i);
        memcpy(tmp, s_cpy, strlen(s_cpy));
        tmp += strlen(s_cpy);
        if(i != src_end - 1) {
            memcpy(tmp, " ", 1);
            ++tmp;
        } else {
            memcpy(tmp, "\0", 1);
        }
    }
    // printf("tmp->start: %s\n", concat_str);
    *(dst) = concat_str;
    *(dst + 1) = 0;
}

// returns number of occurences of >, <, and | in a string-array
int num_occurences(string_array_node* node) {
    int count = 0;
    int i = 0;
    for(i = 0; i < node -> len; ++i) {
        char* curr_input = (*(node -> arr + i));
        if((strcmp(curr_input, input_redirection) == 0) || (strcmp(curr_input, output_redirection) == 0) || (strcmp(curr_input, pipe_redirection) == 0)) {
            ++count;
        }
    }
    return count;
}

/* returns index_of node that contains a '<' or '>' or '|' */
int index_of(string_array_node* src, int start_index) {
    int curr_index = start_index;
    while(curr_index < src -> len) {
        char* curr_input = (*(src -> arr + curr_index));
        if((strcmp(curr_input, input_redirection) == 0) || (strcmp(curr_input, output_redirection) == 0) || (strcmp(curr_input, pipe_redirection) == 0)) {
            return curr_index;
        }
        ++curr_index;
    }
    return -1;
}

/* used to validate excutables on redirections */
char* get_potential_path(char* e) {
    // environment path delimited by :
    char* str_path = getenv("PATH");
    // array of environment paths array 
    string_array_node* paths = to_array(str_path, PATH_DELIM);
    // iterate throuhgh paths array 
    char* potential_path = NULL;
    for(int i = 0; i < paths -> len; ++i) {
        potential_path = concat_path(*(paths -> arr + i), e);
        // add directory with file and verify existence
        if(file_exists(potential_path))
            return potential_path;
        free(potential_path);
    }
    return NULL;
}

/* used to validate excutables on redirections */
bool validate_exec(char* e) {
    if(contains_slash(e) == true) {
        if(file_exists(e) == false) {
            printf("sfish: %s: No such file or directory\n", e);
            return false;
        }
    } else {
        char* path = get_potential_path(e);
        if(path == NULL) {
            printf("sfish: %s: command not found\n", e);
            return false;
        }
    }
    return true;
}

/* given a name of a file redirect input to descriptor specified */
void redirect_input(char* file) {
    int in_fd = open(file, O_RDONLY, S_IRWXU | S_IRWXG | S_IRWXO);
    if(in_fd == -1) {
        // exit child process on invalid file
        printf("sfish: %s: No such file or directory\n", file);
        exit(EXIT_SUCCESS);
    }
    dup2(in_fd, STDIN_FILENO);
    close(in_fd);
}

/* given a file name redirects output to descriptor specified */
void redirect_output(char* file) {
    // assuming output redirection
    if(file_exists(file) == true) {
        remove(file);
    }
    // create/open file
    int out_fd = open(file, O_WRONLY | O_CREAT, S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH);
    // closes output file and STDOUT be copy of out_fd
    dup2(out_fd, STDOUT_FILENO);
    close(out_fd);
}

/* searches executable file name over set of path environment variables */
char* get_path(string_array_node* node) {
    // executable is first value of array
    char* executable = *(node -> arr);
    // environment path delimited by :
    char* str_path = getenv("PATH");
    // array of environment paths array 
    string_array_node* paths = to_array(str_path, PATH_DELIM);
    // iterate throuhgh paths array 
    char* potential_path = NULL;
    for(int i = 0; i < paths -> len; ++i) {
        potential_path = concat_path(*(paths -> arr + i), executable);
        // add directory with file and verify existence
        if(file_exists(potential_path))
            return potential_path;
        free(potential_path);
    }
    return NULL;
}

/* simple concat helper directory + '/' + file + '\0' */
char* concat_path(char* path, char* file) {
    char* c = (char*)malloc(strlen(path) + strlen(file) + 2);
    char* temp = c;
    strcpy(c, path);
    temp += strlen(path);
    strcat(temp, "/");
    temp += 1;
    strcat(temp, file);
    temp += strlen(file);
    *temp = '\0';
    return c;
}

/* returns true if file exists */
bool file_exists(char* path) {
    struct stat buf;
    return stat(path, &buf) == 0 ? true : false;
}

/* returns true if string contains slash */
bool contains_slash(char* s) {
    int len = strlen(s);
    int i;
    for(i = 0; i < len; ++i) {
        if(*s == slash)
            return true;
        s = s + 1;
    }
    return false;
}

/* Fork Wrapper */
pid_t Fork() {
    pid_t pid;
    if((pid = fork()) < 0) {
        printf("Failed to create process.\n");
        exit(EXIT_FAILURE);
    }
    return pid;
}

// main execution point for built-in
void process_builtin(string_array_node* node) {
    int builtin = get_builtin(node);
    if(builtin == HELP_C) {
        // print help menu ~ redirection not supported yet
        printf("%s", HELP_MENU);
    } else if(builtin == EXIT_C) {
        // exit program ~ 
        exit(EXIT_SUCCESS);
    } else if(builtin == CD_C) {
        // cd command ~ 
        cd_built(node);
    } else if(builtin == PWD_C) {
        // pwd command ~ redirection not supported yet
        char* p = pwd_builtin();
        printf("%s\n", p);
    } else if(builtin == PRT_C) {
        printf("prt_cc: %d\n", prt_cc);
    } else if(builtin == CHPMT_C) {
        // change prompt settings ~ 
        change_prompt(node);
    } else if(builtin == CHCLR_C) {
        // change color settings ~ 
        change_color(node);
    } else if(builtin == JOBS_C) {
        display_jobs();
    } else if(builtin == FG_C) {
        fg_builtin(node);
    } else if(builtin == BG_C) {
        bg_builtin(node);
    } else if(builtin == KILL_C) {
        kill_builtin(node);
    } else if(builtin == DISOWN_C) {
        disown_builtin(node);
    }else {
        printf("not supported yet\n");  // @TODO
    }
}

// returns -1 for non built-in, else return built-in code
int get_builtin(string_array_node* node) {
    char* cmd = *(node -> arr);
    if(strcmp(cmd, HELP) == 0) {
        return HELP_C;
    } else if(strcmp(cmd, EXIT) == 0) {
        return EXIT_C;
    } else if(strcmp(cmd, CD) == 0) {
        return CD_C;
    } else if(strcmp(cmd, PWD) == 0) {
        return PWD_C;
    } else if(strcmp(cmd, PRT) == 0) {
        return PRT_C;
    } else if(strcmp(cmd, CHPMT) == 0) {
        return CHPMT_C;
    } else if(strcmp(cmd, CHCLR) == 0) {
        return CHCLR_C;
    } else if(strcmp(cmd, JOBS) == 0) {
        return JOBS_C;
    } else if(strcmp(cmd, FG) == 0) {
        return FG_C;
    }else if(strcmp(cmd, BG) == 0) {
        return BG_C;
    } else if(strcmp(cmd, KILL) == 0) {
        return KILL_C;
    } else if(strcmp(cmd, DISOWN) == 0) {
        return DISOWN_C;
    }
    return -1;
}

// execution for cd built-in, passed in entire input str
void cd_built(string_array_node* node) {
    int len = node -> len;
    if(len == 1) {
        // change to home directory
        chdir(getenv("HOME"));
        return;
    }
    // path specified
    char* path = (*(node -> arr + 1));    
    // previous working directory
    if(strcmp(path, "-") == 0) {
        if(old_pwd == NULL) {
            // cd not been used
            printf("sfish: cd: OLDPWD not set\n");
        } else {
            char* new_old = pwd_builtin();
            chdir(old_pwd);
            free(old_pwd);
            old_pwd = new_old;
        }
        return;
    }
    // store current pwd
    free(old_pwd);
    char* prev = pwd_builtin();
    // attemp to change path - report msg on error
    if(chdir(path) == -1) {
        printf("sfish: cd: %s: No such file or directory\n", path);
    } else {    
        old_pwd = prev;
    }
}

// execution for pwd built-in
// this does redirect, fix later
char* pwd_builtin() {
    char* p = (char*)malloc(sizeof(char) * BUFF_SIZE);
    if((p = getcwd(p, BUFF_SIZE)) != NULL) {
        return p;
    }
    return NULL;
}

// change prompt setting
// chpmt [user|machine] [0|1]
void change_prompt(string_array_node* node) {
    // atleast three args (chmpt|setting|toggle)
    if(node -> len >= 3) {
        char** arr = node -> arr;
        // setting can be user or machine
        char* setting = *(arr + 1);
        // toggle can be enabled or disabled
        char* toggle = *(arr + 2);
        // user changes user option
        if(strcmp(setting, "user") == 0) {
            if(strcmp(toggle, "0") == 0) {
                user_tag = DISABLED;
            } else if(strcmp(toggle, "1") == 0) {
                user_tag = ENABLED;
            }
        } else if(strcmp(setting, "machine") == 0) {
            // user changes machine option
            if(strcmp(toggle, "0") == 0) {
                machine_tag = DISABLED;
            } else if(strcmp(toggle, "1") == 0) {
                machine_tag = ENABLED;
            }
        }
    }
}

// change color of prompt
// chclr SETTING COLOR BOLD
void change_color(string_array_node* node) {
    // exit(-1);
    /*if(node -> len < 4)
        return;
    char** arr = node -> arr;
    char* setting = *(arr + 1);
    char* color = *(arr + 2);
    char* bold = *(arr + 3);
    // check for user
    if(strcmp(setting, "user") == 0) {
        
    } else if(strcmp(setting, "machine") == 0) {
        
    }
    //
    if(strcmp(color, "red") == 0) {

    } else if(strcmp(color, "blue") == 0) {

    } else if(strcmp(color, "green") == 0) {

    } else if(strcmp(color, "yellow") == 0) {

    } else if(strcmp(color, "cyan") == 0) {

    } else if(strcmp(color, "magneta") == 0) {

    } else if(strcmp(color, "black") == 0) {

    } else if(strcmp(color, "white") == 0) {

    }*/
}

/* returns jid if % specified, else returns pid */
int get_id(string_array_node* node, int offset) {
    char* string_id = *(node -> arr + offset);
    bool search_pid = *(string_id) != 37 ? true : false;
    if(!search_pid)
        string_id += 1; // ignore percent
    return atoi(string_id);
}

/* returns true if not jid flag is shown */
bool is_pid_search(string_array_node* node, int offset) {
    char* string_id = *(node -> arr + offset);
    return *(string_id) == 37 ? false : true;
}



/* kill [signal] PID|JID */
/* kill PID|JID */
/* default SIGTERM - 15 */
void kill_builtin(string_array_node* node) {
    if(node -> len < 2)
        return;
    int signal = SIGTERM;
    int id_index = 1;       // default
    if(node -> len > 2) {
        // change signal
        signal = atoi(*(node -> arr + 1));
        // id will be at the end
        id_index = 2;
    }
    if(signal < 1 || signal > 31){
        printf("sfish: kill: %d: invalid signal specification\n", signal);
        return;
    }
    int id = get_id(node, id_index);    // may be a jid
    bool search_type = is_pid_search(node, id_index);
    int actual_id = id;
    // get the job for a given id
    job_node* j = get_job(id, search_type);
    if(j == NULL){
        printf("sfish: kill: (%d) no such process\n", id);
        return;
    }
    if(search_type == false)
        actual_id = j -> pid;   // set corresponding pid for a jid
    // printf("id: %d, actual_id:%d, signal: %d\n", id, actual_id, signal);
    int jid = j -> jid;
    int pid = j -> pid;
    printf("[%d] %d stopped by signal %d\n", jid, pid, signal);
    kill(actual_id, signal);
}


// disown [PID|JID] 
// this deletes the specified job 
// if not pid is specified delete all jobs from list
void disown_builtin(string_array_node* node) {
    if(node -> len < 2) {
        remove_all_jobs();
        return;
    }
    int id = get_id(node, 1);
    bool search_type = is_pid_search(node, 1);
    bool b = remove_job(id, search_type);       // move from bg to fd
    if(b == false){
        printf("sfish: fg: %d no such job\n", id);
    }
}

/* start of key bindings */
/* ctrl-h */
int print_help(int a, int b) {
    printf("%s", HELP_MENU);
    return 1;
}

/* ctrl-b, stores earliest pid */
int store_spid(int a, int b) {
    spid = return_spid();
    return 1;
}

/* ctrl-g, kills process specified by ctrl-b */
int get_spid(int a, int b) {
    // set spid to -1, if invalid
    job_node* j = get_job(spid, true);
    if(j == NULL) {
        spid = -1;
        printf("SPID does not exist and has been set to -1\n");   
        return 1;
    }
    // pid exists, send it SIGTERM
    printf("[%d] %d stopped by signal 15\n", j -> jid, j -> pid);
    kill(spid, SIGTERM);    
    return 1;
}

/*  */
int sfish_info(int a, int b) {
    printf("\n----Info----\n");
    printf("help\n");
    printf("ptr\n");
    printf("----CTRL----\n");
    printf("cd\n");
    printf("chclr\n");
    printf("pwd\n");
    printf("exit\n");
    printf("----Job Control----\n");
    printf("bg\n");
    printf("fg\n");
    printf("disown\n");
    printf("jobs\n");
    printf("----Number of Commands Run----\n");
    printf("%d\n", total_num_commands);
    printf("----Process Table----\n");
    print_process_table();
    return 1;
}



/* extra credit */


/* very messy code for extra credit (works :D) */
char* get_git_info() {
    struct stat sb;
    const char *pathname = ".git";
    bool is_up_to_date = true;
    if(stat(pathname, &sb) == 0 && S_ISDIR(sb.st_mode)) {
        // add a star iff return 1 on 
        pid_t p = Fork();
        char* file = "temp_file_for_storing_git_output.txt";
        if(p == 0) {
            int f = open(file, O_WRONLY | O_CREAT, S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH);
            dup2(f, 1);
            execvp(git_diff[0], git_diff);
        } else {
            int wait_status;
            wait(&wait_status);
            remove(file);
            if(WEXITSTATUS(wait_status) == 1) {
                is_up_to_date = false;
            }
        }
        // set git_info to correct data
        int link[2];
        pipe(link);
        pid_t f = Fork();
        char* branch_name = (char*)malloc(BUFF_SIZE);
        if(f == 0) {
            dup2(link[1], STDOUT_FILENO);
            close(link[0]);
            close(link[1]);
            execvp(branch_cmd[0], branch_cmd);
            exit(0);
        } else {
            close(link[1]);
            read(link[0], branch_name, BUFF_SIZE);
            int len = strlen(branch_name) - 1;
            wait(NULL);
            if(is_up_to_date == true) {
                *(branch_name + len) = ')';
                *(branch_name + len + 1) = '\0';
                return branch_name;
            } else {
                *(branch_name + len) = ')';
                *(branch_name + len + 1) = '*';
                *(branch_name + len + 2) = '\0';
                return branch_name;
            }
        }
    } else {
        return NULL;
    }
}