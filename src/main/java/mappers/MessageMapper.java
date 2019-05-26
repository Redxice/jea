package mappers;

import dto.ForumDto;
import dto.MessageDto;
import models.Forum;
import models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import javax.ejb.Stateless;
import java.util.List;

@Mapper(componentModel = "message")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);
    @Mappings({
            @Mapping(source = "owner.id", target = "owner_id"),
            @Mapping(source = "forum.id", target = "forum_id"),
            @Mapping(source = "owner.name", target = "username")
//            @Mapping(source = "mainPost.id",target = "mainPost_id")
    })
    MessageDto messageToMessageDto(Message message);

    List<MessageDto> messagesToMesssageDtos(List<Message> messages);

    @Mappings({@Mapping(source = "owner_id", target = "owner.id"),
            @Mapping(source = "forum_id", target = "forum.id"),
            @Mapping(source = "username", target = "owner.name")
//            @Mapping(source = "mainPost_id",target = "mainPost.id")
    })
    Message messageDtoToMessage(MessageDto messageDto);
}
