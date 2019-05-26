package dto;

import models.Message;

import java.util.Date;
import java.util.List;

public class MessageDto {

    private long id;
    private Long owner_id;
    private String username;
    private String content;
    private Long forum_id;
    private List<MessageDto> reactions;
    private String creationDate;
//    private Long mainPost_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Long owner_id) {
        this.owner_id = owner_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getForum_id() {
        return forum_id;
    }

    public void setForum_id(Long forum_id) {
        this.forum_id = forum_id;
    }

    public List<MessageDto> getReactions() {
        return reactions;
    }

    public void setReactions(List<MessageDto> reactions) {
        this.reactions = reactions;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
//    public Long getMainPost_id() {
//        return mainPost_id;
//    }
//
//    public void setMainPost_id(Long mainPost_id) {
//        this.mainPost_id = mainPost_id;
//    }
}
