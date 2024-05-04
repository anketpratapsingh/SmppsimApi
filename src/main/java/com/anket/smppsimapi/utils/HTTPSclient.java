package com.anket.smppsimapi.utils;

import static com.anket.smppsimapi.utils.USSDConstants.GET;
import static com.anket.smppsimapi.utils.USSDConstants.HTTPS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anket.smppsimapi.config.SmppsimApiConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HTTPSclient {

	@Autowired
	private SmppsimApiConfig config;
	
	public synchronized void sendSingleSMS(String urlString) throws Exception {
		HttpURLConnection connection = null;
	    try {
	        if(config.getDisplayEndPoint() == 1)
	            log.info("End Point :- " + urlString);
	        StringBuilder result = new StringBuilder();
	        URL url = new URL(urlString);
			String strProtocol = url.getProtocol();
			if (HTTPS.equals(strProtocol)) {
				connection = (HttpsURLConnection) url.openConnection();
			} else {
				connection = (HttpURLConnection) url.openConnection();
			}
			
	        // Set connection and read timeout (e.g., 30 seconds)
	        connection.setConnectTimeout(config.getConnTimeoutMillis());
	        connection.setReadTimeout(config.getReadTimeoutMillis());
	        
	        connection.setRequestMethod(GET);
	        int responseCode = connection.getResponseCode();
	        String responseMessage = connection.getResponseMessage();
	        log.info("Response Code: " + responseCode + " Response Message: " + responseMessage);
	        
	        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line;
	        while ((line = rd.readLine()) != null) {
	            result.append(line);
	        }
	        rd.close();
	        log.debug("Response :- " + result);
	        log.debug("USSD request sent Successfully");
	    } catch (Exception e) {
	        // Handle other exceptions
	        log.error("Failed to send USSD", e);
	        throw e;
	    }
	}

}
