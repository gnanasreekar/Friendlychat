package com.rgs.friendlychat.Chat;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Message {

  private String userID;
  private String username;
  private String message;
  private long timestamp;
  private boolean isNotification;
  private String pic;

  public Message() {

  }

  public Message(String userID, String username, String message, long timestamp, boolean isNotification, String pic) {
    this.userID = userID;
    this.username = username;
    this.message = message;
    this.timestamp = timestamp;
    this.isNotification = isNotification;
    this.pic = pic;
  }

  public boolean isNotification() {
    return isNotification;
  }

  public void setNotification(boolean notification) {
    isNotification = notification;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getPic() {
    return pic;
  }

  @Override
  public String toString() {
    return "Message{" +
        "userID='" + userID + '\'' +
        ", username='" + username + '\'' +
        ", message='" + message + '\'' +
        ", timestamp=" + timestamp +
        ", isNotification=" + isNotification +
        '}';
  }

  //might want to improve this
  @Override
  public boolean equals(Object other) {
    if (other == null) return false;
    if (other == this) return true;
    Message otherMessage = (Message) other;
    if ((this.message + this.username).equals((otherMessage.message + otherMessage.username))) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return username.hashCode() * message.hashCode();
  }
}
