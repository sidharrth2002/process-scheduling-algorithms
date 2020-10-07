import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Collections;

class Process {
    int processID;
    int arrivalTime;
    int burstTime;
    int priority;
    int turnaroundTime = 0;
    int waitingTime = 0;
    int startTime = 0;
    int finishingTime = 0;
    int remainingBurst;
    int enterQueueAt;

    public Process() {}
    public Process(int processID, int arrivalTime, int burstTime, int priority) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        remainingBurst = burstTime;
        enterQueueAt = arrivalTime;
    }
}

public class PremptivePriority {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int numProcesses = 0;

        ArrayList<Process> processList = new ArrayList<>();
        int arrivalTime, burstTime, priority;

        System.out.println("||LET'S DO PREEMPTIVE PRIORITY SCHEDULING!||");
		System.out.println(" ");
        System.out.println("How many processes do you have?");
		System.out.print("= ");
        numProcesses = input.nextInt();

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter the BURST TIME of process #P" + i);
            burstTime = input.nextInt();

            System.out.println("Enter the ARRIVAL TIME of process #P" + i);
            arrivalTime = input.nextInt();

            System.out.println("Enter the PRIORITY of process #P" + i);
            priority = input.nextInt();
            
            processList.add(new Process(i, arrivalTime, burstTime, priority));
        }

        // processList.add(new Process(0, 0, 6, 3));
        // processList.add(new Process(1, 1, 4, 3));
        // processList.add(new Process(2, 5, 6, 1));
        // processList.add(new Process(3, 6, 6, 1));
        // processList.add(new Process(4, 7, 6, 5));
        // processList.add(new Process(5, 8, 6, 6));

        System.out.println(" ");
        System.out.println("Calculating...");
        System.out.println(" ");
		System.out.println("Here is the Gantt Chart: ");
		System.out.println(" ");;
        //linked array to record whether each process in arraylist is completed or not
        boolean isCompleted[] = new boolean[numProcesses];
        
        int currentTime, completedNum;
        currentTime = completedNum = 0;
        double averageTurnaroundTime, averageWaitingTime, totalTurnaroundTime, totalWaitingTime;
        averageTurnaroundTime = averageWaitingTime = totalTurnaroundTime = totalWaitingTime = 0;

        //dummy process, just for the first time it runs
        //will be used to check when each enters the queue
        Process pastProcess = new Process(1000, 0, 0, 0);

        while (completedNum < numProcesses) {
            
            int processIndex = -1;
            int maxPriority = 100000;
            
            for (int i = 0; i < numProcesses; i++) {
                Process currentProcess = processList.get(i);
                if (currentProcess.arrivalTime <= currentTime && isCompleted[i] == false) {
                    //check if current priority higher than maximum priority- smaller number
                    if (currentProcess.priority < maxPriority) {
                        maxPriority = currentProcess.priority;
                        processIndex = i;
                    }

                    //in the case that two processes have the same priority, who entered queue first
                    //enter queue if another process takes over
                    if (currentProcess.priority == maxPriority) {
                        // if (currentProcess.arrivalTime < processList.get(processIndex).arrivalTime) {
                            // if ((currentTime - )
                            if (currentProcess.enterQueueAt < processList.get(processIndex).enterQueueAt) {
                                maxPriority = currentProcess.priority;
                                processIndex = i;
                            }
                    }
                }
            }

            if (processIndex != -1) {
                Process currentProcess = processList.get(processIndex);
                
                //checks if moved to a new process now
                //if new, then update when the previous one enters the queue
                if (!pastProcess.equals(currentProcess)) {
                    pastProcess.enterQueueAt = currentTime;
                }
                if (currentProcess.remainingBurst == currentProcess.burstTime) {
                    currentProcess.startTime = currentTime;
                }
                //for every time unit, decrease counter
                currentProcess.remainingBurst -= 1;
                currentTime += 1;
                System.out.print("| P" + currentProcess.processID + " | ");
                //to check later if a new process is occupying
                pastProcess = currentProcess;

                if (currentProcess.remainingBurst == 0) {
                    currentProcess.finishingTime = currentTime;
                    currentProcess.turnaroundTime = currentProcess.finishingTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                    isCompleted[processIndex] = true;
                    completedNum += 1;
                }
            } else {
                currentTime += 1;
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

        System.out.println();
        System.out.println();
        //printing the details of the processes
        System.out.println("Process\t\t" + "Starting Time\t\t" + "Finishing Time\t\t" + "Waiting Time\t\t" + "Turnaround Time\t\t");
        for (Process process : processList) {
            System.out.println(process.processID + "\t\t" + process.startTime + "\t\t\t" + process.finishingTime + "\t\t\t" + process.waitingTime + "\t\t\t" + process.turnaroundTime);
        }

        System.out.println();

        System.out.println("The average TURNAROUND TIME is " + averageTurnaroundTime);
        System.out.println("The average WAITING TIME is " + averageWaitingTime);
		System.out.println(" ");
    }
}