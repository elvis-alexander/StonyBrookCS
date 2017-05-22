//**DO NOT** CHANGE THE PROTOTYPES FOR THE FUNCTIONS GIVEN TO YOU. WE TEST EACH
//FUNCTION INDEPENDENTLY WITH OUR OWN MAIN PROGRAM.
//Implement map_reduce.h functions here.

/*
quick reminders
b). for analysis TIPS: Index of line by 1, total_count of line does not include new line (total bytes does!) 
*/


#include "map_reduce.h"
#include <errno.h>

/* Helper Functions */

/* description */
void accumulate_ascii(int* src, int* cur) {
	int i = 0;
	int num_ascii = 128;
	for(i = 0; i < num_ascii; ++i) {
		*src += *cur;
		++src;
		++cur;
	}
	return;
}

/* description */
void accumulate_hist(int* src, int* cur) {
	int i = 0;
	for(i = 0; i < NVAL; ++i) {
		*src += *cur;
		++src;
		++cur;
	}
	return;
}

/* description */
char *str_cat_helper(char* dir, char* name) {
	char* rel_path = NULL;
	int len = strlen(dir) + strlen(name) + 1;
	int add_slash = 0;
	if(*(dir + strlen(dir) - 1) != '/') {
		++len;
		add_slash = 1;
	}
	rel_path = (char *)malloc(len * sizeof(char));
	char* tmp = rel_path;
	while(*dir)
		*rel_path++ = *dir++;
	if(add_slash)
		*rel_path++ = '/';
	while(*name)
		*rel_path++ = *name++;
	*rel_path = '\0';
	return tmp;
}

/* description */
int* collapse(int* A,size_t s) {
	int* hist_to_arr = ((int *)malloc(sizeof(int) * (s)));
	memset(hist_to_arr, 0, sizeof(int) * (s+1));
	int* tmp = hist_to_arr;
	int i = 0;
	int j = 0;
	for(i = 0;i < NVAL; ++i) {
		if(*A > 0) {
			for(j = 0; j < *A; ++j) {
				*hist_to_arr = i;
				++hist_to_arr;
			}
		}
		++A;
	}
	return tmp;
}

/* description */
void stats_print_helper(Stats* res_ptr, int file_flag) {
	if(file_flag) {
		printf("File: %s\n", res_ptr -> filename);
	}
	int* hist_start = res_ptr -> histogram;
	int* hist_end = ((res_ptr -> histogram) + NVAL -1);
	int* sort_hist = collapse(res_ptr -> histogram, res_ptr -> n);
	int i = 0;
	int min = 0;
	int max = 0;
	int mid = res_ptr -> n / 2;
	float median = 0;
	int first_percentile_i = 0.25 * res_ptr -> n;
	int third_percentile_i = 0.75 * res_ptr -> n;
	int mode_occur = *hist_start;
	int mode_vals[NVAL];
	int mode_count = 0;
	memset(mode_vals, 0, sizeof(int) * NVAL);

	// find median
	if((res_ptr -> n % 2) == 0) {
		median = ((float)(((float)*(sort_hist + mid)) + *(sort_hist + mid-1)) / 2);		
	} else {
		median = *(sort_hist + mid);		
	}
	
	// find min
	for(i = 0; i < NVAL; ++i) {
		// printf("val here: %d:%d\n", i, *(hist_start + i));
		if((*(hist_start + i)) > 0) {
			min = i;
			break;
		}
	}
	// find max
	for(i = 0; i < NVAL; ++i) {
		if(*(hist_end - i)) {
			max = NVAL - 1 - i;
			break;
		}
	}
	// find max occuring value
	for(i = 0; i < NVAL; ++i) {
		if(*(hist_start + i) > mode_occur) {
			mode_occur = *(hist_start + i);
		}
	}
	// find actual mode values 
	for(i = 0; i < NVAL; ++i) {
		if(*(hist_start + i) == mode_occur) {
			mode_vals[mode_count++] = i;
		}
	}
	// count
	printf("Count: %d\n", res_ptr -> n);
	// mean
	printf("Mean: %f\n", ((float)((float)res_ptr -> sum) / ((float)res_ptr -> n)));
	// mode
	printf("Mode:");
	for(i = 0; i < mode_count; ++i) {
		printf(" %d", mode_vals[i]);
	}
	printf("\n");
	// median
	printf("Median: %f\n", median);
	// find percentiles
	printf("Q1: %f\n", ((float)*(first_percentile_i + sort_hist)));;
	printf("Q3: %f\n", ((float)*(third_percentile_i + sort_hist)));
	// min/max
	printf("Min: %d\n", min);
	printf("Max: %d\n", max);
	if(file_flag)
		printf("\n");
	free(sort_hist);
}

/**
 * Validates the command line arguments passed in by the user.
 * @param  argc The number of arguments.
 * @param  argv The arguments.
 * @return      Returns -1 if arguments are invalid (refer to hw document).
 *              Returns 0 if -h optional flag is selected. Returns 1 if analysis
 *              is chosen. Returns 2 if stfats is chosen. If the -v optional flag
 *              has been selected, validateargs returns 3 if analysis
 *              is chosen and 4 if stats is chosen.
 */
int validateargs(int argc, char** argv) {
	if(argc > 1) {
		char* second_arg = *(argv + 1);
		int v_flag = 0;
		int stats_flag = 0;
		if(strcmp(second_arg, "-h") == 0) {
			// print here
			printf("Usage:  ./mapreduce [h|v] FUNC DIR\n\
        FUNC    Which operation you would like to run on the data:\n\
                ana - Analysis of various text files in a directory.\n\
                stats - Calculates stats on files which contain only numbers.\n\
        DIR     The directory in which thße files are located.\n\n\
        Options:\n\
        -h      Prints this help menu.\n\
        -v      Prints the map function’s results, stating the file it’s from.\n");
			return 0;
		}
		if(strcmp(second_arg, "-v") == 0) {
			v_flag = 1;
		}
		if(v_flag) {
			// get the third arg (ana or stats)
			char* third_arg = *(argv + 2);
			if(strcmp(third_arg, "stats") == 0) {
				stats_flag = 1;
			}
		} else {
			// second arg
			if(strcmp(second_arg, "stats") == 0) {
				stats_flag = 1;
			}
		}
		if(v_flag) {
			if(stats_flag)
				return 4;
			else
				return 3;
		} else {
			if(stats_flag)
				return 2;
			else
				return 1;
		}
	}
	return -1;
}


/**
 * Counts the number of files in a directory EXCLUDING . and ..
 * @param  dir The directory for which number of files is desired.
 * @return     The number of files in the directory EXCLUDING . and ..
 *             If nfiles returns 0, then print "No files present in the
 *             directory." and the program should return EXIT_SUCCESS.
 *             Returns -1 if any sort of failure or error occurs.
 */
int nfiles(char *dir) {
	int count = 0;
	DIR* file_desc = opendir(dir);
	struct dirent* entry = NULL;
	while ((entry = readdir(file_desc)) != NULL) {
		char *name = entry -> d_name;
		if(!((strcmp(name, ".") == 0) || (strcmp(name, "..") == 0))) {
			++count;
		}
	}
	closedir(file_desc);
	return count;
}


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
 int map(char* dir, void* results, size_t size, int (*act)(FILE* f, void* res, char* fn)) {
	// open dir
	char *filename = NULL;
	char* rel_path = NULL;
	int return_sum = 0;
	DIR* file_desc = opendir(dir);
	struct dirent* entry = NULL;
	// process file
	while((entry = readdir(file_desc)) != NULL) {
		// ignore . and .. files
		filename = entry -> d_name;
		if((strcmp(filename, ".") == 0) || (strcmp(filename, "..") == 0)) {
			continue;
		}
		// process file i.e. call stats or analysis
		memset(results, 0, size);
		rel_path = str_cat_helper(dir, filename);
		FILE* file = fopen(rel_path, "r+");
		if(errno != 0) {
			return -1;
		}
		// stats - *act returns 0 on success, -1 on fail or ana - *act returns num of bytes read
		return_sum += (*act)(file, results, strdup(filename));
		fclose(file);
		results += size;
	}
	closedir(file_desc);
	return return_sum;
}


/**
 * This reduce function takes the results produced by map and cumulates all
 * the data to give one final Analysis struct. Final struct should contain 
 * filename of file which has longest line.
 *
 * @param  n       The number of files analyzed.
 * @param  results The results array that has been populated by map.
 * @return         The struct containing all the cumulated data.
 */
struct Analysis analysis_reduce(int n, void* results) {
	struct Analysis* current = ((struct Analysis*)results);
	struct Analysis* ana_data = ((struct Analysis*)malloc(sizeof(struct Analysis)));
	memset(ana_data -> ascii, 0, sizeof(int) * 128);
	int lnlen = 0;
	int lnno = 0;
	char* filename = NULL;
	int i = 0;
	for(i = 0; i < n; i++) {
		accumulate_ascii(ana_data -> ascii, current -> ascii);
		if( (current -> lnlen) > lnlen) {
			lnlen = current -> lnlen;
			lnno = current -> lnno;
			filename = current -> filename;
		}
		++current;
	}
	ana_data -> lnlen = lnlen;
	ana_data -> lnno = lnno;
	ana_data -> filename = strdup(filename);
	return *ana_data;
}


/**
 * This reduce function takes the results produced by map and cumulates all
 * the data to give one final Stats struct. Filename field in the final struct 
 * should be set to NULL.
 *
 * @param  n       The number of files analyzed.
 * @param  results The results array that has been populated by map.
 * @return         The struct containing all the cumulated data.
 */
Stats stats_reduce(int n, void* results) {
	Stats* current = ((Stats *)results);
	Stats* stat_data = ((struct Stats*)malloc(sizeof(struct Stats)));
	memset(stat_data, 0, sizeof(Stats));
	memset(stat_data -> histogram, 0, sizeof(int) * NVAL);
	int total_sum = 0;
	int total_n = 0;
	int i = 0;
	for(i = 0; i < n; ++i) {
		total_sum += current -> sum;
		total_n += current -> n;
		accumulate_hist(stat_data -> histogram, current -> histogram);
		++current;
	}
	stat_data -> sum = total_sum;
	stat_data -> n = total_n;
	stat_data -> filename = NULL;
	return *stat_data;
}


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
void analysis_print(struct Analysis res, int nbytes, int hist) {
	struct Analysis* res_ptr = &res;
	printf("File: %s\n", res_ptr -> filename);
	printf("Longest line length: %d\n", res_ptr -> lnlen);
	printf("Longest line number: %d\n", res_ptr -> lnno);
	if(hist) {
		// last entry
		printf("Total Bytes in directory: %d\n", nbytes);
		printf("Histogram:\n");
		int* ascii_arr = res_ptr -> ascii;
		int occurrences = 0;
		int i = 0;
		for(i = 0; i < 128; ++i) {
			// print histogram of ASCII character occurrences
			occurrences = *(ascii_arr + i);
			if(occurrences > 0) {
				printf(" %d:", i);
				while(occurrences != 0) {
					printf("-");
					--occurrences;
				}
				printf("\n");
			}
		}
	} else {
		printf("\n");
	}
}


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
void stats_print(Stats res, int hist) {
	Stats* res_ptr = &res;
	if(hist) {
		// final result first print histogram
		// histogram
		printf("Histogram:\n");
		int* histogram = res_ptr -> histogram;
		int occurrences = 0;
		int i = 0;
		for(i = 0; i < NVAL; ++i) {
			occurrences = *(histogram + i);
			if(occurrences > 0) {
				printf("%d :", i);
				while(occurrences != 0) {
					printf("-");
					--occurrences;
				}
				printf("\n");
			}
		}
		printf("\n");
	}
	if(hist) {
		stats_print_helper(res_ptr, 0);
	} else {
		stats_print_helper(res_ptr, 1);
	}
}

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
int analysis(FILE* f, void* res, char* filename) {
	struct Analysis* res_analysis = ((struct Analysis*)res);
	memset(res_analysis->ascii, 0, 128*sizeof(int));
	// statistic vars
	int total_bytes = 0;
	int current_byte = 0;
	// current line
	int current_lnlen = 0;
	int current_lnno = 1;
	// longest line
	int lnlen = 0;
	int lnno = 1;
	// pointer start ascii
	int* ascii_arr = res_analysis -> ascii;
	// analysis
	while((current_byte = fgetc(f)) != EOF) {
		++(*(ascii_arr + current_byte));
		++total_bytes;
		++current_lnlen;
		if(current_byte == '\n') {
			if((current_lnlen - 1) > lnlen) {
				lnlen = current_lnlen - 1; 	// dont include \n
				lnno = current_lnno;
			}
			current_lnlen = 0;
			++current_lnno;
		}
	}
	// check for last line
	if(current_lnlen > lnlen) {
		lnlen = current_lnlen;
		lnno = current_lnno;
	}
	// set values into struct
	res_analysis -> lnlen = lnlen;
	res_analysis -> lnno = lnno;
	res_analysis -> filename = filename;
	// return number of bytes read
	return total_bytes;
}


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
int stats(FILE* f, void* res, char* filename) {
	Stats* stats_analysis = ((Stats *)res);
	memset(stats_analysis->histogram, 0, NVAL * sizeof(int));
	int* histogram_ptr = stats_analysis -> histogram;
	int current_num = 0;
	int sum = 0;
	int n = 0;
	while((fscanf(f, "%d", &current_num)) != EOF) {
		if(!(current_num >= 0 && current_num < NVAL)) {
			return -1;
		}
		*(histogram_ptr + current_num) += 1;
		sum += current_num;
		++n;
	}
	stats_analysis -> sum = sum;
	stats_analysis -> n = n;
	stats_analysis -> filename = filename;
	return 0;
}