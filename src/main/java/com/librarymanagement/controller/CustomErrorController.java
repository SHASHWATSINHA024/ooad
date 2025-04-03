package com.librarymanagement.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        Object path = request.getAttribute("javax.servlet.error.request_uri");
        Object exception = request.getAttribute("javax.servlet.error.exception");
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", "error");
        errorDetails.put("code", status != null ? status : 500);
        errorDetails.put("path", path != null ? path : request.getRequestURI());
        
        if (status != null && status.equals(404)) {
            errorDetails.put("message", "The requested resource was not found");
        } else if (status != null && status.equals(403)) {
            errorDetails.put("message", "Access to this resource is forbidden");
        } else if (status != null && status.equals(401)) {
            errorDetails.put("message", "Authentication is required to access this resource");
        } else {
            errorDetails.put("message", "An unexpected error occurred");
        }
        
        if (exception != null) {
            errorDetails.put("exception", exception.toString());
        }
        
        HttpStatus httpStatus = status != null ? 
                HttpStatus.valueOf(Integer.parseInt(status.toString())) : 
                HttpStatus.INTERNAL_SERVER_ERROR;
        
        return new ResponseEntity<>(errorDetails, httpStatus);
    }
} 