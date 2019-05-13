package services;

import dao.ForumDao;
import dto.ForumDto;
import helpers.DaoHelper;
import models.Forum;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Stateless
public class ForumService {
    @Inject
    private ForumDao forumDao;

    public Forum save(Forum forum){
        return forumDao.save(forum);
    }

    public List<Forum> getAll() {
        return forumDao.getAll();
    }

    public List<Forum> getByTag(String tag){
        return  forumDao.getByTag(tag);
    }

    public List<Forum> getByOwner(Long owner_id){
        return forumDao.getByOwner(owner_id);
    }

    public Forum findById(Long id){
        return forumDao.findById(id);
    }

    public void deleteForum(Long id){
        forumDao.delete(id);
    }

    public Forum update(Forum forum) {
         forumDao.update(forum);
         return findById(forum.getId());
    }
}
