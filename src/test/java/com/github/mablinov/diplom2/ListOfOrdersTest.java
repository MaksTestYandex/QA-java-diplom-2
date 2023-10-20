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
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ListOfOrdersTest {
    Random random = new Random();
    private String email = "testUser" + random.nextInt(100) + "@yandex.ru";
    private OrderRequest orderRequest;
    private UserRequest userRequest;
    private boolean needDeleteUser = false;
    private final RequestUserBody userBody = new RequestUserBody(email, "1234", "Ivan");
    private final RequestUserLoginBody userLoginBody = RequestUserLoginBody.from(userBody);

    @Before
    public void setUp() {
        orderRequest = new OrderRequest();
        needDeleteUser = false;
        userRequest = new UserRequest();
    }

    @Test
    @DisplayName("Check get list of orders with authorize ")
    @Description("Create  user, login user, create three orders, get list of orders | assert: status code,key value, list size")
    public void shouldGetListOfOrdersWithAuth() {
        userRequest.createNewUser(userBody);
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
        orderRequest.createOrderWithIngredients(loginUser.extract().path("accessToken"));
        orderRequest.createOrderWithIngredients(loginUser.extract().path("accessToken"));
        orderRequest.createOrderWithIngredients(loginUser.extract().path("accessToken"));
        ValidatableResponse getListOfOrders = orderRequest.getListOfOrders(loginUser.extract().path("accessToken"));
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, getListOfOrders.extract().statusCode());
        assertEquals("Key value failure!", true, getListOfOrders.extract().path("success"));
        assertEquals("Incorrect list size!", getListOfOrders.extract().jsonPath().getList("orders").size(), 3);
        needDeleteUser = true;
    }

    @Test
    @DisplayName("Check get list of orders without authorize ")
    @Description("Create  user, login user, create three orders, get list of orders without auth token  | assert: status code,key value")
    public void shouldGetListOfOrdersWithoutAuth() {
        userRequest.createNewUser(userBody);
        ValidatableResponse loginUser = userRequest.loginUser(userLoginBody);
        orderRequest.createOrderWithIngredients(loginUser.extract().path("accessToken"));
        orderRequest.createOrderWithIngredients(loginUser.extract().path("accessToken"));
        orderRequest.createOrderWithIngredients(loginUser.extract().path("accessToken"));
        ValidatableResponse getListOfOrders = orderRequest.getListOfOrders();
        assertEquals("Status code failure!", HttpURLConnection.HTTP_UNAUTHORIZED, getListOfOrders.extract().statusCode());
        assertEquals("Check authorize", "You should be authorised", getListOfOrders.extract().path("message"));
        needDeleteUser = true;
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

