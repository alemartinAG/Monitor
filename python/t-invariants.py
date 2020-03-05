import re

#LOG = "log-bounded.txt"
#LOG = "log.txt"
LOG = "../res/log.txt"
#LOG = "log1000.txt"

REGEX = "expressions.txt"


file = open(LOG, "r") 
data = file.read()
file.close()

data = re.sub(r"(?:.+)(T\d+)(?:.+\s)", r"\1 ", data)

print(data)

regex = []
replacers = []

file = open(REGEX, "r") 
regtxt = file.read()
file.close()

regex = re.findall(r"(?:Regex.+:\s)(.+)", regtxt)
replacers = re.findall(r"(?:Replace.+:\s)(.+)", regtxt)

iteration = 0
for i in range(len(regex)):
    print("\n ____________________________ \n")
    print("\n\n+++ REMOVING: ", regex[i], "\n")
    keepRunning = True
    while(keepRunning):
        p = re.compile(regex[i])
        m = p.search(data)
        if(m != None):
            print("\n ----- ", iteration, " ----\n ")
            #print("FOUND: ", m.group(0), "\n\n")
        temp = re.sub(regex[i], replacers[i], data, 1)
        if(len(temp) != len(data)):
            data = temp
            data = re.sub(r"(\s{2,})", " ", data)
            print(data)
            iteration += 1
        else:
            keepRunning = False

data = re.sub(r"(\s{2,})", " ", data)
print("\n\n@@@@@@@ RESTO @@@@@@@\n\n", data, "\n\n")

