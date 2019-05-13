package mappers;
import dto.ForumDto;
import models.Forum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.ejb.Stateless;
import java.util.List;


@Mapper(componentModel = "forum")
public interface ForumMapper {
    ForumMapper INSTANCE = Mappers.getMapper(ForumMapper.class);
    @Mapping(source="owner.id", target = "owner_id")
    ForumDto forumToForumDto(Forum forum);
    List<ForumDto> forumsToForumDtos(List<Forum> forums);

    @Mapping(source="owner_id", target = "owner.id")
    Forum forumDtoToForum(ForumDto forumDto);
}
