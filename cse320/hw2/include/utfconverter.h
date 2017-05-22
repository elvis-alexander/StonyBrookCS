#include <stdio.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <stdbool.h>
#include <sys/utsname.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <getopt.h>
#include <sys/times.h>
#include <time.h>
#include <stdint.h>



// mark input/output files
#define UTF8 0
#define UTF16LE 1
#define UTF16BE 2

// used to mark machine
#define LEMACHINE 0
#define BEMACHINE 1

// used to define verbosity
#define LEVELONE 1
#define LEVELTWO 2

typedef struct {
  double wr_sys_time;
  double wr_usr_time;
  double wr_real_time;

  double re_sys_time;
  double re_usr_time;
  double re_real_time;

  double co_sys_time;
  double co_usr_time;
  double co_real_time;
}TimeVerbose;

/** Wrapper for holding data of a current conversion */
typedef struct {
	short numbytes;					// for UTF -> range from 1-4
	unsigned char codepoint[3];		// max size of codepoint
	unsigned char input[4];			// for UTF -> before masking
	unsigned char output[4];		// converted form (checks for surrogate)
	unsigned char mask;				// used to extract bits
	unsigned int value;				// Unicode value
}Conversion;

/* globals and constants */
// const char* USAGE = "USAGE";
char ZERO = 0;

/**
 * @param format - returns str rep of format
 */
char* get_format_msg(int format);

/**
 * Prints the usage statement.
 */
void print_help();

/** Gets input bom 
  * @param in input file
 */
int get_bom(int in);

/** write bom to output */
void write_bom(int out, int out_format);

/* write() wrapper
 * @param out file to write to
 * @param v bytes to write
 * @param s number of bytes
 */
void safe_write(int out, void* v, unsigned int s);

/** main execution point for converting files
  * @param in input file
  * @param out output file
  * @param in_format input format (UTF/16LE/16BE)
  * @param out_format output format
 */
bool convert(const int in, const int out, int in_format, int out_format, TimeVerbose* tv);

/* extra credit UTF16-LE/BE to UTF8  */
bool to_utf(const int in, const int out, int in_format, int out_format, const int machine);

/* converts UTF-8 to UTF-16LE/BE Platform Independent */
bool from_utf(const int in, const int out, int in_format, int out_format, const int machine, TimeVerbose* tv);

/** converts encoding when converting between same occurences
 * @param in input file
 * @param out output file
 */
bool cpy_encoding(const int in, const int out, TimeVerbose* tv);

/** converts UTF-16LE to UTF16BE and vice versa
 * @param in input file
 * @param out output file
 * @param machine marks machine (LE/BE)
 */
bool endian_conversion(const int in, const int out, int machine, TimeVerbose* tv);