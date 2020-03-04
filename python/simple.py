import re

#LOG = "log-bounded.txt"
LOG = "log.txt"
#LOG = "log1000.txt"
#LOG = "log_salida.txt"
#LOG = "test.txt"



#### MAIN ###

file = open(LOG, "r") 
data = file.read()
file.close()

data = re.sub(r"(?:.+)(T\d+)(?:.+\s)", r"\1 ", data)

print(data)

#regex = r"(?:(?:T1\b)(.+?)(?:T2\b)(.+?)(?:T3)|(?:T4)(.+?)(?:T5)(.+?)(?:T6)|(?:T7)(.+?)(?:T8)(.+?)(?:T9))(.+?)(?:(?:T12(?:(.+?)(?:T13)(?:(.+?)(?:(?:T19)(?:(.+?)(?:T20)(?:(.+?)(?:T21))?)?|(?:T22(?:(.+?)(?:T23)(?:(.+?)(?:T24))?)?)))?)?)|(?:(?:T14)(?:(.+?)(?:T15)(?:(.+?)(?:T16)(?:(.+?)(?:T17)(?:(.+?)(?:T18)(?:(.+?)(?:(?:T19)(?:(.+?)(?:T20)(?:(.+?)(?:T21))?)?|(?:T22(?:(.+?)(?:T23)(?:(.+?)(?:T24))?)?)))?)?)?)?)?))"
regex = r"(?:(?:T1\b)(.+?)(?:T2\b)(.+?)(?:T3)|(?:T4)(.+?)(?:T5)(.+?)(?:T6)|(?:T7)(.+?)(?:T8)(.+?)(?:T9))(.+?)(?:(?:T12(?:(.+?)(?:T13)(?:(.+?)(?:(?:T19)(?:(.+?)(?:T20)(?:(.+?)(?:T21)))|(?:T22(?:(.+?)(?:T23)(?:(.+?)(?:T24))))))))|(?:(?:T14)(?:(.+?)(?:T15)(?:(.+?)(?:T16)(?:(.+?)(?:T17)(?:(.+?)(?:T18)(?:(.+?)(?:(?:T19)(?:(.+?)(?:T20)(?:(.+?)(?:T21)))|(?:T22(?:(.+?)(?:T23)(?:(.+?)(?:T24))))))))))))"
#regex = r"(?:(?:T1\b)(.+?)(?:T2\b)(.+?)(?:T3)|(?:T4)(.+?)(?:T5)(.+?)(?:T6)|(?:T7)(.+?)(?:T8)(.+?)(?:T9))(.+?)(?:((?!(?:T14).*))(?:T12(?:(.+?)(?:T13)(?:(.+?)(?:(?:T19)(?:(.+?)(?:T20)(?:(.+?)(?:T21))?)?|(?:T22(?:(.+?)(?:T23)(?:(.+?)(?:T24))?)?)))?)?)|(?:((?!(?:T12).*))(?:T14)(?:(.+?)(?:T15)(?:(.+?)(?:T16)(?:(.+?)(?:T17)(?:(.+?)(?:T18)(?:(.+?)(?:(?:T19)(?:(.+?)(?:T20)(?:(.+?)(?:T21))?)?|(?:T22(?:(.+?)(?:T23)(?:(.+?)(?:T24))?)?)))?)?)?)?)?))"
replace = r"\1\2\3\4\5\6\7\8\9\10\11\12\13\14\15\16\17\18\19\20\21\22"
#replace = r"\1\2\3\4\5\6\7\8\9\10\11\12\13\14\15\16\17\18\19\20\21\22\23\24"
regex2 = r"(?:T11)(.+?)(?:T10)|(?:T11)"
replace2 = r"\1"

data = re.sub(regex2, replace2, data)

keepRunning = True
iteration = 0
while(keepRunning):
    p = re.compile(regex)
    m = p.search(data)
    print("\n ----- ", iteration, " ----\n ")
    if(m != None):
        print("FOUND: ", m.group(0), "\n\n")
    temp = re.sub(regex, replace, data, 1)
    if(len(temp) != len(data)):
        data = temp
        data = re.sub(r"(\s{2,})", " ", data)
        print(data)
        iteration += 1
    else:
        keepRunning = False

data = re.sub(r"(\s{2,})", " ", data)
print("\nRESTO ------\n\n", data)

