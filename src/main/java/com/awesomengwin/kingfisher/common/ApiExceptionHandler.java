package com.awesomengwin.kingfisher.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiClientException.class)
    public ModelAndView handleApiClientException(ApiClientException e) {
        ModelAndView modelAndView = new ModelAndView("fragments/toast", e.getStatusCode());

        String title = "%s - %d %s".formatted(e.getGroup(), e.getStatusCode().value(), e.getStatusText());
        modelAndView.addObject("title", title);
        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(ApiClientNotFoundException.class)
    public ModelAndView handleApiClientNotFoundException(ApiClientNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("fragments/toast", e.getStatusCode());

        String title = "%s - %d %s".formatted(e.getGroup(), e.getStatusCode().value(), e.getStatusText());
        modelAndView.addObject("title", title);
        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(ApiServerException.class)
    public ModelAndView handleApiServerException(ApiServerException e) {
        ModelAndView modelAndView = new ModelAndView("fragments/toast", e.getStatusCode());

        String title = "%s - %d %s".formatted(e.getGroup(), e.getStatusCode().value(), e.getStatusText());
        modelAndView.addObject("title", title);
        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("fragments/toast", HttpStatus.INTERNAL_SERVER_ERROR);

        modelAndView.addObject("title", "Server - 500 Internal Server Error");
        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }
}
