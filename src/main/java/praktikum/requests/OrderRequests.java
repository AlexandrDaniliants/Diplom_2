package praktikum.requests;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import praktikum.order.Ingredients;
import praktikum.order.Order;

import static io.restassured.RestAssured.given;
import static praktikum.constants.OrderConstants.*;

public class OrderRequests {

    @Step("Отправить запрос на получение списка ингредиентов")
    public Ingredients getIngredients(){
        return given()
                .contentType(ContentType.JSON)
                .log().all()
                .get(GET_INGREDIENTS_LIST_PATH)
                .body()
                .as(Ingredients.class);
    }

    @Step("Отправить запрос на создание заказа")
    public Response sendCreateOrderRequest(String accessToken, Order order) {
        RequestSpecification requestSpec = given()
                .contentType(ContentType.JSON)
                .body(order)
                .log().all();
        // Проверка наличия токена авторизации перед добавлением заголовка
        if (accessToken != null) {
            requestSpec.header("Authorization", accessToken);
        }
        return requestSpec.post(CREATE_ORDER_PATH);
    }

    @Step("Отправить запрос на получение списка заказов авторизованного пользователя")
    public Response sendGetOrdersRequestWithAuth(String accessToken) {
        RequestSpecification requestSpec = given()
                .contentType(ContentType.JSON)
                .log().all();
        // Проверка наличия токена авторизации перед добавлением заголовка
        if (accessToken != null) {
            requestSpec.header("Authorization", accessToken);
        }
        return requestSpec.get(GET_ORDERS_PATH);
    }

    @Step("Отправить запрос на получение списка заказов неавторизованного пользователя")
    public Response sendGetOrdersRequestWithoutAuth() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .log().all()
                .get(GET_ORDERS_PATH);
    }
}
