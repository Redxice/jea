package beans;

import dao.UserDao;
import models.User;

import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Named("UserBean")
@SessionScoped
public class UserBean implements Serializable {
    @Inject
    private UserDao userDao;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    private UIComponent loginBtn;

//    @POST
//    @Path("create")
//    @Consumes("application/json")
//    public void createPerson(Person person){
//        System.out.println(person);
//        personDao.save(person);
//    }

    public List<User> getAllUsers(){
        return userDao.getAll();
    }
    public String registerUser(){
        userDao.save(new User(username,password));
        return "test";
    }
    public String login(){
        User user = userDao.validate(username,password);
        if (user != null){
            return "test";
        }else{
            FacesMessage message = new FacesMessage("Invalid password and/or username");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(loginBtn.getClientId(context), message);
        }
        return null;
    }

    public  String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UIComponent getLoginBtn() {
        return loginBtn;
    }

    public void setLoginBtn(UIComponent loginBtn) {
        this.loginBtn = loginBtn;
    }
}
