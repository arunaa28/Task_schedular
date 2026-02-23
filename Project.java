package com.promanage;

public class Project {
    public int id, deadline, revenue, weekNo;
    public String title;
    public double score;

    public Project(int id, String title, int deadline, int revenue, int weekNo) {
        this.id = id; this.title = title; this.deadline = deadline;
        this.revenue = revenue; this.weekNo = weekNo;
    }
}