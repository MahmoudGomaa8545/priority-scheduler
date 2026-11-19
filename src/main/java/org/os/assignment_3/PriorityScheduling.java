package org.os.assignment_3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PriorityScheduling {
    List<Process> processList = new ArrayList<>();

    void push(Process process) {
        processList.add(process);
    }

    private void sortProcessesByArrivalTime(List<Process> processListToSort) {
        Collections.sort(processListToSort, Comparator.comparingInt(Process::getAri_time));
    }

    private void sortProcessesByPriority(List<Process> processListToSort) {
        Collections.sort(processListToSort, Comparator.comparingInt(Process::getPriority));
    }

    List<Process> schedule(int num_pro, int context_switch) {
        sortProcessesByArrivalTime(processList);

        List<Process> completed = new ArrayList<>();
        for (Process process : processList) {
            process.is_complete = false;
            process.star_time = 0;
            process.complete_time = 0;
        }

        int time = 0;

        while (completed.size() < num_pro) {
            List<Process> readyProcesses = new ArrayList<>();

            for (Process process : processList) {
                if (process.getAri_time() <= time && !process.is_complete) {
                    readyProcesses.add(process);
                }
            }

            if (!readyProcesses.isEmpty()) {
                sortProcessesByPriority(readyProcesses);

                Process processToExecute = readyProcesses.get(0);
                processToExecute.star_time = time;
                time += processToExecute.getBurst_time();
                processToExecute.complete_time = time;
                processToExecute.is_complete = true;
                completed.add(processToExecute);
            } else {
                time++;
            }
            time += context_switch;
        }

        return completed;
    }

    String report(List<Process> completed) {
        StringBuilder report = new StringBuilder();
        int total_wait = 0;
        int total_turnaround = 0;

        report.append("Scheduling Report:\n");
        report.append("====================\n");

        for (Process process : completed) {
            String s = "Process " + process.getName() + " | Waiting Time: " + process.waiting_time() + " | Turnaround Time: " + process.turnaround_time();
            report.append(s).append("\n");
            total_wait += process.waiting_time();
            total_turnaround += process.turnaround_time();
        }

        double avg_waiting_time = (double) total_wait / completed.size();
        double avg_turnaround_time = (double) total_turnaround / completed.size();

        report.append("\n====================\n");
        report.append("Average Waiting Time = ").append(avg_waiting_time).append("\n");
        report.append("Average Turnaround Time = ").append(avg_turnaround_time).append("\n");

        return report.toString();
    }
}
