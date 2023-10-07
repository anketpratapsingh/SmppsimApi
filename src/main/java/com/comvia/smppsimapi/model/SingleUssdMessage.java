package com.comvia.smppsimapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SingleUssdMessage {
	
	private String sourceAddress;
	private String shortMessage;
}
