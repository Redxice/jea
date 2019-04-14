package dto;

import models.User;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

public class UserDto {

    private long id;
    private String name;
    private String password;
    private String token;
    private List<Link> links = new ArrayList<>();

    public UserDto(){}

    public UserDto(User user,UriInfo uriInfo) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.token = user.getToken();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
