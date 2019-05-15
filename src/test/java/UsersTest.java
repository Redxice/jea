import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import dto.UserDto;
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

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.RestAssured.basePath;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersTest {
    private static String token;
    @BeforeClass
    public static void setup() throws IOException {
        RestAssured.port = basicServerInfo.port;
        RestAssured.basePath = basicServerInfo.basePath;
        RestAssured.baseURI = basicServerInfo.baseURI;
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(baseURI+":"+port+basePath+"users");
        post.setHeader("Authorization","Bearer Henk:HenkPassword");
        client.execute(post);
        HttpPost postAuthorize = new HttpPost(baseURI+":"+port+basePath+"authentication");
        postAuthorize.setHeader("Authorization","Bearer Henk:HenkPassword");
        HttpResponse httpResponse = client.execute(postAuthorize);
        token = httpResponse.getFirstHeader("Authorization").getValue();
    }


    @Test
    public void aPostUserTest() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "cool@email.com");
        params.put("twoFactorEnabled", "true");
        given()
                .header("Authorization","Bearer test:password")
                .contentType("application/json")
                .content(params)
                .when()
                .post("users")
                .then()
                .statusCode(200);

    }

    @Test
    public void bGetUserInvalidTest() {
        given()
                .pathParam("id", 82521)
                .contentType("application/json")
                .when()
                .get("users/{id}").
                then()
                .statusCode(404);
    }

    @Test
    public void bPostUserExistingUserTest(){
        given()
                .header("Authorization","Bearer Henk:HenkPassword")
                .contentType("application/json")
                .when()
                .post("users")
                .then()
                .statusCode(409);
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
        user.put("name", "Henk");
        user.put("lvl", "30");
        user.put("hoursPlayed","51");
        given()
                .contentType("application/json")
                .header("Authorization",token)
                .body(user)
                .when()
                .put("users")
                .then()
                .statusCode(200);
    }

    @Test
    public void zdeleteUserTest() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost postAuthorize = new HttpPost(baseURI+":"+port+basePath+"authentication");
        postAuthorize.setHeader("Authorization","Bearer test:password");
        String testToken = "";
        UserDto userDto = null;
        try {
            HttpResponse httpResponse = client.execute(postAuthorize);
            testToken = httpResponse.getFirstHeader("Authorization").getValue();
            ObjectMapper mapper = new ObjectMapper();
            userDto = mapper.readValue(httpResponse.getEntity().getContent(), UserDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        given()
                .pathParam("id", userDto.getId())
                .header("Authorization",testToken)
                .when()
                .delete("users/{id}").
                then()
                .statusCode(204);
    }

}


