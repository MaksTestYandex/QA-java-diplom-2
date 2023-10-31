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

import static org.junit.Assert.assertEquals;

public class LoginUserTest {
    private UserRequest userRequest;
    private boolean needDeleteUser = false;
    private final RequestUserBody userBody = new RequestUserBody("testUser58@yandex.ru", "1234", "Ivan");
    private final RequestUserLoginBody userLoginBody = RequestUserLoginBody.from(userBody);
    private final RequestUserLoginBody userLoginBodyWithEmailMistake = new RequestUserLoginBody("tetUser58@yandex.ru", "1234");
    private final RequestUserLoginBody userLoginBodyWithPasswordMistake = new RequestUserLoginBody("testUser58@yandex.ru", "1235");

    @Before
    public void setUp() {
        needDeleteUser = false;
        userRequest = new UserRequest();
    }

    @Test
    @DisplayName("Check user authorize")
    @Description("Create user account, login it | assert: status code")
    public void shouldAuthorizeUser() {
        userRequest.createNewUser(userBody);
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, loginUser.extract().statusCode());
        needDeleteUser = true;
    }

    @Test
    @DisplayName("Check orthography fields for login")
    @Description("Send request with mistake email value | assert: status code, message")
    public void shouldVerifyRequestLoginUserWithEmailMistake() {
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBodyWithEmailMistake);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_UNAUTHORIZED, loginUser.extract().statusCode());
        assertEquals("Key value failure!", "email or password are incorrect", loginUser.extract().path("message").toString());
    }

    @Test
    @DisplayName("Check orthography fields for login")
    @Description("Send request with mistake password value | assert: status code, message")
    public void shouldVerifyRequestLoginUserWithPasswordMistake() {
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBodyWithPasswordMistake);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_UNAUTHORIZED, loginUser.extract().statusCode());
        assertEquals("Key value failure!", "email or password are incorrect", loginUser.extract().path("message").toString());
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
