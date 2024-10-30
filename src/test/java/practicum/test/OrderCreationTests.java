package practicum.test;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.api.OrderApiSteps;
import practicum.api.CheckSteps;
import practicum.api.UserApiSteps;
import practicum.dto.IngredientDto;
import practicum.dto.IngredientsListDto;
import static org.apache.http.HttpStatus.*;

import java.util.*;

import static org.junit.Assert.fail;

@DisplayName("4. Создание заказа")
public class OrderCreationTests {

    private String email, password, name, token;
    private List<IngredientDto> ingredients = new ArrayList<>();
    private final OrderApiSteps orderApiSteps = new OrderApiSteps();
    private final UserApiSteps UserApiSteps = new UserApiSteps();
    private final CheckSteps assertions = new CheckSteps();

        @Before
    @Step("Подготовка тестовых данных")
    public void setUpTestData() {
        email = "morgen"+ UUID.randomUUID() +"@mail.com";
        password = "code" + UUID.randomUUID();
        name = "Alex";

        // Создание пользователя
        Response response = UserApiSteps.registerUser(email, password, name);
        assertions.verifyStatusCode(response, SC_OK);

        // Получение токена
        if (response.getStatusCode() == SC_OK) {
            token = UserApiSteps.extractToken(response);
        }

        // Получение списка ингредиентов
        response = orderApiSteps.getListOfIngredients();
        assertions.verifyStatusCode(response, SC_OK);

        ingredients = response.body().as(IngredientsListDto.class).getData();

        if (token == null || ingredients.isEmpty()) {
            fail("Отсутствует токен или не получен список ингредиентов");
        }
    }

    @After
    @Step("Удаление тестовых данных пользователя")
    public void tearDownTestData() {
        if (token == null) return;
        assertions.verifyStatusCode(UserApiSteps.removeUser(token), SC_ACCEPTED);
    }

    @Test
    @DisplayName("Создание заказа: с авторизацией и с ингредиентами")
    public void  createOrderWithAuthAndIngredients() {  //
        Response response = orderApiSteps.createOrder(
                List.of(ingredients.get(0).get_id(), ingredients.get(ingredients.size() - 1).get_id()),
                token
        );
        assertions.verifyStatusCode(response, SC_OK);
        assertions.verifySuccessLabel(response, "true");
    }

    @Test
    @DisplayName("Создание заказа: без авторизации и с ингредиентами")
    public void createOrderWithoutAuthWithIngredients() {  //
        Response response = orderApiSteps.createOrder(
                List.of(ingredients.get(0).get_id(), ingredients.get(ingredients.size() - 1).get_id()), "Wrong_token");
        assertions.verifyStatusCode(response, SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание заказа: с авторизацией и без ингредиентов")
    public void createOrderWithAuthWithoutIngredients() {  //
        Response response = orderApiSteps.createOrder(
                List.of(),
                token
        );
        assertions.verifyStatusCode(response, SC_BAD_REQUEST);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Создание заказа: с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredients() {  //
        Response response = orderApiSteps.createOrder(
                List.of(ingredients.get(0).get_id(), UUID.randomUUID().toString()),
                token
        );
        assertions.verifyStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }
}
