package practicum.test;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.api.CheckSteps;
import static org.apache.http.HttpStatus.*;
import practicum.api.UserApiSteps;

import java.util.UUID;

import static org.junit.Assert.fail;

@DisplayName("3. Обновление данных пользователя")
public class UpdateUserDataTests {
    private String email, password, name, token;
    private final CheckSteps assertions = new CheckSteps();
    private final UserApiSteps UserApiSteps = new UserApiSteps();

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
        if (token == null) {
            fail("Тестовый пользователь не создан");
        }
    }

    @After
    @Step("Удаление тестовых пользователей")
    public void tearDownTestData() {
        if (token == null) {
            return;
        }
        assertions.verifyStatusCode(UserApiSteps.removeUser(token), SC_ACCEPTED);
    }

    @Test
    @DisplayName("Обновление данных пользователя: с авторизацией")
    public void updateUserDataWithAuthIsSuccessful() {
        String newEmail = "new_" + email;
        String newPassword = "new_" + password;
        String newName = "new_" + name;
        Response response = UserApiSteps.modifyUser(newEmail, newPassword, newName, token);
        assertions.verifyStatusCode(response, SC_OK);
        assertions.verifySuccessLabel(response, "true");
        UserApiSteps.validateUser(response, newEmail, newPassword, newName);
    }

    @Test
    @DisplayName("Обновление данных пользователя: без авторизации")
    public void updateUserDataWithoutAuthIsFailed() {
        String newEmail = "new_" + email;
        String newPassword = "new_" + password;
        String newName = "new_" + name;

        Response response = UserApiSteps.modifyUser(newEmail, newPassword, newName, "");
        assertions.verifyStatusCode(response, SC_UNAUTHORIZED);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "You should be authorised");
    }
}
