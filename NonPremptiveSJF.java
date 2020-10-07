import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Collections;

class Process {
    int processID;
    int arrivalTime;
    int burstTime;
    int turnaroundTime = 0;
    int waitingTime = 0;
    int startTime = 0;
    int finishingTime = 0;

    public Process() {}
    public Process(int processID, int arrivalTime, int burstTime) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }
}

public class NonPremptiveSJF {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int numProcesses = 0;

        ArrayList<Process> processList = new ArrayList<>();
        int arrivalTime, burstTime;

        System.out.println("|LET'S DO NON-PREEMPTIVE PRIORITY SCHEDULING!|");
        System.out.println(" ");
        System.out.println("How many processes do you have?");
		System.out.print("= ");
        numProcesses = input.nextInt();

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter the BURST TIME of process #P" + i);
            burstTime = input.nextInt();

            System.out.println("Enter the ARRIVAL TIME of process #P" + i);
            arrivalTime = input.nextInt();

            processList.add(new Process(i, arrivalTime, burstTime));
        }

        // processList.add(new Process(0, 0, 6));
        // processList.add(new Process(1, 1, 4));
        // processList.add(new Process(2, 5, 6));
        // processList.add(new Process(3, 6, 6));
        // processList.add(new Process(4, 7, 6));
        // processList.add(new Process(5, 8, 6));

        System.out.println(" ");
        System.out.println("Calculating...");
        System.out.println(" ");

        //linked array to record whether each process in arraylist is completed or not
        boolean isCompleted[] = new boolean[numProcesses];
        
        int currentTime, completedNum;
        currentTime = completedNum = 0;
        double averageTurnaroundTime, averageWaitingTime, totalTurnaroundTime, totalWaitingTime;
        averageTurnaroundTime = averageWaitingTime = totalTurnaroundTime = totalWaitingTime = 0;
        
        while (completedNum < numProcesses) {
            //start of processIndex with negative number, will turn positive if index found
            int processIndex = -1;
            int maxBurstTime = 100000;
            // processIndex = maxPriority = -1;

            //find the process with the smallest burst time at this point in time
            for (int i = 0; i < numProcesses; i++) {
                Process currentProcess = processList.get(i);
                if (currentProcess.arrivalTime <= currentTime && isCompleted[i] == false) {
                    //check if current burst time smaller than max burst time
                    if (currentProcess.burstTime < maxBurstTime) {
                        maxBurstTime = currentProcess.burstTime;
                        processIndex = i;
                    }

                    //in the case that two processes have the same burst time, who came in first?
                    if (currentProcess.burstTime == maxBurstTime) {
                        if (currentProcess.arrivalTime < processList.get(processIndex).arrivalTime) {
                            maxBurstTime = currentProcess.burstTime;
                            processIndex = i;
                        }
                    }
                }
            }

            //if process was found
            if (processIndex != -1) {
                Process currentProcess = processList.get(processIndex);

                //calculating start, finishing, turnaround and waiting time for the process
                currentProcess.startTime = currentTime;
                currentProcess.finishingTime = currentProcess.startTime + currentProcess.burstTime;
                currentProcess.turnaroundTime = currentProcess.finishingTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;

                //mark that process as completed
                isCompleted[processIndex] = true;
                //increase number of processes completed
                completedNum = completedNum + 1;
                //mark current time as the finishing time to make sure next checking continues from there
                currentTime = currentProcess.finishingTime;
            } else {
                //if process wasn't found, just go to next time unit and see what has the highest priority there
                currentTime = currentTime + 1;
            }
        }

        //totalling the turnaround and waiting time
        for (int i = 0; i < numProcesses; i++) {
            Process process = processList.get(i);
            totalTurnaroundTime += process.turnaroundTime;
            totalWaitingTime += process.waitingTime;
        }

        averageTurnaroundTime = totalTurnaroundTime/numProcesses;
        averageWaitingTime = totalWaitingTime/numProcesses;

        //printing the details of each process
        System.out.println("Process\t\t" + "Starting Time\t\t" + "Finishing Time\t\t" + "Waiting Time\t\t" + "Turnaround Time\t\t");
        for (Process process : processList) {
            System.out.println(process.processID + "\t\t" + process.startTime + "\t\t\t" + process.finishingTime + "\t\t\t" + process.waitingTime + "\t\t\t" + process.turnaroundTime);
        }

        System.out.println();

        //sort by the finishing time so can print later which process takes place first
        //overload collections.sort
        Collections.sort(processList, new Comparator<Process>(){
            @Override
            public int compare(Process p1, Process p2) {
                return p1.finishingTime < p2.finishingTime ? -1 : 1;
            }
        });

        System.out.println("Here is the Gantt Chart: ");
		System.out.println(" ");
        //print out the sorted processes in order
        for (Process process : processList) {
            System.out.print("| P" + process.processID + " | ");
        }
        System.out.println();

        System.out.println(" ");
        System.out.println("The average TURNAROUND TIME is " + averageTurnaroundTime);
        System.out.println("The average WAITING TIME is " + averageWaitingTime);
    }

}