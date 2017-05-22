# Define (atleast) the following targets: all, clean
# all must build the executable file named mapreduce.
# clean must remove all object files and executable files created.
# Look at the introduction doc for information on how make a Makefile.
# This make file must build according to the structure described in the
# introduction doc, if it doesn't you will get a ZERO!
# $(patsubst pattern,replacement,text)

INCDIR = ./include
SRCDIR = ./src
OUTDIR = ./bin
OBJDIR = ./build
OUTNAME = mapreduce
_HEADERS = const.h map_reduce.h
HEADERS = $(patsubst %,$(INCDIR)/%,$(_HEADERS))
_OBJ = main.o map_reduce.o
OBJ = $(patsubst %,$(OBJDIR)/%,$(_OBJ))

CC=gcc
CFLAGS=-Wall -Werror -I$(INCDIR)

$(OBJDIR)/%.o: $(SRCDIR)/%.c $(HEADERS)
	$(CC) -c -o $@ $< $(CFLAGS)

all: $(OBJ)
	$(CC) -o $(OUTDIR)/$(OUTNAME) $^ $(CFLAGS)

.PHONY: clean

clean:
	rm -f $(OBJDIR)/*.o $(OUTDIR)/$(OUTNAME) *- core $(INCDIR)/*-