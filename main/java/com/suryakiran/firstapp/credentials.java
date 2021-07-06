package com.suryakiran.firstapp;

public class credentials {
    String phonenumber;
    String name;
    String email;
    String profession;
    String UID;
    credentials(String name,String phonenumber,String email,String profession,String UID)
    {
        this.name=name;
        this.phonenumber=phonenumber;
        this.email=email;
        this.profession=profession;
        this.UID=UID;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getProfession() {
        return profession;
    }

    public String getUID() {
        return UID;
    }
}
