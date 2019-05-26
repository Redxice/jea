package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "Forum.findById", query = "select f from Forum f where f.id = :id"),
        @NamedQuery(name = "Forum.getAll", query = "select f from Forum f"),
        @NamedQuery(name ="Forum.findByOwner", query = "select f from Forum f where  f.owner.id = :id"),
        @NamedQuery(name="Forum.findByTag",query = "select f from Forum f where f.tag = :tag "),
        @NamedQuery(name="Forum.deleteById",query = "delete From Forum f where f.id = :id ")
}
)
public class Forum {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String tag;
    @ManyToOne
    private User owner;
    @OneToMany(cascade = {CascadeType.REFRESH},mappedBy = "forum",orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
