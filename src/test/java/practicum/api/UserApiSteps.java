package practicum.api;


import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import practicum.dto.UserDto;
import practicum.dto.UserAuthDto;
import practicum.httprequest.UserHTTPRequests;
import static org.apache.http.HttpStatus.*;


import static org.hamcrest.Matchers.equalTo;

public class UserApiSteps extends UserHTTPRequests {

    @Step("Создание пользователя")
    public Response registerUser(String email, String password, String name) {
        return super.createUser(new UserDto(email, password, name));
    }

    @Step("Авторизация пользователя")
    public Response loginUser(String email, String password) {
        return super.loginUser(new UserDto(email, password));
    }

    @Step("Удаление пользователя")
    public Response removeUser(String token) {
        return super.deleteUser(token);
    }

    @Step("Обновление данных пользователя")
    public Response modifyUser(String email, String password, String name, String token) {
        return super.updateUser(new UserDto(email, password, name), token);
    }

    @Step("Валидация данных пользователя")
    public void validateUser(Response response, String expectedMail, String expectedPassword, String expectedName) {
        UserDto actualUser = response.body().as(UserAuthDto.class).getUser();
        Allure.addAttachment("Данные пользователя", actualUser.toString());

        MatcherAssert.assertThat("Email не соответствует", actualUser.getEmail(), equalTo(expectedMail));
        MatcherAssert.assertThat("Имя не соответствует", actualUser.getName(), equalTo(expectedName));

        new CheckSteps().verifyStatusCode(loginUser(expectedMail, expectedPassword), SC_OK);
    }

    @Step("Извлечение токена доступа")
    public String extractToken(Response response) {
        String token = response.body().as(UserAuthDto.class).getAccessToken().split(" ")[1];
        Allure.addAttachment("Статус ответа", response.getStatusLine());
        Allure.addAttachment("Токен доступа", token);
        return token;
    }
}
