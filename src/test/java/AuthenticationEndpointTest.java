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

public class AuthenticationEndpointTest {
    private static String token;
    @BeforeClass
    public static void setup()throws IOException {
        RestAssured.port = basicServerInfo.port;
        RestAssured.basePath = basicServerInfo.basePath;
        RestAssured.baseURI = basicServerInfo.baseURI;

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(baseURI+":"+port+basePath+"users");
        post.setHeader("Authorization","Bearer Henk:HenkPassword");
        client.execute(post);
//        HttpPost postAuthorize = new HttpPost(basicServerInfo.baseURI+basicServerInfo.basePath+"/authentication");
//        postAuthorize.setHeader("Authorization","Bearer Henk:HenkPassword");
//        HttpResponse httpResponse = client.execute(postAuthorize);
//        token = httpResponse.getFirstHeader("Authorization").getValue();
    }

    @Test
    public void postAuthorization() {
        given()
                .contentType("application/json")
                .header("Authorization","Bearer Henk:HenkPassword")
                .when()
                .post("authentication")
                .then()
                .statusCode(200);
    }

}
