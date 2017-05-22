(* compress - eliminate consecutive duplicates *)

fun compress(L) = 
	if L = [] then []
	else if tl(L) = [] then L
	else if hd(L) = hd(tl(L)) then compress(tl(L))
	else hd(L)::compress(tl(L));
	
compress(["a","a","a","a","b","c","c","a","a","d","e","e","e","e"]);


fun pack_helper(Element, Prefix, L) = 
	if L = [] then [Prefix]
	else if Element = hd(L) then pack_helper(hd(L), hd(L)::Prefix, tl(L))
	else Prefix::pack_helper(hd(L), [hd(L)], tl(L));

fun pack(L) = 
	pack_helper(hd(L), [hd(L)], L);

(*pack(["a","a","a","a","b","c","c","a","a","d","e","e","e","e"]);*)

fun list_len(L) =
	if L = [] then 0
	else 1 + list_len(tl(L));
	
fun encode_helper(L) = 
	if L = [] then []
	else (list_len(hd(L)), hd(hd(L)))::encode_helper(tl(L));

fun encode(L) = 
	encode_helper(pack(L));

encode(["a","a","a","a","b","c","c","a","a","d","e","e","e","e"]);

fun create_list(N, Elem) = 
	if N = 0 then []
	else Elem::create_list(N-1, Elem);
	
fun decode(L: (int * string) list) = 
	if L = [] then []
	else create_list(#1(hd(L)), #2(hd(L)))::decode(tl(L));

decode([(5, "a"),(1, "b"),(2, "c"),(2, "a"),(1, "d"),(4, "e")]);


fun findAll(E,L) =  
	if L=[] then []  
	else if E=hd(L) then E::findAll(E,tl(L))  
	else findAll(E,tl(L));
	
fun removeAll(E,L) =   
	if L=[] then []  
	else if E=hd(L) then removeAll(E,tl(L))  
	else hd(L)::removeAll(E,tl(L));
	
fun pack_all(L) =   
	if L=[] then []  
	else findAll(hd(L),L)::pack(removeAll(hd(L),L));
	
pack_all(["a","b","a"]);

fun toDigits(N) =   
	if N<10 then [N]  
	else toDigits(N div 10)@[N mod 10];
	
toDigits(123);  
	
fun fromDigitsHelper(RunningSum,L)=  
	if L=[] then RunningSum  
	else fromDigitsHelper(RunningSum*10+hd(L),tl(L));
	
fun fromDigits(L) =   
	fromDigitsHelper(0,L);  
	
fromDigits([1,2,3]);

6. Eliminate consecutive duplicates of list elements. If a list contains repeated elements they should be replaced with a single copy of the element. 
The order of the elements should not be changed.
Example: 
compress([a,a,a,a,b,c,c,a,a,d,e,e,e,e]); 	-> 			[a,b,c,a,d,e]
7. Pack consecutive duplicates of list elements into sublists. If a list contains repeated elements they should be placed in separate sublists. 
Example: pack([a,a,a,a,b,c,c,a,a,d,e,e,e,e]); [[a,a,a,a],[b],[c,c],[a,a],[d],[e,e,e,e]].
8. Run-length encoding of a list. 
Consecutive duplicates of elements are encoded as terms [N,E] where N is the number of duplicates of the element E. 
Example: encode([a,a,a,a,b,c,c,a,a,d,e,e,e,e]); [[4,a],[1,b],[2,c],[2,a],[1,d][4,e]]
9. Decode a run-length encoded list. Given a run-length code list generated as specified in problem 8. Construct its uncompressed version.
10. Create a list containing all integers within a given range. Example: range(4,9); [4,5,6,7,8,9].