package com.binplus.earnquizmoney.Model;

public class Student {
    private String name;
    private int marks;
    private int rank;
    private Long time;



    public Student(String name, int marks, Long time) {
        this.name = name;
        this.marks = marks;
        this.time = time;
    }
    public Long getTime(){
        return time;
    }

    public void setTime(Long time){
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank=rank;
}
}
