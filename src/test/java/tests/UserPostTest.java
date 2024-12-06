
package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

public class UserPostTest extends base.BaseTest {

    @Test
    public void testGetAllPosts() {
        Response response = given()
                .when()
                .get("/posts")
                .then()
                .log().all()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200");
    }

    @Test
    public void testGetPostById() {
        int postId = 1;
        Response response = given()
                .when()
                .get("/posts/" + postId)
                .then()
                .log().all()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200");
        Assert.assertEquals(response.jsonPath().getInt("id"), postId, "Post ID does not match");
    }

    @Test
    public void testCreatePost() {
        String newPost = """
                {
                    "userId": 1,
                    "title": "New Post Title",
                    "body": "This is a new post."
                }
                """;

        Response response = given()
                .header("Content-Type", "application/json")
                .body(newPost)
                .when()
                .post("/posts")
                .then()
                .log().all()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 201, "Expected status code 201");
    }

    @Test
    public void testUpdatePost() {
        int postId = 1;
        String updatedPost = """
                {
                    "userId": 1,
                    "id": 1,
                    "title": "Updated Post Title",
                    "body": "This post has been updated."
                }
                """;

        Response response = given()
                .header("Content-Type", "application/json")
                .body(updatedPost)
                .when()
                .put("/posts/" + postId)
                .then()
                .log().all()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200");
    }

    @Test
    public void testDeletePost() {
        int postId = 1;
        Response response = given()
                .when()
                .delete("/posts/" + postId)
                .then()
                .log().all()
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200");
    }
}

