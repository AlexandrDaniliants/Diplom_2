package praktikum.requests;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import praktikum.user.User;
import praktikum.user.UserRandomData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static praktikum.constants.UserConstants.CHANGE_USER_DATA_PATH;
import static praktikum.constants.UserConstants.DELETE_USER_PATH;

public class UserRequests extends ApiUri{

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

//    //Создание пользователя
//    @Step("Отправить запрос на создание пользователя")
//    public Response createUser(User user) {
//        return given().log().all()
//                .contentType(ContentType.JSON)
//                .body(user)
//                .post(CREATE_USER_PATH)
//                .then().log().all()
//                .extract().response();
//    }

    // Отправка запроса для регистрации или авторизации пользователя
    @Step("Отправить запрос на создание или авторизацию пользователя")
    public Response sendRequest(User user, String usedPath) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .post(usedPath)
                .then().log().all()
                .extract().response();
    }

    // Проверка статус-кода
    @Step("Проверить статус код в ответе")
    public void checkStatusCode(Response response, int expectedStatusCode) {
        response.then().assertThat()
                .statusCode(expectedStatusCode);
    }

    // Проверка поля "success"
    @Step("Проверить поле success в ответе")
    public void checkSuccessField(Response response, Boolean expectedStatus) {
        response.then()
                .body("success", equalTo(expectedStatus));
    }

    // Проверка поля "user.name"
    @Step("Проверить поле user.name в ответе")
    public void checkUserNameField(Response response, User user) {
        response.then()
                .body("user.name", equalTo(user.getName()));
    }

    // Проверка поля "user.email"
    @Step("Проверить поле user.email в ответе")
    public void checkUserEmailField(Response response, User user) {
        response.then()
                .body("user.email", equalToIgnoringCase(user.getEmail()));
    }

    // Проверка поля "message"
    @Step("Проверить поле message в ответе")
    public void checkMessageField(Response response, String expectedMessage) {
        response.then()
                .body("message", equalTo(expectedMessage));
    }

    // Проверка поля "accessToken"
    @Step("Проверить что поле accessToken в ответе не является null")
    public void checkAccessTokenField(Response response) {
        response.then()
                .body("accessToken", notNullValue());
    }

    // Проверка поля "refreshToken"
    @Step("Проверить что поле refreshToken в ответе не является null")
    public void checkRefreshTokenField(Response response) {
        response.then()
                .body("refreshToken", notNullValue());
    }

    // Извлечение accessToken
    @Step("Извлечь accessToken из поля accessToken ответа")
    public String extractAccessToken(Response response) {
        return response.path("accessToken");
    }

    // Изменение имени пользователя
    @Step("Изменить имя пользователя")
    public User changeUserName(User user) {
        user.setName("New" + UserRandomData.userRandomName());
        return user;
    }

    // Изменение Email пользователя
    @Step("Изменить Email пользователя")
    public User changeUserEmail(User user) {
        user.setEmail("New" + UserRandomData.userRandomEmail());
        return user;
    }

    // Отправка запроса на изменение данных пользователя с accessToken в заголовке
    @Step("Отправить запрос на изменение данных пользователя, в заголовке передать accessToken")
    public Response sendRequestForChangUserDataWithAccessToken(String accessToken, User changedUserData) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .body(changedUserData)
                .patch(CHANGE_USER_DATA_PATH )
                .then().log().all()
                .extract().response();
    }

    // Отправка запроса на изменение данных пользователя без accessToken в заголовке
    @Step("Отправить запрос на изменение данных пользователя без accessToken в заголовке")
    public Response sendRequestForChangUserData(User changedUserData) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(changedUserData)
                .patch(CHANGE_USER_DATA_PATH)
                .then().log().all()
                .extract().response();
    }

    // Удаление пользователя
    @Step("Отправить запрос на удаление пользователя")
    public void deleteUser(User user, String accessToken) {
        Response response = given().log().all()
                .header("Authorization", accessToken)
                .delete(DELETE_USER_PATH)
                .then().log().all()
                .extract().response();
    }
}
