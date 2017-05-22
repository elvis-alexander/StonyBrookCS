#include "map_reduce.h"

//Space to store the results for analysis map
struct Analysis analysis_space[NFILES];
//Space to store the results for stats map
Stats stats_space[NFILES];

//Sample Map function action: Print file contents to stdout and returns the number bytes in the file.
int cat(FILE* f, void* res, char* filename) {
    char c;
    int n = 0;
    printf("%s\n", filename);
    while((c = fgetc(f)) != EOF) {
        printf("%c", c);
        n++;
    }
    printf("\n");
    return n;
}

int main(int argc, char** argv) {
    // ./mapreduce [h|v] FUNC DIR
    // 1 = ./mapreduce ana DIR or 2 = ./mapreduce stats DIR 3 = ./mapreduce -v ana DIR or 4 = ./mapreduce -v stats DIR
    int args_return = validateargs(argc, argv);
    if(args_return == 0) {
        return EXIT_SUCCESS;
    }
    int num_files = 0;
    int map_return = 0;
    char* direct_ = NULL;
    
    if(args_return == 1 || args_return == 2) {
        direct_ = *(argv + 2);
    } else {
        direct_ = *(argv + 3);
    }
    num_files = nfiles(direct_);  

    if(num_files == 0) {
        printf("No files present in the directory.\n");
        return EXIT_SUCCESS;
    }

    if(args_return == 1 || args_return == 3) {
         map_return = map(direct_, analysis_space, sizeof(struct Analysis), analysis);
    } else if(args_return == 2 || args_return == 4) {
        map_return = map(direct_, stats_space, sizeof(Stats), stats);
    }

    if(map_return == -1) {
        return EXIT_SUCCESS;
    }
    
    if(args_return == 1 || args_return == 3) {
        struct Analysis ana_reduce = analysis_reduce(num_files, analysis_space);
        if(args_return == 1) {
            // no loop
            analysis_print(ana_reduce, map_return, 1);
        } else {
            // loop 
            int i = 0;
            while(i < num_files) {
                analysis_print(analysis_space[i], 0, 0);
                ++i;
            }
            analysis_print(ana_reduce, map_return, 1);
        }
    } else if(args_return == 2 || args_return == 4) {
        Stats sta_reduce = stats_reduce(num_files, stats_space);
        if(args_return == 2) {
            // no loop
            stats_print(sta_reduce, 1);
        } else {
            // loop
            int i = 0;
            while(i < num_files) {
                stats_print(stats_space[i], 0);
                ++i;
            }
            stats_print(sta_reduce, 1);
        }
    }
    return EXIT_SUCCESS;
}
