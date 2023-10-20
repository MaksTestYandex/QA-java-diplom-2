package com.github.mablinov.diplom2;

import com.github.mablinov.diplom2.user.RequestUserBody;
import com.github.mablinov.diplom2.user.RequestUserLoginBody;
import com.github.mablinov.diplom2.user.UserRequest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class CreateUserTest {
    Random random = new Random();
    private String email = "testUser" + random.nextInt(100) + "@yandex.ru";
    private UserRequest userRequest;
    private boolean needDeleteUser = false;
    private final RequestUserBody userBody = new RequestUserBody(email, "1234", "Ivan");
    private final RequestUserLoginBody userLoginBody = RequestUserLoginBody.from(userBody);

    private final RequestUserBody userBodyWithoutPassword = new RequestUserBody(email, "", "Ivan");

    @Before
    public void setUp() {
        needDeleteUser = false;
        userRequest = new UserRequest();
    }

    @Test
    @DisplayName("Check that user account is created ")
    @Description("Create  user account | assert: status code,message")
    public void shouldCreateNewUser() {
        ValidatableResponse createNewUser = userRequest.createNewUser(userBody);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, createNewUser.extract().statusCode());
        assertEquals("Key value failure!", true, createNewUser.extract().path("success"));
        needDeleteUser = true;
    }

    @Test
    @DisplayName("Check that user account may delete ")
    @Description("Create, login and delete user account | assert: status code")
    public void shouldDeleteCourier() {
        userRequest.createNewUser(userBody);
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
        ValidatableResponse deleteUser = userRequest.deleteUser(loginUser.extract().path("accessToken"));
        assertEquals("Status code failure!", HttpURLConnection.HTTP_ACCEPTED, deleteUser.extract().statusCode());
    }

    @Test
    @DisplayName("Check that user account is unique ")
    @Description("Try to create duplicate user account | assert: status code")
    public void shouldNotCreateDuplicateUser() {
        userRequest.createNewUser(userBody);
        ValidatableResponse createNewUser = userRequest.createNewUser(userBody);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_FORBIDDEN, createNewUser.extract().statusCode());
        needDeleteUser = true;
    }

    @Test
    @DisplayName("Check necessary fields for create user account ")
    @Description("Send request without password value  | assert: status code")
    public void shouldVerifyNecessaryFieldsInRequestCreateNewUser() {
        ValidatableResponse createNewUser = userRequest.createNewUser(userBodyWithoutPassword);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_FORBIDDEN, createNewUser.extract().statusCode());
    }

    @After
    public void deleteCreatedUser() {
        if (needDeleteUser) {
            ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
            ValidatableResponse deleteUser = userRequest.deleteUser(loginUser.extract().path("accessToken"));
            assertEquals("Status code failure!", HttpURLConnection.HTTP_ACCEPTED, deleteUser.extract().statusCode());
        }
    }
}
