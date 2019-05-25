package services;

import dao.MessageDao;
import dao.UserDao;
import dto.MessageDto;
import models.Message;
import models.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class MessageService {
    @Inject
    private MessageDao messageDao;
    @Inject
    private UserDao userDao;

    public Message save(Message message){
        return messageDao.save(message);
    }

    public boolean checkIfUserIdMatch(String username,Message message){
        User user = userDao.findUserByName(username);
        return message.getId() == user.getId();
    }

    public Message findById(long id) {
        return messageDao.find(id);
    }
/**
 * returns mainPost
 */
    public Message addReaction(Message mainPost, Message reaction) {
        mainPost.getReactions().add(reaction);
        return messageDao.update(mainPost);
    }
}
