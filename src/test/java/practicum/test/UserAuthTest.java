package practicum.test;

import io.qameta.allure.Link;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.api.CheckSteps;
import practicum.api.UserApiSteps;
import static org.apache.http.HttpStatus.*;

import java.util.UUID;

import static org.junit.Assert.fail;

@DisplayName("2. Авторизация пользователя")
public class UserAuthTest {
    private String email, password, name, token;
    private final UserApiSteps UserApiSteps = new UserApiSteps();
    private final CheckSteps assertions = new CheckSteps();
    @Before
    @Step("Подготовка данных для тестирования")
    public void setUpTestData() {
        email = "morgen"+ UUID.randomUUID() +"@mail.com";
        password = "code" + UUID.randomUUID();
        name = "Alex";


        // Регистрация пользователя
        Response response = UserApiSteps.registerUser(email, password, name);
        assertions.verifyStatusCode(response, SC_OK);

        // Получение токена авторизации
        if (response.getStatusCode() == SC_OK) {
            token = UserApiSteps.extractToken(response);
        }
        if (token == null)
            fail("Тестовый пользователь не был создан");

    }

    @After
    @Step("Очистка данных тестового пользователя")
    public void tearDownTestData() {
        if(token.isEmpty())
            return;
        assertions.verifyStatusCode(UserApiSteps.removeUser(token), SC_ACCEPTED);
    }

    @Test
    @DisplayName("Успешная авторизация существующего пользователя")
    public void successfulLoginUser() {
        Response response = UserApiSteps.loginUser(email, password);
        assertions.verifyStatusCode(response, SC_OK);
        assertions.verifySuccessLabel(response, "true");
    }

    @Test
    @DisplayName("Неуспешная авторизация с некорректным email")
    public void failedLoginWithInvalidEmail() {
        Response response = UserApiSteps.loginUser("wrongEmail_" + UUID.randomUUID() + "@mail.com", password);
        assertions.verifyStatusCode(response, SC_UNAUTHORIZED);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "email or password are incorrect");
    }

    @Test
    @DisplayName("Неуспешная авторизация с неверным паролем")
    public void failedLoginWithInvalidPassword() {
        Response response = UserApiSteps.loginUser(email, password + UUID.randomUUID());
        assertions.verifyStatusCode(response, SC_UNAUTHORIZED);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "email or password are incorrect");
    }

    @Test
    @DisplayName("Неуспешная авторизация без указания email")
    public void failedLoginWithoutEmail() {
        Response response = UserApiSteps.loginUser("", password);
        assertions.verifyStatusCode(response, SC_UNAUTHORIZED);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "email or password are incorrect");
    }

    @Test
    @DisplayName("Неуспешная авторизация без указания пароля")
    public void failedLoginWithoutPassword() {
        Response response = UserApiSteps.loginUser(email, "");
        assertions.verifyStatusCode(response, SC_UNAUTHORIZED);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "email or password are incorrect");
    }

}

