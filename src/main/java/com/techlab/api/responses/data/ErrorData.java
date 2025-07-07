package com.techlab.api.responses.data;

import java.util.HashMap;

public class ErrorData<T> {
    private HashMap<String, TokenizedData<T>> errors = new HashMap<>();

    public ErrorData() {}

    public ErrorData(HashMap<String, TokenizedData<T>> errors) {
        this.errors = errors;
    }

    public ErrorData<T> addError(String key, TokenizedData<T> error) {
        errors.put(key, error);
        return this;
    }

    public HashMap<String, TokenizedData<T>> getErrors() {
        return this.errors;
    }

    public void setErrors(HashMap<String, TokenizedData<T>> errors) {
        this.errors = errors;
    }
}
