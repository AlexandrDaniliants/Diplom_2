package praktikum.userTests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.requests.UserRequests;
import praktikum.user.User;
import praktikum.user.UserRandomData;

import java.net.HttpURLConnection;

import static praktikum.constants.UserConstants.*;

public class UserRegistrationTests {
    //Сщздаем пользователя
    User user = new User(UserRandomData.userRandomName(), UserRandomData.userRandomEmail(), UserRandomData.userRandomPassword());

    private UserRequests userRequests = new UserRequests();

    @Before
    public void UserSetUp(){
        userRequests.setUpUri();
        userRequests.setUser(user);
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Проверка успешного создания пользователя при корректных данных")
    public void testSuccessfulUserCreation() {
        // Регистрируем пользователя
        Response response = userRequests.sendRequest(user, CREATE_USER_PATH);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, true);
        userRequests.checkUserNameField(response, user);
        userRequests.checkUserEmailField(response, user);
        userRequests.checkAccessTokenField(response);
        userRequests.checkRefreshTokenField(response);
//        Response response =
//                given().log().all()
//                .contentType(ContentType.JSON)
//                .body(user)
//                .post(CREATE_USER_PATH)
//                .then().log().all()
//                .assertThat()
//                .statusCode(HttpURLConnection.HTTP_OK)
//                .body("success", equalTo(true))
//                .body("user.email", equalTo(user.getEmail().toLowerCase()))
//                .body("user.name", equalTo(user.getName()))
//                .body("accessToken", notNullValue())
//                .body("refreshToken", notNullValue())
//                .extract().response();
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    @Description("Проверка обработки ошибки при попытке зарегистрировать уже существующего пользователя")
    public void testUserAlreadyExists() {
        // Регистрируем пользователя
        Response responseUniqueUser = userRequests.sendRequest(user, CREATE_USER_PATH);
        // Проверяем статус-код
        userRequests.checkStatusCode(responseUniqueUser, HttpURLConnection.HTTP_OK);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(responseUniqueUser, true);
//        Response createUserResponse =
//                given().log().all()
//                        .contentType(ContentType.JSON)
//                        .body(user)
//                        .when()
//                        .post(CREATE_USER_PATH)
//                        .then().log().all()
//                        .assertThat()
//                        .statusCode(HttpURLConnection.HTTP_OK)
//                        .body("success", equalTo(true))
//                        .extract().response();
        // Регистрируем пользователя повторно
        Response responseRepeatedUser = userRequests.sendRequest(user, CREATE_USER_PATH);
        // Проверяем статус-код
        userRequests.checkStatusCode(responseRepeatedUser, HttpURLConnection.HTTP_FORBIDDEN);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(responseRepeatedUser, false);
        userRequests.checkMessageField(responseRepeatedUser, USER_ALREADY_EXISTS_MESSAGE);
//        Response response =
//                given().log().all()
//                        .contentType(ContentType.JSON)
//                        .body(user)
//                        .when()
//                        .post(CREATE_USER_PATH)
//                        .then().log().all()
//                        .assertThat()
//                        .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
//                        .body("success", equalTo(false))
//                        .body("message", equalTo("User already exists"))
//                        .extract().response();
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля name")
    @Description("Проверка обработки ошибки при отсутствии обязательного поля name в запросе")
    public void testMissingRequiredFieldName() {
        // Создаем пользователя без имени
        User user = new User(EMPTY_USER_NAME, UserRandomData.userRandomEmail(), UserRandomData.userRandomPassword());
        // Регистрируем пользователя
        Response response = userRequests.sendRequest(user, CREATE_USER_PATH);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_FORBIDDEN);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, false);
        userRequests.checkMessageField(response, MISSING_REQUIRED_FIELDS_MESSAGE);
//        Response response =
//                given().log().all()
//                        .contentType(ContentType.JSON)
//                        .body(user)
//                        .when()
//                        .post(CREATE_USER_PATH)
//                        .then().log().all()
//                        .assertThat()
//                        .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
//                        .body("success", equalTo(false))
//                        .body("message", equalTo("Email, password and name are required fields"))
//                        .extract().response();
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля email")
    @Description("Проверка обработки ошибки при отсутствии обязательного поля email в запросе")
    public void testMissingRequiredFieldEmail() {
        // Создаем пользователя без Email
        User user = new User(UserRandomData.userRandomName(), EMPTY_USER_EMAIL, UserRandomData.userRandomPassword());
        // Регистрируем пользователя
        Response response = userRequests.sendRequest(user, CREATE_USER_PATH);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_FORBIDDEN);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, false);
        userRequests.checkMessageField(response, MISSING_REQUIRED_FIELDS_MESSAGE);
//        Response response =
//                given().log().all()
//                        .contentType(ContentType.JSON)
//                        .body(user)
//                        .when()
//                        .post(CREATE_USER_PATH)
//                        .then().log().all()
//                        .assertThat()
//                        .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
//                        .body("success", equalTo(false))
//                        .body("message", equalTo("Email, password and name are required fields"))
//                        .extract().response();
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля password")
    @Description("Проверка обработки ошибки при отсутствии обязательного поля password в запросе")
    public void testMissingRequiredFieldPassword(){
        // Создаем пользователя без пароля
        User user = new User(UserRandomData.userRandomName(), UserRandomData.userRandomEmail(), EMPTY_USER_PASSWORD);
        // Регистрируем пользователя
        Response response = userRequests.sendRequest(user, CREATE_USER_PATH);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_FORBIDDEN);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, false);
        userRequests.checkMessageField(response, MISSING_REQUIRED_FIELDS_MESSAGE);
//        Response response =
//                given().log().all()
//                        .contentType(ContentType.JSON)
//                        .body(user)
//                        .when()
//                        .post(CREATE_USER_PATH)
//                        .then().log().all()
//                        .assertThat()
//                        .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
//                        .body("success", equalTo(false))
//                        .body("message", equalTo("Email, password and name are required fields"))
//                        .extract().response();
    }

    @After
    public void userRemoval(){
        //Удаляем зарегистрированного пользователя
        userRequests.deleteUser(user);
    }
}

