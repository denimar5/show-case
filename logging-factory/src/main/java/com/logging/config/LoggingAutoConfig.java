package com.logging.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.logging.filter.SpringLoggingFilter;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;

public class LoggingAutoConfig {

	private static final String LOGSTASH_APPENDER_NAME = "LOGSTASH";
	@Value("${spring.logstash.url:localhost:8500}")
	String url;
	@Value("${spring.application.name:-}")
	String name;

	@Bean
	public UUIDGenerator generator() {
		return new UUIDGenerator();
	}

	@Bean
	public SpringLoggingFilter loggingFilter() {
		return new SpringLoggingFilter(generator());
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptorList = new ArrayList<ClientHttpRequestInterceptor>();
		restTemplate.setInterceptors(interceptorList);
		return restTemplate;
	}

	@Bean
	public LogstashTcpSocketAppender logstashAppender() {

		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		LogstashTcpSocketAppender logstashTcpSocketAppender = new LogstashTcpSocketAppender();
		logstashTcpSocketAppender.setName(LOGSTASH_APPENDER_NAME);
		logstashTcpSocketAppender.setContext(loggerContext);
		logstashTcpSocketAppender.addDestination(url);
		LogstashEncoder encoder = new LogstashEncoder(); 
		encoder.setContext(loggerContext);
		encoder.setIncludeContext(true);
		encoder.setCustomFields("{\"appname\":\"" + name + "\"}");
		encoder.start();
		logstashTcpSocketAppender.setEncoder(encoder);
		logstashTcpSocketAppender.start();
		loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(logstashTcpSocketAppender);
		return logstashTcpSocketAppender;

	}

}
