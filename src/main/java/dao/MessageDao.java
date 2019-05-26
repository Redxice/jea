package dao;

import models.Message;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class MessageDao {
    @PersistenceContext(unitName = "myUnit")
    private EntityManager entityManager;

    public List<Message> getAll() {
        return entityManager.createNamedQuery("models.Message.getAll", Message.class).getResultList();
    }

    public Message find(Long id) {
        return entityManager.createNamedQuery("models.Message.findOne", Message.class).setParameter("id", id).getSingleResult();
    }

    public Message save(Message message) {
        entityManager.persist(message);
        entityManager.flush();
        return message;
    }

    public Message update(Message message) {
        entityManager.merge(message);
        entityManager.flush();
        return message;
    }
    public List<Message> findByForumId(Long id){
          return entityManager.createNamedQuery("models.Message.findByForumId", Message.class).setParameter("id", id).getResultList();
    }

    public void delete(Message message) {
        entityManager.remove(message);
    }

    public List<Message> findByUser(Long id) {
        return entityManager.createNamedQuery("models.Message.findByUserId", Message.class).setParameter("id", id).getResultList();
    }

    public List<Message> findByMainPost(Long id) {
        return entityManager.createNamedQuery("models.Message.findByMainPostId", Message.class).setParameter("id", id).getResultList();
    }
}
