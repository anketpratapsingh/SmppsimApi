package com.comvia.smppsimapi.controller;

import com.comvia.smppsimapi.model.SingleUssdMessage;
import com.comvia.smppsimapi.model.UssdMessage;
import com.comvia.smppsimapi.model.UssdResponse;
import com.comvia.smppsimapi.service.ISimpleMOInjector;
import com.comvia.smppsimapi.utils.USSDStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/smppsim")
public class SmppsimController {

	@Autowired
	private ISimpleMOInjector simpleMOInjector;

	@PostMapping("/ussdMessage")
	public ResponseEntity<UssdResponse> ussdRequest(@RequestBody UssdMessage ussdMessage) {
		log.info("JSON request :: {}", ussdMessage.toString());
		return processRequest(ussdMessage);
	}

	@PostMapping("/singleUssdMessage")
	public ResponseEntity<UssdResponse> singleUssdRequest(@RequestBody SingleUssdMessage singleUssdMessage) {
		log.info("JSON request :: {}", singleUssdMessage.toString());
		return processRequest(singleUssdMessage);
	}

	private ResponseEntity<UssdResponse> processRequest(Object message) {
		
		if (message instanceof UssdMessage) {
			UssdMessage ussdMessage = (UssdMessage) message;
			if (ussdMessage.getMsisdn() == null) {
				return buildErrorResponse(USSDStatusCode.MISSING_REQUIRED_PARAMETER);
			} else if (ussdMessage.getMsisdn().length() != 12) {
				return buildErrorResponse(USSDStatusCode.INVALID_INPUT);
			}
			UssdResponse ussdResponse = simpleMOInjector.processUssdMessage(ussdMessage);
			return ResponseEntity.status(HttpStatus.OK).body(ussdResponse);
		} else if (message instanceof SingleUssdMessage) {
			SingleUssdMessage singleUssdMessage = (SingleUssdMessage) message;
			if (singleUssdMessage.getMsisdn() == null || singleUssdMessage.getShortMessage() == null) {
				return buildErrorResponse(USSDStatusCode.MISSING_REQUIRED_PARAMETERS);
			} else if (singleUssdMessage.getMsisdn().length() != 12) {
				return buildErrorResponse(USSDStatusCode.INVALID_INPUT);
			}
			UssdResponse ussdResponse = simpleMOInjector.processSingleUssdMessage(singleUssdMessage);
			return ResponseEntity.status(HttpStatus.OK).body(ussdResponse);
		}
		return buildErrorResponse(USSDStatusCode.INVALID_INPUT);
	}

	private ResponseEntity<UssdResponse> buildErrorResponse(USSDStatusCode statusCode) {
		UssdResponse errorResponse = new UssdResponse(statusCode.getStatusCode(), statusCode.getStatusMessage(),
				statusCode.getDescription());
		log.warn("Invalid Request");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
}
