import os,subprocess
from sys import argv
for arg in argv[1:]:
    os.system(''' echo {0}'''.format(arg))
os.system("pwd")
status,output=subprocess.getstatusoutput(''' ipconfig ''')
if status==0:
    print(output)
else:
    print("illegal commands!")


