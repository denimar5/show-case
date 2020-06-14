package com.logging.interceptor;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper {

	private ServletOutputStream outputStream;
	private PrintWriter writer;
	private ServletOutputStreamWrapper copier;

	public ResponseWrapper(HttpServletResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
	}
	
	 @Override
	    public ServletOutputStream getOutputStream() throws IOException {
	        if (writer != null) {
	            throw new IllegalStateException("getWriter() metodo já executou response.");
	        }
	 
	        if (outputStream == null) {
	            outputStream = getResponse().getOutputStream();
	            copier = new ServletOutputStreamWrapper(outputStream);
	        }
	 
	        return copier;
	    }
	 
	    @Override
	    public PrintWriter getWriter() throws IOException {
	        if (outputStream != null) {
	            throw new IllegalStateException("getOutputStream() metodo já executou response.");
	        }
	 
	        if (writer == null) {
	            copier = new ServletOutputStreamWrapper(getResponse().getOutputStream());
	            writer = new PrintWriter(new OutputStreamWriter(copier, getResponse().getCharacterEncoding()), true);
	        }
	 
	        return writer;
	    }
	 
	    @Override
	    public void flushBuffer() throws IOException {
	        if (writer != null) {
	            writer.flush();
	        }
	        else if (outputStream != null) {
	            copier.flush();
	        }
	    }
	 
	    public byte[] getContentAsByteArray() {
	        if (copier != null) {
	            return copier.getCopy();
	        }
	        else {
	            return new byte[0];
	        }
	    }

}
