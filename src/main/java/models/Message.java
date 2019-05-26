package models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "models.Message.findOne", query = "select m from Message m where m.id = :id"),
        @NamedQuery(name = "models.Message.getAll", query = "select m from Message m"),
        @NamedQuery(name = "models.Message.findByForumId", query = "select m from Message m where m.forum.id = :id and m.mainPost = null"),
        @NamedQuery(name = "models.Message.findByUserId", query = "select m from Message m where m.owner.id = :id "),
        @NamedQuery(name = "models.Message.findByMainPostId", query = "select m from Message m where m.mainPost.id = :id")
}
)
public class Message implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    private User owner;
    @NotEmpty(message = "Message is empty")
    private String content;
    private String creationDate;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Message mainPost;
    @OneToMany(cascade ={CascadeType.MERGE,CascadeType.REFRESH},mappedBy = "mainPost",orphanRemoval=true)
    private List<Message> reactions = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Forum forum;
    public Message(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Message> getReactions() {
        return reactions;
    }

    public void setReactions(List<Message> reactions) {
        this.reactions = reactions;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Message getMainPost() {
        return mainPost;
    }

    public void setMainPost(Message mainPost) {
        this.mainPost = mainPost;
    }
    public void addReaction(Message reaction){
        reaction.setMainPost(this);
        this.reactions.add(reaction);
    }

}
