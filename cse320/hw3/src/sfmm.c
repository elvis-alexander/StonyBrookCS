#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "sfmm.h"
#include <string.h>

/*PRE-DEFINED MACROS */
#define ALLOC_BIT 0x1
#define UNALLOC_BIT 0x0
#define PAGE_SIZE 4096
#define HEAD_SIZE 8
#define HEAD_TAIL_SIZE 16
#define BYTE_SIZE 8
/*PRE-DEFINED MACROS */

// static globals
static size_t internal = 0;
static size_t external = 0;
static size_t allocations = 0;
static size_t frees = 0;
static size_t coalesce = 0;


void init_heap();
void create_header(sf_free_header* h, uintptr_t block_size, uintptr_t alloc);
void split_block(sf_free_header* curr,int new_size,int actual_size);
void new_block(sf_free_header* curr,int new_size,int actual_size);
void insert_head(sf_free_header* new_head);
void add_heap_space(int size_requested);
int get_size(sf_free_header *f);

// start of the heap
void* start;
// end of the heap
void* end;
// start of the explicit list
sf_free_header* freelist_head = NULL;

void *sf_malloc(size_t size){
	// printf("-------------------------------MALLOC-----------------------------------------\n\n");
	// return NULL on 0 or huge memory requests
	if(size == 0 || (size > (4 * PAGE_SIZE - 16))) {
		errno = EINVAL;
		return NULL;
	}
	// if memory not been requested
	if(freelist_head == NULL) {
		init_heap();
	}

	// align payload to 16
	uintptr_t payload_size = 0;
	if(size < 16)
		payload_size = 16;					// minimum size requiered
	else if(size % 16 == 0)
		payload_size = (uintptr_t)size;		// no need to pad
	else if(size % 8 == 0)
		payload_size = (uintptr_t)(size+8);	// increment by 8 to align 16
	else
		payload_size = (uintptr_t)(size + (16 - (size % 16)));	// round up to 16
	
	++allocations;
	internal += (payload_size - size);

	// new block size
	uintptr_t new_block_s = payload_size + HEAD_TAIL_SIZE;
	sf_free_header* curr = NULL;
	bool found_block = false;
	while(found_block == false) {
		// first fit algorithm (start from head and search {O(n) time})
		curr = freelist_head;
		// search list of free_nodes
		while(get_size(curr) < new_block_s && (curr -> next != NULL)) {
			curr = curr -> next;
		}
		// block_size of potential block
		int curr_size = get_size(curr);

		if(curr_size >= (new_block_s)) {
			// space enough to split block (i.e. no splinters)
			curr -> header.padding_size =  payload_size - size;
			split_block(curr, new_block_s, curr_size);
			found_block = true;
		} else {
			add_heap_space(new_block_s);
		}
	}
	
	// sf_blockprint(curr);// @?
	/*printf("returning print\n");
	sf_blockprint(curr);
	printf("printing block_print for free list\n");
	sf_blockprint(freelist_head);
	printf("printing free blocks information\n");
	sf_snapshot(false);*/
	return ((void*)(((uintptr_t*)curr)+1));
}


void sf_free(void *ptr){
	// combine freed block based on coalescing case
	// printf("---------------------------------FREE------------------------------------------\n");
	if(ptr == NULL) {
		errno = EINVAL;
		return;
	}

	++frees;	
	// ref to current header
	sf_header* curr_head = ((sf_header*)ptr) - 1;
	// correct (shifted) curr size
	int curr_size = curr_head -> block_size << 4;
	// current footer
	sf_footer* curr_foot = ((sf_footer*)(curr_head + (curr_size/BYTE_SIZE - 1)));
	// is this the first block in heap
	bool is_first = (start == (((sf_header*)ptr)-1));

	// write a case to tell if this is the first block that was ever allocated
	sf_footer* prev_footer;
	int prev_size;
	bool prev_allocated;
	if(is_first == true) {
		prev_allocated = true;
	} else {
		// prev status (ptr to prev-foot, size, and allocated bit)
		prev_footer = ((sf_footer*)(curr_head-1));
		prev_size = prev_footer -> block_size << 4;
		prev_allocated = prev_footer -> alloc ? ALLOC_BIT : UNALLOC_BIT;
	}
	// 
	sf_header* next_head;
	int next_size;
	bool next_allocated;
	// if(next block is the end of the list) {
	if(0) {
		next_allocated = true;
	} else {
		// next status (ptr to next-head, size, and allocated bit)
		next_head = curr_head + (curr_size / BYTE_SIZE);
		next_size = next_head -> block_size << 4;
		next_allocated = next_head -> alloc ? ALLOC_BIT : UNALLOC_BIT;
	}

	// cast to have a current free header
	sf_free_header* curr_free_head = ((sf_free_header*)(curr_head));
	

	if((next_allocated == true) && (prev_allocated == true)) {
		// no coalescing
		// printf("no coalescing\n");
		// printf("internally: %d\n", curr_head -> block_size);
		// set curr's next to be head
		curr_free_head -> next = freelist_head;
		// set heads prev to be curr
		freelist_head -> prev = curr_free_head;
		// set head to be unalloc
		curr_head -> alloc = UNALLOC_BIT;
		// set padding size to 0. @tag
		curr_head -> padding_size = 0;
		// set footer to be unalloc
		curr_foot -> alloc = UNALLOC_BIT;
		// insert curr_free to beginning of list
		freelist_head = curr_free_head;

	} else if(next_allocated == true) {
		++coalesce;
		// coalesce with prev block
		// printf("coalesce with prev block\n");
		// new_size shifted to right 4 (curr_size and prev_size are actual sizes i.e. they have been shifted left 4)
		int new_size = (curr_size + prev_size) >> 4;
		// get a reference to the previous head
		sf_header* prev_head = curr_head - (prev_size / BYTE_SIZE);
		sf_free_header* prev_free_head = ((sf_free_header*)(prev_head));
		// set previous header's details i.e block_size and no need to set alloc (should be set already)
		prev_head -> block_size = new_size;
		// set padding size to 0 padding size to 0 @tag
		prev_head -> padding_size = 0;
		// set currents footer's details i.e. block_size and alloc
		curr_foot -> alloc = UNALLOC_BIT;
		curr_foot -> block_size = new_size;

		// links to next and prev of (prev node)
		// prev_nodes prev would be null if prev_free_head=freelist_head
		if(prev_free_head != freelist_head && prev_free_head -> prev != NULL) {
			prev_free_head -> prev -> next = prev_free_head -> next;
		}

		// the prev of my next node would have already been set if prev_free=freelist
		if(prev_free_head != freelist_head && prev_free_head -> next != NULL) {
			prev_free_head -> next -> prev = prev_free_head -> prev;
		}

		
		// if statement to watch out for self loop
		if(prev_free_head != freelist_head) {
			// set next of prev to head
			prev_free_head -> next = freelist_head;
			// set prev of prev_head to null
			prev_free_head -> prev = NULL;
			// set prev of head to curr
			freelist_head -> prev = prev_free_head;
		}

		// add to head of the list
		freelist_head = prev_free_head;	

	} else if(prev_allocated == true) {
		++coalesce;		
		// coalesce with next block
		// printf("coalesce with next block\n");
		// new_size shifted to right 4 (curr_size and next_size are actual sizes i.e. they have been shifted left 4)
		int new_size = (curr_size + next_size) >> 4;
		//  get a reference to next footer
		sf_footer* next_foot = ((sf_footer*)(next_head + (next_size/BYTE_SIZE - 1)));
		sf_free_header* next_free_head = ((sf_free_header*)(next_head));
		// set current header's details i.e block_size and must set alloc
		curr_head -> alloc = UNALLOC_BIT;
		curr_head -> block_size = new_size;
		// set padding size to 0 @tag
		curr_head -> padding_size = 0;
		// set next footer's details i.e block_size and no need to set alloc (should be set already)
		next_foot -> block_size = new_size;
		// next_foot -> alloc = UNALLOC_BIT;		

		// links to next and prev of (prev node)
		// next_node prev would be null if next_free_head=freelist_head
		// pedantic - one case would have been enough
		if(next_free_head != freelist_head && next_free_head -> prev != NULL) {
			next_free_head -> prev -> next = next_free_head ->next;
		}

		// the prev of my next node would have already been set if prev_free=freelist
		if(next_free_head != freelist_head && next_free_head -> next != NULL) {
			next_free_head -> next -> prev = next_free_head -> prev;
		}

		// if statement to watch out for self loop
		if(curr_free_head != freelist_head) {
			// set next of prev to head
			curr_free_head -> next = freelist_head;
			// set prev of prev_head to null
			curr_free_head -> prev = NULL;
			// set prev of head to curr
			freelist_head -> prev = curr_free_head;
		}

		// add to head of the list
		freelist_head = curr_free_head;

	} else {
		++coalesce;
		// coalesce with prev & next blocks
		// printf("coalesce with prev & next blocks\n");
		// new_size shifted to right 4 (curr_size, prev_size,next_size  are actual sizes i.e. they have been shifted left 4)
		int new_size = (curr_size + prev_size + next_size) >> 4;
		// get a reference to the previous head
		sf_header* prev_head = curr_head - (prev_size / BYTE_SIZE);
		sf_free_header* prev_free_head = ((sf_free_header*)(prev_head));
		//  get a reference to next footer
		sf_footer* next_foot = ((sf_footer*)(next_head + (next_size/BYTE_SIZE - 1)));
		sf_free_header* next_free_head = ((sf_free_header*)(next_head));
		// set previous header's details i.e block_size and no need to set alloc (should be set already)
		prev_head -> block_size = new_size;
		// set padding size to zero @tag
		prev_head -> padding_size = 0;
		// set next footer's details i.e block_size and no need to set alloc (should be set already)
		next_foot -> block_size = new_size;

		// edge cases
		// bool starts_with_next = false;
		// bool starts_with_prev = false;
		if(prev_free_head -> prev == next_free_head && freelist_head == next_free_head) {
			// starts_with_prev = true;
		}
		if(next_free_head -> prev == prev_free_head && freelist_head == prev_free_head) {
			// starts_with_next = true;
		}
		
		// links to next and prev of (prev node)
		// prev_nodes prev would be null if prev_free_head=freelist_head
		if(prev_free_head != freelist_head && prev_free_head -> prev != NULL) {
			prev_free_head -> prev -> next = prev_free_head -> next;
		}

		// the prev of my next node would have already been set if prev_free=freelist
		if(prev_free_head != freelist_head && prev_free_head -> next != NULL) {
			prev_free_head -> next -> prev = prev_free_head -> prev;
		}

		// links to next and prev of (next node)
		// next_node prev would be null if next_free_head=freelist_head
		if(next_free_head != freelist_head && next_free_head -> prev != NULL) {
			next_free_head -> prev -> next = next_free_head ->next;
		}

		// the prev of my next node would have already been set if prev_free=freelist
		if(next_free_head != freelist_head && next_free_head -> next != NULL) {
			next_free_head -> next -> prev = next_free_head -> prev;
		}

		// if statement to watch out for self loop
		if(prev_free_head != freelist_head) {
			// set next of prev to head
			prev_free_head -> next = freelist_head;
			// set prev of prev_head to null
			prev_free_head -> prev = NULL;
			// set prev of head to curr
			freelist_head -> prev = prev_free_head;
		}
		// add to head of the list
		freelist_head = prev_free_head;

	}
	// @?
	/*if(freelist_head != NULL) {
		printf("freelist_sfblock: ----\n");
		sf_blockprint(freelist_head);
	}
	if(freelist_head -> next != NULL) {
		printf("freelist_sfblock -> next: ----\n");
		sf_blockprint(freelist_head -> next);
	}	
	if(freelist_head -> next -> next != NULL) {
		printf("freelist_sfblock -> next -> next: ----\n");
		sf_blockprint(freelist_head -> next -> next);
	}*/
	
	// sf_snapshot(false);
}


void *sf_realloc(void *ptr, size_t size){
  return NULL;
}

int sf_info(info* meminfo){
	meminfo ->  internal = internal;
	meminfo ->  external = external;
	meminfo ->  allocations = allocations;
	meminfo ->  frees = frees;
	meminfo ->  coalesce = coalesce;
	return 0;
}

// initializes heap on init malloc call
void init_heap() {
	// this value does not change start of heap ptr
	start = sf_sbrk(0);
	// the end of where the heap is
	end = sf_sbrk(1);
	// mark the start of the free list
	freelist_head = (sf_free_header*)start;
	// set size to page - 16 (because of header and footer)
	create_header(freelist_head, PAGE_SIZE, UNALLOC_BIT);
	freelist_head -> next = NULL;
	freelist_head -> prev = NULL;
	// printf("printing block print on init heap (1st time)\n");	// @?
	// sf_blockprint(freelist_head);		// @?
}

void new_block(sf_free_header* curr,int new_size,int actual_size) {
	printf("splinters...\n");

}

void split_block(sf_free_header* curr,int new_size,int actual_size) {

	// split header add
	sf_free_header* split_header = ((sf_free_header*)(((sf_header*)curr) + (new_size/BYTE_SIZE)));
	// create info for new block without affecting next and prev
	create_header(curr, new_size, ALLOC_BIT);
	// set next and prev of split_footer
	split_header -> next = curr -> next;
	split_header -> prev = curr -> prev;
	// set contents of split block (not the pointers just size and alloc/unalloc bit)
	create_header(split_header, actual_size - new_size, UNALLOC_BIT);

	if(curr -> prev == NULL && curr -> next == NULL) {
		// list currently has one free block
		freelist_head = split_header;
	} else if(curr -> prev != NULL && curr -> next == NULL) {
		// split with previous block free
		// essentially malloc at the rear/end
		curr -> prev -> next = NULL;
		insert_head(split_header);
	} else if(curr -> prev == NULL && curr -> next != NULL) {
		// split with next block free
		// basically malloc at the front
		curr -> next -> prev = split_header;
		freelist_head = split_header;
	} else {
		// split with previous and next block free
		curr -> next -> prev = curr -> prev;
		curr -> prev -> next = curr -> next;
		insert_head(split_header);
	}
}

// sets alloc and block_size for a free block (head and foot)
void create_header(sf_free_header* h, uintptr_t block_size, uintptr_t alloc) {
	(h -> header).alloc = alloc;
	(h -> header).block_size = block_size >> 4;
	sf_footer* f = (sf_footer*)(((uintptr_t*)h) + (block_size - 8)/8);
	f -> alloc = alloc;
	f -> block_size = block_size >> 4;
}

// return block_size of header node
int get_size(sf_free_header *f) {
	return (f -> header.block_size) << 4;
}

// increase heap space by one page until heap has enough space to fit block
void add_heap_space(int size_requested) {
	// printf("size_requested: %d\n", size_requested);
	sf_header* temp_header = (((sf_header*) end));
	sf_header* temp_footer = temp_header + ((size_requested / BYTE_SIZE));
	int sbrk_count = 0;
	while(((void*)temp_footer) > end) {
		end = sf_sbrk(1);
		++sbrk_count;
	}
	/*create_header(((sf_free_header*)temp_header), 8208,UNALLOC_BIT);
	sf_blockprint(temp_header);
	exit(1);*/

	int new_size = sbrk_count * PAGE_SIZE;
	create_header(((sf_free_header*)temp_header), new_size, UNALLOC_BIT);
	((sf_free_header*)temp_header) -> next = NULL;
	((sf_free_header*)temp_header) -> prev = NULL;


	sf_free_header* temp_free_header = ((sf_free_header*)temp_header);
	if(temp_free_header != NULL) {
		insert_head(temp_free_header);
	} else {
		// this should ever occur
		temp_free_header -> next = NULL;
		temp_free_header -> prev = NULL;
		freelist_head = temp_free_header;
	}
}

void insert_head(sf_free_header* new_head) {
	new_head -> next = freelist_head;
	new_head -> prev = NULL;
	freelist_head -> prev = new_head;
	freelist_head =  new_head;
}