package com.comvia.smppsimapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class UssdMessage {
 
	private String sourceAddress;
	private String selectOfferType;
	private String selectOffe;
	private String paymentMode;
	private String currencyType;

}
