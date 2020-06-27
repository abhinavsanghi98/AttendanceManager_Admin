package com.abhinavsanghi.vijayasangeetadmin;

public class Details {
    String enrollment;
    String name;
    String attendance;
    String absent;
    String percent;
    String due;

    public Details(String enrollment, String name, String attendance, String absent, String percent,String due) {
        this.enrollment = enrollment;
        this.name = name;
        this.attendance = attendance;
        this.absent = absent;
        this.percent = percent;
        this.due=due;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public String getName() {
        return name;
    }

    public String getAttendance() {
        return attendance;
    }

    public String getAbsent() {
        return absent;
    }

    public String getPercent() {
        return percent;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
    public void setDue(String due){this.due=due;}
    public String getDue(){return due;}
}
