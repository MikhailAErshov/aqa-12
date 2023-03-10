import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;

import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasLength;

public class UserTest extends BaseTest {

    @Test
    public void shouldCreateUser() {
        RequestSpecification request = RestAssured.given();

        JSONObject object = new JSONObject();
        object.put("id", faker.random().nextLong());
        object.put("name", faker.name().name());
        object.put("firstName", faker.name().firstName());
        object.put("lastName", faker.name().lastName());
        object.put("email", faker.internet().emailAddress());
        object.put("password", faker.internet().password());
        object.put("phone", faker.phoneNumber().phoneNumber());
        object.put("userStatus", faker.random().nextInt(0, 2));

        request.body(object.toString());
        request.header("content-type", "application/json");

        System.out.println(object);

        Response response = request.post("/user");
        response.then()
                .statusCode(200)
                .assertThat()
                .body(
                        //matchesJsonSchemaInClasspath("userCreateResponse.json"),
                        "code", equalTo(200),
                        "type", equalTo("unknown"),
                        "message", hasLength(19)
                );
    }

    @Test
    public void shouldNotCreateUser() {
        RequestSpecification request = RestAssured.given();

        Response response = request.post("/user");
        response.then()
                .statusCode(415)
                .assertThat()
                .body(
                        "code", equalTo(415),
                        "type", equalTo("unknown"),
                        "message", containsString("A message body reader for Java class " +
                                "io.swagger.sample.model.User")
                );
    }

    @Test
    public void shouldGetUser() {
        RequestSpecification postRequest = RestAssured.given();

        JSONObject object = new JSONObject();
        object.put("id", faker.random().nextLong());
        object.put("name", faker.animal().name());
        object.put("firstName", faker.name().firstName());
        object.put("lastName", faker.name().lastName());
        object.put("email", faker.internet().emailAddress());
        object.put("password", faker.internet().password());
        object.put("phone", faker.phoneNumber().phoneNumber());
        object.put("userStatus", faker.random().nextInt(0, 2));

        System.out.println(object);

        postRequest.body(object.toString());
        postRequest.header("content-type", "application/json");

        Response postResponse = postRequest.post("/user");

        postResponse.then().log().body().statusCode(200);

        RequestSpecification getRequest = RestAssured.given();
        Response getResponse = getRequest.get("/user/" + object.get("name"));

        getResponse.then()
                .statusCode(200)
                .body(
                    "id", equalTo(object.get("id")),
                        "name", equalTo(object.get("name")),
                        "firstName", equalTo(object.get("firstName"))
                );
    }

    @Test
    public void shouldNotGetUser() throws FileNotFoundException {
        File file = new File("src/test/resources/userCreateResponse.json");
        FileReader reader = new FileReader(file);
        Scanner sc = new Scanner(reader);
        StringBuilder json = new StringBuilder();
        while (sc.hasNextLine()) {
            json.append(sc.nextLine());
        }

        JSONObject jsonObj = new JSONObject(json.toString());

        when()
                .get("https://petstore.swagger.io/v2/user/{username}", 123)
                .then()
                .log().body()
                .assertThat()
                .body(equalTo(jsonObj.toString()));
    }

    @Test
    public void shouldNotGetUser2() {
        when()
                .get("https://petstore.swagger.io/v2/user/{username}", 123)
                .then()
                .assertThat()
                .log().body()
                .body(matchesJsonSchemaInClasspath("userCreateResponseSchema.json"));
    }
}
