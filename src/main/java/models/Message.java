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
        @NamedQuery(name = "models.Message.getAll", query = "select m from Message m")
}
)
public class Message implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User owner;
    @OneToOne
    private Message mainPost;
    @NotEmpty(message = "Message is empty")
    private String content;
    private Date creationDate;
    @OneToMany(orphanRemoval=true)
    private List<Message> reactions = new ArrayList<>();
    @ManyToOne
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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
}
