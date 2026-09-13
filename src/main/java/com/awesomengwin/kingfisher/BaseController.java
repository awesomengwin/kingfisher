package com.awesomengwin.kingfisher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class BaseController {

    @ModelAttribute("currentPage")
    public String currentPage(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
