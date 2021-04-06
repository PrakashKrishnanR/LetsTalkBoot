package com.app.expd.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(403);
        Map<String, Object> data = new HashMap<>();
        data.put(
                "timestamp",
                Instant.now());
        data.put(
                "message",
                e.getMessage());

        httpServletResponse.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
    }
}
