package dto;

import models.Message;

public class MessageDto {
    private long id;
    private long userID;
    private String content;


    public MessageDto(Message message){
        this.id = message.getId();
        this.userID = message.getUserID();
        this.content = message.getContent();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
