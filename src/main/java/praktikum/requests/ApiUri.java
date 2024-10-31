package praktikum.requests;

import io.restassured.RestAssured;

import static praktikum.constants.ApiRequestsConstants.API_REQUEST_URI;


public class ApiUri {
    public static void setUpUri() {
        RestAssured.baseURI = API_REQUEST_URI;
    }
}
