package com.dm.teamquery.config;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ExtendedLogger {

    final public String getRequestString(HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        String requestType = request.getMethod();
        String remoteIP = request.getRemoteAddr();

        return requestType + " request from " + remoteIP + " on " + requestURI;
    }

    final public String getRequestError(HttpServletRequest request, Exception e){
        return getRequestString(request) +  ": Exception encoundered during request: " + e.getMessage();
    }

}
