package endpoints;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import dao.UserDao;
import helpers.RestHelper;
import models.User;
import security.KeyManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@Path("authentication")
public class AuthenticationEndpoint {

    @Inject
    private UserDao userDao;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(@Context HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            List<String> usernameAndPassword = RestHelper.getUsernameAndPassword(authorizationHeader);
            if (usernameAndPassword.size() == 2) {
                String username = usernameAndPassword.get(0);
                String password = usernameAndPassword.get(1);
                User user = userDao.validate(username, password);
                if (user != null) {
                    String token = generateToken(user);
                    return Response.ok().header("Authorization","Bearer "+token).build();
                }
            }
        }

        return Response.status(401).build();
    }


    private String generateToken(User user) {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("jeaServer")
                .notBeforeTime(new Date(System.currentTimeMillis()-300000))
                .expirationTime(new Date(System.currentTimeMillis()+1800000))
                .issueTime(new Date(System.currentTimeMillis()))
                .claim("username", user.getName())
                .claim("scope","User")
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

