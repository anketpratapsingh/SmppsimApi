package com.comvia.smppsimapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comvia.smppsimapi.entitie.UssdMessage;
import com.comvia.smppsimapi.service.ISimpleMOInjector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/smppsim/ussd")
public class SmppsimController {

	@Autowired
	private ISimpleMOInjector simpleMOInjector;
	
	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody UssdMessage ussdMessage) {
		log.info("JSON request :: " + ussdMessage.toString());
		if(isValidSelection(ussdMessage.getUserMessageReference())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session Id required of every request.");
		}else if (isValidSelection(ussdMessage.getSourceAddress()) ||  ussdMessage.getSourceAddress().length() != 12) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please enter valid MSISDN");
		}
		simpleMOInjector.processUssdMessage(ussdMessage);
		return ResponseEntity.status(HttpStatus.OK).body("SMS sent Successfully");
	}
	
	private boolean isValidSelection(String selection) {
	    return null == selection || selection.isEmpty();
	}
}
