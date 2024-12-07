package tests;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class CommentsAPITest {
    private static ExtentReports extent;
    private static ExtentTest test;

    @BeforeSuite
    public void setupExtentReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("TestReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeClass
    public void setupBaseURI() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void testGetCommentsByPostId() {
        test = extent.createTest("Retrieve Comments for Post ID 2");
        Response response = RestAssured.get("/comments?postId=2");
        test.info("GET /comments?postId=2");

        Assert.assertEquals(response.getStatusCode(), 200);
        test.pass("Verified status code is 200");

        Assert.assertTrue(response.jsonPath().getList("$").size() > 0);
        test.pass("Verified response contains comments");
    }

    @Test
    public void testAddCommentToPost() {
        test = extent.createTest("Add a Comment to Post ID 2");
        String requestBody = "{\n" +
                "  \"postId\": 2,\n" +
                "  \"name\": \"New Comment\",\n" +
                "  \"email\": \"test@example.com\",\n" +
                "  \"body\": \"This is a test comment.\"\n" +
                "}";

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/comments");
        test.info("POST /comments with request body: " + requestBody);

        Assert.assertEquals(response.getStatusCode(), 201);
        test.pass("Verified status code is 201");

        Assert.assertEquals(response.jsonPath().getString("postId"), "2");
        test.pass("Verified postId in response is 2");
    }

    @Test
    public void testUpdateComment() {
        test = extent.createTest("Update a Comment");
        String requestBody = "{\n" +
                "  \"name\": \"Updated Comment\",\n" +
                "  \"email\": \"updated@example.com\",\n" +
                "  \"body\": \"This is an updated comment.\"\n" +
                "}";

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .put("/comments/1");
        test.info("PUT /comments/1 with request body: " + requestBody);

        Assert.assertEquals(response.getStatusCode(), 200);
        test.pass("Verified status code is 200");

        Assert.assertEquals(response.jsonPath().getString("name"), "Updated Comment");
        test.pass("Verified comment name is updated");
    }

    @Test
    public void testDeleteComment() {
        test = extent.createTest("Delete a Comment");
        Response response = RestAssured.delete("/comments/1");
        test.info("DELETE /comments/1");

        Assert.assertEquals(response.getStatusCode(), 200);
        test.pass("Verified status code is 200");
    }

    @AfterSuite
    public void generateReport() {
        extent.flush();
    }
}