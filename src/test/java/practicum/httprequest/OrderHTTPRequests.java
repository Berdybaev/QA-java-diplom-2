package practicum.httprequest;

import io.restassured.response.Response;
import practicum.dto.OrderDto;
import practicum.util.UrlPath;


public class OrderHTTPRequests extends BaseHTTPRequests {

    public Response  createNewOrder(OrderDto order, String token) {
        return postRequestWithAuth(
                UrlPath.BASE_URL + UrlPath.ORDERS,
                order,
                "application/json",
                token
        );
    }

    public Response fetchIngredientList() {
        return getRequestWithoutAuth(UrlPath.BASE_URL + UrlPath.INGREDIENTS);
    }

    public Response fetchOrderList(String token) {
        return getRequestWithAuth(
                UrlPath.BASE_URL + UrlPath.ORDERS,
                token
        );
    }

    public Response fetchAllOrders() {
        return getRequestWithoutAuth(
                UrlPath.BASE_URL + UrlPath.ORDERS_ALL
        );
    }
}
