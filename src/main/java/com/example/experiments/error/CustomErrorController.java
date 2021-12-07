package com.example.experiments.error;

import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

// NOTE: Spring boot maps the servlet error page to /error, and provides a BasicErrorController there
//  ErrorController provides custom whitelabel error page
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        // create custom error
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());

        // set custom errors and messages
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            errors.setStatus(statusCode);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errors.setError(HttpStatus.NOT_FOUND.toString());
                errors.setMessage("The following request could not be found.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errors.setError(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                errors.setMessage("An internal error occurred.");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errors.setError(HttpStatus.FORBIDDEN.toString());
                errors.setMessage("The following request is forbidden.");
            }
        }

        // display error.html page
        model.addAttribute("errors", errors);
        if(path != null) model.addAttribute("path", path);
        return "error";
    }
}
