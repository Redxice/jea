package dao;

import models.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;


@Stateless
public class UserDao implements Serializable {

    @PersistenceContext(unitName = "myUnit")
    private EntityManager entityManager;

    public List<User> getAll() {
        return entityManager.createNamedQuery("User.getAll", User.class).getResultList();
    }

    public User find(Long id) {
        return DoaHelper.getSingleResult(User.class,entityManager.createNamedQuery("User.findOne", User.class).setParameter("id", id).getResultList());
    }

    public User save(User user) {
        entityManager.persist(user);
        return validate(user.getName(),user.getPassword());
    }

    public User update(User user) {
        return entityManager.merge(user);
    }

    public void delete(User user) {
        entityManager.remove(user);
    }

    public User validate(String username,String password){
        User user = DoaHelper.getSingleResult(User.class,entityManager.createNamedQuery("User.validate", User.class)
                .setParameter("username",username)
                .setParameter("password",password)
                .getResultList());
        return user;
    }
}
