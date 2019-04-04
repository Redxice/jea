package endpoints;

import dao.MessageDao;
import models.Message;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Stateless
@Path("messages")
public class MessageResource {
    @Inject
    private MessageDao messageDao;
    @GET
    @Path("/all")
    @Produces("application/json")
    public List<Message> all() {
        return messageDao.getAll();
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
