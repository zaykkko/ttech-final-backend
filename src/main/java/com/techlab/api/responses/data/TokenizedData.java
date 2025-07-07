package com.techlab.api.responses.data;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TokenizedData<T> {
    private String token;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public TokenizedData(String token) {
        this.token = token;
    }

    public TokenizedData(String token, T data) {
        this.token = token;
        this.data = data;
    }

    public String getToken() {
        return this.token;
    }

    public Object getData() {
        return this.data;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setData(T data) {
        this.data = data;
    }
}
