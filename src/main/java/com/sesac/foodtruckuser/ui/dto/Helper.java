package com.sesac.foodtruckuser.ui.dto;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.LinkedHashMap;
import java.util.LinkedList;

@Service
public class Helper {
    public static LinkedList<LinkedHashMap<String, String>> refineErrors(BindingResult results) {
        LinkedList errorList = new LinkedList<LinkedHashMap<String, String>>();
        results.getFieldErrors().forEach(r -> {
            LinkedHashMap<String, String> result = new LinkedHashMap<>();
            result.put("field", r.getField());
            result.put("message", r.getDefaultMessage());
            errorList.push(result);
        });
        return errorList;
    }
}
