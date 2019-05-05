import com.jayway.restassured.RestAssured;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class MessageTest {

    @BeforeClass
    public static void setup() {
        RestAssured.port = basicServerInfo.port;
        RestAssured.basePath = basicServerInfo.basePath;
        RestAssured.baseURI = basicServerInfo.baseURI;
    }

    @Test
    public void getAllMessagesTest() {
        given().when().get("messages/all").then().statusCode(200);
    }

    @Test
    public void postMessageTest() {
        Map<String, String> message = new HashMap<>();
        message.put("userID", "1");
        message.put("Content", "Cool content");
        given()
                .contentType("application/json")
                .body(message)
                .when()
                .post("messages")
                .then()
                .statusCode(204);
    }

    @Test
    public void getMessageTest() {
        given()
                .pathParam("id", 1)
                .when()
                .get("messages/{id}").
                then()
                .statusCode(200);
    }

    @Test
    public void updateMessageTest() {
        Map<String, String> message = new HashMap<>();
        message.put("id", "1");
        message.put("userID", "1");
        message.put("Content", "Cool content");
        given()
                .contentType("application/json")
                .body(message)
                .when()
                .put("messages")
                .then()
                .statusCode(204);

    }

    @Test
    public void deleteMessageTest() {
        given()
                .pathParam("id", 1)
                .when()
                .delete("messages/{id}").
                then()
                .statusCode(204);
    }
}
