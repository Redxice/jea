package endpoints;

import dao.MessageDao;
import dto.MessageDto;
import endpoints.security.Secured;
import helpers.RestHelper;
import mappers.MessageMapper;
import models.Forum;
import models.Message;
import services.ForumService;
import services.MessageService;
import services.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
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
    private UserService userService;

    private MessageMapper messageMapper = MessageMapper.INSTANCE;

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
    public Response save(MessageDto messageDto,@Context HttpServletRequest httpServletRequest) {
        String username = RestHelper.getUsernameFromJWT(httpServletRequest.getHeader("Authorization"));
        long id = userService.findByName(username).getId();
        if (id == messageDto.getOwner_id()){
            messageDto.setCreationDate(new Date(System.currentTimeMillis()).toString());
            Message message = messageMapper.messageDtoToMessage(messageDto);
            message = messageService.save(message);
            message.setOwner(userService.find(message.getOwner().getId()));
            messageDto = messageMapper.messageToMessageDto(message);
            return Response.status(200).entity(messageDto).build() ;
        }
        else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

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
        messageDto.setCreationDate(new Date(System.currentTimeMillis()).toString());
        Message message = messageMapper.messageDtoToMessage(messageDto);
        if (messageService.checkIfUserIdMatch(username, message)) {
            Message mainPost = messageService.findById(id);
            if (mainPost == null) {
                return Response.status(404).entity("mainPost could not be found").build();
            } else {
                return Response.status(200).entity(messageService.addReaction(mainPost, message)).build();
            }
        }
        return Response.status(401).build();

    }
}
