import sys

if len(sys.argv) != 2:
    print("Usage: python hello.py <name>")
else:
    print("Hello,", sys.argv[1], "!")
