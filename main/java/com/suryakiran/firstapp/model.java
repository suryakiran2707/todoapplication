package com.suryakiran.myapplication;
import java.util.List;

public class model {

    String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String info;
    List<String> documents;
    String time;
    String Date;

    public String getInfo() {
        return info;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
