package com.anket.smppsimapi.service;

import com.anket.smppsimapi.model.SingleUssdMessage;
import com.anket.smppsimapi.model.UssdMessage;
import com.anket.smppsimapi.model.UssdResponse;

public interface  ISimpleMOInjector {

	UssdResponse processUssdMessage(UssdMessage ussdMessage);
	UssdResponse processSingleUssdMessage(SingleUssdMessage singleUssdMessage);	
	
}
