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
        //TO:DO find a way to run methods in certain order.
        @BeforeClass
        public static void setup(){
            RestAssured.port = 8080;
            RestAssured.basePath = "/jea_war_exploded/api/";
            RestAssured.baseURI = "http://localhost";
        }
        @Test
        public void agetAllUsersTest(){
            given().when().get("users").then().statusCode(200);
        }
        @Test
        public void apostUserTest(){
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
        public void agetUserInvalidTest(){
            given()
                    .pathParam("id",82521)
                    .when()
                    .get("users/{id}").
                    then()
                    .statusCode(500);
        }
        @Test
        public void getUserValidTest(){
            given()
                    .pathParam("id",1)
                    .when()
                    .get("users/{id}").
                    then()
                    .statusCode(200);
        }
        @Test
        public void updateUserTest(){
            Map<String,String> user = new HashMap<>();
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
        public void zdeleteUserTest(){
            given()
                    .pathParam("id",1)
                    .when()
                    .delete("users/{id}").
                    then()
                    .statusCode(204);
        }

    }


