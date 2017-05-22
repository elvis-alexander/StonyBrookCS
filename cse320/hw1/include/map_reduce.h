//**DO NOT** CHANGE THE PROTOTYPES FOR THE FUNCTIONS GIVEN TO YOU. WE TEST EACH
//FUNCTION INDEPENDENTLY WITH OUR OWN MAIN PROGRAM.
#ifndef MAPRED_H
#define MAPRED_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dirent.h>
#include "const.h"

//Struct to make analysis more intuitive.
struct Analysis {
  int ascii[128];  //space to store counts for each ASCII character.
  int lnlen;       //the longest line’s length
  int lnno;        //the longest line’s line number.
  char* filename;  //the file corresponding to the struct.
};

//Struct to make statistics more intuitive.
typedef struct Stats {
  int histogram[NVAL]; //space to store the count for each number.
  int sum;             //the sum total of all the numbers in the file.
  int n;               //the total count of numbers the files.
  char* filename;      //the file corresponding to the struct.
                       //(don't print for final result)
} Stats;

/**
 * Validates the command line arguments passed in by the user.
 * @param  argc The number of arguments.
 * @param  argv The arguments.
 * @return      Returns -1 if arguments are invalid (refer to hw document).
 *              Returns 0 if -h optional flag is selected. Returns 1 if analysis
 *              is chosen. Returns 2 if stats is chosen. If the -v optional flag
 *              has been selected, validateargs returns 3 if analysis
 *              is chosen and 4 if stats is chosen.
 */
int validateargs(int argc, char** argv);

/**
 * Counts the number of files in a directory EXCLUDING . and ..
 * @param  dir The directory for which number of files is desired.
 * @return     The number of files in the directory EXCLUDING . and ..
 *             If nfiles returns 0, then print "No files present in the
 *             directory." and the program should return EXIT_SUCCESS.
 *             Returns -1 if any sort of failure or error occurs.
 */
int nfiles(char* dir);																	// Clean Up(1)
	
/**
 * The map function goes through each file in a directory, performs some action on
 * the file and then stores the result.
 *
 * @param  dir     The directory that was specified by the user.
 * @param  results The space where map can store the result for each file.
 * @param  size    The size of struct containing result data for each file.
 * @param  act     The action (function map will call) that map will perform on
 *                 each file. Its argument f is the file stream for the specific
 *                 file. act assumes the filestream is valid, hence, map should
 *                 make sure of it. Its argument res is the space for it to store
 *                 the result for that particular file. Its argument fn is a
 *                 string describing the filename. On failure returns -1, on
 *                 sucess returns value specified in description for the act
 *                 function.
 *
 * @return        The map function returns -1 on failure, sum of act results on
 *                success.
 */
int map(char* dir, void* results, size_t size, int (*act)(FILE* f, void* res, char* fn));

/**
 * This reduce function takes the results produced by map and cumulates all
 * the data to give one final Analysis struct. Final struct should contain 
 * filename of file which has longest line.
 *
 * @param  n       The number of files analyzed.
 * @param  results The results array that has been populated by map.
 * @return         The struct containing all the cumulated data.
 */
struct Analysis analysis_reduce(int n, void* results);

/**
 * This reduce function takes the results produced by map and cumulates all
 * the data to give one final Stats struct. Filename field in the final struct 
 * should be set to NULL.
 *
 * @param  n       The number of files analyzed.
 * @param  results The results array that has been populated by map.
 * @return         The struct containing all the cumulated data.
 */
Stats stats_reduce(int n, void* results);

/**
 * Always prints the following:
 * - The name of the file (for the final result the file with the longest line)
 * - The longest line in the directory's length.
 * - The longest line in the directory's line number.
 *
 * Prints only for the final result:
 * - The total number of bytes in the directory.
 *
 * If the hist parameter is non-zero print the histogram of ASCII character
 * occurrences. When printing out details for each file (i.e the -v option was
 * selected) you MUST NOT print the histogram. However, it MUST be printed for
 * the final result.
 *
 * Look at sample output for examples of how this should be print. You have to
 * match the sample output for full credit.
 *
 * @param res    The final result returned by analysis_reduce
 * @param nbytes The number of bytes in the directory.
 * @param hist   If this is non-zero, prints additional information. (Only non-
 *               zero for printing the final result.)
 */
void analysis_print(struct Analysis res, int nbytes, int hist);

/**
 * Always prints the following:
 * Count (total number of numbers read), Mean, Mode, Median, Q1, Q3, Min, Max
 *
 * Prints only for each Map result:
 * The file name
 *
 * If the hist parameter is non-zero print the the histogram. When printing out
 * details for each file (i.e the -v option was selected) you MUST NOT print the
 * histogram. However, it MUST be printed for the final result.
 *
 * Look at sample output for examples of how this should be print. You have to
 * match the sample output for full credit.
 *
 * @param res  The final result returned by stats_reduce
 * @param hist If this is non-zero, prints additional information. (Only non-
 *             zero for printing the final result.)
 */
void stats_print(Stats res, int hist);

/**
 * This function performs various different analyses on a file. It
 * calculates the total number of bytes in the file, stores the longest line
 * length and the line number, and frequencies of ASCII characters in the file.
 *
 * @param  f        The filestream on which the action will be performed. You
 *                  you can assume the filestream passed by map will be valid.
 * @param  res      The slot in the results array in which the data will be
 *                  stored.
 * @param  filename The filename of the file currently being processed.
 * @return          Return the number of bytes read.
 */
int analysis(FILE* f, void* res, char* filename);

/**
 * This function counts the number of occurrences of each number in a file. It
 * also calculates the sum total of all numbers in the file and how many numbers
 * are in the file. If the file has an invalid entry return -1.
 *
 * @param  f        The filestream on which the action will be performed. You
 *                  you can assume the filestream passed by map will be valid.
 * @param  res      The slot in the results array in which the data will be
 *                  stored.
 * @param  filename The filename of the file currently being processed.
 * @return          Return 0 on success and -1 on failure.
 */
int stats(FILE* f, void* res, char* filename);

#endif
