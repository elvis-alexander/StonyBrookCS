#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <signal.h>
#include <readline/readline.h>
#include <readline/history.h>
#include <errno.h>


// buffer size for pwd//
#define BUFF_SIZE 1024

/* built-in commands */
// codes
#define HELP_C 0
#define EXIT_C 1
#define CD_C 2
#define PWD_C 3
#define PRT_C 4
#define CHPMT_C 5
#define CHCLR_C 6
#define JOBS_C 7
#define FG_C 8
#define BG_C 9
#define KILL_C 10
#define DISOWN_C 11
// strings for built-ins
#define HELP "help"
#define EXIT "exit"
#define CD "cd"
#define PWD "pwd"
#define PRT "prt"
#define CHPMT "chpmt"
#define CHCLR "chclr"
#define JOBS "jobs"
#define FG "fg"
#define BG "bg"
#define KILL "kill"
#define DISOWN "disown"
// for terminal - builtin-s
#define ENABLED 1
#define DISABLED 0
// bold
#define RESET "\e[0m"
#define BOLD "\e[4m"// change to 1
// color
#define RED 	"\e[31m"
#define BLUE 	"\e[34m"
#define GREEN 	"\e[32m"
#define YELLOW 	"\e[33m"
#define CYAN 	"\e[36m"
#define MAGENTA "\e[45m"
#define BLACK 	"\e[30m"
#define WHITE 	"\e[97m"

// for help menu
#define HELP_MENU "help\nexit\ncd [dir]\npwd\nprt\nchpmt [SETTING] [TOGGLE]\nchclr [SETTING] [COLOR] [BOLD]\njobs\nfg [PID|JID]\nbg [PID|JID]\nkill [signal] [PID|JID]\ndisown [PID|JID]\n"

/* end of built-in commands */

/* start of exectuables/redirection */
#define PATH_DELIM ":"

// 
#define CTRL_H 8	// print help menu
#define CTRL_B 2	// store pid
#define CTRL_G 7	// get pid
#define CTRL_P 16	// sfish info
/* terminal data */
// global flags for terminal
char* user = NULL;
char* host_name = NULL;
// flags get turned on/off
int user_tag;
int machine_tag;

// utility to help parse arguments
// stores str array with len and data
typedef struct string_array_node{
	size_t len;
	char** arr;
}string_array_node;

typedef struct pipe_node {
	string_array_node** pipe_cmd;
	int len;
}pipe_node;

// helper to parse command
string_array_node* to_array(char* s, const char* d);

// built-in helpers and executors
char* get_prompt(int u, int m);
int get_builtin(string_array_node* node);
void process_builtin(string_array_node* node);
char* pwd_builtin();
void cd_built();
void change_prompt(string_array_node* node);
void change_color(string_array_node* node);
void fg_builtin(string_array_node* node);
void bg_builtin(string_array_node* node);
void kill_builtin(string_array_node* node);
void disown_builtin(string_array_node* node);
/* end of builtins */


// store previous working directory
char* old_pwd = NULL;
// prompt options
char* user_bold = RESET;
char* user_color = CYAN;
char* machine_bold = RESET;
char* machine_color = YELLOW;
// end of built-ins

// helpers for executable
char slash = '/';
// for redirection
char* input_redirection = "<";
char* output_redirection = ">";
char* pipe_redirection = "|";

// parsing and control flow
void process_executable(string_array_node* node, char* cmd_cpy);
bool contains_redirection(string_array_node* node);
void process_redirection(string_array_node* node);
char* get_path(string_array_node* node);
char* concat_path(char* path, char* file);
bool contains_slash(char* s);
bool file_exists(char* path);
pid_t Fork();

// redirection helpers
void redirect_output(char* file);
void redirect_input(char* file);
int index_of(string_array_node* src, int start_index);
int num_occurences(string_array_node* node);
void array_cpy(char** src, int src_start, int src_end, char** dst);
pipe_node* split_array(string_array_node* node);
int spawn_process(int input_fd, int output_fd, char** args);
char* get_potential_path(char* e);
bool validate_exec(char* e);

/* start of builtin */
// helper for jobs builtins
int get_id(string_array_node* node, int offset);
bool is_pid_search(string_array_node* node, int offset);
// handler methods for jobs builtin
void child_handler(int s);
void kill_foreground(int s);
void suspend_process(int s);
/* end of builtins */

/* custom features  part X */
int print_help(int a, int b);
int store_spid(int a, int b);
int get_spid(int a, int b);
int sfish_info(int a, int b);




/* for extra credit */
char* branch_cmd[] = {"git", "rev-parse", "--abbrev-ref", "HEAD", NULL};
char* git_diff[] = {"git","diff","--exit-code",NULL};
char* get_git_info();















