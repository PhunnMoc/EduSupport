package com.example.edusuport.model;

public class MessageList {
    private String idpartner, name, phone, lastMsg, profilePic, chatKey;
    private String role;

    public MessageList(String idpartner, String name, String phone, String lastMsg, String profilePic, String role,String chatKey) {
        this.idpartner = idpartner;
        this.name = name;
        this.phone = phone;
        this.lastMsg = lastMsg;
        this.profilePic = profilePic;
        this.chatKey = chatKey;
        this.role = role;
    }

    public String getIdpartner() {
        return idpartner;
    }

    public void setIdpartner(String idpartner) {
        this.idpartner = idpartner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
