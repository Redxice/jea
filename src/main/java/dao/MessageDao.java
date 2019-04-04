package dao;

import models.Message;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MessageDao {
    @PersistenceContext(unitName = "myUnit")
    private EntityManager entityManager;

    public List<Message> getAll() {
        return entityManager.createNamedQuery("Message.getAll", Message.class).getResultList();
    }

    public Message find(Long id) {
        return entityManager.createNamedQuery("Message.findOne", Message.class).setParameter("id", id).getSingleResult();
    }

    public void save(Message message) {
        entityManager.persist(message);
    }

    public void update(Message message) {
        entityManager.merge(message);
    }

    public void delete(Message message) {
        entityManager.remove(message);
    }
}
