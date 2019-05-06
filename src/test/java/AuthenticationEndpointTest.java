import com.jayway.restassured.RestAssured;
import dao.UserDao;
import helpers.RestHelper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.*;

/**
 * think of a better way to test this class
 */
public class AuthenticationEndpointTest {

    @BeforeClass
    public static void setup()throws IOException {
        RestAssured.port = basicServerInfo.port;
        RestAssured.basePath = basicServerInfo.basePath;
        RestAssured.baseURI = basicServerInfo.baseURI;

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(baseURI+":"+port+basePath+"users");
        post.setHeader("Authorization","Bearer Henk:HenkPassword");
        client.execute(post);
    }

    @Test
    public void postAuthorizationTest() {
        given()
                .contentType("application/json")
                .header("Authorization","Bearer Henk:HenkPassword")
                .when()
                .post("authentication")
                .then()
                .statusCode(200);
    }
    @Test
    public void postAuthorizationInvalidCredentialsTest() {
        given()
                .contentType("application/json")
                .header("Authorization","Bearer Henk:a")
                .when()
                .post("authentication")
                .then()
                .statusCode(401);
    }
    @Test
    public void postAuthorizationInvalidHeaderTest() {
        given()
                .contentType("application/json")
                .header("Authorization","Basic Henk:a")
                .when()
                .post("authentication")
                .then()
                .statusCode(401);
    }

}
