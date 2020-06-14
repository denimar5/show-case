package com.logging.interceptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class RequestWrapper extends HttpServletRequestWrapper {
		
	 private byte[] body;
	 
	    public RequestWrapper(HttpServletRequest request) {
	        super(request);
	        try {
	            body = IOUtils.toByteArray(request.getInputStream());
	        } catch (IOException ex) {
	            body = new byte[0];
	        }
	    }
	 
	    @Override
	    public ServletInputStream getInputStream() throws IOException {
	        return new ServletInputStream() {
	            public boolean isFinished() {
	                return false;
	            }
	 
	            public boolean isReady() {
	                return true;
	            }
	 
	            public void setReadListener(ReadListener readListener) {
	 
	            }
	 
	            ByteArrayInputStream byteArray = new ByteArrayInputStream(body);
	 
	            @Override
	            public int read() throws IOException {
	                return byteArray.read();
	            }
	        };
	    }

}
