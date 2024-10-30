package practicum.httprequest;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public abstract class BaseHTTPRequests {

    public Response postRequestWithoutAuth(String url, Object requestBody, String contentType) {
        return given(this.buildRequest(contentType))
                .body(requestBody)
                .post(url);
    }

    public Response postRequestWithAuth(String url, Object requestBody, String contentType, String token) {
        return given(this.buildRequest(contentType))
                .auth().oauth2(token)
                .body(requestBody)
                .post(url);
    }

    public Response getRequestWithoutAuth(String url) {
        return given(this.buildRequest())
                .get(url);
    }

    public Response getRequestWithAuth(String url, String token) {
        return given(this.buildRequest())
                .auth().oauth2(token)
                .get(url);
    }

    public Response deleteRequestWithAuth(String url, String token) {
        return given(this.buildRequest())
                .auth().oauth2(token)
                .delete(url);
    }

    public Response patchRequestWithAuth(String url, Object requestBody, String contentType, String token) {
        return given(this.buildRequest(contentType))
                .auth().oauth2(token)
                .body(requestBody)
                .patch(url);
    }

    private RequestSpecification buildRequest() {
        return new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setRelaxedHTTPSValidation()
                .build();
    }

    private RequestSpecification buildRequest(String contentType) {
        return new RequestSpecBuilder()
                .addHeader("Content-type", contentType)
                .addFilter(new AllureRestAssured())
                .setRelaxedHTTPSValidation()
                .build();
    }


}
