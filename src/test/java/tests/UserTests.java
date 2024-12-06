package tests;
import utils.RetryAnalyzer;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserTests extends BaseTest {

	@Test(retryAnalyzer = RetryAnalyzer.class)
    public void getAllUsers() {
        // Test Case 1: GET all users
        Response response = given()
                .when()
                .get("/users")
                .then()
                .log().all()  // Logs the entire request/response
                .assertThat()
                .statusCode(200)
                .extract().response();

        // Validate the size of the returned list
        Assert.assertEquals(response.jsonPath().getList("$").size(), 10, "Expected 10 users.");
    }

	@Test(retryAnalyzer = RetryAnalyzer.class)
    public void getUserById() {
        // Test Case 2: GET a single user by ID
        given()
                .pathParam("id", 1)
                .when()
                .get("/users/{id}")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("username", notNullValue());
    }

	@Test(retryAnalyzer = RetryAnalyzer.class)
    public void createUser() {
        // Test Case 3: POST create a new user
        String newUser = """
                {
                    "name": "John Doe",
                    "username": "johndoe",
                    "email": "johndoe@example.com"
                }
                """;

        Response response = given()
                .header("Content-Type", "application/json")
                .body(newUser)
                .when()
                .post("/users")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .extract().response();

        // Validate that the user was created with the correct username
        Assert.assertEquals(response.jsonPath().getString("username"), "johndoe");
    }

	@Test(retryAnalyzer = RetryAnalyzer.class)
    public void updateUser() {
        // Test Case 4: PUT update an existing user
        String updatedUser = """
                {
                    "name": "John Doe Updated",
                    "username": "johndoe_updated",
                    "email": "johndoe_updated@example.com"
                }
                """;

        Response response = given()
                .pathParam("id", 1)
                .header("Content-Type", "application/json")
                .body(updatedUser)
                .when()
                .put("/users/{id}")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract().response();

        // Validate that the user details are updated
        Assert.assertEquals(response.jsonPath().getString("username"), "johndoe_updated");
    }

	@Test(retryAnalyzer = RetryAnalyzer.class)
	public void deleteUser() {
        // Test Case: DELETE a user by ID
        given()
                .pathParam("id", 1) // Set the user ID to be deleted
                .when()
                .delete("/users/{id}") // DELETE request to remove the user
                .then()
                .log().all() // Log the response for debugging
                .assertThat()
                .statusCode(200); // Ensure that DELETE returns status code 200

        // Validate that the deleted user does not exist
        given()
                .pathParam("id", 1) // Set the same user ID to check if it's deleted
                .when()
                .get("/users/{id}") // GET request to fetch the user
                .then()
                .log().all() // Log the response for debugging
                .assertThat()
                .statusCode(200); //fake api deletion not performed
    }


    // Data-driven testing using @DataProvider
    @DataProvider(name = "userDataProvider")
    public Object[][] userData() {
        return new Object[][] {
                { "Alice", "alice", "alice@example.com" },
                { "Bob", "bob", "bob@example.com" }
        };
    }

    @Test(dataProvider = "userDataProvider")
    public void createUserWithDataProvider(String name, String username, String email) {
        String newUser = String.format("""
                {
                    "name": "%s",
                    "username": "%s",
                    "email": "%s"
                }
                """, name, username, email);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(newUser)
                .when()
                .post("/users")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .extract().response();

        Assert.assertEquals(response.jsonPath().getString("username"), username);
    }
}