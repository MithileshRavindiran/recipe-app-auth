package nl.abn.assignment.it;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

import nl.abn.assignment.RecipeAppAuthApplication;
import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.domain.RecipeDetails;
import nl.abn.assignment.domain.RecipeType;
import nl.abn.assignment.repository.RecipeRepository;
import nl.abn.assignment.util.TestHelperUtil;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        "spring.profiles.active=it" }, classes = RecipeAppAuthApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
@ActiveProfiles("it")
public class RecipeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private RecipeRepository recipeRepository;

    private String token;

    @BeforeEach
    public void setup() throws JsonProcessingException {

//        IntegrationTestUtil.initTable(recipeRepository);
//            //Generate JWT Token
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("username", "mithilesh");
//            headers.set("password", "admin");
//
//            ResponseEntity<String> responseEntity = restTemplate.postForEntity(createURLWithPort("/login"),
//                headers,
//                String.class);
//            assertEquals(200, responseEntity.getStatusCodeValue());
//            Map map = new ObjectMapper().readValue(responseEntity.getBody(), Map.class);
//            token = map.get("access_token").toString();
        }

    @Test
    void listRecipeDetailsWithFilter() throws Exception {
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(createURLWithPort("/recipes/filter"),
                QueryRequest.builder()
                        .noOfServings(4)
                        .excludeIngredients(false)
                        .ingredients(List.of("salt", "sugar"))
                        .build(),
                Object.class);
        assertAll("Validating response",
                () -> assertEquals(200, responseEntity.getStatusCodeValue()));
    }

    @Test
    void listRecipeDetailsWithOutFilter() throws Exception {
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(createURLWithPort("/recipes/filter"),
                QueryRequest.builder()
                        .build(),
                Object.class);
        assertAll("Validating response",
                () -> assertEquals(200, responseEntity.getStatusCodeValue()));
    }

    @Test
    void getRecipeDetailsById() throws Exception {
        ResponseEntity<RecipeDetails> responseEntity = restTemplate.getForEntity(createURLWithPort("/recipes/62d2198c85ca871f9fc45c35"),
                RecipeDetails.class);
        assertAll("Validating response",
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertEquals("62d2198c85ca871f9fc45c35", responseEntity.getBody().getId()),
                () -> assertEquals(RecipeType.VEGAN, responseEntity.getBody().getRecipeType()),
                () -> assertEquals(2, responseEntity.getBody().getInstructions().size()),
                () -> assertEquals(2, responseEntity.getBody().getIngredients().size()));
    }

    @Test
    void getNonExistingRecipeDetails() throws Exception {
        ResponseEntity<RecipeDetails> responseEntity = restTemplate.getForEntity(createURLWithPort("/recipes/62d2198c85ca871f9fc45c99"),
                RecipeDetails.class);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    void creatRecipe() throws Exception {
        var recipe = TestHelperUtil.createRecipeRequest("My Favourite Recipe ", RecipeType.VEGAN);
        HttpHeaders headerToEndpoint = new HttpHeaders();
        headerToEndpoint.set("Authorization", "Bearer "+token);
        HttpEntity request = new HttpEntity(recipe, headerToEndpoint);

        ResponseEntity<RecipeDetails> responseEntity = restTemplate.exchange(createURLWithPort("/recipes"),
            HttpMethod.POST,
            request,
            RecipeDetails.class);
        assertAll("Validating response",
                () -> assertEquals(201, responseEntity.getStatusCodeValue()),
                () -> assertNotNull(responseEntity.getBody().getId()),
                () -> assertEquals(RecipeType.VEGAN, responseEntity.getBody().getRecipeType()),
                () -> assertEquals(2, responseEntity.getBody().getInstructions().size()),
                () -> assertEquals(2, responseEntity.getBody().getIngredients().size()));
    }

    @Test
    void recipeNameTooLong() throws Exception {

        var recipe = TestHelperUtil.createRecipeRequest("My Faaaqaaaaaaaaaaaaaaaaaaaaaahshashahahakjdhkhdkhakdhkjadhvourite Recipe ", RecipeType.VEGAN);
        HttpHeaders headerToEndpoint = new HttpHeaders();
        headerToEndpoint.set("Authorization", "Bearer "+token);
        HttpEntity request = new HttpEntity(recipe, headerToEndpoint);


        ResponseEntity<RecipeDetails> responseEntity = restTemplate.exchange(createURLWithPort("/recipes"),
            HttpMethod.POST,
            request,
            RecipeDetails.class);
//        ResponseEntity<RecipeDetails> responseEntity = restTemplate.postForEntity(createURLWithPort("/recipes"),
//                TestHelperUtil.createRecipeRequest("My Faaaqaaaaaaaaaaaaaaaaaaaaaahshashahahakjdhkhdkhakdhkjadhvourite Recipe ", RecipeType.VEGAN),
//                RecipeDetails.class);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    void recipeNameMissing() throws Exception {
        var recipe = TestHelperUtil.createRecipeRequest(null, RecipeType.VEGAN);
        HttpHeaders headerToEndpoint = new HttpHeaders();
        headerToEndpoint.set("Authorization", "Bearer "+token);
        HttpEntity request = new HttpEntity(recipe, headerToEndpoint);


        ResponseEntity<RecipeDetails> responseEntity = restTemplate.exchange(createURLWithPort("/recipes"),
            HttpMethod.POST,
            request,
            RecipeDetails.class);
//        ResponseEntity<RecipeDetails> responseEntity = restTemplate.postForEntity(createURLWithPort("/recipes"),
//                TestHelperUtil.createRecipeRequest(null, RecipeType.VEGAN),
//                RecipeDetails.class);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    void recipeTypeMissing() throws Exception {
        var recipe = TestHelperUtil.createRecipeRequest(null, null);
        HttpHeaders headerToEndpoint = new HttpHeaders();
        headerToEndpoint.set("Authorization", "Bearer "+token);
        HttpEntity request = new HttpEntity(recipe, headerToEndpoint);

        ResponseEntity<RecipeDetails> responseEntity = restTemplate.exchange(createURLWithPort("/recipes"),
            HttpMethod.POST,
            request,
            RecipeDetails.class);
//        ResponseEntity<RecipeDetails> responseEntity = restTemplate.postForEntity(createURLWithPort("/recipes"),
//                TestHelperUtil.createRecipeRequest(null, null),
//                RecipeDetails.class);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteRecipeType() throws Exception {
        HttpHeaders headerToEndpoint = new HttpHeaders();
        headerToEndpoint.set("Authorization", "Bearer "+token);
        HttpEntity request = new HttpEntity(null, headerToEndpoint);

        var recipe = TestHelperUtil.createRecipeRequest("My Favourite Recipe Dummy ", RecipeType.VEGAN);
        HttpEntity requestForCreate = new HttpEntity(recipe, headerToEndpoint);

        ResponseEntity<RecipeDetails> createResponseEntity = restTemplate.exchange(createURLWithPort("/recipes"),
            HttpMethod.POST,
            requestForCreate,
            RecipeDetails.class);

        var id = createResponseEntity.getBody().getId();
        ResponseEntity<Void> responseEntity = restTemplate.exchange(createURLWithPort("/recipes/"+id),
            HttpMethod.DELETE,
            request,
            Void.class);
        //ResponseEntity<Void> responseEntity = restTemplate.exchange(createURLWithPort("/recipes/62d2198c85ca871f9fc45c35"), HttpMethod.DELETE, null, Void.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteNonExistingRecipeType() throws Exception {
        HttpHeaders headerToEndpoint = new HttpHeaders();
        headerToEndpoint.set("Authorization", "Bearer "+token);
        HttpEntity request = new HttpEntity(null, headerToEndpoint);


        ResponseEntity<Void> responseEntity = restTemplate.exchange(createURLWithPort("/recipes/62d2198c85ca871f9fc45c99"),
            HttpMethod.DELETE,
            request,
            Void.class);
//        ResponseEntity<Void> responseEntity = restTemplate
//                .exchange(createURLWithPort("/recipes/62d2198c85ca871f9fc45c99"), HttpMethod.DELETE, null, Void.class);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    private String createURLWithPort(String uri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + uri);
        return builder.build().toUriString();
    }
}
