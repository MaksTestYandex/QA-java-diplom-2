package com.github.mablinov.diplom2.orders;

import com.github.mablinov.diplom2.RequestSpec;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderRequest {

    @Step
    public ValidatableResponse createOrderWithIngredients() {
        return given()
                .spec(RequestSpec.requestSpecification())
                .body("{\"ingredients\": [\"61c0c5a71d1f82001bdaaa72\",\"61c0c5a71d1f82001bdaaa6f\",\"61c0c5a71d1f82001bdaaa75\",\"61c0c5a71d1f82001bdaaa6d\"]}")
                .when()
                .log().all()
                .post("/orders")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse createOrderWithIngredients(String token) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .header("Authorization", token)
                .body("{\"ingredients\": [\"61c0c5a71d1f82001bdaaa72\",\"61c0c5a71d1f82001bdaaa6f\",\"61c0c5a71d1f82001bdaaa75\",\"61c0c5a71d1f82001bdaaa6d\"]}")
                .when()
                .log().all()
                .post("/orders")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse createOrderWithoutIngredients(String token) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .header("Authorization", token)
                .body("{\"ingredients\": [\"0\"]}")
                .when()
                .log().all()
                .post("/orders")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse createOrderWithHashCodeMistakeIngredients(String token) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .header("Authorization", token)
                .body("{\"ingredients\": [\"61c0c5a71d1f82001bdaab72\",\"61c0c5a71d1f82001bdaaa6f\",\"61c0c5a71d1f82001bdaaa75\",\"61c0c5a71d1f82001bdaaa6d\"]}")
                .when()
                .log().all()
                .post("/orders")
                .then()
                .log().all();
    }

    @Step
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

    @Step
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
