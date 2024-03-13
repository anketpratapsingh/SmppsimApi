package com.comvia.smppsimapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ComponentScan(basePackages = "com.comvia.smppsimapi.*")
@PropertySource("file:${user.dir}/config/SmppsimApi.properties")
@Getter
@Setter
@ToString
public class SmppsimApiConfig {

	@Value("${smppsim.ip.address}")
	private String ipAddress;

	@Value("${smppsim.port}")
	private int port;

	@Value("${smppsim.destination.address}")
	private String destinationAddress;
	
	@Value("${ussd.short.code}")
	private String shortCode;
	
	@Value("${ussd.rendom.range}")
	private int rendomRange;
	
	@Value("${ussd.log.display.end.point.flag}")
	private int displayEndPoint;
	
	@Value("${ussd.connection.timeoutMillis}")
    private int connTimeoutMillis ;
    
    @Value("${ussd.read.timeoutMillis}")
    private int readTimeoutMillis ;


}
