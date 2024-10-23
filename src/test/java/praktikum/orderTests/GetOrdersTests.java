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
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static praktikum.constants.ApiRequestsConstants.API_REQUEST_URI;
import static praktikum.constants.UserConstants.*;

public class GetOrdersTests {

    UserRequests userRequests = new UserRequests();
    OrderRequests orderRequests = new OrderRequests();
    // Создаем пользователя
    User user = new User(UserRandomData.userRandomName(), UserRandomData.userRandomEmail(), UserRandomData.userRandomPassword());

    @Before
    public void setUp() {
        RestAssured.baseURI = API_REQUEST_URI;
        // Регистрируем пользователя
        userRequests.sendRequest(user, CREATE_USER_PATH);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Получение списка заказов для авторизованного пользователя")
    public void getOrdersForAuthorizedUser() {
        // Авторизуем пользователя
        Response loginResponse = userRequests.sendRequest(user, LOGIN_USER_PATH);
        // Извлекаем accessToken
        String accessToken = userRequests.extractAccessToken(loginResponse);
        // Генерация случайного количества заказов от 1 до 10
        Random random = new Random();
        int numberOfOrders = random.nextInt(10) + 1;
        // Создание сгенерированного количества заказов
        for (int i = 0; i < numberOfOrders; i++) {
            // Получаем список ингредиентов
            Ingredients ingredients = orderRequests.getIngredients();
            // Создаем случайный набор ингредиентов для заказа
            RandomIngredientsSet randomIngredientsSet = new RandomIngredientsSet();
            List<String> randomOrder = randomIngredientsSet.createRandomOrder(ingredients);
            // Создаем заказ
            Order order = new Order((ArrayList<String>) randomOrder);
            // Отправляем запрос на создание заказа
            Response response = orderRequests.sendCreateOrderRequest(accessToken, order);
            // Проверяем статус-код создания заказа
            userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        }
        // Получение списка заказов
        Response response = orderRequests.sendGetOrdersRequestWithAuth(accessToken);
        // Проверяем статус-код получения списка заказов
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        // Проверяем поле success в ответе
        userRequests.checkSuccessField(response, true);
        // Подсчитываем количество заказов в ответе
        int ordersCount = response.jsonPath().getList("orders").size();
         //Проверяем, что количество заказов соответствует сгенерированному количеству
        assertEquals("Количество заказов не соответствует ожидаемому количеству", numberOfOrders, ordersCount);
    }

    @Test
    @DisplayName("Получение заказов неавторизованным пользователем")
    @Description("Попытка получить заказы без авторизации")
    public void getOrdersForUnauthorizedUser() {
        // Отправляем запрос на получение заказов без токена
        Response response = orderRequests.sendGetOrdersRequestWithoutAuth();
        // Проверяем статус-код
        userRequests.checkStatusCode(response, HttpURLConnection.HTTP_UNAUTHORIZED);
        // Проверяем поле message в ответе
        userRequests.checkMessageField(response, YOU_SHOULD_BE_AUTHORIZED_MESSAGE);
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
