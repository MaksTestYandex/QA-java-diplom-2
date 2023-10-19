package com.github.mablinov.diplom2.user;

import com.github.mablinov.diplom2.RequestSpec;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserRequest {
    public static final String USER_PATH = "/auth/";

    @Step
    public ValidatableResponse createNewUser(RequestUserBody userBody) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .body(userBody)
                .when()
                .log().all()
                .post(USER_PATH + "register")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse loginUser(RequestUserLoginBody userLoginBody) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .body(userLoginBody)
                .when()
                .log().all()
                .post(USER_PATH + "login")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse logoutUser(String logoutBody) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .body("{\"token\": \"" + logoutBody + "\"}")
                .when()
                .log().all()
                .post(USER_PATH + "logout")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .header("Authorization", token)
                .when()
                .log().all()
                .delete(USER_PATH + "user")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse updateUser(String token, RequestUserBody userBody) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .header("Authorization", token)
                .body(userBody)
                .when()
                .log().all()
                .patch(USER_PATH + "user")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse updateUser(RequestUserBody userBody) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .body(userBody)
                .when()
                .log().all()
                .patch(USER_PATH + "user")
                .then()
                .log().all();
    }

}
