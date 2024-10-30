package practicum.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import practicum.dto.OrderDto;
import practicum.httprequest.OrderHTTPRequests;


import java.util.List;

public class OrderApiSteps extends OrderHTTPRequests {

    @Step("Создание заказа")
    public Response createOrder(List<String> ingredients, String token) {
        return super.createNewOrder(new OrderDto(ingredients), token);
    }

    @Step("Получение списка ингредиентов")
    public Response getListOfIngredients() {
        return super.fetchIngredientList();
    }

    @Step("Получение списка заказов пользователя")
    public Response getUserListOrders(String token) {
        return super.fetchOrderList(token);
    }

    @Step("Получение всех заказов")
    public Response getAllOrders() {
        return super.fetchAllOrders();
    }
}



