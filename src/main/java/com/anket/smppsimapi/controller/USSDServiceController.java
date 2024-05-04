package com.anket.smppsimapi.controller;

import static com.anket.smppsimapi.utils.USSDStatusCode.INTERNAL_SERVER_ERROR;
import static com.anket.smppsimapi.utils.USSDStatusCode.SUCCESS;

import java.lang.reflect.Field;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anket.gRPC.USSDServiceGrpc.USSDServiceImplBase;
import com.anket.gRPC.Ussd.SingleUSSDRequest;
import com.anket.gRPC.Ussd.USSDRequest;
import com.anket.gRPC.Ussd.USSDResponse;
import com.anket.gRPC.Ussd.USSDResponse.Builder;
import com.anket.smppsimapi.config.SmppsimApiConfig;
import com.anket.smppsimapi.service.SingleUSSDRequestHandler;
import com.anket.smppsimapi.utils.HTTPSclient;
import com.anket.smppsimapi.utils.USSDStatusCode;
import com.anket.smppsimapi.utils.Utils;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Service
@Slf4j
public class USSDServiceController extends USSDServiceImplBase {
	
	@Autowired
    private SmppsimApiConfig config;

    @Autowired
    private HTTPSclient httpsClient;
    
    @Autowired
    private Utils utils;
    
    private static SecureRandom random = new SecureRandom();
	
	@Override
	public void ussdRequest(USSDRequest request, StreamObserver<USSDResponse> responseObserver) {
		// Validate MSISDN length using the utility method
		if (!Utils.isValidMsisdn(request.getMsisdn())) {
			Utils.handleInvalidMsisdn(responseObserver);
			return;
		}
		Builder response = USSDResponse.newBuilder();
		try {
			String urlString = "";
			boolean mainMenu = true;
			int userMessageReference = random.nextInt(config.getRendomRange());

			// Access all fields using Reflection
			Field[] fields = USSDRequest.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				// Check if the field name starts with "shortMessage"
				if (field.getName().startsWith("shortMessage")) {
					String value = (String) field.get(request);
					log.info(field.getName() + ": " + value);
					if (Utils.isValidSelection(value)) {
						String optionalTLV1Val = mainMenu ? "0001" : "0012" ;
						mainMenu = false;
						urlString = Utils.paramToUrlString(value, request.getMsisdn(), userMessageReference, optionalTLV1Val, config);
						httpsClient.sendSingleSMS(urlString);
						handleUssdResponce(SUCCESS, response);
						responseObserver.onNext(response.build());
						log.info("USSD request sent Successfully");
						utils.sleep(7000);
					}else {
						if (mainMenu) {
							Utils.handleInvalidShortMessage(responseObserver);
							return ;
						}
						break;
					}
				}
			}
			
		} catch (InterruptedException e) {
			log.error("Interrupted Exception :: ", e);
			Thread.currentThread().interrupt();
			handleUssdResponce(INTERNAL_SERVER_ERROR, response);
			responseObserver.onNext(response.build());
		} catch (Exception e) {
			log.error("Error occurred :: ", e);
			handleUssdResponce(Utils.getStatusForException(e), response);
			responseObserver.onNext(response.build());
		}
		
		log.info("Program terminated");
		responseObserver.onCompleted();
	}

	@Override
	public StreamObserver<SingleUSSDRequest> singleUssdRequest(StreamObserver<USSDResponse> responseObserver) {
		return new SingleUSSDRequestHandler(responseObserver, config, httpsClient);
	}

	private void handleUssdResponce(USSDStatusCode statusCode, Builder response) {
		response.setStatusCode(statusCode.getStatusCode());
		response.setStatusMessage(statusCode.getStatusMessage());
		response.setStatusDescription(statusCode.getDescription());
	}
}
