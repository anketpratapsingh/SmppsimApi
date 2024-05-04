package com.anket.smppsimapi.utils;

import java.util.HashMap;
import java.util.Map;

public enum USSDStatusCode {

	// Success codes and messages
    SUCCESS(200, "Success", "USSD request sent Successfully"),

    // Server error codes and messages
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Server Unavailable", "SMPP Server Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout", "Gateway Timeout"),

    // Additional error codes for input validation and missing parameters
    INVALID_INPUT(1001, "Invalid MSISDN", "MSISDN must be a non-empty string of length 12"),
    MISSING_REQUIRED_PARAMETER(1002, "Required parameter is missing", "MSISDN is missing"),
    MISSING_REQUIRED_PARAMETERS(1003, "Required parameter is missing", "MSISDN or Short Message is missing"),
    INVALID_SHORT_MESSAGE(1004, "Invalid short message", "Short message must be a non-empty string"),
    MSISDN_MISMATCH(1005, "MSISDN mismatch", "Received MSISDN does not match the expected MSISDN"),

    // New error code for general failure
    GENERAL_FAILURE(1006, "General failure", "An unknown error occurred while processing the request"),
	UNKNOWN_HOST(1007, "Unknown Host", "Invalid hostname or DNS resolution failure");
	
    private static final Map<Integer, String> statusCodeMap = initializeStatusCodeMap();

    private int statusCode;
    private String statusMessage;
    private String description;

    USSDStatusCode(int statusCode, String statusMessage, String description) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.description = description;
    }

    private static Map<Integer, String> initializeStatusCodeMap() {
        Map<Integer, String> map = new HashMap<>();
        for (USSDStatusCode status : USSDStatusCode.values()) {
            map.put(status.statusCode, status.statusMessage);
        }
        return map;
    }

    public static String getStatusMessage(int statusCode) {
        return statusCodeMap.getOrDefault(statusCode, "Unknown status");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getDescription() {
        return description;
    }
}
