package endpoints;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import dao.UserDao;
import models.User;
import org.apache.sling.commons.json.JSONObject;
import security.KeyManager;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationEndpoint {
    @Inject
    private UserDao userDao;

    @POST
    public User authenticateUser(@Context HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getHeader("username");
        String password = httpServletRequest.getHeader("password");

        User user = userDao.validate(username, password);
        if (user != null) {
            String token = generateToken(username);
            user.setToken(token);
            return user;
        } else {
            return null;
        }

    }

    private String generateToken(String username) {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .claim("username", username)
                .build();
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256),
                new Payload(claims.toJSONObject()));
        try {
            jwsObject.sign(new MACSigner(KeyManager.getSharedKey()));
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return jwsObject.serialize();
    }
}

