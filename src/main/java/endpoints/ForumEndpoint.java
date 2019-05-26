package endpoints;

import dao.ForumDao;
import dao.UserDao;
import dto.ForumDto;
import dto.MessageDto;
import endpoints.interceptors.logger;
import endpoints.security.Secured;
import helpers.RestHelper;
import mappers.ForumMapper;
import mappers.MessageMapper;
import models.Forum;
import models.Message;
import models.User;
import services.ForumService;
import services.ForumUpdateService;
import services.MessageService;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.SseEventSink;
import java.util.List;
import java.util.Set;

@RequestScoped
@Path("forums")
@Consumes("application/json")
@Produces("application/json")
public class ForumEndpoint {

    @Inject
    private ForumService forumService;

    @Inject
    private UserDao userService;
    @Inject
    private ForumUpdateService forumUpdateService;
    @Inject
    private MessageService messageService;


    private ForumMapper forumMapper = ForumMapper.INSTANCE;

    private MessageMapper messageMapper = MessageMapper.INSTANCE;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        List<ForumDto> forumDtos = forumMapper.forumsToForumDtos(forumService.getAll());
        return Response.status(200).entity(forumDtos).build();
    }
    @GET
    @Path("/subscribe")
    @Produces("text/event-stream")
    public void listen(@Context SseEventSink sseEventSink){
        forumUpdateService.listen(sseEventSink);
    }

    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveForum(ForumDto forumDto){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ForumDto>> violations = validator.validate(forumDto);
        if(violations.isEmpty()){
            Forum forum = forumMapper.forumDtoToForum(forumDto);
            forum.setOwner(userService.find(forum.getOwner().getId()));
            forum = forumService.save(forum);
            ForumDto forumDto1 = forumMapper.forumToForumDto(forum);
            forumUpdateService.broadcast(forumDto1);
            return Response.ok(forumDto1).build();
        }
        String message = violations.iterator().hasNext() ? violations.iterator().next().getMessage() : "";

        return Response.status(400).entity(message).build();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id){
        Forum forum = forumService.findById(id);
        if(forum != null){
            ForumDto forumDto = forumMapper.forumToForumDto(forum);
            forumDto.setMessageDtos(messageService.getByForum(id));
            return Response.ok(forumDto).build();
        }
        return Response.status(404).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors(logger.class)
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateForum(ForumDto forumDto,@Context HttpServletRequest httpServletRequest){
        String username = RestHelper.getUsernameFromJWT(httpServletRequest.getHeader("Authorization"));
        Forum forum = forumMapper.forumDtoToForum(forumDto);
        User owner = userService.find(forum.getOwner().getId());
        if(owner != null && owner.getName().equals(username)){
            ForumDto forum_new = forumMapper.forumToForumDto(forumService.update(forum));
            forumUpdateService.broadcast(forum_new);
            return Response.ok(forum_new).build();
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
    @GET
    @Path("{id}/messages")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getForumMessages(@PathParam("id") Long id){
        Forum forum = forumService.findById(id);
        if(forum == null){
            return Response.status(404).build();
        }
        List<MessageDto> messageDtos = messageMapper.messagesToMesssageDtos(forum.getMessages());
        return Response.ok(messageDtos).build();
    }





}
