// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    public static Results Run(int quantum, int runtime, Vector<sProcess> processVector, Results result) {
        int i = 0;
        int comptime = 0;
        //int currentProcess = 0;
        int previousProcess = 0;
        int size = processVector.size();
        int completed = 0;
        String resultsFile = "Summary-Processes";

        Vector<sProcess> sortedProcessVector = processVector;
        sortedProcessVector.sort(new sProcessComparator());

        Queue<sProcess> queue = new LinkedList<>();

        result.schedulingType = "Batch (Nonpreemptive)";
        result.schedulingName = "Round Robin scheduling";
        try {
            //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
            //OutputStream out = new FileOutputStream(resultsFile);
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));


            int lastProcessIndex = 0;

            for (sProcess s : sortedProcessVector) {
                if (s.delay == 0) {
                    queue.add(s);
                    ++lastProcessIndex;
                } else {
                    break;
                }
            }


            sProcess process = null;

            if (!queue.isEmpty()) {
                process = (sProcess) sortedProcessVector.elementAt(0);
                out.println("Process: " + processVector.indexOf(sortedProcessVector.elementAt(0)) + " registered... (" + process.cputime + " " + process.delay + " " + process.cpudone + ")");
            }

            int quantumCounter = 0;

            //System.out.println(processVector.size());

            while (comptime < runtime) {
                //add entered processes
                if (lastProcessIndex < sortedProcessVector.size()) {
                    while (sortedProcessVector.elementAt(lastProcessIndex).delay == comptime) {
                        queue.add(sortedProcessVector.elementAt(lastProcessIndex));
                        out.println("Process joined queue");
                        out.println("Process: " + processVector.indexOf(sortedProcessVector.elementAt(lastProcessIndex)) + " (" + sortedProcessVector.elementAt(lastProcessIndex).cputime + " " + sortedProcessVector.elementAt(lastProcessIndex).delay + " " + sortedProcessVector.elementAt(lastProcessIndex).cpudone + ")");

                        ++lastProcessIndex;
                        if (lastProcessIndex >= sortedProcessVector.size()) {
                            break;
                        }
                    }
                }

                if (process == null && !queue.isEmpty()){
                    process = queue.peek();
                }

                if (process != null && process.cpudone == process.cputime) {
                    completed++;
                    quantumCounter = 0;
                    out.println("Quantum time reset");
                    out.println("Process: " + processVector.indexOf(process) + " completed... (" + process.cputime + " " + process.delay + " " + process.cpudone + ")");
                    if (completed == size) {
                        result.compuTime = comptime;
                        out.close();
                        return result;
                    }

                    queue.remove();

                    if (!queue.isEmpty()) {
                        process = queue.peek();
                        out.println("Process: " + processVector.indexOf(process) + " registered... (" + process.cputime + " " + process.delay + " " + process.cpudone + ")");
                    }
                    else{
                        process = null;
                    }
                }

                //TODO: check empty queue???

                //if quantum time expired
                if (quantumCounter == quantum) {
                    quantumCounter = 0;
                    process.numblocked++;
                    out.println("Quantum time expired");
                    out.println("Process: " + processVector.indexOf(process) + " forced out... (" + process.cputime + " " + process.delay + " " + process.cpudone + ")");

                    queue.add(queue.poll());
                        process = queue.peek();
                        out.println("Process: " + processVector.indexOf(process) + " registered... (" + process.cputime + " " + process.delay + " " + process.cpudone + ")");
                }

                if (process != null){
                    quantumCounter++;
                    process.cpudone++;
                }

                comptime++;
            }
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }
        result.compuTime = comptime;
        return result;
    }
}
