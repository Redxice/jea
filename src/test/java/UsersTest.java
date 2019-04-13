import com.jayway.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class UsersTest {
        //TO:DO find a way to run methods in certain order.
        @BeforeClass
        public static void setup(){
            RestAssured.port = 8080;
            RestAssured.basePath = "/jea_war_exploded/api/";
            RestAssured.baseURI = "http://localhost";
        }
        @Test
        public void getAllUsersTest(){
            given().when().get("users/all").then().statusCode(200);
        }
        @Test
        public void postUserTest(){
            Map<String,String> user = new HashMap<>();
            user.put("name","Henk");
            user.put("password","HenkPassword");
            given()
                    .contentType("application/json")
                    .body(user)
                    .when()
                    .post("users")
                    .then()
                    .statusCode(204);
        }
        @Test
        public void getUserInvalidTest(){
            given()
                    .pathParam("id",1)
                    .when()
                    .get("users/{id}").
                    then()
                    .statusCode(500);
        }
        @Test
        public void getUserValidTest(){
            given()
                    .pathParam("id",2)
                    .when()
                    .get("users/{id}").
                    then()
                    .statusCode(200);
        }
        @Test
        public void updateUserTest(){
            Map<String,String> user = new HashMap<>();
            user.put("id","2");
            user.put("name","Henk");
            user.put("password","HenkPassword2");
            given()
                    .contentType("application/json")
                    .body(user)
                    .when()
                    .put("users")
                    .then()
                    .statusCode(204);

        }

        @Test
        public void deleteUserTest(){
            given()
                    .pathParam("id",1)
                    .when()
                    .delete("users/{id}").
                    then()
                    .statusCode(204);
        }

    }


