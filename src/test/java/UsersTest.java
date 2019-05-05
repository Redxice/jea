import com.jayway.restassured.RestAssured;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersTest {
    private static String token;
    @BeforeClass
    public static void setup() throws IOException {
        RestAssured.port = basicServerInfo.port;
        RestAssured.basePath = basicServerInfo.basePath;
        RestAssured.baseURI = basicServerInfo.baseURI;
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(basicServerInfo.baseURI+basicServerInfo.basePath+"/users");
        post.setHeader("Authorization","Bearer Henk:HenkPassword");
        client.execute(post);
        HttpPost postAuthorize = new HttpPost(basicServerInfo.baseURI+basicServerInfo.basePath+"/authentication");
        postAuthorize.setHeader("Authorization","Bearer Henk:HenkPassword");
        HttpResponse httpResponse = client.execute(postAuthorize);
        token = httpResponse.getFirstHeader("Authorization").getValue();
    }

//    @Test
//    public void cgetAllUsersTest() {
//        given().when().get("users").then().statusCode(200);
//    }

    @Test
    public void aPostUserTest() {
        given()
                .header("Authorization","Bearer test:password")
                .when()
                .post("users")
                .then()
                .statusCode(204);

    }

    @Test
    public void bGetUserInvalidTest() {
        given()
                .pathParam("id", 82521)
                .when()
                .get("users/{id}").
                then()
                .statusCode(500);
    }

    @Test
    public void bGetUserValidTest() {
        given()
                .pathParam("id", 1)
                .when()
                .get("users/{id}").
                then()
                .statusCode(200);
    }

    @Test
    public void updateUserTest() {
        Map<String, String> user = new HashMap<>();
        user.put("name", "test");
        user.put("lvl", "30");
        user.put("hoursPlayed","51");
        given()
                .contentType("application/json")
                .body(user)
                .when()
                .put("users")
                .then()
                .statusCode(202);

    }

    @Test
    public void zdeleteUserTest() {
        given()
                .pathParam("id", 1)
                .when()
                .delete("users/{id}").
                then()
                .statusCode(204);
    }

}


