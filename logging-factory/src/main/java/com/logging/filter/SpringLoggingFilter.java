package com.logging.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.logging.interceptor.RequestWrapper;
import com.logging.interceptor.ResponseWrapper;

public class SpringLoggingFilter extends OncePerRequestFilter {
	 
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLoggingFilter.class);
    private UUIDGenerator generator;
    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
  
    public SpringLoggingFilter(UUIDGenerator generator) {
        this.generator = generator;
    }
 
    private String generateUniqueCorrelationId() {
        return UUID.randomUUID().toString();
    }
    
    private String getCorrelationIdFromHeader(final HttpServletRequest request) {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER_NAME);
        if ((correlationId == null)|| (correlationId.length()<1)) {
            correlationId = generateUniqueCorrelationId();
        }
        return correlationId;
    }
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        generator.generateId(request);
        final long startTime = System.currentTimeMillis();
        final RequestWrapper wrappedRequest = new RequestWrapper(request);
        String correlationID = getCorrelationIdFromHeader(request);
        LOGGER.info("Request: X-Correlation-ID= {}, method={}, uri={}, payload={}",new String(correlationID)  ,  wrappedRequest.getMethod(), wrappedRequest.getRequestURI(), IOUtils.toString(wrappedRequest.getInputStream(), wrappedRequest.getCharacterEncoding()));
        final ResponseWrapper wrappedResponse = new ResponseWrapper(response);     
        chain.doFilter(wrappedRequest, wrappedResponse);
        final long duration = System.currentTimeMillis() - startTime;    
        LOGGER.info("Response:X-Correlation-ID= {}, ({} ms):, status={}, payload={}",  new String(correlationID), new String("X-Response-Time : "+ duration), new String("X-Response-Status : "+ wrappedResponse.getStatus()),
        		
        IOUtils.toString(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding()));
    }
}
