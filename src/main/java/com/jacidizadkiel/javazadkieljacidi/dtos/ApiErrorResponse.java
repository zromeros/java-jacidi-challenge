package com.jacidizadkiel.javazadkieljacidi.dtos;

public class ApiErrorResponse {

    private String message;
    private int httpCode;
    private ErrorDetail error;

    public ApiErrorResponse(String message, int httpCode, ErrorDetail error) {
        this.message = message;
        this.httpCode = httpCode;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public ErrorDetail getError() {
        return error;
    }

    public void setError(ErrorDetail error) {
        this.error = error;
    }

    public static class ErrorDetail {

        private String description;
        private String id;
        private String extra;

        public ErrorDetail(String description, String id, String extra) {
            this.description = description;
            this.id = id;
            this.extra = extra;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }
    }
}