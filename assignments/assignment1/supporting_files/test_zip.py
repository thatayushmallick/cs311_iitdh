#!/bin/python

import sys
import os
import zipfile
from evaluate import evaluate
import shutil

zip_file = sys.argv[1]

students = []
for t in zip_file.split("/")[-1].split("_"):
    if t.isnumeric():
        students.append(t)
    else:
        break
print("Students : " + str(students))

submissions_temp_dir = "./submissions/" 

if not os.path.exists(submissions_temp_dir):
	os.mkdir(submissions_temp_dir)

zip_ref = zipfile.ZipFile(zip_file, 'r')
zip_ref.extractall(submissions_temp_dir)
zip_ref.close()

total_marks = 0
scored_marks = 0
for asm_file in os.listdir(submissions_temp_dir):
	[t,s] = evaluate(submissions_temp_dir + asm_file)
	print(asm_file + ";\tscore = " + str(s) + " out of " + str(t))
	total_marks = total_marks + t
	scored_marks = scored_marks + s

shutil.rmtree(submissions_temp_dir)

print("total score = " + str(scored_marks) + " out of " + str(total_marks))
