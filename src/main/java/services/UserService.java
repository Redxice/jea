package services;

import dao.UserDao;
import helpers.RestHelper;
import models.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Stateless
public class UserService {
    @Inject
    private UserDao userDao;


    public User find(Long id) {
       return userDao.find(id);
    }

    public User validate(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            List<String> usernameAndPassword = RestHelper.getUsernameAndPassword(authorizationHeader);
            if (usernameAndPassword.size() == 2) {
                String username = usernameAndPassword.get(0);
                String password = usernameAndPassword.get(1);
                return userDao.validate(username, password);
            }
            return null;
        }
        return null;
    }

    public User findByName(String username) {
        return userDao.findUserByName(username);
    }

    public User update(User user) {
        return userDao.update(user);
    }
    public boolean checkIfUserCanEdit(String editor_name,Long edit_id){
        User user = findByName(editor_name);
        if(user==null){
            return false;
        }else {
            return user.getId() == edit_id;
        }
    }
}
