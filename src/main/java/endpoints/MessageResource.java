package endpoints;

import dao.MessageDao;
import dto.MessageDto;
import models.Message;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("messages")
public class MessageResource {

    @Inject
    private MessageDao messageDao;

    @GET
    public List<Message> all() {
        return messageDao.getAll();
    }
    @GET
    @Path("/test")
    public MessageDto message() {
        Message message = new Message();
        message.setContent("dasf");
        MessageDto messageDto = new MessageDto();
        return messageDto;
    }

    @POST
    public void save(Message message) {
        messageDao.save(message);
    }

    @PUT
    public void update(Message message) {
        messageDao.update(message);
    }

    @GET
    @Path("{id}")
    public Message getMessage(@PathParam("id")Long id){
        return messageDao.find(id);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Message message = messageDao.find(id);
        messageDao.delete(message);
    }
}
