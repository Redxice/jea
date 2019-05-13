package dao;

import helpers.DaoHelper;
import models.Forum;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class ForumDao {
    @PersistenceContext(unitName = "myUnit")
    private EntityManager entityManager;

    public Forum save(Forum forum){
        entityManager.persist(forum);
        entityManager.flush();
        return forum;
    }

    public List<Forum> getAll() {
        return entityManager.createNamedQuery("Forum.getAll", Forum.class).getResultList();
    }

    public List<Forum> getByTag(String tag){
        return  entityManager.createNamedQuery("Forum.findByTag",Forum.class)
                .setParameter("tag",tag)
                .getResultList();
    }

    public List<Forum> getByOwner(Long owner_id){
        return entityManager.createNamedQuery("Forum.findByOwner",Forum.class)
                .setParameter("id",owner_id)
                .getResultList();
    }

    public Forum findById(Long id){
        return DaoHelper.getSingleResult(Forum.class,entityManager.createNamedQuery("Forum.findById",Forum.class)
                .setParameter("id",id)
                .getResultList());
    }

    public void update(Forum forum) {
        entityManager.merge(forum);
    }

    public void delete(Long id) {
        entityManager.createNamedQuery("Forum.deleteById",Forum.class)
        .setParameter("id",id)
        .executeUpdate();
        entityManager.flush();
    }



}
