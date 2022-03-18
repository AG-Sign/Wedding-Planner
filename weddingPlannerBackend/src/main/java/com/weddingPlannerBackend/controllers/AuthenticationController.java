package com.weddingPlannerBackend.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import com.weddingPlannerBackend.services.AuthenticationService;

@CrossOrigin
public class AuthenticationController {

    @Component
    public static class MyHandlerInterceptor implements HandlerInterceptor {

        @Autowired
        private AuthenticationService authenticationService;

        private String getDecodedToken(String encodedToken) {
            return encodedToken.split(" ")[1];
        }

        
        @Override
        public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler)
                throws InvalidTokenException {
        	System.out.println("HEREE");
       
        	
            String encodedToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = getDecodedToken(encodedToken);
            if (!authenticationService.isAuthenticated(token)) throw new InvalidTokenException();
            return true;
        }

    }

    public static class InvalidTokenException extends RuntimeException {

    }

    @ControllerAdvice
    public static class MyExceptionHandler {

        @ExceptionHandler(InvalidTokenException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        @ResponseBody
        public Map<String, Object> handler() {
            Map<String, Object> m1 = new HashMap<>();
            m1.put("message", "Invalid token");
            return m1;
        }
    }
}
