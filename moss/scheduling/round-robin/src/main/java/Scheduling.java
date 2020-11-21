// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06
// Edited by Momotenko Yuriy, 2020 November 20

import java.io.*;
import java.util.*;

public class Scheduling {
    private static int quantum = 100;
    private static int processnum = 5;
    private static int meanDev = 1000;
    private static int standardDev = 100;
    private static int runtime = 1000;
    private static Vector processVector = new Vector();
    private static Results result = new Results("null", "null", 0);
    private static String resultsFile = "Summary-Results";

    private static void Init(String file) {
        File f = new File(file);
        String line;
        int cputime = 0;
        int delay = 0;
        double X = 0.0;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("numprocess")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    processnum = Common.s2i(st.nextToken());
                }
                if (line.startsWith("meandev")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    meanDev = Common.s2i(st.nextToken());
                }
                if (line.startsWith("standdev")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    standardDev = Common.s2i(st.nextToken());
                }
                if (line.startsWith("quantum")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    quantum = Common.s2i(st.nextToken());
                }
                if (line.startsWith("process")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    delay = Common.s2i(st.nextToken());
                    X = Common.R1();
                    while (X == -1.0) {
                        X = Common.R1();
                    }
                    X = X * standardDev;
                    cputime = (int) X + meanDev;
                    processVector.addElement(new sProcess(cputime, delay, 0, 0));
                }
                if (line.startsWith("runtime")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    runtime = Common.s2i(st.nextToken());
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int i = 0;

        if (args.length != 1) {
            System.out.println("Usage: 'java Scheduling <INIT FILE>'");
            System.exit(-1);
        }
        File f = new File(args[0]);
        if (!(f.exists())) {
            System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
            System.exit(-1);
        }
        if (!(f.canRead())) {
            System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
            System.exit(-1);
        }
        System.out.println("Working...");
        Init(args[0]);
        if (processVector.size() < processnum) {
            i = 0;
            while (processVector.size() < processnum) {
                double X = Common.R1();
                while (X == -1.0) {
                    X = Common.R1();
                }
                X = X * standardDev;
                int cputime = (int) X + meanDev;
                processVector.addElement(new sProcess(cputime, i * 100, 0, 0));
                i++;
            }
        }
        result = SchedulingAlgorithm.Run(quantum, runtime, processVector, result);
        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            out.println("Scheduling Type: " + result.schedulingType);
            out.println("Scheduling Name: " + result.schedulingName);
            out.println("Simulation Run Time: " + result.compuTime);
            out.println("Mean: " + meanDev);
            out.println("Standard Deviation: " + standardDev);
            out.println("Quantum Time: " + quantum);
            out.println("Process #\tCPU Time\tDelay\tCPU Completed\tCPU Blocked");
            for (i = 0; i < processVector.size(); i++) {
                sProcess process = (sProcess) processVector.elementAt(i);
                out.print(i);
                if (i < 100) {
                    out.print("\t\t");
                } else {
                    out.print("\t");
                }
                out.print(process.cputime);
                if (process.cputime < 100) {
                    out.print(" (ms)\t\t");
                } else {
                    out.print(" (ms)\t");
                }
                out.print(process.delay);
                if (process.delay < 100) {
                    out.print(" (ms)\t\t");
                } else {
                    out.print(" (ms)\t");
                }
                out.print(process.cpudone);
                if (process.cpudone < 100) {
                    out.print(" (ms)\t\t");
                } else {
                    out.print(" (ms)\t");
                }
                out.println(process.numblocked + " times");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Completed.");
    }
}


