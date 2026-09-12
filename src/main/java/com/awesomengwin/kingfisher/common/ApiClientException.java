package com.awesomengwin.kingfisher.common;

import org.springframework.http.HttpStatusCode;

public class ApiClientException extends RuntimeException {
    private final String group;
    private final HttpStatusCode statusCode;
    private final String statusText;

    public ApiClientException(String group, HttpStatusCode statusCode, String statusText, String message) {
        super(message);
        this.group = group;
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public String getGroup() {
        return group;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }
}
