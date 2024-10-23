package praktikum.orderTests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.order.Ingredients;
import praktikum.order.Order;
import praktikum.order.RandomIngredientsSet;
import praktikum.requests.OrderRequests;
import praktikum.requests.UserRequests;
import praktikum.user.User;
import praktikum.user.UserRandomData;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static praktikum.constants.ApiRequestsConstants.API_REQUEST_URI;
import static praktikum.constants.OrderConstants.INGREDIENTS_NEEDED_MESSAGE;
import static praktikum.constants.UserConstants.CREATE_USER_PATH;
import static praktikum.constants.UserConstants.LOGIN_USER_PATH;

public class OrderCreateTests {

    private String accessToken;

    UserRequests userRequests = new UserRequests();
    OrderRequests orderRequests = new OrderRequests();
    // Создаем пользователя
    User user = new User(UserRandomData.userRandomName(), UserRandomData.userRandomEmail(), UserRandomData.userRandomPassword());

    @Before
    public void setUp() {
        RestAssured.baseURI = API_REQUEST_URI;
        // Регистрируем пользователя
        Response response = userRequests.sendRequest(user, CREATE_USER_PATH);
        // Извлекаем accessToken
        accessToken = userRequests.extractAccessToken(response);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами, с авторизацией")
    @Description("Создание заказа с токеном авторизации")
    public void createOrderWithAuth() {
        // Получаем списка ингредиентов
        Ingredients ingredients = orderRequests.getIngredients();
        // Создаем случайный набор ингредиентов для заказа
        RandomIngredientsSet randomIngredientsSet = new RandomIngredientsSet();
        List<String> randomOrder = randomIngredientsSet.createRandomOrder(ingredients);
        // Создаем заказ
        Order order = new Order((ArrayList<String>) randomOrder);
        // Отправляем запрос на создание заказа
        Response response = orderRequests.sendCreateOrderRequest(accessToken, order);
        // проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами, без авторизации")
    @Description("Создание заказа без токена авторизации")
    public void createOrderWithoutAuth() {
        // Получаем список ингредиентов
        Ingredients ingredients = orderRequests.getIngredients();
        // Создаем случайный набор ингредиентов для заказа
        RandomIngredientsSet randomIngredientsSet = new RandomIngredientsSet();
        List<String> randomOrder = randomIngredientsSet.createRandomOrder(ingredients);
        // Создаем заказ
        Order order = new Order((ArrayList<String>) randomOrder);
        // Отправляем запрос на создание заказа
        Response response = orderRequests.sendCreateOrderRequest(null, order);
        // проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, с авторизацией")
    @Description("Создание заказа без ингредиентов, ожидается ошибка 400")
    public void createOrderWithoutIngredients() {
        // Создаем заказ без ингредиентов
        Order order = new Order(new ArrayList<>());
        // Отправляем запрос на создание заказа
        Response response = orderRequests.sendCreateOrderRequest(accessToken, order);
        // проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
        // Проверяем поле message
        userRequests.checkMessageField(response, INGREDIENTS_NEEDED_MESSAGE);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Попытка создать заказ с неверным идентификатором ингредиента")
    public void createOrderWithInvalidIngredient() {
        // Создаем список с неправильным идентификатором ингредиента
        List<String> wrongIngredient = Arrays.asList("wrongIngredientId");
        // Создаем заказ с неверным идентификатором ингредиента
        Order order = new Order(new ArrayList<>(wrongIngredient));
        // Отправляем запрос на создание заказа
        Response response = orderRequests.sendCreateOrderRequest(accessToken, order);
        // проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    @After
    public void userRemoval(){
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
