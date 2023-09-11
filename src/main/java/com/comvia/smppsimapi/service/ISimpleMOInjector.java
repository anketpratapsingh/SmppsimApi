package com.comvia.smppsimapi.service;

import com.comvia.smppsimapi.entitie.UssdMessage;

public interface  ISimpleMOInjector {

	void processUssdMessage(UssdMessage ussdMessage);
	
}
