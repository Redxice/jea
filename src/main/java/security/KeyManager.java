package security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.KeyPair;

public class KeyManager {
   private static KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.HS256);

    public static KeyPair getKeyPair() {
        return keyPair;
    }
}
