package com.awesomengwin.kingfisher.common;

import org.springframework.http.HttpStatus;

public class ApiClientNotFoundException extends ApiClientException {

    public ApiClientNotFoundException(String group, String message) {
        super(group, HttpStatus.NOT_FOUND, message);
    }
}
