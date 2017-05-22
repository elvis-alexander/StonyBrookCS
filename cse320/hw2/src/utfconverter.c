#include "utfconverter.h"

/*
UTF8 -> UTF16LE/BE (good)
UTF16LE -> UTF16BE (good)
UTF16BE -> UTF16LE (good)
UTF8 -> UTF8 (good)
UTF16LE -> UTF16LE (good)
UTF16BE -> UTF16BE (good)
*/
int main(int argc, char* argv[]) {
	// main execution, parse arguments
	struct option long_options[] = 
	{
		{"help", no_argument, NULL, 'h'},
		{"UTF", required_argument, NULL, 'u'}
	};
	int option = 0;
	int verbosity = 0;
	char* input_path = NULL;
	char* output_path = NULL;
	int out_format = 0;
	int u_flag = 0;
	int is_std_out = 1;

	while( (option = getopt_long(argc, argv, "vhu:", long_options, NULL)) != -1) {
		switch(option) {
			case 'h':
				print_help();
				exit(EXIT_SUCCESS);
				break;
			case 'u':
				u_flag = 1;
				if(strcmp(optarg, "8") == 0) {
					out_format = UTF8;
				} else if (strcmp(optarg, "16LE") == 0) {
					out_format = UTF16LE;
				} else if(strcmp(optarg, "16BE") == 0) {
					out_format = UTF16BE;
				}
				break;
			case 'v':
				++verbosity;
				break;
			default:
				print_help();
				exit(EXIT_FAILURE);
				break;
		}
	}
	// look output_format
	if(!u_flag) {
		print_help();
		exit(EXIT_FAILURE);
	}


	// get input/output(if need be)
	if(optind < argc && (argc - optind == 2)) {
		input_path = argv[optind++];
		if(strcmp(argv[optind], "stdout") == 0) {
			is_std_out = 1;
		} else {
			output_path = argv[optind++];
			is_std_out = 0;
		}
	} else if(optind < argc && (argc - optind == 1)) {
		// no output file specified
		input_path = argv[optind++];
		is_std_out = 1;
	} else {
		print_help();
		exit(EXIT_FAILURE);
	}



	// check if file is equivalent (if present)
	if((!is_std_out) && (strcmp(input_path, output_path) == 0)) {
		print_help();
		exit(EXIT_FAILURE);
	}


	// after opening...
	int new_output = -1;
	if(is_std_out == 0) {
		// ouput file specified
		struct stat buff;
		memset(&buff, 0, sizeof(struct stat));
		if(stat(output_path, &buff) == 0) {
			// file exists just append to it
			new_output = open(output_path, O_WRONLY | O_APPEND);
		} else {
			// open/create new file
			new_output = open(output_path, O_CREAT | O_WRONLY,S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH);
		}
	}

	// read input file
	int in = open(input_path, O_RDONLY);

	// validate bom after opening...
	int in_format = get_bom(in);
	if(in_format == -1) {
		print_help();
		exit(EXIT_FAILURE);
	}

	// was stdout specified?
	TimeVerbose* tv = (TimeVerbose *)malloc(sizeof(TimeVerbose));
	memset(tv, 0, sizeof(TimeVerbose));
	if(is_std_out) {
		// output to stdout
		write_bom(STDOUT_FILENO, out_format);
		convert(in, STDOUT_FILENO, in_format, out_format, tv);
	} else {
		// output to specified file
		write_bom(new_output, out_format);
		convert(in, new_output, in_format, out_format,tv);
	}


	///////////////////////////////////////////////////////////////////
	// used for verbose I & II
	struct stat s_buff;
	memset(&s_buff, 0, sizeof(struct stat));
	stat(input_path, &s_buff);
	char path[128];
	char* abs_path = realpath(input_path, path);
	char* in_format_msg = get_format_msg(in_format);
	char* out_format_msg = get_format_msg(out_format);
	char hostname[128];
	gethostname(hostname, sizeof(hostname));
	struct utsname os;
	memset(&os, 0, sizeof(struct utsname));
	uname(&os);
	if(verbosity == 1 || verbosity > 1) {
		fprintf(stderr, "\tInput file size: %.2fK\n", (double)s_buff.st_size/1000);
		fprintf(stderr, "\tInput file path: %s\n", abs_path);
		fprintf(stderr, "\tInput file encoding: %s\n", in_format_msg);
		fprintf(stderr, "\tOutput encoding: %s\n", out_format_msg);
		fprintf(stderr, "\tHostmachine: %s\n", hostname);
		fprintf(stderr, "\tOperating System: %s\n", os.sysname);
	}
	// more info...
	if(verbosity > 1) {
		fprintf(stderr, "\tReading: real=%.1f, user=%.1f, sys=%.1f\n", tv -> re_real_time, tv -> re_usr_time, tv -> re_sys_time);
		fprintf(stderr, "\tConverting: real=%.1f, user=%.1f, sys=%.1f\n", tv -> co_real_time, tv -> co_usr_time, tv -> co_sys_time);
		fprintf(stderr, "\tWriting: real=%.1f, user=%.1f, sys=%.1f\n", tv -> wr_real_time, tv -> wr_usr_time, tv -> wr_sys_time);
		fprintf(stderr, "\tAscii: 100%%\n");
		fprintf(stderr, "\tSurrogates: 0%%\n");
		fprintf(stderr, "\tGlyphs: 0%%\n");
	}
	free(tv);
	// clean up file descriptors
	close(in);
	if(is_std_out == 0) {
		close(new_output);
	}
	return EXIT_SUCCESS;
}

char* get_format_msg(int format) {
	if(format == UTF8) {
		return "UTF-8";
	} else if(format == UTF16LE) {
		return "UTF-16LE";
	} else if(format == UTF16BE) {
		return "UTF-16BE";
	}
	return NULL;
}

void print_help() {
  printf("Command line utility for convverting UTF files to and from UTF-8, UTF-16LE, and UTF-16BE\n\n\
  Usage:\n\
    ./utf [-h|--help] -u OUT_ENC | --UTF=OUT_ENC IN_FILE [OUT_FILE]\n\n\
    Option arguments:\n\
      -h, --help  Displays this usage.\n\
      -v, -vv     Toggles the verbosity of the program to level 1 or 2.\n\n\
    Mandatory argument:\n\
      -u OUT_ENC , --UTF=OUT_ENC\t Sets the output encoding.\n\
                                   Valid values for OUT_ENC: 8, 16LE, 16BE\n\n\
    Position arguments:\n\
      IN_FILE     The file to convert.\n\
      [OUT_FILE]  Output filename, If not presetn, defaults to stdout.\n");
}
int get_bom(int in) {
	unsigned char buf[3];
	memset(buf, 0, sizeof(buf));
	if(read(in, &buf[0], 1) && read(in, &buf[1], 1)) {
		if(buf[0] == 0xFF && buf[1] == 0xFE) { 
			return UTF16LE;
		} else if(buf[0] == 0xFE && buf[1] == 0xFF) {
			return UTF16BE;
		} else if(read(in, &buf[2], 1)) {
			if(buf[0] == 0xEF && buf[1] == 0xBB && buf[2] == 0xBF) {
				return UTF8;				
			}
		}
	}
	return -1;
}

void write_bom(int out, int out_format) {
	char ff = 0xFF;
	char fe = 0xFE;
	char ef = 0xEF;
	char bb = 0xBB;
	char bf = 0xBF;
	if(out_format == UTF8) {
		safe_write(out, &ef, 1);
		safe_write(out, &bb, 1);
		safe_write(out, &bf, 1);
	} else if(out_format == UTF16LE) {
		safe_write(out, &ff, 1);
		safe_write(out, &fe, 1);
	} else if(out_format == UTF16BE) {
		safe_write(out, &fe, 1);
		safe_write(out, &ff, 1);
	}
}

void safe_write(int out, void* v, unsigned int s) {
	write(out, v, s);
}

bool convert(const int in, const int out, int in_format, int out_format, TimeVerbose* tv) {
	// determine endianess of machine
	unsigned int machine = BEMACHINE;	// BE is equal to 1
	char* pmachine = (char *)&machine;
	if(*pmachine) {
		machine = LEMACHINE;
	}
	if(in_format == UTF8 && (out_format == UTF16LE || out_format == UTF16BE)) {
		return from_utf(in, out, in_format, out_format, machine, tv);
	} else if(in_format == UTF16LE && out_format == UTF16BE) {
		return endian_conversion(in, out, machine, tv);
	} else if(in_format == UTF16BE && out_format == UTF16LE) {
		return endian_conversion(in, out, machine, tv);
	}else if( (in_format == UTF16LE || in_format == UTF16BE) && out_format == UTF8) {
		// to_utf(in, out, in_format, out_format, machine);
		print_help();
		exit(1);
	} else if(in_format == out_format){
		// @TODO same transformation BEMACHINE
		cpy_encoding(in, out, tv);
	}
	return true;
}


bool to_utf(const int in, const int out, int in_format, int out_format, const int machine) {
	return true;
}

bool from_utf(const int in, const int out, int in_format, int out_format, const int machine, TimeVerbose* tv) {
	// conversion will store unicode,input,mask and converted value
	Conversion conversion;
	memset(&conversion, 0, sizeof(conversion));
	const unsigned char two_mask = 0x3F;	// mask 6 lsb
	const unsigned int surrogate = 0x10000;	// flags surrogate value
	char* ptr = NULL;
	unsigned int i = 0;

	///////////////////////////////// READ
	clock_t re_time_st;
	struct tms re_cpu_st;
	clock_t re_time_en;
	struct tms re_cpu_en;
	intmax_t re_sys_time = 0;
	intmax_t re_usr_time = 0;
	intmax_t re_real_time = 0;
	///////////////////////////////// WRITE
	clock_t wr_time_st;
	struct tms wr_cpu_st;
	clock_t wr_time_en;
	struct tms wr_cpu_en;
	intmax_t wr_sys_time = 0;
	intmax_t wr_usr_time = 0;
	intmax_t wr_real_time = 0;
	///////////////////////////////// Conversion
	clock_t co_time_st;
	struct tms co_cpu_st;
	clock_t co_time_en;
	struct tms co_cpu_en;
	intmax_t co_sys_time = 0;
	intmax_t co_usr_time = 0;
	intmax_t co_real_time = 0;
	////////////////////////////////////////////////////////
	re_time_st = times(&re_cpu_st);
	// convert UTF8 to UTF16LE
	while(read(in, &(conversion.input[0]), 1)) {
		re_time_en = times(&re_cpu_en);
		re_sys_time += (re_cpu_en.tms_stime - re_cpu_st.tms_stime);
		re_usr_time += (re_cpu_en.tms_utime - re_cpu_st.tms_utime);
		re_real_time += (re_time_en - re_time_st);
	
		unsigned char tmp = conversion.input[0];
		// printf("tmp: %4x\n", tmp);
		// printf("%s\n", byte_to_binary(tmp));
		co_time_st = times(&co_cpu_st);
		if(tmp >>  7 == 0) {
			conversion.numbytes = 1;
		} else if( tmp >> 5 == 0x6) {
			conversion.numbytes = 2;
			conversion.mask = 0x1F;	// extract 5 lsb
		} else if( tmp >> 4 == 0xE) {
			conversion.numbytes = 3;
			conversion.mask = 0xF;	// extract 4 lsb
		} else if( tmp >> 3 == 0x1E) {
			conversion.numbytes = 4;
			conversion.mask = 0x7;	// extract 3 lsb
		}
		co_time_en = times(&co_cpu_en);
		co_sys_time += (co_cpu_en.tms_stime - co_cpu_st.tms_stime);
		co_usr_time += (co_cpu_en.tms_utime - co_cpu_st.tms_utime);
		co_real_time += (co_time_en - co_time_st);
		// only one byte needed to represent so wasteful...
		if(conversion.numbytes == 1) {
			// platform independent
			if(out_format == UTF16LE) {
				// @TODO utf8 -> utf16LE; (Byte = 1)
				wr_time_st = times(&wr_cpu_st);
				safe_write(out, &(conversion.input[0]), 1);
				safe_write(out, &(ZERO), 1);
				wr_time_en = times(&wr_cpu_en);
				wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
				wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
				wr_real_time += (wr_time_en - wr_time_st);
			} else {
				// @TODO utf8 -> utf16BE (Byte = 1)
				wr_time_st = times(&wr_cpu_st);
				safe_write(out, &(ZERO), 1);
				safe_write(out, &(conversion.input[0]), 1);
				wr_time_en = times(&wr_cpu_en);
				wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
				wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
				wr_real_time += (wr_time_en - wr_time_st);
			}
			// set start time for read
			re_time_st = times(&re_cpu_st);
		} else {
			// read rest of bytes necessary
			re_time_st = times(&re_cpu_st);
			for(i = 1; i < conversion.numbytes; ++i) {
				read(in, &(conversion.input[i]), 1);
			}
			re_time_en = times(&re_cpu_en);
			re_sys_time += (re_cpu_en.tms_stime - re_cpu_st.tms_stime);
			re_usr_time += (re_cpu_en.tms_utime - re_cpu_st.tms_utime);
			re_real_time += (re_time_en - re_time_st);
			// evaluate unicode values
			conversion.value = conversion.input[0] & conversion.mask;
			for(i = 1; i < conversion.numbytes; ++i) {
				conversion.value = (conversion.value << 6) | (conversion.input[i] & two_mask);
			}
			// check for surrogate
			if(conversion.value < surrogate) {
				if(out_format  == UTF16LE) {
					// in LEMACHINE looks like E9 00 00 00
					// in BEMACHINE looks like 00 00 00 E9
					// result should look like E9 00
					if(machine == LEMACHINE) {
						wr_time_st = times(&wr_cpu_st);
						safe_write(out, &conversion.value, 2);
						wr_time_en = times(&wr_cpu_en);
						wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
						wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
						wr_real_time += (wr_time_en - wr_time_st);
					} else {
						ptr = (char *)&conversion.value;
						wr_time_st = times(&wr_cpu_st);
						safe_write(out, ptr + 3, 1);
						safe_write(out, ptr + 2, 1);
						wr_time_en = times(&wr_cpu_en);
						wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
						wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
						wr_real_time += (wr_time_en - wr_time_st);
					}
				} else {
					// in LEMACHINE looks like E9 00 00 00
					// in BEMACHINE looks like 00 00 00 E9
					// BE result should look like 00 E9
					if(machine == LEMACHINE) {
						ptr = (char *)&conversion.value;
						wr_time_st = times(&wr_cpu_st);
						safe_write(out, ptr+1, 1);
						safe_write(out, ptr, 1);
						wr_time_en = times(&wr_cpu_en);
						wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
						wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
						wr_real_time += (wr_time_en - wr_time_st);
					} else {
						ptr = (char *)&conversion.value;
						wr_time_st = times(&wr_cpu_st);
						safe_write(out, ptr + 2, 1);
						safe_write(out, ptr + 3, 1);
						wr_time_en = times(&wr_cpu_en);
						wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
						wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
						wr_real_time += (wr_time_en - wr_time_st);
					}
				}
			} else {
				// create the msb's of the surrogate
				co_time_st = times(&co_cpu_st);
				conversion.value -= surrogate;
				unsigned int msb = conversion.value >> 10;
				msb += 0xD800;
				unsigned int lsb = conversion.value & 0x3FF;
				lsb  += 0xDC00;
				co_time_en = times(&co_cpu_en);
				co_sys_time += (co_cpu_en.tms_stime - co_cpu_st.tms_stime);
				co_usr_time += (co_cpu_en.tms_utime - co_cpu_st.tms_utime);
				co_real_time += (co_time_en - co_time_st);
				if(out_format == UTF16LE) {
					/*
						in LEMACHINE 
							msb looks like 35 d8 00 00
							lsb looks like 9c dc 00 00

						in BEMACHINE
							msb looks like 00 00 d8 35
							lsb looks like 00 00 dc 9c

						LE result should look like 35 d8 9c dc
					*/
					if(machine == LEMACHINE) {
						wr_time_st = times(&wr_cpu_st);
						safe_write(out, &msb, 2);
						safe_write(out, &lsb, 2);
						wr_time_en = times(&wr_cpu_en);
						wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
						wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
						wr_real_time += (wr_time_en - wr_time_st);
					} else {
						ptr = (char *)&msb;
						wr_time_st = times(&wr_cpu_st);
						safe_write(out, ptr+3, 1);
						safe_write(out, ptr+2, 1);
						ptr = (char *)&lsb;
						safe_write(out, ptr + 3, 1);
						safe_write(out, ptr + 2, 1);
						wr_time_en = times(&wr_cpu_en);
						wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
						wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
						wr_real_time += (wr_time_en - wr_time_st);
					}
				} else {
					/*
					in LEMACHINE 
							msb looks like 35 d8 00 00
							lsb looks like 9c dc 00 00

					in BEMACHINE
						msb looks like 00 00 d8 35
						lsb looks like 00 00 dc 9c

					BE result should look like d8 35 dc 9c
					*/
					if(machine == LEMACHINE) {
						ptr = (char *)&msb;
						wr_time_st = times(&wr_cpu_st);
						safe_write(out, ++ptr, 1);
						safe_write(out, --ptr, 1);
						ptr = (char *)&lsb;
						safe_write(out, ++ptr, 1);
						safe_write(out, --ptr, 1);
						wr_time_en = times(&wr_cpu_en);
						wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
						wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
						wr_real_time += (wr_time_en - wr_time_st);
					} else {
						ptr = (char *)&msb;
						wr_time_st = times(&wr_cpu_st);
						safe_write(out, ptr + 2, 1);
						safe_write(out, ptr + 3, 1);
						ptr = (char *)&lsb;
						safe_write(out, ptr + 2, 1);
						safe_write(out, ptr + 3, 1);
						wr_time_en = times(&wr_cpu_en);
						wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
						wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
						wr_real_time += (wr_time_en - wr_time_st);
					}
				}
			}
			re_time_st = times(&re_cpu_st);
		}
		// after write reset :)
		memset(&conversion, 0, sizeof(conversion));
	}

	tv -> wr_sys_time = (double) wr_sys_time/sysconf(_SC_CLK_TCK);
	tv -> wr_usr_time = (double) wr_usr_time/sysconf(_SC_CLK_TCK);
	tv -> wr_real_time = (double) wr_real_time/sysconf(_SC_CLK_TCK);

	tv -> re_sys_time = (double) re_sys_time/sysconf(_SC_CLK_TCK);
	tv -> re_usr_time = (double) re_usr_time/sysconf(_SC_CLK_TCK);
	tv -> re_real_time = (double) re_real_time/sysconf(_SC_CLK_TCK);
	
	tv -> co_sys_time = (double) co_sys_time/sysconf(_SC_CLK_TCK);
	tv -> co_usr_time = (double) co_usr_time/sysconf(_SC_CLK_TCK);
	tv -> co_real_time = (double) co_real_time/sysconf(_SC_CLK_TCK);	
	return true;
}

bool cpy_encoding(const int in, const int out, TimeVerbose* tv) {
	char p = 0;
	///////////////////////////////// WRITE
	clock_t wr_time_st;
	struct tms wr_cpu_st;
	clock_t wr_time_en;
	struct tms wr_cpu_en;
	intmax_t wr_sys_time = 0;
	intmax_t wr_usr_time = 0;
	intmax_t wr_real_time = 0;
	///////////////////////////////// READ
	clock_t re_time_st;
	struct tms re_cpu_st;
	clock_t re_time_en;
	struct tms re_cpu_en;
	intmax_t re_sys_time = 0;
	intmax_t re_usr_time = 0;
	intmax_t re_real_time = 0;
	///////////////////////////////// Conversion
	clock_t co_time_st;
	struct tms co_cpu_st;
	clock_t co_time_en;
	struct tms co_cpu_en;
	intmax_t co_sys_time = 0;
	intmax_t co_usr_time = 0;
	intmax_t co_real_time = 0;
	/////////////////////////////////
	co_time_st = times(&co_cpu_st);
	re_time_st = times(&re_cpu_st);
	while(read(in, &p, 1)) {
		re_time_en = times(&re_cpu_en);
		re_sys_time += (re_cpu_en.tms_stime - re_cpu_st.tms_stime);
		re_usr_time += (re_cpu_en.tms_utime - re_cpu_st.tms_utime);
		re_real_time += (re_time_en - re_time_st);
		wr_time_st = times(&wr_cpu_st);
		safe_write(out, &p, 1);		
		// end write time
		wr_time_en = times(&wr_cpu_en);
		wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
		wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
		wr_real_time += (wr_time_en - wr_time_st);
		co_time_en = times(&co_cpu_en);
		co_sys_time += (co_cpu_en.tms_stime - co_cpu_st.tms_stime);
		co_usr_time += (co_cpu_en.tms_utime - co_cpu_st.tms_utime);
		co_real_time += (co_time_en - co_time_st);
		co_time_st = times(&co_cpu_st);		
		re_time_st = times(&re_cpu_st);
	}
	tv -> wr_sys_time = (double) wr_sys_time/sysconf(_SC_CLK_TCK);
	tv -> wr_usr_time = (double) wr_usr_time/sysconf(_SC_CLK_TCK);
	tv -> wr_real_time = (double) wr_real_time/sysconf(_SC_CLK_TCK);

	tv -> re_sys_time = (double) re_sys_time/sysconf(_SC_CLK_TCK);
	tv -> re_usr_time = (double) re_usr_time/sysconf(_SC_CLK_TCK);
	tv -> re_real_time = (double) re_real_time/sysconf(_SC_CLK_TCK);
	
	tv -> co_sys_time = (double) co_sys_time/sysconf(_SC_CLK_TCK);
	tv -> co_usr_time = (double) co_usr_time/sysconf(_SC_CLK_TCK);
	tv -> co_real_time = (double) co_real_time/sysconf(_SC_CLK_TCK);	
	
	return true;
}

/*
typedef struct {
	double read_real;
	double read_usr;
	double read_sys;

	double convert_real;
	double convert_usr;
	double convert_sys;

	double write_real;
	double write_usr;
	double write_sys;
}TimeVerbose;
*/



// missing user time
bool endian_conversion(const int in, const int out, int machine, TimeVerbose* tv) {
	char b1 = 0;
	char b2 = 0;
	///////////////////////////////// WRITE
	clock_t wr_time_st;
	struct tms wr_cpu_st;
	clock_t wr_time_en;
	struct tms wr_cpu_en;
	intmax_t wr_sys_time = 0;
	intmax_t wr_usr_time = 0;
	intmax_t wr_real_time = 0;
	///////////////////////////////// READ
	clock_t re_time_st;
	struct tms re_cpu_st;
	clock_t re_time_en;
	struct tms re_cpu_en;
	intmax_t re_sys_time = 0;
	intmax_t re_usr_time = 0;
	intmax_t re_real_time = 0;
	///////////////////////////////// Conversion
	clock_t co_time_st;
	struct tms co_cpu_st;
	clock_t co_time_en;
	struct tms co_cpu_en;
	intmax_t co_sys_time = 0;
	intmax_t co_usr_time = 0;
	intmax_t co_real_time = 0;
	/////////////////////////////////

	// start counting time for read on first loop
	re_time_st = times(&re_cpu_st);
	co_time_st = times(&co_cpu_st);
	while(read(in, &b1, 1) && read(in, &b2, 1)) {
		// end read time
		re_time_en = times(&re_cpu_en);
		re_sys_time += (re_cpu_en.tms_stime - re_cpu_st.tms_stime);
		re_usr_time += (re_cpu_en.tms_utime - re_cpu_st.tms_utime);
		re_real_time += (re_time_en - re_time_st);
		// start write time
		wr_time_st = times(&wr_cpu_st);
		safe_write(out, &b2, 1);
		safe_write(out, &b1, 1);
		// end write time
		wr_time_en = times(&wr_cpu_en);
		wr_sys_time += wr_cpu_en.tms_stime - wr_cpu_st.tms_stime;
		wr_usr_time += wr_cpu_en.tms_utime - wr_cpu_st.tms_utime;
		wr_real_time += (wr_time_en - wr_time_st);
		// start read time
		co_time_en = times(&co_cpu_en);
		co_sys_time += (co_cpu_en.tms_stime - co_cpu_st.tms_stime);
		co_usr_time += (co_cpu_en.tms_utime - co_cpu_st.tms_utime);
		co_real_time += (co_time_en - co_time_st);
		co_time_st = times(&co_cpu_st);
		re_time_st = times(&re_cpu_st);
	}

	// write
	tv -> wr_sys_time = (double) wr_sys_time/sysconf(_SC_CLK_TCK);
	tv -> wr_usr_time = (double) wr_usr_time/sysconf(_SC_CLK_TCK);
	tv -> wr_real_time = (double) wr_real_time/sysconf(_SC_CLK_TCK);

	// read
	tv -> re_sys_time = (double) re_sys_time/sysconf(_SC_CLK_TCK);
	tv -> re_usr_time = (double) re_usr_time/sysconf(_SC_CLK_TCK);
	tv -> re_real_time = (double) re_real_time/sysconf(_SC_CLK_TCK);

	// co	
	tv -> co_sys_time = (double) co_sys_time/sysconf(_SC_CLK_TCK);
	tv -> co_usr_time = (double) co_usr_time/sysconf(_SC_CLK_TCK);
	tv -> co_real_time = (double) co_real_time/sysconf(_SC_CLK_TCK);	
	return true;
}
