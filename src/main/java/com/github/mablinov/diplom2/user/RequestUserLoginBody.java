package com.github.mablinov.diplom2.user;

public class RequestUserLoginBody {
    private String email;
    private String password;

    public RequestUserLoginBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public RequestUserLoginBody() {
    }
    public static RequestUserLoginBody from(RequestUserBody userBody) {
        return new RequestUserLoginBody(userBody.getEmail(), userBody.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
