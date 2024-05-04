package com.anket.smppsimapi.utils;

import static com.anket.smppsimapi.utils.USSDStatusCode.GATEWAY_TIMEOUT;
import static com.anket.smppsimapi.utils.USSDStatusCode.GENERAL_FAILURE;
import static com.anket.smppsimapi.utils.USSDStatusCode.INVALID_INPUT;
import static com.anket.smppsimapi.utils.USSDStatusCode.SERVICE_UNAVAILABLE;
import static com.anket.smppsimapi.utils.USSDStatusCode.UNKNOWN_HOST;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

import com.anket.gRPC.Ussd.USSDResponse;
import com.anket.gRPC.Ussd.USSDResponse.Builder;
import com.anket.smppsimapi.config.SmppsimApiConfig;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Utils {

	public static boolean isValidSelection(String selection) {
		return selection != null && !selection.isEmpty();
	}

	public static boolean isValidMsisdn(String msisdn) {
		return msisdn != null && !msisdn.isEmpty() && msisdn.length() == 12;
	}

	public static String paramToUrlString(String shortMessage, String sourceAddress, int userMessageReference,
			String optionalTLV1Val, SmppsimApiConfig config) {
		String result = "http://" + config.getIpAddress() + ":" + config.getPort() + "/inject_mo?";

		result = result + "short_message=" + shortMessage.replace("#", "%23");
		result = result + "&source_addr=" + sourceAddress;
		result = result + "&destination_addr=" + config.getDestinationAddress();
		result = result
				+ "&submit=Submit+Message&service_type=&source_addr_ton=1&source_addr_npi=1&dest_addr_ton=1&dest_addr_npi=1&esm_class=0&protocol_ID=&priority_flag=&registered_delivery_flag=0&data_coding=0";
		result = result + "&user_message_reference=" + userMessageReference;
		result = result
				+ "&source_port=&destination_port=&sar_msg_ref_num=&sar_total_segments=&sar_segment_seqnum=&user_response_code=&privacy_indicator=&payload_type=&message_payload=&callback_num=&source_subaddress=&dest_subaddress=&language_indicator=&tlv1_tag=1281&tlv1_len=1";
		result = result + "&tlv1_val=" + optionalTLV1Val;
		result = result
				+ "&tlv2_tag=5376&tlv2_len=30&tlv2_val=1500&tlv3_tag=5632&tlv3_len=15&tlv3_val=CF1D&tlv4_tag=&tlv4_len=&tlv4_val=&tlv5_tag=&tlv5_len=&tlv5_val=&tlv6_tag=&tlv6_len=&tlv6_val=&tlv7_tag=&tlv7_len=&tlv7_val=";

		return result;
	}

	public static void handleInvalidMsisdn(StreamObserver<USSDResponse> responseObserver) {
		log.error("MSISDN not valid, closing streaming");
		Status status = Status.INVALID_ARGUMENT.withDescription("MSISDN not valid")
				.augmentDescription("errorCode: " + INVALID_INPUT.getStatusCode())
				.augmentDescription("errorMessage: " + INVALID_INPUT.getStatusMessage())
				.augmentDescription("errorDescription: " + INVALID_INPUT.getDescription());
		responseObserver.onError(status.asRuntimeException());
	}

	public static USSDStatusCode getStatusForException(Exception e) {
		if (e instanceof SocketTimeoutException) {
			return GATEWAY_TIMEOUT;
		} else if (e instanceof ConnectException) {
			return SERVICE_UNAVAILABLE;
		} else if (e instanceof UnknownHostException) {
			return UNKNOWN_HOST;
		} else {
			return GENERAL_FAILURE;
		}
	}

	public void sleep(long milliseconds) throws InterruptedException {
		Thread.sleep(milliseconds);
	}

}
