import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import dao.ForumDao;
import dao.UserDao;
import dto.ForumDto;
import dto.UserDto;
import models.Forum;
import models.User;
import net.minidev.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.RestAssured.basePath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class ForumEndpointTest {
    /**
     * user needs a token to use the forum endpoint except for get forum and get messages
     */
    private static String token;
    private static Long userID;
    private static Long forumID;
    private static ForumDto forumDto;

    @BeforeClass
    public static void setup() throws IOException {
        RestAssured.port = basicServerInfo.port;
        RestAssured.basePath = basicServerInfo.basePath;
        RestAssured.baseURI = basicServerInfo.baseURI;
        /**
         * create user if he doesn't already exist
         */
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(baseURI + ":" + port + basePath + "users");
        post.setHeader("Authorization", "Bearer Henk:HenkPassword");
        client.execute(post);
        /**
         * get token and userID for later use in tests
         */
        HttpPost postAuthorize = new HttpPost(baseURI + ":" + port + basePath + "authentication");
        postAuthorize.setHeader("Authorization", "Bearer Henk:HenkPassword");
        HttpResponse httpResponse = client.execute(postAuthorize);
        token = httpResponse.getFirstHeader("Authorization").getValue();
        ObjectMapper mapper = new ObjectMapper();
        userID = mapper.readValue(httpResponse.getEntity().getContent(), UserDto.class).getId();
        /**
         *
         */

        HttpPost postForum = new HttpPost(baseURI + ":" + port + basePath + "forums");
        postForum.setHeader("Authorization", token);
        forumDto = new ForumDto();
        forumDto.setOwner_id(userID);
        forumDto.setTag("#Hunting");
        forumDto.setTitle("testHuntingForum");
        Gson gson = new Gson();
        StringEntity requestEntity = new StringEntity(
                gson.toJson(forumDto),
                ContentType.APPLICATION_JSON);
        postForum.setEntity(requestEntity);
        HttpResponse response = client.execute(postForum);
        mapper = new ObjectMapper();
        forumDto = mapper.readValue(response.getEntity().getContent(), ForumDto.class);
        forumID = forumDto.getId();
    }

    @Test
    public void postForumTest() {
        Map<String, String> forum = createForumMap(String.valueOf(userID),"#Hunting","testHuntingForum",null);
        given()
                .header("Authorization", token)
                .contentType("application/json")
                .content(forum)
                .when()
                .post("forums")
                .then()
                .assertThat()
                .body("id", notNullValue())
                .body("ownerID", equalTo(String.valueOf(userID)))
                .body("tag", equalTo("#Hunting"))
                .body("title", equalTo("testHuntingForum"))
                .statusCode(200);
    }

    @Test
    public void postForumWithoutToken() {
        Map<String, String> forum = createForumMap(String.valueOf(userID),"#Hunting","testHuntingForum",null);

        given()
                .contentType("application/json")
                .content(forum)
                .when()
                .post("forums")
                .then()
                .statusCode(401);
    }

    /**
     * forum will be added to the user's subscription list
     */
    @Test
    public void subscribeOnForum() {
        given()
                .header("Authorization", token)
                .contentType("application/json")
                .pathParam("id", forumID)
                .when()
                .post("forums/subscribe")
                .then()
                .statusCode(200);
    }

    @Test
    public void subscribeOnNonExistingForum() {
        given()
                .header("Authorization", token)
                .contentType("application/json")
                .pathParam("id", 99999)
                .when()
                .post("forums/subscribe")
                .then()
                .statusCode(404);
    }

    @Test
    public void subscribeOnForumAlreadySubscribed() {
        given()
                .header("Authorization", token)
                .contentType("application/json")
                .pathParam("id", forumID)
                .when()
                .post("forums/subscribe")
                .then()
                .statusCode(200);
    }

    @Test
    public void getMessagesFromForum() {
        given()
                .header("Authorization", token)
                .contentType("application/json")
                .pathParam("id", forumID)
                .when()
                .get("forums/messages")
                .then()
                .body("messages", notNullValue())
                .statusCode(200);
    }

    /**
     * only the owner can update the forum
     */
    @Test
    public void updateForum() {
        Map<String, String> forum = createForumMap(String.valueOf(userID),"#newTag","testHuntingForum",forumID);
        given()
                .header("Authorization", token)
                .contentType("application/json")
                .pathParam("id", forumID)
                .content(forum)
                .when()
                .put("forums")
                .then()
                .assertThat()
                .body("tag", equalTo("#newTag"))
                .statusCode(200);
    }

    @Test
    public void updateForumWithoutToken() {
        Map<String, String> forum = createForumMap(String.valueOf(userID),"#Hunting","testHuntingForum",forumID);
        given()
                .contentType("application/json")
                .content(forum)
                .when()
                .put("/"+forumID)
                .then()
                .statusCode(401);
    }

    @Test
    public void updateNonExistingForum() {
        Map<String, String> forum = createForumMap(String.valueOf(userID),"#Hunting","testHuntingForum",51231253513L);
        given()
                .contentType("application/json")
                .header("Authorization",token)
                .content(forum)
                .when()
                .put("forums")
                .then()
                .statusCode(401);
    }

    /**
     * to do split code in methods
     * @throws IOException
     */
    @Test
    public void updateForumNotOwner() throws IOException {
        String token2 = createUserAndGetToken("Piet");
        given()
                .contentType("application/json")
                .header("Authorization",token2)
                .content(forumDto)
                .when()
                .put("forums")
                .then()
                .statusCode(401);
    }

    @Test
    public void deleteForum() throws IOException {
        Long id = createForum();
        given()
                .contentType("application/json")
                .header("Authorization", token)
                .when()
                .delete("/"+id)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    public void deleteForumWithoutToken() {
        given()
                .contentType("application/json")
                .when()
                .delete("/"+forumID)
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    public void deleteNonExistingForum() {
        given()
                .header("Authorization", token)
                .contentType("application/json")
                .when()
                .delete("/"+999999)
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void deleteForumNotOwner() throws IOException {
        String token_new = createUserAndGetToken("Piet");
        given()
                .header("Authorization", token_new)
                .contentType("application/json")
                .pathParam("id", forumID)
                .when()
                .delete("forums")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    public void getAllSubscribers() {
        given()
                .header("Authorization", token)
                .contentType("application/json")
                .pathParam("id", forumID)
                .when()
                .get("forums/subscribers")
                .then()
                .assertThat()
                .statusCode(200);
    }

    private static Map<String, String> createForumMap(String ownerID, String tag, String title,Long id) {
        Map<String, String> forum = new HashMap<>();
        forum.put("ownerID", ownerID);
        forum.put("tag", tag);
        forum.put("title", title);
        forum.put("id",String.valueOf(id));
        return forum;
    }
    private Long createForum() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost postForum = new HttpPost(baseURI + ":" + port + basePath + "forums");
        postForum.setHeader("Authorization", token);
        ForumDto forumDto = new ForumDto();
        forumDto.setOwner_id(userID);
        forumDto.setTag("#Hunting");
        forumDto.setTitle("testHuntingForum");
        Gson gson = new Gson();
        StringEntity requestEntity = new StringEntity(
                gson.toJson(forumDto),
                ContentType.APPLICATION_JSON);
        postForum.setEntity(requestEntity);
        HttpResponse response = client.execute(postForum);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getEntity().getContent(), ForumDto.class).getId();
    }
    private String createUserAndGetToken(String name) throws IOException {
        /**
         * create user if he doesn't already exist
         */
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(baseURI + ":" + port + basePath + "users");
        post.setHeader("Authorization", "Bearer "+name+":HenkPassword");
        client.execute(post);
        /**
         * get token and userID for later use in tests
         */
        HttpPost postAuthorize = new HttpPost(baseURI + ":" + port + basePath + "authentication");
        postAuthorize.setHeader("Authorization", "Bearer "+name+":HenkPassword");
        HttpResponse httpResponse = client.execute(postAuthorize);
        return httpResponse.getFirstHeader("Authorization").getValue();

    }
}
