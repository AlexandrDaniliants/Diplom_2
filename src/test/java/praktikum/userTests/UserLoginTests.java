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

public class UserLoginTests {
    // Создаем пользователя
    User user = new User(UserRandomData.userRandomName(), UserRandomData.userRandomEmail(), UserRandomData.userRandomPassword());
    UserRequests userRequests = new UserRequests();

    @Before
    public void UserSetUp(){
        userRequests.setUpUri();
        userRequests.setUser(user);
        // Региструем нового пользователя
        Response response = userRequests.sendRequest(user, CREATE_USER_PATH);
        // Проверяем ствтус-код регистрации
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    @Description("Проверка успешной авторизации под существующим пользователем")
    public void testSuccessfulUserLogin() {
        // Авторизуемся под существующим пользователем
        Response response = userRequests.sendRequest(user, LOGIN_USER_PATH);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, true);
        userRequests.checkUserNameField(response, user);
        userRequests.checkUserEmailField(response, user);
        userRequests.checkAccessTokenField(response);
        userRequests.checkRefreshTokenField(response);
    }

    @Test
    @DisplayName("Авторизация с неверным логином/Email")
    @Description("Проверка обработки ошибки при неверном логине")
    public void testUserAutorizationWithIncorrectLogin() {
        // Создаем пользователя с неверными данными
        User incorrectUser = new User(user.getName(), UserRandomData.userRandomEmail(), user.getPassword());
        // Авторизуемся под пользователем с неверными данными
        Response response = userRequests.sendRequest(incorrectUser, LOGIN_USER_PATH);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_UNAUTHORIZED);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, false);
        userRequests.checkMessageField(response, INCORRECT_EMAIL_OR_PASSWORD_MESSAGE);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка обработки ошибки при неверном пароле")
    public void testUserAutorizationWithIncorrectPassword() {
        // Создаем пользователя с неверными данными
        User incorrectUser = new User(user.getName(), user.getEmail(), UserRandomData.userRandomPassword());

        // Авторизуемся под пользователем с неверными данными
        Response response = userRequests.sendRequest(incorrectUser, LOGIN_USER_PATH);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_UNAUTHORIZED);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, false);
        userRequests.checkMessageField(response, INCORRECT_EMAIL_OR_PASSWORD_MESSAGE);
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    @Description("Проверка успешного изменения имени авторизованного пользователя")
    public void testUserNameChangeWithAuthorization() {
        // Авторизуемся под существующим пользователем и получаем accessToken
        Response response = userRequests.sendRequest(user, LOGIN_USER_PATH);
        // Извлекаем accessToken
        String accessToken = userRequests.extractAccessToken(response);
        // Изменяем имя пользователя
        User changedUserName = userRequests.changeUserName(user);
        // Отправка запроса на изменение данных пользователя с accessToken в заголовке
        Response responseChangeData = userRequests.sendRequestForChangUserDataWithAccessToken(accessToken, changedUserName);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, true);
        userRequests.checkUserNameField(responseChangeData, changedUserName);
        userRequests.checkUserEmailField(response, user);
    }

    @Test
    @DisplayName("Изменение Email пользователя с авторизацией")
    @Description("Проверка успешного изменения Email авторизованного пользователя")
    public void testUserEmailChangeWithAuthorization() {
        // Авторизуемся под существующим пользователем и получаем accessToken
        Response response = userRequests.sendRequest(user, LOGIN_USER_PATH);
        // Извлекаем accessToken
        String accessToken = userRequests.extractAccessToken(response);
        // Изменяем Email пользователя
        User changedUserEmail = userRequests.changeUserEmail(user);
        // Отправка запроса на изменение данных пользователя с accessToken в заголовке
        Response responseChangeData = userRequests.sendRequestForChangUserDataWithAccessToken(accessToken, changedUserEmail);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, true);
        userRequests.checkUserNameField(response, user);
        userRequests.checkUserEmailField(responseChangeData, changedUserEmail);
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    @Description("Проверка обработки ошибки при изменении имени без авторизации")
    public void testUserNameChangeWithoutAuthorization() {
        // Изменяем имя пользователя
        User changedUserName = userRequests.changeUserName(user);
        // Отправка запроса на изменение данных пользователя с измененным именем пользователя
        Response response = userRequests.sendRequestForChangUserData(changedUserName);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_UNAUTHORIZED);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, false);
        userRequests.checkMessageField(response, YOU_SHOULD_BE_AUTHORIZED_MESSAGE);
    }

    @Test
    @DisplayName("Изменение Email пользователя без авторизации")
    @Description("Проверка обработки ошибки при изменении имени без авторизации")
    public void testUserEmailChangeWithoutAuthorization() {
        // Изменяем имя пользователя
        User changedUserEmail = userRequests.changeUserEmail(user);
        // Отправка запроса на изменение данных пользователя с измененным именем пользователя
        Response response = userRequests.sendRequestForChangUserData(changedUserEmail);
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_UNAUTHORIZED);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(response, false);
        userRequests.checkMessageField(response, YOU_SHOULD_BE_AUTHORIZED_MESSAGE);
    }

    @Test
    @DisplayName("Изменение email пользователя на уже существующий в базе данных")
    @Description("Проверка ошибки при попытке изменить email на уже существующий в базе данных")
    public void testUserEmailChangeToExistingEmail() {
        // Создаем второго пользователя
        User secondUser = new User(UserRandomData.userRandomName(), UserRandomData.userRandomEmail(), UserRandomData.userRandomPassword());
        // Регистрируем второго пользователя
        Response response = userRequests.sendRequest(secondUser, CREATE_USER_PATH);
        // Проверяем статус-код регистрации
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        // Авторизуем второго пользователя
        userRequests.sendRequest(secondUser, LOGIN_USER_PATH);
        // Проверяем статус-код авторизации
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        // Проверяем содержимое тела ответа на авторизацию
        userRequests.checkSuccessField(response, true);
        userRequests.checkAccessTokenField(response);
        // Извлекаем accessToken
        String secondUserAccessToken = userRequests.extractAccessToken(response);
        // Email второго пользователя меняем на Email существующего пользователя
        secondUser.setEmail(user.getEmail());
        // Пытаемся изменить email второго пользователя на email уже существующего пользователя
        // Отправка запроса на изменение данных второго пользователя с accessToken в заголовке
        Response responseChangeData = userRequests.sendRequestForChangUserDataWithAccessToken(secondUserAccessToken, secondUser);
        // Проверяем статус-код
        userRequests.checkStatusCode(responseChangeData, HttpURLConnection.HTTP_FORBIDDEN);
        // Проверяем содержимое тела ответа
        userRequests.checkSuccessField(responseChangeData, false);
        userRequests.checkMessageField(responseChangeData, USER_WITH_SUCH_EMAIL_EXISTS_MESSAGE);
    }

    @After
    public void userRemoval() {
        // Авторизуемся под пользователем для получения accessToken
        Response response = userRequests.sendRequest(user, LOGIN_USER_PATH);
        // Извлекаем accessToken
        String accessToken = userRequests.extractAccessToken(response);
        // Если accessToken не null, удаляем зарегистрированного пользователя, передавая accessToken
        if (accessToken != null) {
            userRequests.deleteUser(user, accessToken);
            // Проверяем, что пользователь успешно удален
            userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);}
    }
}





