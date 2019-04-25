package com.dm.scifi.config;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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
