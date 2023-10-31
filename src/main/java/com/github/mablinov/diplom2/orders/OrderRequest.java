package com.github.mablinov.diplom2.orders;

import com.github.mablinov.diplom2.RequestSpec;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderRequest {

    @Step("Создание заказа с ингредиентами, без авторизации")
    public ValidatableResponse createOrderWithIngredients(String json) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .body(json)
                .when()
                .log().all()
                .post("/orders")
                .then()
                .log().all();
    }

    @Step("Создание заказа с ингредиентами, с авторизацией")
    public ValidatableResponse createOrderWithIngredients(String token, String json) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .header("Authorization", token)
                .body(json)
                .when()
                .log().all()
                .post("/orders")
                .then()
                .log().all();
    }

    @Step("Создание заказа без ингредиентов")
    public ValidatableResponse createOrderWithoutIngredients(String token) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .header("Authorization", token)
                .when()
                .log().all()
                .post("/orders")
                .then()
                .log().all();
    }

    @Step("Получение списка заказов, с авторизацией")
    public ValidatableResponse getListOfOrders(String token) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .header("Authorization", token)
                .when()
                .log().all()
                .get("/orders")
                .then()
                .log().all();
    }

    @Step("Получение списка заказов, без авторизации")
    public ValidatableResponse getListOfOrders() {
        return given()
                .spec(RequestSpec.requestSpecification())
                .when()
                .log().all()
                .get("/orders")
                .then()
                .log().all();
    }
}
