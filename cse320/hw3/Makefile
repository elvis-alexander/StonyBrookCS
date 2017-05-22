CC = gcc
BIN = bin
BLD = build
SRC = src
INC = include
CFLAGS = -Wall -Werror -I$(INC) -g -lcriterion
UTIL = $(BLD)/sfutil.o

.PHONY: all clean

all: create_dirs $(BIN)/sfunit $(BIN)/sfalloc

create_dirs:
	@mkdir -p $(BIN)
	@mkdir -p $(BLD)

$(BIN)/sfunit: $(SRC)/sfunit.c $(BLD)/sfmm.o $(UTIL)
	$(CC) $(CFLAGS) $^ -o $@

$(BIN)/sfalloc: $(SRC)/sfalloc.c $(BLD)/sfmm.o $(UTIL)
	$(CC) $(CFLAGS) $^ -o $@


$(BLD)/sfmm.o: $(SRC)/sfmm.c $(UTIL)
	$(CC) $(CFLAGS) -c $^ -o $@

clean:
	rm -rf $(BIN)
	rm -f $(BLD)/sfmm.o
