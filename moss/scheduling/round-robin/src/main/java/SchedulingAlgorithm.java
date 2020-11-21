// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {
    public static Results Run(int quantum, int runtime, Vector<sProcess> processVector, Results result) {
        int comptime = 0;
        int size = processVector.size();
        int completed = 0;
        String resultsFile = "Summary-Processes";

        Vector<sProcess> sortedProcessVector = processVector;
        sortedProcessVector.sort(new sProcessComparator());

        Queue<sProcess> queue = new LinkedList<>();

        result.schedulingType = "Batch (Nonpreemptive)";
        result.schedulingName = "Round Robin scheduling";
        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

            int lastProcessIndex = 0;
            sProcess process = null;
            int quantumCounter = 0;

            while (comptime < runtime) {
                //add entered processes
                if (lastProcessIndex < size) {
                    while (sortedProcessVector.elementAt(lastProcessIndex).delay == comptime) {
                        sProcess processCopy = sortedProcessVector.elementAt(lastProcessIndex);
                        queue.add(processCopy);
                        out.println("Process joined queue");
                        out.println("Process: " + processVector.indexOf(processCopy) + " (" + processCopy.cputime + " " + processCopy.delay + " " + processCopy.cpudone + ")");

                        ++lastProcessIndex;
                        if (lastProcessIndex >= size) {
                            break;
                        }
                    }
                }

                if (process == null && !queue.isEmpty()) {
                    process = queue.peek();
                    out.println("Process: " + processVector.indexOf(process) + " registered... (" + process.cputime + " " + process.delay + " " + process.cpudone + ")");
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
                    } else {
                        process = null;
                    }
                }

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

                if (process != null) {
                    quantumCounter++;
                    process.cpudone++;
                }

                comptime++;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        result.compuTime = comptime;
        return result;
    }
}
