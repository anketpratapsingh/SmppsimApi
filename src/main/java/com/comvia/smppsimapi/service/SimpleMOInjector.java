package com.comvia.smppsimapi.service;

import static com.comvia.smppsimapi.utils.USSDStatusCode.*;
import java.security.SecureRandom;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.comvia.smppsimapi.config.SmppsimApiConfig;
import com.comvia.smppsimapi.model.SingleUssdMessage;
import com.comvia.smppsimapi.model.UssdMessage;
import com.comvia.smppsimapi.model.UssdResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SimpleMOInjector implements ISimpleMOInjector {

	@Autowired
	private SmppsimApiConfig config;

	private static int userMessageReference;
	private static SecureRandom random = new SecureRandom();

	@Override
	public UssdResponse processUssdMessage(UssdMessage ussdMessage) {
		UssdResponse ussdResponse;
		try {
			String urlString = "";
			int userMessageReference = random.nextInt(config.getRendomRange());

			urlString = paramToUrlString(config.getShortCode().replace("#", "%23"), ussdMessage.getSourceAddress(),
					userMessageReference, "0001");
			sendSingleSMS(urlString);
			log.info("Main menu USSD request sent Successfully");
			if (isValidSelection(ussdMessage.getSelectOfferType())) {
				urlString = paramToUrlString(ussdMessage.getSelectOfferType(), ussdMessage.getSourceAddress(),
						userMessageReference, "0012");
				sleep(5000);
				sendSingleSMS(urlString);
				log.info("Select offer type USSD request sent Successfully");
				if (isValidSelection(ussdMessage.getSelectOffe())) {
					urlString = paramToUrlString(ussdMessage.getSelectOffe(), ussdMessage.getSourceAddress(),
							userMessageReference, "0012");
					sleep(10000);
					sendSingleSMS(urlString);
					log.info("Offer selection USSD request sent Successfully");
					if (isValidSelection(ussdMessage.getPaymentMode())) {
						urlString = paramToUrlString(ussdMessage.getPaymentMode(), ussdMessage.getSourceAddress(),
								userMessageReference, "0012");
						sleep(5000);
						sendSingleSMS(urlString);
						log.info("Payment mode selection USSD request sent Successfully");
						if (isValidSelection(ussdMessage.getCurrencyType())) {
							urlString = paramToUrlString(ussdMessage.getCurrencyType(), ussdMessage.getSourceAddress(),
									userMessageReference, "0012");
							sleep(5000);
							sendSingleSMS(urlString);
							log.info("Currency type selection USSD request sent Successfully");
						}
					}
				}
			}
			ussdResponse = new UssdResponse(SUCCESS.getStatusCode(), SUCCESS.getStatusMessage(),
					SUCCESS.getDescription());
		} catch (InterruptedException e) {
			log.error("Interrupted Exception : ", e);
			Thread.currentThread().interrupt();
			ussdResponse = new UssdResponse(INTERNAL_SERVER_ERROR.getStatusCode(),
					INTERNAL_SERVER_ERROR.getStatusMessage(), INTERNAL_SERVER_ERROR.getDescription());
		} catch (Exception e) {
			log.error("Error occurred : ", e.getMessage());
			ussdResponse = new UssdResponse(SERVICE_UNAVAILABLE.getStatusCode(), SERVICE_UNAVAILABLE.getStatusMessage(),
					SERVICE_UNAVAILABLE.getDescription());
		}
		log.info("Program terminated");
		return ussdResponse;
	}

	@Override
	public UssdResponse processSingleUssdMessage(SingleUssdMessage singleUssdMessage) {

		UssdResponse ussdResponse;
		try {
			String urlString = "";
			if (singleUssdMessage.getShortMessage().equals(config.getShortCode())) {
				userMessageReference = random.nextInt(config.getRendomRange());
				urlString = paramToUrlString(config.getShortCode().replace("#", "%23"),
						singleUssdMessage.getSourceAddress(), userMessageReference, "0001");
				sendSingleSMS(urlString);
				log.info("Main menu USSD request sent Successfully");
			} else {
				urlString = paramToUrlString(singleUssdMessage.getShortMessage(), singleUssdMessage.getSourceAddress(),
						userMessageReference, "0012");
				sendSingleSMS(urlString);
				log.info("USSD request sent Successfully");
			}
			ussdResponse = new UssdResponse(SUCCESS.getStatusCode(), SUCCESS.getStatusMessage(),
					SUCCESS.getDescription());
		} catch (Exception e) {
			log.error("Error occurred : ", e.getMessage());
			ussdResponse = new UssdResponse(SERVICE_UNAVAILABLE.getStatusCode(), SERVICE_UNAVAILABLE.getStatusMessage(),
					SERVICE_UNAVAILABLE.getDescription());
		}
		log.info("Program terminated");
		return ussdResponse;
	}

	private boolean isValidSelection(String selection) {
		return selection != null && !selection.isEmpty();
	}

	private void sleep(long milliseconds) throws InterruptedException {
		Thread.sleep(milliseconds);
	}

	public String paramToUrlString(String shortMessage, String sourceAddress, int userMessageReference, String optionalTLV1Val){
		String result = "http://" + config.getIpAddress() + ":" + config.getPort() + "/inject_mo?";
		
		result = result + "short_message=" + shortMessage;
		result = result + "&source_addr=" + sourceAddress;
		result = result + "&destination_addr=" + config.getDestinationAddress();
		result = result + "&submit=Submit+Message&service_type=&source_addr_ton=1&source_addr_npi=1&dest_addr_ton=1&dest_addr_npi=1&esm_class=0&protocol_ID=&priority_flag=&registered_delivery_flag=0&data_coding=0";
		result = result + "&user_message_reference=" + userMessageReference;	
		result = result + "&source_port=&destination_port=&sar_msg_ref_num=&sar_total_segments=&sar_segment_seqnum=&user_response_code=&privacy_indicator=&payload_type=&message_payload=&callback_num=&source_subaddress=&dest_subaddress=&language_indicator=&tlv1_tag=1281&tlv1_len=1";
		result = result + "&tlv1_val=" + optionalTLV1Val;
		result = result + "&tlv2_tag=5376&tlv2_len=30&tlv2_val=1500&tlv3_tag=5632&tlv3_len=15&tlv3_val=CF1D&tlv4_tag=&tlv4_len=&tlv4_val=&tlv5_tag=&tlv5_len=&tlv5_val=&tlv6_tag=&tlv6_len=&tlv6_val=&tlv7_tag=&tlv7_len=&tlv7_val=";	
		
		return result;
	}
	
	private void sendSingleSMS(String urlString) throws Exception{
		try {
			log.debug("End Point :- " + urlString);
			StringBuilder result = new StringBuilder();
		    URL url = new URL(urlString);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod("GET");
		    int responseCode = conn.getResponseCode();
			String responseMessage = conn.getResponseMessage();
		    log.info("Response Code: " + responseCode + " Response Message: " + responseMessage);
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) {
		       result.append(line);
		    }
		    rd.close();
		    log.debug("Response :- " + result);
			log.debug("USSD request sent Successfully");
		} catch (Exception e) {
			log.error("Failed to send SMS", e);
			throw e;
		}
	}

}
