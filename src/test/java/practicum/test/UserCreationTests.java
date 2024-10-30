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

import java.util.ArrayList;
import java.util.UUID;

@DisplayName("1. Создание пользователя")
public class UserCreationTests {
    private String email, password, name;
    private ArrayList<String> tokens = new ArrayList<>();
    private final UserApiSteps UserApiSteps = new UserApiSteps();
    private final CheckSteps assertions = new CheckSteps();

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        email = "morgen"+ UUID.randomUUID() +"@mail.com";
        password = "code" + UUID.randomUUID();
        name = "Alex";
    }

    @After
    @Step("Удаление тестовых пользователей")
    public void tearDownTestData() {
        if (tokens.isEmpty())
            return;
        for (String token : tokens) {
            assertions.verifyStatusCode(UserApiSteps.removeUser(token), SC_ACCEPTED);
        }
    }

    @Test
    @DisplayName("Создание пользователя")
    public void createNewUserIsSuccess() {
        Response response = UserApiSteps.registerUser(email, password, name);
        if (response.getStatusCode() == SC_OK) {
            tokens.add(UserApiSteps.extractToken(response));
        }

        assertions.verifyStatusCode(response, SC_OK);
        assertions.verifySuccessLabel(response, "true");
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    public void createNewSimilarUsersIsFailed() {
        Response responseFirstUser = UserApiSteps.registerUser(email, password, name);
        Response responseSecondUser = UserApiSteps.registerUser(email, password, name);
        if (responseFirstUser.getStatusCode() == SC_OK) {
            tokens.add(UserApiSteps.extractToken(responseFirstUser));
        }
        if (responseSecondUser.getStatusCode() == SC_OK) {
            tokens.add(UserApiSteps.extractToken(responseSecondUser));
        }

        assertions.verifyStatusCode(responseSecondUser, SC_FORBIDDEN);
        assertions.verifySuccessLabel(responseSecondUser, "false");
        assertions.verifyMessageLabel(responseSecondUser, "User already exists");
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createNewUserMissedEmailIsFailed() {
        Response response = UserApiSteps.registerUser("", password, name);
        if (response.getStatusCode() == SC_OK) {
            tokens.add(UserApiSteps.extractToken(response));
        }

        assertions.verifyStatusCode(response, SC_FORBIDDEN);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создание пользователя без password")
    public void createNewUserMissedPasswordIsFailed() {
        Response response = UserApiSteps.registerUser(email, "", name);
        if (response.getStatusCode() == SC_OK) {
            tokens.add(UserApiSteps.extractToken(response));
        }

        assertions.verifyStatusCode(response, SC_FORBIDDEN);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создание пользователя без name")
    public void createNewUserMissedNameIsFailed() {
        Response response = UserApiSteps.registerUser(email, password, "");
        if (response.getStatusCode() == SC_OK) {
            tokens.add(UserApiSteps.extractToken(response));
        }

        assertions.verifyStatusCode(response, SC_FORBIDDEN);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создание пользователя без всех заполненных полей")
    public void createNewUserMissedAllParamsIsFailed() {
        Response response = UserApiSteps.registerUser("", "", "");
        if (response.getStatusCode() == SC_OK) {
            tokens.add(UserApiSteps.extractToken(response));
        }

        assertions.verifyStatusCode(response, SC_FORBIDDEN);
        assertions.verifySuccessLabel(response, "false");
        assertions.verifyMessageLabel(response, "Email, password and name are required fields");
    }
}
