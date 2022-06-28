package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
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
        queryParameters.put("per_page",5);
        queryParameters.put("page",1);
        queryParameters.put("sort",sort); // followers
        queryParameters.put("order",order); // ascending

        response=given().baseUri("https://api.github.com/search")
                .and().queryParams(queryParameters)
                .when().get("/users");
        response.then().log().all();
    }

    @Then("User validates result response body contains {string} in {string} order")
    public void user_validates_result_response_body_contains_in_order(String sort, String order) {
        List<String> urls=response.body().jsonPath().getList("items.followers_url");

        for(int i=1;i<urls.size(); i++){
            ResponseBody responseBody1=given().baseUri(urls.get(i)).when().get().body();
            ResponseBody responseBody2=given().baseUri(urls.get(i-1)).when().get().body();
            if(order.equalsIgnoreCase("asc")&&!responseBody2.asString().equalsIgnoreCase("[]")&&!responseBody1.asString().equalsIgnoreCase("[]")) {
                int size1 = responseBody1.jsonPath().getList("id").size();
                int size2 = responseBody2.jsonPath().getList("id").size();
                Assert.assertTrue("Expected: "+size1+" Actual:"+size2,size1 <=size2);
            }else if (order.equalsIgnoreCase("desc")&&responseBody2.asString()!="[]"&&responseBody1.asString()!="[]") {
                int size1 = responseBody1.jsonPath().getList("id").size();
                int size2 = responseBody2.jsonPath().getList("id").size();
                Assert.assertTrue("Expected: "+size1+" Actual:"+size2,size1>=size2);
            }else{Assert.fail();}
        }


        System.out.println(urls);

    }

}
