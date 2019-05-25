package services;

import dao.MessageDao;
import dto.ForumDto;
import dto.MessageDto;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@ApplicationScoped
public class ForumUpdateService {
    private SseBroadcaster broadcaster;
    private OutboundSseEvent.Builder eventBuilder;

    @Context
    public void setSse(Sse sse){
        this.eventBuilder = sse.newEventBuilder();
        this.broadcaster = sse.newBroadcaster();
    }
    public void broadcast(ForumDto forumDto){
        OutboundSseEvent outboundSseEvent = this.eventBuilder
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(forumDto).build();
    this.broadcaster.broadcast(outboundSseEvent);}

    public void listen(@Context SseEventSink sseEventSink){
        this.broadcaster.register(sseEventSink);
    }

}
