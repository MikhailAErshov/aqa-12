import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class MinPriceTest {

    @Test
    public void shouldGetMinPrices() {
        RestAssured.baseURI = "https://api.kazanexpress.ru/api/v2";
        RequestSpecification getRequest = RestAssured.given();
        getRequest.header("Authorization", "a2F6YW5leHByZXNzLWN1c3RvbWVyOmN1c3RvbWVyU2VjcmV0S2V5");

        getRequest.get("delivery/min-free-price?cityId=1")
                .then()
                .log().body()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("minPriceRequest.json"));
    }
}
