package com.comvia.smppsimapi.entitie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UssdMessage {
 
	private String sourceAddress;
	private String userMessageReference;
	private String selectOfferType;
	private String selectOffe;
	private String paymentMode;
	private String currencyType;

}
