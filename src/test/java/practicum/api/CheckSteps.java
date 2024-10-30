package practicum.api;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import practicum.dto.UserAuthDto;
import static org.hamcrest.Matchers.equalTo;

public class CheckSteps {

    @Step("Проверка статус-кода ответа")
    public void verifyStatusCode(Response response, int code) {
        Allure.addAttachment("Статус ответа", response.getStatusLine());
        response.then().statusCode(code);
    }

    @Step("Проверка флага успеха в ответе")
    public void verifySuccessLabel(Response response, String expectedValue) {
        MatcherAssert.assertThat(
                "Флаг success не соответствует ожидаемому значению",
                expectedValue,
                equalTo(response.body().as(UserAuthDto.class).getSuccess())
        );
    }

    @Step("Проверка содержимого сообщения в ответе")
    public void verifyMessageLabel(Response response, String expectedMessage) {

        MatcherAssert.assertThat(
                "Содержимое message не соответствует ожидаемому значению",
                expectedMessage,
                equalTo(response.body().as(UserAuthDto.class).getMessage())
        );
    }
}

