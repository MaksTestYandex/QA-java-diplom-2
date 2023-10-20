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

public class UpdateUserTest {
    Random random = new Random();
    private String email = "testUser" + random.nextInt(100) + "@yandex.ru";
    private String updatedEmail = "update" + email;
    private String password = "1234";
    private String updatedPassword = password + "5678";
    private String name = "Ivan";
    private String updatedName = name + "Update";
    private UserRequest userRequest;
    private final RequestUserBody userBody = new RequestUserBody(email, password, name);
    private final RequestUserLoginBody userLoginBody = RequestUserLoginBody.from(userBody);
    private final RequestUserBody updateUserBody = new RequestUserBody(updatedEmail, updatedPassword, updatedName);
    private final RequestUserLoginBody updatedUserLoginBody = RequestUserLoginBody.from(updateUserBody);

    private RequestUserLoginBody userLoginBodyForDelete;

    @Before
    public void setUp() {
        userLoginBodyForDelete = null;
        userRequest = new UserRequest();
    }

    @Test
    @DisplayName("Check update user data ")
    @Description("Create  user account, login it, update it | assert: status code, key value, email and Name updated")
    public void shouldUpdateUserData() {
        userRequest.createNewUser(userBody);
        userLoginBodyForDelete = userLoginBody;
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
        ValidatableResponse updateUser = userRequest.updateUser(loginUser.extract().path("accessToken"), updateUserBody);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, updateUser.extract().statusCode());
        userLoginBodyForDelete = updatedUserLoginBody;
        assertEquals("Key value failure!", true, updateUser.extract().path("success"));
        assertEquals("Email is not updated!", updatedEmail.toLowerCase(), updateUser.extract().path("user.email"));
        assertEquals("Name is not updated", updatedName, updateUser.extract().path("user.name"));
    }

    @Test
    @DisplayName("Check update not auth user data ")
    @Description("Create  user account, update it | assert: status code")
    public void shouldUpdateUserDataWithoutAuth() {
        userRequest.createNewUser(userBody);
        userLoginBodyForDelete = userLoginBody;
        ValidatableResponse updateUser = userRequest.updateUser(updateUserBody);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_UNAUTHORIZED, updateUser.extract().statusCode());
    }

    @After
    public void deleteCreatedUser() {
        if (userLoginBodyForDelete != null) {
            ValidatableResponse loginUser = userRequest.loginUser(userLoginBodyForDelete);
            ValidatableResponse deleteUser = userRequest.deleteUser(loginUser.extract().path("accessToken"));
            assertEquals("Status code failure!", HttpURLConnection.HTTP_ACCEPTED, deleteUser.extract().statusCode());
        }
    }
}
