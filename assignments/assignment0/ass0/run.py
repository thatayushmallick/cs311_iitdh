import subprocess
import matplotlib.pyplot as plt
import numpy as np
import os

def compile_and_run_java(java_filename, java_input):
    compile_process = subprocess.run(['javac', java_filename], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    
    if compile_process.returncode == 0:
        run_process = subprocess.run(['java', java_filename[:-5]], text=True, input=str(java_input[0])+'\n' + str(java_input[1])+'\n', stdout=subprocess.PIPE, stderr=subprocess.PIPE)
      
if __name__ == "__main__":
  try:
     os.remove("output.txt")
  except OSError as e:
     pass
  java_filename = 'Printer.java'
  x = 100;
  for width in range(10,21,5):
      for probability in range(1,x,1):
          valueList = [width, probability/x]
          compile_and_run_java(java_filename, valueList)

  file = open("output.txt", "r")
  w = 0;
  probabilityArray = []
  timeArray = []
  for i in file:
      line = i.split()
      probabilityArray.append(float(line[1]))
      timeArray.append(int(line[2]))

  plt.plot(probabilityArray[0:x-2], timeArray[0:x-2], label="Border width 10")
  plt.plot(probabilityArray[x-1:2*x-3], timeArray[x-1:2*x-3], label="Border width 15")
  plt.plot(probabilityArray[2*x-2:3*x-4], timeArray[2*x-2:3*x-4], label="Border width 20")
  plt.xlabel("Probability Of Duty Cycle")
  plt.ylabel("Time Taken In Seconds")
  plt.legend()
  plt.show()
