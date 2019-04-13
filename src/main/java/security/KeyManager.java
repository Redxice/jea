package security;


import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.SecureRandom;

public class KeyManager {

    private static byte[] sharedKey = new byte[32];
    @PostConstruct
    private void setSharedKey(){
        new SecureRandom().nextBytes(sharedKey);
    }
   public static byte[] getSharedKey(){
        return sharedKey;
   }
}
