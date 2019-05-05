package helpers;

import com.nimbusds.jose.JWSObject;
import models.UserRoles;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RestHelper {
    public static List<String> getUsernameAndPassword(String authorizationHeader) {
        int indexStartUsername = authorizationHeader.indexOf(" ");
        int indexEndUsername = authorizationHeader.indexOf(":");
        String username = authorizationHeader.substring(indexStartUsername,indexEndUsername);
        String password = authorizationHeader.substring(indexEndUsername+1);
        List<String> credentials = new ArrayList<>();
        credentials.add(username);
        credentials.add(password);
        return credentials;
    }
    public static String getUsernameFromJWT(String jwt){
        try {
            JWSObject jwsObject = JWSObject.parse(jwt);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
