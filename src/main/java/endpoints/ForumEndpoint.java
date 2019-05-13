package endpoints;

import dao.ForumDao;
import dao.UserDao;
import dto.ForumDto;
import dto.MessageDto;
import endpoints.security.Secured;
import helpers.RestHelper;
import mappers.ForumMapper;
import mappers.MessageMapper;
import models.Forum;
import models.User;
import services.ForumService;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Stateless
@Path("forums")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ForumEndpoint {
    @Inject
    private ForumService forumService;

    @Inject
    private UserDao userDao;

    private ForumMapper forumMapper = ForumMapper.INSTANCE;

    private MessageMapper messageMapper = MessageMapper.INSTANCE;

    @GET
    public Response getAll(){
        return Response.status(200).entity(forumService.getAll()).build();
    }

    @POST
    @Secured
    public Response saveForum(ForumDto forumDto){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ForumDto>> violations = validator.validate(forumDto);
        if(violations.isEmpty()){
            Forum forum = forumMapper.forumDtoToForum(forumDto);
            forum.setOwner(userDao.find(forum.getOwner().getId()));
            forum = forumService.save(forum);
            return Response.ok(forumMapper.forumToForumDto(forum)).build();
        }
        String message = violations.iterator().hasNext() ? violations.iterator().next().getMessage() : "";

        return Response.status(400).entity(message).build();
    }
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id){
        Forum forum = forumService.findById(id);
        if(forum != null){
            return Response.ok(forumMapper.forumToForumDto(forum)).build();
        }
        return Response.status(404).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    public Response deleteForum(@PathParam("id") Long id,@Context HttpServletRequest httpServletRequest){
        String username = RestHelper.getUsernameFromJWT(httpServletRequest.getHeader("Authorization"));
        Forum forum = forumService.findById(id);
        if(forum == null){
            return Response.status(404).build();
        }
        if(forum.getOwner().getName().equals(username)){
            forumService.deleteForum(id);
            return Response.status(204).build();
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @Secured
    public Response updateForum(ForumDto forumDto,@Context HttpServletRequest httpServletRequest){
        String username = RestHelper.getUsernameFromJWT(httpServletRequest.getHeader("Authorization"));
        Forum forum = forumMapper.forumDtoToForum(forumDto);
        User owner = userDao.find(forum.getOwner().getId());
        if(owner != null && owner.getName().equals(username)){
            forum = forumService.update(forum);
            return Response.ok(forumMapper.forumToForumDto(forum)).build();
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
    @GET
    @Path("{id}/messages")
    public Response getForumMessages(@PathParam("id") Long id){
        Forum forum = forumService.findById(id);
        if(forum == null){
            return Response.status(404).build();
        }
        List<MessageDto> messageDtos = messageMapper.messagesToMesssageDtos(forum.getMessages());
        return Response.ok(messageDtos).build();
    }




}
