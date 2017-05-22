/**
 * === DO NOT MODIFY THIS FILE ===
 * If you need some other prototpyes or constants in a header, please put them
 * in another header file.
 *
 * When we grade, we will be replacing this file with our own copy.
 * You have been warned.
 * === DO NOT MODIFY THIS FILE ===
 */
#ifndef SFMM_H
#define SFMM_H
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

#define ALLOC_SIZE_BITS 4
#define BLOCK_SIZE_BITS 28
#define UNUSED_SIZE_BITS 28
#define PADDING_SIZE_BITS 4

#define SF_HEADER_SIZE \
    ((ALLOC_SIZE_BITS + BLOCK_SIZE_BITS + UNUSED_SIZE_BITS + PADDING_SIZE_BITS) >> 3)
#define SF_FOOTER_SIZE SF_HEADER_SIZE

/*
                Format of a memory block
    +----------------------------------------------+
    |                  64-bits wide                |
    +----------------------------------------------+

    +----------------+------------+-------+--------+
    |  Padding Size| _Unused  | Block Size |  000a | <- Header Block
    |    in bytes  |  28 bits |  in bytes  |       |
    |     4 bits   |          |   28 bits  | 4 bits|
    +--------------+----------+------------+-------+
    |                                              | Content of
    |         Payload and Padding                  | the payload
    |           (N Memory Rows)                    |
    |                                              |
    |                                              |
    +---------------+---------------------+--------+
    |     Unused    | Block Size in bytes |   000a | <- Footer Block
    +---------------+---------------------+--------+

*/
struct __attribute__((__packed__)) sf_header {
    uint64_t alloc : ALLOC_SIZE_BITS;
    uint64_t block_size : BLOCK_SIZE_BITS;
    uint64_t unused_bits : UNUSED_SIZE_BITS;
    uint64_t padding_size : PADDING_SIZE_BITS;
};

typedef struct sf_header sf_header;

struct __attribute__((__packed__)) sf_free_header {
    sf_header header;
    /* Note: These next two fields are only used when the block is free.
     *       They are not part of header, but we place them here for ease
     *       of access.
     */
    struct sf_free_header *next;
    struct sf_free_header *prev;
};
typedef struct sf_free_header sf_free_header;

struct __attribute__((__packed__)) sf_footer {
    uint64_t alloc : ALLOC_SIZE_BITS;
    uint64_t block_size : BLOCK_SIZE_BITS;
    /* Other 32-bits are unused */
};
typedef struct sf_footer sf_footer;

typedef struct {
    size_t internal;
    size_t external;
    size_t allocations;
    size_t frees;
    size_t coalesce;
} info;
/**
 * You should store the head of your free list in this variable.
 */
extern sf_free_header *freelist_head;

/* sfmm.c: Where you will define your functions for this assignment. */

/**
* This is your implementation of malloc. It creates dynamic memory which
* is aligned and padded properly for the underlying system. This memory
* is uninitialized.
* @param size The number of bytes requested to be allocated.
* @return If successful, the pointer to a valid region of memory to use is
* returned, else the value NULL is returned and the ERRNO is set  accordingly.
* If size is set to zero, then the value NULL is returned.
*/
void *sf_malloc(size_t size);

/**
 * Resizes the memory pointed to by ptr to be size bytes.
 * @param ptr Address of the memory region to resize.
 * @param size The minimum size to resize the memory to.
 * @return If successful, the pointer to a valid region of memory to use is
 * returned, else the value NULL is returned and the ERRNO is set accordingly.
 *
 * A realloc call with a size of zero should return NULL and set the ERRNO
 * accordingly.
 */
void *sf_realloc(void *ptr, size_t size);

/**
 *  This function will copy the the correct values to the fields
 *  in the memory info struct.
 *  @param meminfo A pointer to the memory info struct passed
 *  to the function, upon return it will containt the calculated
 *  for current fragmentation
 *  @return If successful return 0, if failure return -1
 */
int sf_info(info *meminfo);

/**
* Marks a dynamically allocated region as no longer in use.
* Adds the newly freed block to the free list.
* @param ptr Address of memory returned by the function sf_malloc.
*/
void sf_free(void *ptr);













/* sfutil.c: Helper functions already created for this assignment. */

/**
*  It should be called in your implementation ONCE,
*  before using any of the other sfmm functions. Note, this function
*  does not allocate any space to your allocator.
*/
void sf_mem_init();

/**
 * This routine will finialize your memory allocator. It should be called
 * in your program one time only, after using any of the other sfmm functions,
 * and prior to exiting your program.
 */
void sf_mem_fini();

/**
 * Extends the heap by one page size (4096 bytes) and returns the start address of
 * the new area.
 * You cannot shrink the heap using this function.
 * Calling sf_sbrk(0)  can be used to find the
 * current location of the heap.
 * This function is guaranteed to return a multiple of 8 but not 16.
 *
 * @param increment If 0 will return current location of the heap, otherwise
 * for any non-zero number the heap will increase by one page size.
 *
 * @return On success, sf_sbrk() returns the
 * current heap pointer. (If the break was increased, then this value
 * is a pointer to the start of the newly allocated memory).
 * On error (4 pages allocated already), (void *) -1 is returned, and errno
 * is set to ENOMEM
 */
void *sf_sbrk();

/**
* Function which outputs the state of the free-list to stdout.
* Performs checks on the placement of the header and footer,
* and if the memory payload is correctly aligned.
* See sf_snapshot section for details on output format.
* @param verbose If true, snapshot will additionally print out
* each memory block using sf_blockprint function.
*/
void sf_snapshot(bool verbose);

/**
* Function which prints human readable block format
* @param block Address of the block header in memory.
*/
void sf_blockprint(void *block);

/**
* Prints human readable block format from the address of the payload.
* IE. subtracts header size from the data pointer to obtain the address
* of the block header. Calls sf_blockprint internally to print.
* @param data Pointer to payload data in memory
* (value returned by sf_malloc).
*/
void sf_varprint(void *data);

#endif