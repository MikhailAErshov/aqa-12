import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreTest extends BaseTest {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeEach
    public void setUp() {
        requestSpecification = RestAssured.given();

        ResponseSpecBuilder specBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200);
        responseSpecification = specBuilder.build();
    }

    @Test
    public void shouldPostStoreOrder() {
        JSONObject jsonObject = new JSONObject()
                .put("id", 292929)
                .put("idOrder", 85)
                .put("quantity", 3)
                .put("shipDate", Instant.now())
                .put("status", "placed")
                .put("complete", true);

        StoreDto storeDto = requestSpecification
                .body(jsonObject.toString())
                .contentType("application/json")
                .post("/store/order")
                .as(new TypeRef<>() {
                });

        assertThat(storeDto.getId()).isNotNull();
    }

    @Test
    public void shouldGetStoreOrderByOrderId() {
        StoreDto response = requestSpecification
                .get("/store/order/292929")
                .as(new TypeRef<>() {
                });

        assertThat(response)
                .extracting(
                        StoreDto::getQuantity,
                        StoreDto::getStatus
                ).containsExactly(
                        3,
                        "placed"
                );
    }

    @Test
    public void shouldDeleteStoreOrderByOrderId() {
        JSONObject jsonObjectStore = new JSONObject()
                .put("id", 565656)
                .put("idOrder", 90)
                .put("quantity", 5)
                .put("shipDate", Instant.now())
                .put("status", "placed")
                .put("complete", true);

        StoreDto createStoreDto = generateStoreOrder(jsonObjectStore);

        requestSpecification
                .delete("/store/order/{OrderId}", createStoreDto.getId())
                .then()
                .statusCode(200);
    }

    private StoreDto generateStoreOrder(Object o) {
        return requestSpecification
                .body(o.toString())
                .contentType("application/json")
                .post("/store/order")
                .as(new TypeRef<>() {
                });
    }

    @Test
    public void shouldGetStoreInventory() {
        InventoryDto response = requestSpecification
                .get("/store/inventory")
                .then()
                .extract()
                .as(new TypeRef<>() {
                });
        assertThat(response.getTotvs()).isGreaterThan(0);
        assertThat(response.getSold()).isGreaterThan(0);
        assertThat(response.getString()).isGreaterThan(0);
    }

}
