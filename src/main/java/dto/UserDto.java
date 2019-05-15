package dto;

import helpers.LinkAdapter;
import models.User;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDto {

    private long id;
    @NotEmpty(message="name is missing")
    private String name;
    private int level;
    private int hoursPlayed;
    private List<String> links = new ArrayList<>();
    private boolean twoFactorEnabled;
    private String email;

    public UserDto(){}

    public UserDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.level = user.getLevel();
        this.hoursPlayed = user.getHoursPlayed();
        this.twoFactorEnabled = user.isTwoFactorEnabled();
        this.email = user.getEmail();
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
        this.links.add(self.toString());
        this.links.add(deleteUser.toString());
        this.links.add(getUser.toString());
        this.links.add(updateUser.toString());
        this.links.add(saveUser.toString());
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

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
