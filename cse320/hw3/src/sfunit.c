#include <criterion/criterion.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "sfmm.h"

/**
 *  HERE ARE OUR TEST CASES NOT ALL SHOULD BE GIVEN STUDENTS
 *  REMINDER MAX ALLOCATIONS MAY NOT EXCEED 4 * 4096 or 16384 or 128KB
 */

Test(sf_memsuite, Malloc_an_Integer, .init = sf_mem_init, .fini = sf_mem_fini) {
    int *x = sf_malloc(sizeof(int));
    *x = 4;
    cr_assert(*x == 4, "Failed to properly sf_malloc space for an integer!");
}

Test(sf_memsuite, Free_block_check_header_footer_values, .init = sf_mem_init, .fini = sf_mem_fini) {
    void *pointer = sf_malloc(sizeof(short));
    sf_free(pointer);
    pointer = pointer - 8;
    sf_header *sfHeader = (sf_header *) pointer;
    cr_assert(sfHeader->alloc == 0, "Alloc bit in header is not 0!\n");
    sf_footer *sfFooter = (sf_footer *) (pointer - 8 + (sfHeader->block_size << 4));
    cr_assert(sfFooter->alloc == 0, "Alloc bit in the footer is not 0!\n");
}

Test(sf_memsuite, PaddingSize_Check_char, .init = sf_mem_init, .fini = sf_mem_fini) {
    void *pointer = sf_malloc(sizeof(char));
    pointer = pointer - 8;
    sf_header *sfHeader = (sf_header *) pointer;
    cr_assert(sfHeader->padding_size == 15, "Header padding size is incorrect for malloc of a single char!\n");
}

Test(sf_memsuite, Check_next_prev_pointers_of_free_block_at_head_of_list, .init = sf_mem_init, .fini = sf_mem_fini) {
    int *x = sf_malloc(4);
    memset(x, 0, 4);
    cr_assert(freelist_head->next == NULL);
    cr_assert(freelist_head->prev == NULL);
}

Test(sf_memsuite, Coalesce_no_coalescing, .init = sf_mem_init, .fini = sf_mem_fini) {
    void *x = sf_malloc(4);
    void *y = sf_malloc(4);
    memset(y, 0xFF, 4);
    sf_free(x);
    cr_assert(freelist_head == ((void*)x)-8);
    sf_free_header *headofx = (sf_free_header*) (x-8);
    sf_footer *footofx = (sf_footer*) (x - 8 + (headofx->header.block_size << 4)) - 8;
    sf_blockprint((sf_free_header*)((void*)x-8));
    // all of the above would be true
    cr_assert(headofx->header.alloc == 0);
    cr_assert(headofx->header.block_size << 4 == 32);
    cr_assert(headofx->header.padding_size == 0);
    cr_assert(footofx -> alloc == 0);
    cr_assert(footofx -> block_size << 4 == 32);
}

/*
//############################################
// STUDENT UNIT TESTS SHOULD BE WRITTEN BELOW
// DO NOT DELETE THESE COMMENTS
//############################################
*/

// Test Case 1: coalescing with a right block (simple case - no links to curr block)
Test(sf_memsuite, Coalesce_right_block, .init = sf_mem_init, .fini = sf_mem_fini) {
    typedef struct {
        double year_1;  // 8 bytes
        double year_2;  // 8 bytes
        double year_3;  // 8 bytes
        double year_4;  // 8 bytes
    }salary_database;
    // salary_database needs a blocksize of 48 (32 + 16(H/F))
    salary_database* p1 = (salary_database*)sf_malloc(sizeof(salary_database));
    salary_database* p2 = (salary_database*)sf_malloc(sizeof(salary_database));
    salary_database* p3 = (salary_database*)sf_malloc(sizeof(salary_database));
    salary_database* p4 = (salary_database*)sf_malloc(sizeof(salary_database));


    sf_header *p1_h = ((sf_header*)p1) - 1;
    sf_header *p2_h = ((sf_header*)p2) - 1;
    sf_header *p3_h = ((sf_header*)p3) - 1;
    sf_header *p4_h = ((sf_header*)p4) - 1;

    // salary_database needs a blocksize of 48 (32 + 16(H/F))
    cr_assert(p1_h -> block_size == 48 >> 4);
    cr_assert(p2_h -> block_size == 48 >> 4);
    cr_assert(p3_h -> block_size == 48 >> 4);
    cr_assert(p4_h -> block_size == 48 >> 4);

    // sf_free_header* init_free_list = freelist_head;
    sf_free(p3);
    cr_assert(p3_h -> alloc == 0);
    cr_assert(p3_h -> block_size == 48 >> 4);
    cr_assert(p3_h -> padding_size == 0);

    sf_free(p2);
    // verify coalesced with next block (p3_h)
    cr_assert(p2_h -> alloc == 0);
    cr_assert(p2_h -> block_size == (48*2) >> 4);
    cr_assert(p2_h -> padding_size == 0);    
}

// Test Case 2: assure right coalesced case, when right coalesced block has previous and next links make sure they stay connected
Test(sf_memsuite, Coalesce_right_block_extended, .init = sf_mem_init, .fini = sf_mem_fini) {

    // long double = 16 bytes
    long double* n1 = (long double*)sf_malloc(sizeof(long double));
    long double* n2 = (long double*)sf_malloc(sizeof(long double));
    long double* n3 = (long double*)sf_malloc(sizeof(long double));
    long double* n4 = (long double*)sf_malloc(sizeof(long double)); 
    long double* n5 = (long double*)sf_malloc(sizeof(long double)); 
    long double* n6 = (long double*)sf_malloc(sizeof(long double)); 
    long double* n7 = (long double*)sf_malloc(sizeof(long double)); 
    long double* n8 = (long double*)sf_malloc(sizeof(long double));

    // assure alignment
    cr_assert((unsigned long)(n1) % 16 == 0);
    cr_assert((unsigned long)(n2) % 16 == 0);
    cr_assert((unsigned long)(n3) % 16 == 0);
    cr_assert((unsigned long)(n4) % 16 == 0);
    cr_assert((unsigned long)(n5) % 16 == 0);
    cr_assert((unsigned long)(n6) % 16 == 0);
    cr_assert((unsigned long)(n7) % 16 == 0);
    cr_assert((unsigned long)(n8) % 16 == 0);

    // 8 contiguous blocks 32 bytes (block_size)

    sf_free_header* init_free_list = freelist_head;
    // free block 2 and verify links
    sf_free(n2);
    sf_free_header* num2_free_header = ((sf_free_header*) (((void*)n2)-8));
    cr_assert(num2_free_header -> next = init_free_list);    
    // free block 5 and verify links
    sf_free(n5);
    sf_free_header* num5_free_header = ((sf_free_header*) (((void*)n5)-8));
    cr_assert(num5_free_header -> next = num2_free_header);    
    cr_assert(num2_free_header -> prev = num5_free_header);
    // free block 7 and verify links
    sf_free(n7);
    sf_free_header* num7_free_header = ((sf_free_header*) (((void*)n7)-8));
    cr_assert(freelist_head == num7_free_header);
    cr_assert(num7_free_header -> next = num5_free_header);
    cr_assert(num5_free_header -> prev = num7_free_header);
    cr_assert(num5_free_header -> next = num2_free_header);    
    cr_assert(num2_free_header -> prev = num5_free_header);
    cr_assert(num2_free_header -> next = freelist_head);
    
    // Main Point of Unit Test (assure links of next block are set and newly freed is inserted into head)
    // coalesces with block 5
    // keeps link of previous and next of block 5
    sf_free(n4);
    sf_free_header* num4_free_header = ((sf_free_header*) (((void*)n4)-8));
    // links of coalesced block havent been lost
    cr_assert(num7_free_header -> next == num2_free_header);
    cr_assert(num2_free_header -> prev == num7_free_header);
    // newly freed block inserted into beginning of list
    cr_assert(freelist_head == num4_free_header);
    cr_assert(num4_free_header -> header.block_size == (64 >> 4));
}


// Test Case 3: this tests coalescing a left block when the previous and next are null of the current block
Test(sf_memsuite, Coalesce_left_block, .init = sf_mem_init, .fini = sf_mem_fini) {
    // dynamically allocate 4 integers
    int* num1 = sf_malloc(sizeof(int));
    int* num2 = sf_malloc(sizeof(int));
    int* num3 = sf_malloc(sizeof(int));
    int* num4 = sf_malloc(sizeof(int));
    // free block and verify data
    sf_free(num2);
    // free list must point to the header of num2
    cr_assert(freelist_head == ((void*)num2)-8);
    sf_free_header* num2_free_header = ((sf_free_header*) (((void*)num2)-8));
    // alloc and block size must be set appropriately
    cr_assert(num2_free_header -> header.alloc == 0);
    cr_assert(num2_free_header -> header.block_size = 32);
    // free block and left-coalesce
    sf_free(num3);
    // free list still points to left block
    cr_assert(freelist_head == ((void*)num2)-8);
    cr_assert(num2_free_header -> header.alloc == 0);
    cr_assert(num2_free_header -> header.block_size = 32 * 2);
    printf("%p %p\n", num1, num4);
}

// Test Case 4: assure left coalesced case, when left coalesced block has previous and next links make sure they stay connected
Test(sf_memsuite, Coalesce_left_block_extended, .init = sf_mem_init, .fini = sf_mem_fini) {
    // dynamically allocate 4 integers
    int* num1 = sf_malloc(sizeof(int));
    int* num2 = sf_malloc(sizeof(int));
    int* num3 = sf_malloc(sizeof(int));
    int* num4 = sf_malloc(sizeof(int));
    int* num5 = sf_malloc(sizeof(int));
    int* num6 = sf_malloc(sizeof(int));
    int* num7 = sf_malloc(sizeof(int));
    int* num8 = sf_malloc(sizeof(int));

    // assure alignment
    cr_assert((unsigned long)(num1) % 16 == 0);
    cr_assert((unsigned long)(num2) % 16 == 0);
    cr_assert((unsigned long)(num3) % 16 == 0);
    cr_assert((unsigned long)(num4) % 16 == 0);
    cr_assert((unsigned long)(num5) % 16 == 0);
    cr_assert((unsigned long)(num6) % 16 == 0);
    cr_assert((unsigned long)(num7) % 16 == 0);
    cr_assert((unsigned long)(num8) % 16 == 0);
  
    // left - coalescing
    sf_free_header* init_free_list = freelist_head;
    // assure details of newly freed block
    sf_free(num2);
    // free list must point to the start of num2
    cr_assert(freelist_head == ((void*)num2)-8, "free list must point to newly freed block!");
    sf_free_header* num2_free_header = ((sf_free_header*) (((void*)num2)-8));
    sf_header num2_header = num2_free_header -> header;
    // next should be the init free list after malloc occured
    cr_assert(num2_free_header -> next == init_free_list, "No coalescing case - next ptr of freed block must point to previously start of free list");
    // unalloc and block size set appropriately.
    cr_assert(num2_header.alloc == 0, "Unalloc bit of freed block must be 0");
    cr_assert((num2_header.block_size << 4) == 32, "Block size of integer must be 32");
    
    // assure details of next block on no coalescing case
    sf_free(num4);
    cr_assert(freelist_head == ((void*)num4)-8, "free list must point to newly freed block!");
    sf_free_header* num4_free_header = ((sf_free_header*) (((void*)num4)-8));
    cr_assert(num4_free_header -> next == num2_free_header, "No coalescing case - next ptr of freed block must point to previously start of free list");
    cr_assert(num2_free_header -> prev == num4_free_header, "Previous of previous free list must point to current free block");

    // free block and assure next and prev of current and links to links
    sf_free(num7);
    cr_assert(freelist_head == ((void*)num7)-8, "free list must point to newly freed block!");
    sf_free_header* num7_free_header = ((sf_free_header*) (((void*)num7)-8));
    cr_assert(num7_free_header -> next == num4_free_header);
    cr_assert(num7_free_header -> prev == NULL);
    cr_assert(num4_free_header -> prev == num7_free_header);
    cr_assert(num4_free_header -> next == num2_free_header);
    cr_assert(num2_free_header -> prev == num4_free_header);
    cr_assert(num2_free_header -> next == init_free_list);

    // assure left coalesced case, when left coalesced
    // block has previous and next links make sure they get
    // connected
    sf_free(num5);
    cr_assert((((sf_header*)num4_free_header) -> block_size << 4)== 64);
    cr_assert((((sf_header*)num4_free_header) -> alloc) == 0);
    // testing if it conducted LIFO when freeing
    // free list must point to start of left coalesced block
    cr_assert(freelist_head == num4_free_header);
    cr_assert(num7_free_header -> next == num2_free_header);
    cr_assert(num2_free_header -> prev == num7_free_header);
    cr_assert(num4_free_header -> next = num7_free_header);
}

// Test Case 5: this tests coalescing with a left and right block
Test(sf_memsuite, Coalesce_with_both_blocks, .init = sf_mem_init, .fini = sf_mem_fini) {
    // long double = 16 bytes
    long double* n1 = (long double*)sf_malloc(sizeof(long double));
    long double* n2 = (long double*)sf_malloc(sizeof(long double));
    long double* n3 = (long double*)sf_malloc(sizeof(long double));
    long double* n4 = (long double*)sf_malloc(sizeof(long double)); 
    long double* n5 = (long double*)sf_malloc(sizeof(long double)); 
    long double* n6 = (long double*)sf_malloc(sizeof(long double)); 
    long double* n7 = (long double*)sf_malloc(sizeof(long double)); 
    

    // assure alignment - removes unused warning for variables
    cr_assert((unsigned long)(n1) % 16 == 0);
    cr_assert((unsigned long)(n2) % 16 == 0);
    cr_assert((unsigned long)(n3) % 16 == 0);
    cr_assert((unsigned long)(n4) % 16 == 0);
    cr_assert((unsigned long)(n5) % 16 == 0);
    cr_assert((unsigned long)(n6) % 16 == 0);
    cr_assert((unsigned long)(n7) % 16 == 0);
    // main unit testing - coalescing with prev and next block

    // free block 2
    sf_free(n2);
    // free block 4
    sf_free(n4);
    // free block 6
    sf_free(n6);
    sf_free_header* num6_free_header = ((sf_free_header*)(((void*)n6)-8));
    // validate details of newly freed block
    cr_assert(num6_free_header -> header.alloc == 0);
    cr_assert(num6_free_header -> header.padding_size == 0);
    cr_assert(num6_free_header -> header.block_size == (32 >> 4));
    // test coalesce when next and prev blocks are free
    sf_free(n3);
    sf_free_header* num2_free_header = ((sf_free_header*) (((void*)n2)-8));
    cr_assert(freelist_head == num2_free_header);
    cr_assert(num2_free_header->header.block_size == 96 >> 4);    
    cr_assert(num2_free_header -> next == num6_free_header);
    // sf_snapshot(false);
}

// Test Case 6: this tests coalescing with a left and right block
// provides case when the coalesced blocks had prev and next blocks 
// (both coalesced blocks have prev and next blocks)
Test(sf_memsuite, Coalesce_with_both_blocks_extended, .init = sf_mem_init, .fini = sf_mem_fini) {
    // dynamically allocate 4 integers
    int* num1 = sf_malloc(sizeof(int));
    int* num2 = sf_malloc(sizeof(int));
    int* num3 = sf_malloc(sizeof(int));
    int* num4 = sf_malloc(sizeof(int));
    int* num5 = sf_malloc(sizeof(int));
    int* num6 = sf_malloc(sizeof(int));
    int* num7 = sf_malloc(sizeof(int));
    int* num8 = sf_malloc(sizeof(int));
    int* num9 = sf_malloc(sizeof(int));
    int* num10 = sf_malloc(sizeof(int));
    int* num11 = sf_malloc(sizeof(int));
    int* num12 = sf_malloc(sizeof(int));
    int* num13 = sf_malloc(sizeof(int));
    int* num14 = sf_malloc(sizeof(int));
    int* num15 = sf_malloc(sizeof(int));

    // assure alignment
    cr_assert((unsigned long)(num1) % 16 == 0);
    cr_assert((unsigned long)(num2) % 16 == 0);
    cr_assert((unsigned long)(num3) % 16 == 0);
    cr_assert((unsigned long)(num4) % 16 == 0);
    cr_assert((unsigned long)(num5) % 16 == 0);
    cr_assert((unsigned long)(num6) % 16 == 0);
    cr_assert((unsigned long)(num7) % 16 == 0);
    cr_assert((unsigned long)(num8) % 16 == 0);
    cr_assert((unsigned long)(num9) % 16 == 0);
    cr_assert((unsigned long)(num10) % 16 == 0);
    cr_assert((unsigned long)(num11) % 16 == 0);
    cr_assert((unsigned long)(num12) % 16 == 0);
    cr_assert((unsigned long)(num13) % 16 == 0);
    cr_assert((unsigned long)(num14) % 16 == 0);
    cr_assert((unsigned long)(num15) % 16 == 0);

    // 
    sf_free(num2);  // next = end_ | prev = 4    || prev will become 14
    sf_free(num4);  // next = 2 | prev = 14      || will become part of coalesced block
    sf_free(num14);  // next = 4 | prev = 10     || next will become 2

    sf_free(num10); // next = 14 | prev = 6     || prev will become 12
    sf_free(num6);  // next = 10 | prev = 12    || will become part of coalesced block
    sf_free(num12);  // next = 6 | prev = NULL  || next will become 10

    // main point  here
    sf_free(num5);  // start of the list

    // assure prev blocks link havent been updated
    sf_free_header* num2_free_header = ((sf_free_header*) (((void*)num2)-8));
    sf_free_header* num14_free_header = ((sf_free_header*) (((void*)num14)-8));
    cr_assert(num2_free_header -> prev == num14_free_header);
    cr_assert(num14_free_header -> next == num2_free_header);

    // assure next blocks link havent been updated
    sf_free_header* num10_free_header = ((sf_free_header*) (((void*)num10)-8));
    sf_free_header* num12_free_header = ((sf_free_header*) (((void*)num12)-8));
    cr_assert(num10_free_header -> prev == num12_free_header);
    cr_assert(num12_free_header -> next == num10_free_header);
}


// Test Case 7: alignment and consistency of arbitrary data types and structs
Test(sf_memsuite, Mallocing_Structs, .init = sf_mem_init, .fini = sf_mem_fini) {

    // define person
    typedef struct {
        int age;
        bool isAdult;
    }Person;
    // define house
    typedef struct {
        long price;
        int num_rooms;
        int num_bathrooms;
        Person* owner;
    }House;

    House* h = ((House*)sf_malloc(sizeof(House)));
    sf_free_header* house_header = (void*)h - 8;
    h -> price = 100;
    h -> num_rooms = 5;
    h -> num_bathrooms = 2;
    // sizeof(House) = 24 -> should pad to 32 + (16/H+F)
    cr_assert((house_header -> header.block_size << 4) == 48);
    cr_assert(house_header -> header.alloc == 1);
    Person* p = ((Person*)sf_malloc(sizeof(Person)));
    h -> owner = p;
    p -> age = 21;
    p -> isAdult = true;
    sf_free_header* person_header = (void*)p - 8;
    // sizeof(Person) = 8 -> should pad to 16 + (16/H+F)
    cr_assert((person_header -> header.block_size << 4) == 32);
    cr_assert(person_header -> header.alloc == 1);    
    // verify house
    cr_assert(h -> price == 100);
    cr_assert(h -> num_rooms == 5);
    cr_assert(h -> num_bathrooms == 2);
    cr_assert(h -> owner == p);
    cr_assert(p -> age == 21);
    cr_assert(p -> isAdult == true);

    // new owner bought the house
    Person* new_owner = ((Person*)sf_malloc(sizeof(Person)));
    new_owner -> age = 35;
    new_owner -> isAdult = true;
    // change details of house and verify
    h -> owner = new_owner;
    h -> num_rooms = 15;
    h -> num_bathrooms = 5;

    // verify new details of the house
    cr_assert(h -> num_rooms == 15);
    cr_assert(h -> num_bathrooms == 5);
    cr_assert(h -> owner == new_owner);
    // verify new owner and previous ower details
    cr_assert(p -> age == 21);
    cr_assert(p -> isAdult == true);
    cr_assert(new_owner -> age == 35);
    cr_assert(new_owner -> isAdult == true);
}

// Test Case 8 use malloc to perform merge routine between arrays
Test(sf_memsuite, Mallocing_Arrays, .init = sf_mem_init, .fini = sf_mem_fini) {
    int arr1[] = {10, 12, 16, 21, 23, 30};
    int arr2[] = {15, 17, 19, 24, 25, 32};
    int len = 6;
    int total_len = 12;

    // perform merge routine on dynamically allocated data 
    // and verify, while also creating dynamic data for 
    // indeces to verify data is not being corrupted anywhere
    int* p_arr = ((int*)sf_malloc(len * sizeof(int)));
    int* start_arr = p_arr;
    int* left = ((int*)sf_malloc(sizeof(int)));
    int* right = ((int*)sf_malloc(sizeof(int)));

    while((*left + *right) < total_len) {
        if(*left < len && (*right >= len || arr1[*left] < arr2[*right])) {
            *p_arr = arr1[*left];
            *left = *left + 1;
        } else {
            *p_arr = arr2[*right];
            *right = *right + 1;
        }
        p_arr = p_arr + 1;
    }
    int i = 0;
    for(i = 0; i < total_len-1; ++i) {
        cr_assert(*(start_arr+i) <= *(start_arr+i+1));
    }
    cr_assert(*left == len);
    cr_assert(*right == len);
}

// Test Case 9: extending the heap and freeing large chunks of memory
Test(sf_memsuite, Expanding_Heap, .init = sf_mem_init, .fini = sf_mem_fini) {
    void* large_ptr = sf_malloc(8192);
    sf_header* h_large_ptr = large_ptr-8;
    // verify header information
    cr_assert(h_large_ptr -> alloc == 1);
    cr_assert(h_large_ptr -> padding_size == 0);
    // 8192(paylod) + 16(H/F) = 8208
    cr_assert(h_large_ptr -> block_size == (8208>>4));
    // verify footer details
    int block_size = h_large_ptr -> block_size << 4;
    sf_footer* f = (sf_footer*) (h_large_ptr + (block_size / 8 - 1));
    cr_assert(f -> block_size == (8208>>4));
    cr_assert(f -> alloc == 1);

    // verify null pointer on extreme malloc case
    void* two_large_ptr = sf_malloc(4096 * 4);
    cr_assert(two_large_ptr == NULL);

}


// Test Case 10: copying strings from stack to dynamic data
Test(sf_memsuite, string_cpy, .init = sf_mem_init, .fini = sf_mem_fini) {

    char* prof = "jwong";
    // char* prof_rev = "gnowj";

    char* ta1 = "neal";
    // char* ta_name_1_rev = "laen";

    char* ta2 = "mehrab";
    // char* ta_name_2_rev = "barhem";

    char* ta3 = "mrunal";
    // char* ta_name_3_rev = "lanurm";

    char* ta4 = "romin";
    // char* ta_name_4_rev = "nimor";

    // almost a palindrome.
    char* ta5 = "nauman";
    // char* ta_name_5_rev = "namuan";


    int len_arr[] = {
        strlen(prof), strlen(ta1),
        strlen(ta2), strlen(ta3),
        strlen(ta4), strlen(ta5)
    };

    char* prof_cpy = (char*)sf_malloc(len_arr[0] + 1);
    char* ta1_cpy = (char*)sf_malloc(len_arr[1] + 1);
    char* ta2_cpy = (char*)sf_malloc(len_arr[2] + 1);
    char* ta3_cpy = (char*)sf_malloc(len_arr[3] + 1);
    char* ta4_cpy = (char*)sf_malloc(len_arr[4] + 1);
    char* ta5_cpy = (char*)sf_malloc(len_arr[5] + 1);

    // copy into heap and verify
    while(*prof) {
        *(prof_cpy++) = *(prof);
        ++prof;
    }
    *prof_cpy = '\0';

    while(*ta1) {
        *(ta1_cpy++) = *(ta1);
        ++ta1;
    }
    *ta1_cpy = '\0';

    while(*ta2) {
        *(ta2_cpy++) = *(ta2);
        ++ta2;
    }
    *ta2_cpy = '\0';

    while(*ta3) {
        *(ta3_cpy++) = *(ta3);
        ++ta3;
    }
    *ta3_cpy = '\0';

    while(*ta4) {
        *(ta4_cpy++) = *(ta4);
        ++ta4;
    }
    *ta4_cpy = '\0';


    while(*ta5) {
        *(ta5_cpy++) = *(ta5);
        ++ta5;
    }
    *ta5_cpy = '\0';

    // verify
    prof = prof - len_arr[0];
    prof_cpy = prof_cpy - len_arr[0];

    ta1 = ta1 - len_arr[1];
    ta1_cpy = ta1_cpy - len_arr[1];

    ta2 = ta2 - len_arr[2];
    ta2_cpy = ta2_cpy - len_arr[2];

    ta3 = ta3 - len_arr[3];
    ta3_cpy = ta3_cpy - len_arr[3];

    ta4 = ta4 - len_arr[4];
    ta4_cpy = ta4_cpy - len_arr[4];

    cr_assert(*ta1 == *ta1_cpy);
    cr_assert(*(ta2+1) == *(ta2_cpy+1));
    cr_assert(*(ta3+2) == *(ta3_cpy+2));
    cr_assert(*(ta4+3) == *(ta4_cpy+3));

    // assert all of the strings in jwong match with pointer on the heap
    cr_assert(*prof == *prof_cpy);
    cr_assert(*(prof+1) == *(prof_cpy+1));
    cr_assert(*(prof+2) == *(prof_cpy+2));
    cr_assert(*(prof+3) == *(prof_cpy+3));
    cr_assert(*(prof+4) == *(prof_cpy+4));
}
