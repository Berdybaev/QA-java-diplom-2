package practicum.test;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.api.CheckSteps;
import practicum.api.OrderApiSteps;
import practicum.api.UserApiSteps;
import practicum.dto.IngredientDto;
import practicum.dto.IngredientsListDto;
import static org.apache.http.HttpStatus.*;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.fail;

@DisplayName("5. Получение списка заказов")
public class GetListofOrdersTest {
    private String email, password, name, token;
    private boolean OrderCreated = false;
    private final OrderApiSteps orderApiSteps = new OrderApiSteps();
    private final UserApiSteps userApiSteps = new UserApiSteps();
    private final CheckSteps assertions = new CheckSteps();

    @Before
    @Step("Подготовка тестовых данных")
    public void createTestData() {
        email = "morgen"+ UUID.randomUUID() +"@mail.com";
        password = "code" + UUID.randomUUID();
        name = "Alex";


        // Создание пользователя
        Response response = userApiSteps.registerUser(email, password, name);
        assertions.verifyStatusCode(response, SC_OK);

        // Получение токена авторизации
        if (response.getStatusCode() == SC_OK) {
            token = userApiSteps.extractToken(response);
        }

        //Получение списка ингредиентов
        response = orderApiSteps.getListOfIngredients();
        assertions.verifyStatusCode(response, SC_OK);

        List<IngredientDto> ingredients = response.body().as(IngredientsListDto.class).getData();

        // Создание заказа
        response = orderApiSteps.createOrder(
                List.of(ingredients.get(0).get_id(), ingredients.get(ingredients.size() - 1).get_id()),
                token
        );
        assertions.verifyStatusCode(response, SC_OK);
        if (response.getStatusCode() == SC_OK) {
            OrderCreated = true;
        }
    }

    @After
    @Step("Удаление тестовых пользователей")
    public void tearDownTestData() {
        if (token == null)
            return;
        assertions.verifyStatusCode(userApiSteps.removeUser(token), SC_ACCEPTED);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя: авторизованный пользователь")
    public void getListOrderWithAuth() {
        if (token == null || !OrderCreated)
            fail("Не создан тестовый пользователь или заказ");

        Response response = orderApiSteps.getUserListOrders(token);
        assertions.verifyStatusCode(response, SC_OK);
        assertions.verifySuccessLabel(response, "true");
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя: неавторизованный пользователь")
    public void getListOrderWithoutAuth() {
        if (token == null || !OrderCreated)
            fail("Не создан тестовый пользователь или заказ");

        Response response = orderApiSteps.getUserListOrders("");
        assertions.verifyStatusCode(response, SC_UNAUTHORIZED);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "You should be authorised");
    }

    @Test
    @DisplayName("Получение всех заказов")
    public void getOrderListAllIsSuccess() {
        Response response = orderApiSteps.getAllOrders();
        assertions.verifyStatusCode(response, SC_OK);
        assertions.verifySuccessLabel(response, "true");
    }
}
