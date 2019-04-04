package endpoints;

import dao.UserDao;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import models.User;
import security.KeyManager;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Key;

@Path("/authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationEndpoint {
    @Inject
    private UserDao userDao;
    @POST
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam(("password")) String password) {
        User user = userDao.validate(username, password);
        if (user != null) {
            String token = generateToken(username);
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

    }

    private String generateToken(String username) {
        return Jwts.builder().setSubject(username).signWith(KeyManager.getKeyPair().getPrivate()).compact();
    }
}

