package com.awesomengwin.kingfisher.common;

public class ApiClientNotFoundException extends RuntimeException {
    private final String group;

    public ApiClientNotFoundException(String group, String message) {
        super(message);
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
}
