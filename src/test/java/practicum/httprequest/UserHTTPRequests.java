package practicum.httprequest;

import io.restassured.response.Response;
import practicum.dto.UserDto;
import practicum.util.UrlPath;


public class UserHTTPRequests extends BaseHTTPRequests {
    public Response createUser(UserDto user) {
        return postRequestWithoutAuth(
                UrlPath.BASE_URL + UrlPath.CREATE_USER,
                user,
                "application/json"
        );
    }

    public Response deleteUser(String token) {
        return deleteRequestWithAuth(
                UrlPath.BASE_URL + UrlPath.USER,
                token
        );
    }

    public Response loginUser(UserDto user) {
        return postRequestWithoutAuth(
                UrlPath.BASE_URL + UrlPath.LOGIN_USER,
                user,
                "application/json"
        );
    }

    public Response updateUser(UserDto user, String token) {
        return patchRequestWithAuth(
                UrlPath.BASE_URL + UrlPath.USER,
                user,
                "application/json",
                token
        );
    }
}

