package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiSteps {

    Response response;

    @Given("User sends api call with {int} users per page")
    public void user_sends_api_call_with_users_per_page(int perPage) {

        Map<String, Object> queryParameters =new HashMap<>();
        queryParameters.put("q","tom");
        queryParameters.put("per_page",perPage);
        queryParameters.put("page",1);

        response=given().baseUri("https://api.github.com/search")
                .and().queryParams(queryParameters)
                .when().get("/users");
        response.then().log().all();
    }

    @Then("User validates status code {int}")
    public void user_validates_status_code(Integer statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("User validates result contains {int} users only")
    public void user_validates_result_contains_users_only(int perPage) {
        Assert.assertEquals(perPage, response.jsonPath().getList("items").size());
    }

    @Given("User sends api call for users with sorting {string} and order {string}")
    public void user_sends_api_call_for_users_with_sorting_and_order(String sort, String order) {
        Map<String, Object> queryParameters =new HashMap<>();
        queryParameters.put("q","tom");
        queryParameters.put("sort",sort);
        queryParameters.put("order",order);

        response=given().baseUri("https://api.github.com/search")
                .and().queryParams(queryParameters)
                .when().get("/users");
        response.then().log().all();
    }

    @Then("User validates result response body contains {string} in {string} order")
    public void user_validates_result_response_body_contains_in_order(String sort, String order) {

    }

}
