package org.os.assignment_3;

public class Process {
    String pro_name;
    String col;
    int ari_time;
    int burst_time;
    int star_time;
    int complete_time;
    int priority;
    boolean is_complete = false;

    Process(String pro_name, String col, int ari_time, int burst_time) {
        this.pro_name = pro_name;
        this.col = col;
        this.ari_time = ari_time;
        this.burst_time = burst_time;
        this.priority = 0; // Default priority; will be set later
    }

    int waiting_time() {
        return star_time - ari_time;
    }

    int turnaround_time() {
        return complete_time - ari_time;
    }

    public String getName() {
        return pro_name;
    }

    public int getAri_time() {
        return ari_time;
    }

    public int getBurst_time() {
        return burst_time;
    }

    public String getColor() {
        return col;
    }

    public int getStar_time() {
        return star_time;
    }

    public int getComplete_time() {
        return complete_time;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
