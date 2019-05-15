package services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import endpoints.security.KeyManager;
import helpers.RestHelper;
import models.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Stateless
public class AuthenticatorService {
    private static GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    @Inject
    private UserService userService;

    public static GoogleAuthenticator getGoogleAuthenticator() {
        return googleAuthenticator;
    }

    public String generateQrCode(User user) {
        GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();
        user.setTwoFactorAuthKey(googleAuthenticatorKey.getKey());
        userService.update(user);
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("jeaServer", user.getEmail(), googleAuthenticatorKey);
    }
    public String generateToken(User user) {
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
