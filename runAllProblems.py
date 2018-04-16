# import subprocess as sub
from subprocess import call, check_output
import os
import re

cspFolder = "csp/"
binFolder = "bin"
logFile = "searchLog.csv"
problemPatern = re.compile(".*\.csp$")
branchPatern = re.compile("Branches explored: (\d+)")
arcPatern = re.compile("Arcs Revised: (\d+)")
errorPatern  = re.compile("")


heuristics = ["Name", "Random", "MaxDegree"]
call("rm -f " + cspFolder + "/*.csph",shell=True)
call("java -classpath " + binFolder + " HeuristicGenerator "+cspFolder,shell=True)
#call("rm -f " + logFile + " && touch " + logFile + " && echo \"Algorithm,Heuristic,Problem,Nodes explored,Arcs Revised\" >> "+logFile,shell=True)

for file in sorted(os.listdir(cspFolder)):
    if(problemPatern.match(file)):
        problem = os.path.splitext(file)[0]
        print(problem)
        BTOutput = check_output("java -classpath " + binFolder + " -ea BTSolver " + cspFolder + file ,shell=True)
        branchSearch = branchPatern.search(BTOutput)
        nodesExplored = branchSearch.group(1)
        arcsRevised = 0
        lineOut = "Backtracking,None, " + problem + ", " + str(nodesExplored) + ", " + str(arcsRevised)
        call("echo \"" + lineOut + "\" >> "+logFile,shell=True)
        print(lineOut)

        FCSDOutput = check_output("java -classpath " + binFolder + " -ea FCSolver " + cspFolder + file ,shell=True)
        branchSearch = branchPatern.search(FCSDOutput)
        nodesExplored = branchSearch.group(1)
        arcSearch = arcPatern.search(FCSDOutput)    
        arcsRevised = arcSearch.group(1)
        lineOut = "Forward Checking, Smallest Domain, " + problem + ", " + str(nodesExplored) + ", " + str(arcsRevised)
        call("echo \"" + lineOut + "\" >> "+logFile,shell=True)
        print(lineOut)

        for h in  ["Random", "MaxDegree"]:
            FCOutput = check_output("java -classpath " + binFolder + " -ea FCSolver " + cspFolder + file + " " + cspFolder+problem+h+".csph",shell=True)
            branchSearch = branchPatern.search(FCOutput)
            nodesExplored = branchSearch.group(1)
            arcSearch = arcPatern.search(FCOutput)    
            arcsRevised = arcSearch.group(1)
            lineOut = "Forward Checking, " + h + ", " + problem + ", " + str(nodesExplored) + ", " + str(arcsRevised)
            call("echo \"" + lineOut + "\" >> "+logFile,shell=True)
            print(lineOut)

 #        MACSDOutput = check_output("java -classpath " + binFolder + " -ea MACSolver " + cspFolder + file ,shell=True)
 #        branchSearch = branchPatern.search(MACSDOutput)
 #        nodesExplored = branchSearch.group(1)
 #        arcSearch = arcPatern.search(MACSDOutput)    
 #        arcsRevised = arcSearch.group(1)
 #        lineOut = "Maintaining Arc Consistency, Smallest Domain, " + problem + ", " + str(nodesExplored) + ", " + str(arcsRevised)
 #        call("echo \"" + lineOut + "\" >> "+logFile,shell=True)
	# print(lineOut)

 #        for h in heuristics:
 #            MACOutput = check_output("java -classpath " + binFolder + " -ea MACSolver " + cspFolder + file + " " + cspFolder+problem+h+".csph",shell=True)
 #            branchSearch = branchPatern.search(MACOutput)
 #            nodesExplored = branchSearch.group(1)
 #            arcSearch = arcPatern.search(MACOutput)    
 #            arcsRevised = arcSearch.group(1)
 #            lineOut = "Maintaining Arc Consistency, " + h + ", " + problem + ", " + str(nodesExplored) + ", " + str(arcsRevised)
 #            call("echo \"" + lineOut + "\" >> "+logFile,shell=True)
	#     print(lineOut)