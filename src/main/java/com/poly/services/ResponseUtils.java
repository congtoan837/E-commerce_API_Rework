package com.poly.services;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class ResponseUtils {

    Logger LOGGER = Logger.getLogger(ResponseUtils.class);

    public ResponseEntity<?> getResponseEntity(String code, String mess, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("code",code);
        response.put("messenger",mess);
        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> getResponseEntity(Object data, String code, String mess, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("data",data);
        response.put("code",code);
        response.put("messenger",mess);
        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> getResponseEntity(Object data, String code, String mess, long totalRow, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("data",data);
        response.put("code",code);
        response.put("messenger",mess);
        response.put("totalRow",totalRow);
        return new ResponseEntity<>(response, status);
    }

}
