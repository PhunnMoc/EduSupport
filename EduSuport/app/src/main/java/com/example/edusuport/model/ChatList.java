package com.example.edusuport.model;

public class ChatList {

    private String idUser, name, message, date, time;

    public ChatList(String idUser, String name, String message, String date, String time) {
        this.idUser = idUser;
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public ChatList() {
    }

    public String getidUser() {
        return idUser;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
