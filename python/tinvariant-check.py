import re

#LOG = "log-bounded.txt"
#LOG = "log.txt"
#LOG = "log1000.txt"
LOG = "log_salida.txt"

REGEX = "regex-completa.txt"
#REGEX = "regex-simple.txt"

file = open(LOG, "r") 
data = file.read()
file.close()
data = re.sub(r"(?:.+)(T\d+)(?:.+\s)", r"\1 ", data)

regex = []
replacers = []

file = open(REGEX, "r") 
regtxt = file.read()
file.close()

regex = re.findall(r"(?:Regex.+:\s)(.+)", regtxt)
replacers = re.findall(r"(?:Replace.+:\s)(.+)", regtxt)


if(len(regex) != len(replacers)):
    print("Expressions and Replacers doesn't match")

index = 0
for i in range(len(regex)):
    print("\n- Checking: ", regex[i])
    prev = ""
    while(len(prev) != len(data)):
        print("\nIteration ", index, ":\n")
        print(data)
        prev = data
        #p = re.compile(regex[i])
        #m = p.search(data)
        #print("----------------\n")
        #if(m != None):
        #    print(m.group())
        #    print("------------\n")
        #else:
        #    print("NONE")
        data = re.sub(regex[i], replacers[i], data, 1)
        data = re.sub(r"(\s{2,})", " ", data)
        #if(m != None):
        #    print("\nIteration ", index, ":\n")
        #    print(data)
        index += 1

data = re.sub(r"(\s{2,})", " ", data)
print("\n\n- Resto: [", data, "]")