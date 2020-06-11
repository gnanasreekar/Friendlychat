package com.rgs.friendlychat.Chat;

public class ChatUserPOJO {
    String UID,Name,Email;

    public ChatUserPOJO(String UID, String name, String email) {
        this.UID = UID;
        Name = name;
        Email = email;
    }

    public String getUID() {
        return UID;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }
}
