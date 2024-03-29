package models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(name = "models.Message.findOne", query = "select m from Message m where m.id = :id"),
        @NamedQuery(name = "models.Message.getAll", query = "select m from Message m")
}
)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private long userID;
    private String content;
    public Message(){

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
