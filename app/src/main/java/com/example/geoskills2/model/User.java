package com.example.geoskills2.model;

public class User {
    private String name;
    private String email;
    private int points;

    private String uiid;
    private int profileSelected;

    public String getUiid() {
        return uiid;
    }

    public void setUiid(String uiid) {
        this.uiid = uiid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    // Para o firebase
    public User() {

    }

    public User(String name, String email, int points) {
        this.name = name;
        this.email = email;
        this.points = points;
    }
    public User(String name, String email, int points,String uiid) {
        this.name = name;
        this.email = email;
        this.points = points;
        this.uiid = uiid;
    }
    public User(String name, String email, int points,String uiid, int profileSelected) {
        this.name = name;
        this.email = email;
        this.points = points;
        this.uiid = uiid;
        this.profileSelected = profileSelected;
    }
    public User(String name, String email, int points,int profileSelected) {
        this.name = name;
        this.email = email;
        this.points = points;
        this.uiid = uiid;
        this.profileSelected = profileSelected;
    }


}
