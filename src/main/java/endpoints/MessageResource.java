package endpoints;

import dao.MessageDao;
import dto.MessageDto;
import models.Message;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Stateless
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Path("messages")
public class MessageResource {

    @Inject
    private MessageDao messageDao;

    @GET
    @Produces("application/json")
    public List<Message> all() {
        return messageDao.getAll();
    }
    @GET
    @Path("/test")
    public MessageDto message() {
        Message message = new Message();
        message.setContent("dasf");
        MessageDto messageDto = new MessageDto(message);
        messageDto.setUri("/messages/test");
        return messageDto;
    }

    @POST
    @Consumes("application/json")
    public void save(Message message) {
        messageDao.save(message);
    }

    @PUT
    @Consumes("application/json")
    public void update(Message message) {
        messageDao.update(message);
    }

    @GET
    @Path("{id}")
    @Consumes("application/json")
    public Message getMessage(@PathParam("id")Long id){
        return messageDao.find(id);
    }

    @DELETE
    @Path("{id}")
    @Consumes("application/json")
    public void delete(@PathParam("id") Long id) {
        Message message = messageDao.find(id);
        messageDao.delete(message);
    }
}
