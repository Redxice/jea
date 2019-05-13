package dao;

import helpers.DaoHelper;
import models.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
public class UserDao implements Serializable {

    @PersistenceContext(unitName = "myUnit")
    private EntityManager entityManager;

    public List<User> getAll() {
        return entityManager.createNamedQuery("User.getAll", User.class).getResultList();
    }

    public User find(Long id) {
        return DaoHelper.getSingleResult(User.class,entityManager.createNamedQuery("User.findOne", User.class).setParameter("id", id).getResultList());
    }

    public User save(User user){
        try {
            user.setPassword(encodeSHA256(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    public User update(User user) {
        return entityManager.merge(user);
    }

    public void delete(User user) {
        entityManager.remove(user);
    }
    public void delete(Long id){
        entityManager.createNamedQuery("User.deleteById",User.class)
                .setParameter("id",id)
                .executeUpdate();
    }

    public User validate(String name,String password){
        String passwordEncoded = password;
        try {
            passwordEncoded = encodeSHA256(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = DaoHelper.getSingleResult(User.class,entityManager.createNamedQuery("User.validate", User.class)
                .setParameter("name",name)
                .setParameter("password",passwordEncoded)
                .getResultList());
        return user;

    }
    public User findUserByName(String username){
        User user = DaoHelper.getSingleResult(User.class,entityManager.createNamedQuery("User.findByName",User.class)
        .setParameter("name",username)
        .getResultList());
        return user;
    }
    private static String encodeSHA256(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}
