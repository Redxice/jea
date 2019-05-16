package endpoints;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import dto.QrDto;
import dto.UserDto;
import endpoints.security.Secured;
import helpers.RestHelper;
import models.User;
import endpoints.security.KeyManager;
import services.AuthenticatorService;
import services.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Stateless
@Path("authentication")
public class AuthenticationEndpoint {

    @Inject
    private UserService userService;

    @Inject
    private AuthenticatorService authenticatorService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response authenticateUser(@Context HttpServletRequest httpServletRequest) {
                User user = userService.validate(httpServletRequest);
                if (user != null) {
                    if (!user.isTwoFactorEnabled()) {
                        String token = authenticatorService.generateToken(user);

                        return Response.ok(new UserDto(user)).header("Authorization", "Bearer " + token).build();
                    }
                    else {
                        String qrCode= authenticatorService.generateQrCode(user);
                        return Response.status(200).entity(new QrDto(qrCode) ).build();
                    }
                }
        return Response.status(401).build();
    }

    @POST
    @Path("/2fa")
    public Response confirmTwoFactorAuth(@Context HttpServletRequest httpServletRequest,String code){
        User user = userService.validate(httpServletRequest);
        if (user != null) {
            boolean result = AuthenticatorService.getGoogleAuthenticator().authorize(user.getTwoFactorAuthKey(),Integer.valueOf(code));
            if(result){
                String token = authenticatorService.generateToken(user);
                return Response.ok(new UserDto(user)).header("Authorization", "Bearer " + token).build();
            }
            return Response.status(401).build();
        }
        return Response.status(401).build();
    }

    }



