import com.jayway.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersTest {

    @BeforeClass
    public static void setup() {
        RestAssured.port = basicServerInfo.port;
        RestAssured.basePath = basicServerInfo.basePath;
        RestAssured.baseURI = basicServerInfo.baseURI;
    }

    @Test
    public void cgetAllUsersTest() {
        given().when().get("users").then().statusCode(200);
    }

    @Test
    public void apostUserTest() {
        given()
                .header("Authorization","Bearer Henk:HenkPassword")
                .when()
                .post("users")
                .then()
                .statusCode(204);
    }

    @Test
    public void agetUserInvalidTest() {
        given()
                .pathParam("id", 82521)
                .when()
                .get("users/{id}").
                then()
                .statusCode(500);
    }

    @Test
    public void getUserValidTest() {
        given()
                .pathParam("id", 1)
                .when()
                .get("users/{id}").
                then()
                .statusCode(200);
    }

    @Test
    public void getInvalidUser() {
        given()
                .pathParam("id", 99999)
                .when()
                .get("users/{id}").
                then()
                .statusCode(200);
    }

    @Test
    public void updateUserTest() {
        Map<String, String> user = new HashMap<>();
        user.put("name", "Henk");
        user.put("password", "HenkPassword2");
        given()
                .contentType("application/json")
                .body(user)
                .when()
                .put("users")
                .then()
                .statusCode(204);

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


