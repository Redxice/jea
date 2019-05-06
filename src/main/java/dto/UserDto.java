package dto;

import helpers.LinkAdapter;
import models.User;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

public class UserDto {

    private long id;
    private String name;
    private int level;
    private int hoursPlayed;
    @XmlElement(name = "link")
    @XmlJavaTypeAdapter(LinkAdapter.class)
    private List<Link> links = new ArrayList<>();

    public UserDto(){}

    public UserDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.level = user.getLevel();
        this.hoursPlayed = user.getHoursPlayed();
    }
    public UserDto(User user,UriInfo uriInfo) {
        this(user);
        generateLinks(uriInfo,user);
    }

    private void generateLinks(UriInfo uriInfo, User user){
        Link self = Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder())
                .rel("self").type("GET").build();
        Link deleteUser = Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder()
                .queryParam("id",id)).type("DELETE").build();
        Link getUser = Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder()
                .queryParam("id",id)).type("GET").build();
        Link updateUser = Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder()
                .queryParam("User",user)).type("PUT").build();
        Link saveUser = Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder()
                .queryParam("User",user)).type("POST").build();
        this.links.add(self);
        this.links.add(deleteUser);
        this.links.add(getUser);
        this.links.add(updateUser);
        this.links.add(saveUser);
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHoursPlayed() {
        return hoursPlayed;
    }

    public void setHoursPlayed(int hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
