import time

fib_memoized = {}

def simple_fibonacci(n):
    if n <= 2:
        return 1
    else:
        return simple_fibonacci(n - 1) + simple_fibonacci(n - 2)


def dynamic_fibonacci(n):
    if n in fib_memoized:
        return fib_memoized[n]
    fib_num = 0
    if n <= 2:
        fib_num = 1
    else:
        fib_num = dynamic_fibonacci(n - 1) + dynamic_fibonacci(n - 2)
    fib_memoized[n] = fib_num
    return fib_num




def main():
    print "Naive approach"
    print (int(round(time.time() * 1000)))
    print(simple_fibonacci(30))
    print (int(round(time.time() * 1000)))

    print
    print "Dynamic Programming"
    print (int(round(time.time() * 1000)))
    print(dynamic_fibonacci(30))
    print (int(round(time.time() * 1000)))

main()
