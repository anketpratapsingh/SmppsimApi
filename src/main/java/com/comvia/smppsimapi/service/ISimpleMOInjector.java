package com.comvia.smppsimapi.service;

import com.comvia.smppsimapi.model.SingleUssdMessage;
import com.comvia.smppsimapi.model.UssdMessage;
import com.comvia.smppsimapi.model.UssdResponse;

public interface  ISimpleMOInjector {

	UssdResponse processUssdMessage(UssdMessage ussdMessage);
	UssdResponse processSingleUssdMessage(SingleUssdMessage singleUssdMessage);	
	
}
