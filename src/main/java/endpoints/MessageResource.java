package endpoints;

import dao.MessageDao;
import dto.MessageDto;
import endpoints.security.Secured;
import helpers.RestHelper;
import mappers.MessageMapper;
import models.Message;
import services.MessageService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("messages")
public class MessageResource {

    @Inject
    private MessageDao messageDao;
    @Inject
    private MessageService messageService;
    @Inject
    private MessageMapper messageMapper;

    @GET
    public Response all() {
        return Response.ok(messageDao.getAll()).build()  ;
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
    @Secured
    public Response save(MessageDto messageDto) {
      return Response.status(200).entity(messageService.save(messageMapper.messageDtoToMessage(messageDto))).build() ;
    }

    @GET
    @Path("{id}")
    public Response getMessage(@PathParam("id") Long id) {
        Message message = messageService.findById(id);
        if(message == null){
            return Response.status(404).build();
        }
        return Response.status(200).entity(messageDao.find(id)).build();
    }

    @DELETE
    @Path("{id}")
    @Secured
    public Response delete(@PathParam("id") Long id, @Context HttpServletRequest httpServletRequest) {
        String username = RestHelper.getUsernameFromJWT(httpServletRequest.getHeader("Authorization"));
        Message message = messageDao.find(id);
        if(message == null){
            return Response.status(404).build();
        }
        if(messageService.checkIfUserIdMatch(username,message)){
            messageDao.delete(message);
            return Response.status(204).build();
        }else{
            return Response.status(401).build();
        }
    }

    @POST
    @Secured
    @Path("{id}")
    public Response reactToMessage(@PathParam("id") Long id, MessageDto messageDto, @Context HttpServletRequest httpServletRequest) {
        String username = RestHelper.getUsernameFromJWT(httpServletRequest.getHeader("Authorization"));
        Message message = messageMapper.messageDtoToMessage(messageDto);
        if (messageService.checkIfUserIdMatch(username, message)) {
            Message mainPost = messageService.findById(message.getMainPost().getId());
            if (mainPost == null) {
                return Response.status(404).entity("mainPost could not be found").build();
            } else {
                Message updatedMainPost = messageService.addReaction(mainPost, message);
                MessageDto messageDtoMainPost =messageMapper.messageToMessageDto(updatedMainPost);
                return Response.status(200).entity(messageDtoMainPost).build();
            }
        }
        return Response.status(401).build();

    }
}
