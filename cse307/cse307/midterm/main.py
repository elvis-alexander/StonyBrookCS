def lastElement(L):
    return L[len(L) - 1]


def kthElement(L, k):
    return L[k];


def reverseList(L):
    for i in range(0, len(L) /2):
        tmp = L[i]
        L[i] = L[len(L) - i - 1]
        L[len(L) - i - 1] = tmp
    return L


def removeLastElem(L):
    del L[len(L) - 1]


def palindrome_help(L, leftIndex, rightIndex):
    if leftIndex == rightIndex:
        return True
    elif L[leftIndex] == L[rightIndex]:
        return palindrome_help(L, leftIndex+1, rightIndex-1)
    else:
        return False


def palindrome(L):
    if len(L) == 1 | len(L) == 0:
        return True
    else:
        return palindrome_help(L, 0, len(L) - 1)


# 6. Eliminate consecutive duplicates of list elements. If a list contains repeated elements
# they should be replaced with a single copy of the element. The order of the elements should not be changed.
# Example: compress([a,a,a,a,b,c,c,a,a,d,e,e,e,e]); [a,b,c,a,d,e]
def compress(L):
    compress_list = []
    for i in range(0, len(L)):
        if i == len(L) - 1:
            if compress_list[len(compress_list) - 1] != L[i]:
                compress_list.append(L[i])
        elif L[i] != L[i+1]:
            compress_list.append(L[i])
    print compress_list

# 7. Pack consecutive duplicates of list elements into sublists.
#   If a list contains repeated elements they should be placed in separate sublists.
#   Example: pack([a,a,a,a,b,c,c,a,a,d,e,e,e,e]); [[a,a,a,a],[b],[c,c],[a,a],[d],[e,e,e,e]].
def get_num_elem(L, index):
    count = 1
    for i in range(index, len(L) - 1):
        if L[i] == L[i+1]:
            count += 1
        else:
            break
    return count


def pack(L):
    current_index = 0
    pack_list = []
    while current_index < len(L):
        num_elem = get_num_elem(L, current_index)
        list = []
        for i in range(current_index, current_index+num_elem):
            list.append(L[current_index])
        pack_list.append(list)
        current_index += num_elem
    return pack_list


# 8. Run-length encoding of a list.
# Consecutive duplicates of elements are encoded as terms [N,E] where N is the number of duplicates of the element
# E. Example: encode([a,a,a,a,b,c,c,a,a,d,e,e,e,e]); [[4,a],[1,b],[2,c],[2,a],[1,d][4,e]]
def encode(L):
    pack_list = pack(L)
    encode_list = []
    for i in range(0, len(pack_list)):
        list = []
        list.append(len(pack_list[i]))
        list.append(pack_list[i][0])
        encode_list.append(list)
    return encode_list


# 9. Decode a run-length encoded list. Given a run-length code
# list generated as specified in problem 8. Construct its uncompressed version.
def uncode(L):
    uncode_list = []
    for i in range(0, len(L)):
        for j in range(0, L[i][0]):
            uncode_list.append(L[i][1])
    return uncode_list


# 10. Create a list containing all integers within a given range. Example: range(4,9); [4,5,6,7,8,9].
def create_list(r1, r2):
    new_list = []
    for i in range(r1, r2+1):
        new_list.append(i)
    return new_list


def main():
    print(pack([1, 1, 1, 1, 2, 3, 3, 1, 1, 4, 5, 5, 5, 5]))
    print(encode([1, 1, 1, 1, 2, 3, 3, 1, 1, 4, 5, 5, 5, 5]))
    print(uncode([[4, 1], [1, 2], [2, 3], [2, 1], [1, 4], [4, 5]]))
    print(create_list(4, 6))
    print(compress([1, 1, 1, 1, 2, 3, 3, 1, 1, 4, 5, 5, 5, 5]))

main()
