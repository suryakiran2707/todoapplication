package com.suryakiran.myapplication;
import java.util.Date;
import java.util.List;

public class FirstTime {
    String Name;
    String info;
    List<String> documents;
    String time;
    String Date;

    FirstTime(){

    }




    public void setDate(String Date) {
        this.Date = Date.substring(0,2)+"-"+Date.substring(2,4)+"-"+Date.substring(4,6);
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return Name;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
