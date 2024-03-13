package com.comvia.smppsimapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SingleUssdMessage {
	
	private String msisdn;
	private String shortMessage;
}
