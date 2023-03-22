import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.hamcrest.Matchers.equalTo;

public class PetTest extends BaseTest {
    //new TypeRef<> объекты и коллекции
    //extracting объекты + вложенный path и коллекции
    //ResponseSpecification

    RequestSpecification requestSpec;
    ResponseSpecification responsePetDto;

    @BeforeEach
    public void setUp() {
        requestSpec = RestAssured.given();
//        responsePetDto = RestAssured.expect()
//                .statusCode(200);
        ResponseSpecBuilder specBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200);
        responsePetDto = specBuilder.build();
    }

    @Test
    public void shouldGetPetById() {
        requestSpec
                .get("/pet/1")
                .then()
                .spec(responsePetDto)
                .body("name", equalTo("doggie"));
    }

    @Test
    public void shouldGetPetByIdWithExtracting() {
        String categoryName = requestSpec
                .get("/pet/1")
                .then()
                .spec(responsePetDto)
                .body("name", equalTo("doggie"))
                .extract()
                .path("name");

        System.out.println(categoryName);
    }

    @Test
    public void shouldGetPetByIdWithTypeRef() {
        PetDto response = requestSpec
                .get("/pet/1")
                .as(new TypeRef<>() {});

        assertThat(response.getName()).isEqualTo("doggie");

        assertThat(response)
                .extracting(
                        PetDto::getId,
                        PetDto::getStatus
                ).containsExactly(
                        1,
                        "available"
                );
    }

    @Test
    public void shouldFindPetByStatus() {
        List<PetDto> response = requestSpec
                .param("status", "pending")
                .get("/pet/findByStatus")
                .as(new TypeRef<>() {});

        assertThat(response)
                .extracting(
                        PetDto::getId,
                        PetDto::getName,
                        PetDto::getStatus
                )
                .containsExactlyInAnyOrder(
                        tuple(
                                123456789,
                                "Buddy",
                                "pending"
                        ),
                        tuple(
                                109,
                                "John",
                                "pending"
                        )
                );
    }

    @Test
    public void shouldFindPetByStatusWithExtracting() {
        List<Map<String, String>> tags = requestSpec
                .param("status", "pending")
                .get("/pet/findByStatus")
                .then()
                .extract()
                .path("[0].tags");

        System.out.println(tags);
    }

    @Test
    public void shouldAddPet() {
        TagDto tag1 = new TagDto();
        tag1.setId(1L);
        tag1.setName("Tag Name");

        JSONObject tag = new JSONObject()
                .put("id", tag1.getId())
                .put("name", tag1.getName());

        CategoryDto category1 = new CategoryDto();
        category1.setId(2L);
        category1.setName("Category Name");

        JSONObject category = new JSONObject()
                .put("id", category1.getId())
                .put("name", category1.getName());

        JSONObject pet = new JSONObject()
                .put("id", 3L)
                .put("name", "Pet Name")
                .put("tags", new JSONArray(List.of(tag)))
                .put("photoUrls", List.of("1", "2"))
                .put("status", "sold")
                .put("category", category);

        PetDto petRes = requestSpec
                .body(pet.toString())
                .contentType("application/json")
                .post("/pet")
                .as(new TypeRef<>() {});

        assertThat(petRes.getId()).isNotNull();
        assertThat(petRes)
                .extracting(
                        PetDto::getCategory,
                        PetDto::getPhotoUrls
                ).containsExactly(
                        category1,
                        List.of("1", "2")
                );

//        assertThat(petRes)
//                .extracting(PetDto::getCategory)
//                .extracting(CategoryDto::getId)
    }

    @Test
    public void shouldDeletePet() {
        TagDto tag1 = new TagDto();
        tag1.setId(1L);
        tag1.setName("Tag Name");

        JSONObject tag = new JSONObject()
                .put("id", tag1.getId())
                .put("name", tag1.getName());

        CategoryDto category1 = new CategoryDto();
        category1.setId(2L);
        category1.setName("Category Name");

        JSONObject category = new JSONObject()
                .put("id", category1.getId())
                .put("name", category1.getName());

        JSONObject pet = new JSONObject()
                .put("id", 3L)
                .put("name", "Pet Name")
                .put("tags", new JSONArray(List.of(tag)))
                .put("photoUrls", List.of("1", "2"))
                .put("status", "sold")
                .put("category", category);

        PetDto createdPet = generatePet(pet);

        requestSpec
                .delete("/pet/{id}", createdPet.getId())
                .then()
                .statusCode(200);
    }

    private PetDto generatePet(Object o) {
         return requestSpec
                .body(o.toString())
                .contentType("application/json")
                .post("/pet")
                .as(new TypeRef<>() {});
    }
}
