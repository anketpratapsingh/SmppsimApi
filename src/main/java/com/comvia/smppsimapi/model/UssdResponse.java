package com.comvia.smppsimapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UssdResponse {

	private int statusCode;
    private String statusMessage;
    private String statusDescription;

}
