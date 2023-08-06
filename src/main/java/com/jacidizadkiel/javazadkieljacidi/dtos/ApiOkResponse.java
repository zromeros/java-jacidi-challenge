package com.jacidizadkiel.javazadkieljacidi.dtos;

public class ApiOkResponse<T> {
    
    private String message;
    private int httpCode;
    private T data;

    public ApiOkResponse(String message, int httpCode, T data) {
        this.message = message;
        this.httpCode = httpCode;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }
    public int getHttpCode() {
        return httpCode;
    }
    public T getData() {
        return data;
    }
}
