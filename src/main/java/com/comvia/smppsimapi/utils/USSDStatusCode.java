package com.comvia.smppsimapi.utils;

import java.util.HashMap;
import java.util.Map;

public enum USSDStatusCode {

    // Success codes and messages
    SUCCESS(200, "Success", "USSD request sent Successfully"),

    // Server error codes and messages
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Server Unavailable", "SMPP Server Unavailable"),

    // Additional error codes for input validation and missing parameters
    INVALID_INPUT(400, "Invalid input data", "Please enter valid MSISDN"),
    MISSING_REQUIRED_PARAMETER(401, "Required parameter is missing", "MSISDN is missing");

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
