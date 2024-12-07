package com.github.vvsslova.libraryrest.services;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class MessageService {
    public String getBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            msg.append(error.getField()).append("-").append(error.getDefaultMessage()).append(";");
        }
        return msg.toString();
    }
}
