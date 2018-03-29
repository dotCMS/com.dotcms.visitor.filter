package com.dotcms.osgi.logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface VisitorLogger {

    void log(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException;

    public default VisitorLogger getVisitorLoggerImpl() {
        return new VisitorLoggerImpl();
    }
}
