package com.abhinavsanghi.vijayasangeetadmin;

public class Student {

    String sname;
    String sid;
    String spass;
    String batch;
    String due;


    public Student(String sname, String sid, String spass, String batch,String due) {
        this.sname = sname;
        this.sid = sid;
        this.spass = spass;
        this.batch = batch;
        this.due=due;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSpass() {
        return spass;
    }

    public void setSpass(String spass) {
        this.spass = spass;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
    public String getDue(){return due;}
    public void setDue(){this.due=due;}
}
