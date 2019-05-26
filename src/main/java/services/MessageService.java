package services;

import dao.MessageDao;
import dao.UserDao;
import dto.MessageDto;
import mappers.MessageMapper;
import models.Forum;
import models.Message;
import models.User;
import sun.misc.resources.Messages;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class MessageService {
    @Inject
    private MessageDao messageDao;
    @Inject
    private UserDao userDao;

    private MessageMapper messageMapper = MessageMapper.INSTANCE;

    public Message save(Message message) {
        return messageDao.save(message);
    }

    public boolean checkIfUserIdMatch(String username, Message message) {
        User user = userDao.findUserByName(username);
        return message.getOwner().getId() == user.getId();
    }

    public Message findById(long id) {
        return messageDao.find(id);
    }

    /**
     * returns mainPost
     */
    public MessageDto addReaction(Message mainPost, Message reaction) {
        reaction.setMainPost(mainPost);
        messageDao.save(reaction);
        List<Message> messages = messageDao.findByMainPost(mainPost.getId());
        MessageDto messageDto = messageMapper.messageToMessageDto(mainPost);
        for (Message message: messages) {
            MessageDto reactionDto = messageMapper.messageToMessageDto(message);
            messageDto.getReactions().add(reactionDto);
        }

        return  messageDto;
    }
    public List<MessageDto> getByForum(Long id) {
        List<Message> messages = messageDao.findByForumId(id);
        return mapMessages(messages);
    }
    public List<MessageDto> getByUser(Long id){
        List<Message> messages= messageDao.findByUser(id);
        return mapMessages(messages);
    }

    private List<MessageDto> mapMessages(List<Message> messages){
        List<MessageDto> messageDtos = new ArrayList<>();
        for (Message message: messages){
            MessageDto messageDto = messageMapper.messageToMessageDto(message);
            if(message.getMainPost()==null) {
                for (Message reaction : getReactions(message)) {
                    MessageDto reactioDto = messageMapper.messageToMessageDto(reaction);
                    messageDto.getReactions().add(reactioDto);
                }
                messageDtos.add(messageDto);
            }

        }
        return messageDtos;
    }
   private List<Message> getReactions(Message message){
        return messageDao.findByMainPost(message.getId());
   }

}
