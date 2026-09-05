package com.awesomengwin.kingfisher.common;

import org.springframework.http.HttpStatusCode;

public class ApiServerException extends RuntimeException {
    private final String group;
    private final HttpStatusCode statusCode;

    public ApiServerException(String group, HttpStatusCode statusCode, String message) {
        super(message);
        this.group = group;
        this.statusCode = statusCode;
    }

    public String getGroup() {
        return group;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
