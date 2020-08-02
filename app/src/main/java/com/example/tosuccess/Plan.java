package com.example.tosuccess;

import static java.lang.String.valueOf;

public class Plan implements Comparable<Plan>{
    String activityName;
    int minutesAfterMidnightStart;
    int minutesAfterMidnightEnd;
    String startingTime;
    String endTime;
    Boolean completed;

    @Override
    public int compareTo(Plan p){
        return this.minutesAfterMidnightStart - p.getMinutes();
    }

    public Plan(String ActivityName, int minutesAfterMidnightStart, int minutesAfterMidnightEnd, String startingTime, String endTime,  Boolean activityCompleted){
        activityName = ActivityName;
        this.minutesAfterMidnightStart = minutesAfterMidnightStart;
        this.minutesAfterMidnightEnd = minutesAfterMidnightEnd;
        this.startingTime = startingTime;
        this.endTime = endTime;
        completed = activityCompleted;


    }
    public String getName(){
        return activityName;
    }
    public Boolean getCompleted(){
        return  completed;
    }
    public String getStrTimeStart(){return startingTime;}
    public String getStrTimeEnd(){return endTime;}
    public int getMinutes(){return minutesAfterMidnightStart;}
}
