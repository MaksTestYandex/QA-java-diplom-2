package com.github.mablinov.diplom2;

import com.github.mablinov.diplom2.orders.OrderRequest;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateOrderTest {
    private final Random random = new Random();
    private final String email = "testUser" + random.nextInt(100) + "@yandex.ru";
    private OrderRequest orderRequest;
    private UserRequest userRequest;
    private boolean needDeleteUser = false;
    private final RequestUserBody userBody = new RequestUserBody(email, "1234", "Ivan");
    private final RequestUserLoginBody userLoginBody = RequestUserLoginBody.from(userBody);
    private final String jsonIngredients = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa72\",\"61c0c5a71d1f82001bdaaa6f\",\"61c0c5a71d1f82001bdaaa75\",\"61c0c5a71d1f82001bdaaa6d\"]}";
    private final String jsonIngredientsWithMistakeInFirstIngredient = "{\"ingredients\": [\"61c0c5a71d1f82001bdaab72\",\"61c0c5a71d1f82001bdaaa6f\",\"61c0c5a71d1f82001bdaaa75\",\"61c0c5a71d1f82001bdaaa6d\"]}";
    private final String jsonIngredientsWithMistakeInAllIngredients = "{\"ingredients\": [\"61c0c5a71d1f82001bdaab2\",\"61c0c5a71d1f82001bdaadf\",\"61c0c5a71d1f82001bdaad5\",\"61c0c5a71d1f82001bdaadd\"]}";

    @Before
    public void setUp() {
        orderRequest = new OrderRequest();
        needDeleteUser = false;
        userRequest = new UserRequest();
    }

    @Test
    @DisplayName("Check that order is created with authorize")
    @Description("Create user, login user, create order | assert: status code, success value, list size")
    public void shouldCreateNewOrderWithAuth() {
        userRequest.createNewUser(userBody);
        needDeleteUser = true;
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
        ValidatableResponse createNewOrder = orderRequest.createOrderWithIngredients(loginUser.extract().path("accessToken"), jsonIngredients);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, createNewOrder.extract().statusCode());
        assertEquals("Success value failure!", true, createNewOrder.extract().path("success"));
        assertEquals("Incorrect list size!", createNewOrder.extract().jsonPath().getList("order.ingredients").size(), 4);
    }

    @Test
    @DisplayName("Check that order is created without authorize")
    @Description("Create  order | assert: status code, success value, order number")
    public void shouldCreateNewOrderWithoutAuth() {
        ValidatableResponse createNewOrder = orderRequest.createOrderWithIngredients(jsonIngredients);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, createNewOrder.extract().statusCode());
        assertEquals("Success value failure!", true, createNewOrder.extract().path("success"));
        assertNotNull("Number is null", createNewOrder.extract().path("order.number"));
    }

    @Test
    @DisplayName("Check that order is not created without ingredients")
    @Description("Create user, login user, create order | assert: status code")
    public void shouldCreateNewOrderWithAuthWithoutIngredients() {
        userRequest.createNewUser(userBody);
        needDeleteUser = true;
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
        ValidatableResponse createNewOrder = orderRequest.createOrderWithoutIngredients(loginUser.extract().path("accessToken"));
        assertEquals("Status code failure!", HttpURLConnection.HTTP_BAD_REQUEST, createNewOrder.extract().statusCode());
    }

    @Test
    @DisplayName("Check that order is created with hashCode mistake of ingredient")
    @Description("Create user, login user, create order  | assert: status code, list size")
    public void shouldCreateNewOrderWithHashCodeMistakeOneIngredient() {
        userRequest.createNewUser(userBody);
        needDeleteUser = true;
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
        ValidatableResponse createNewOrder = orderRequest.createOrderWithHashCodeMistakeIngredients(loginUser.extract().path("accessToken"), jsonIngredientsWithMistakeInFirstIngredient);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, createNewOrder.extract().statusCode());
        assertEquals("Incorrect list size!", createNewOrder.extract().jsonPath().getList("order.ingredients").size(), 3);
    }

    @Test
    @DisplayName("Check that order is created with hashCode mistake of ingredient")
    @Description("Create user, login user, create order  | assert: status code")
    public void shouldCreateNewOrderWithHashCodeMistakesAllIngredients() {
        userRequest.createNewUser(userBody);
        needDeleteUser = true;
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
        ValidatableResponse createNewOrder = orderRequest.createOrderWithHashCodeMistakeIngredients(loginUser.extract().path("accessToken"), jsonIngredientsWithMistakeInAllIngredients);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_INTERNAL_ERROR, createNewOrder.extract().statusCode());
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
