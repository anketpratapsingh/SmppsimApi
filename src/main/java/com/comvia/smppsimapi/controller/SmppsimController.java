package com.comvia.smppsimapi.controller;

import static com.comvia.smppsimapi.utils.USSDStatusCode.INVALID_INPUT;
import static com.comvia.smppsimapi.utils.USSDStatusCode.MISSING_REQUIRED_PARAMETER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.comvia.smppsimapi.model.SingleUssdMessage;
import com.comvia.smppsimapi.model.UssdMessage;
import com.comvia.smppsimapi.model.UssdResponse;
import com.comvia.smppsimapi.service.ISimpleMOInjector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/smppsim")
public class SmppsimController {

	@Autowired
	private ISimpleMOInjector simpleMOInjector;

	@PostMapping("/ussdMessage")
	public ResponseEntity<UssdResponse> ussdRequest(@RequestBody UssdMessage ussdMessage) {
		log.info("JSON request :: " + ussdMessage.toString());
		if (ussdMessage.getSourceAddress().length() != 12) {
			UssdResponse errorResponse;
			if (isValidSelection(ussdMessage.getSourceAddress())) {
				errorResponse = new UssdResponse(MISSING_REQUIRED_PARAMETER.getStatusCode(),
						MISSING_REQUIRED_PARAMETER.getStatusMessage(), MISSING_REQUIRED_PARAMETER.getDescription());
			} else {
				errorResponse = new UssdResponse(INVALID_INPUT.getStatusCode(), INVALID_INPUT.getStatusMessage(),
						INVALID_INPUT.getDescription());
			}
			return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
		}
		UssdResponse ussdResponse = simpleMOInjector.processUssdMessage(ussdMessage);
		return ResponseEntity.status(HttpStatus.OK).body(ussdResponse);
	}

	@PostMapping("/singleUssdMessage")
	public ResponseEntity<UssdResponse> singleUssdRequest(@RequestBody SingleUssdMessage singleUssdMessage) {

		log.info("JSON request :: " + singleUssdMessage.toString());
		if (singleUssdMessage.getSourceAddress().length() != 12) {
			UssdResponse errorResponse;
			if (isValidSelection(singleUssdMessage.getSourceAddress())) {
				errorResponse = new UssdResponse(MISSING_REQUIRED_PARAMETER.getStatusCode(),
						MISSING_REQUIRED_PARAMETER.getStatusMessage(), MISSING_REQUIRED_PARAMETER.getDescription());
			} else {
				errorResponse = new UssdResponse(INVALID_INPUT.getStatusCode(), INVALID_INPUT.getStatusMessage(),
						INVALID_INPUT.getDescription());
			}
			return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
		}
		UssdResponse ussdResponse = simpleMOInjector.processSingleUssdMessage(singleUssdMessage);
		return ResponseEntity.status(HttpStatus.OK).body(ussdResponse);
	}

	private boolean isValidSelection(String selection) {
		return null == selection || selection.isEmpty();
	}
}
