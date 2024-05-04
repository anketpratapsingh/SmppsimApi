package com.anket.smppsimapi.service;

import static com.anket.smppsimapi.utils.USSDStatusCode.INTERNAL_SERVER_ERROR;
import static com.anket.smppsimapi.utils.USSDStatusCode.INVALID_SHORT_MESSAGE;
import static com.anket.smppsimapi.utils.USSDStatusCode.MSISDN_MISMATCH;
import static com.anket.smppsimapi.utils.USSDStatusCode.SUCCESS;

import java.security.SecureRandom;

import com.anket.gRPC.Ussd.SingleUSSDRequest;
import com.anket.gRPC.Ussd.USSDResponse;
import com.anket.gRPC.Ussd.USSDResponse.Builder;
import com.anket.smppsimapi.config.SmppsimApiConfig;
import com.anket.smppsimapi.utils.HTTPSclient;
import com.anket.smppsimapi.utils.USSDStatusCode;
import com.anket.smppsimapi.utils.Utils;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SingleUSSDRequestHandler implements StreamObserver<SingleUSSDRequest> {

	private final StreamObserver<USSDResponse> responseObserver;
	// Flag to track whether streaming has started
	private boolean streamingStarted = false;
	// Initial MSISDN for comparison
	private String initialMsisdn = null;

	private boolean firstRequest = true;

	private SmppsimApiConfig config;

	private HTTPSclient httpsClient;

	private int userMessageReference;
	private SecureRandom random = new SecureRandom();

	public SingleUSSDRequestHandler(StreamObserver<USSDResponse> responseObserver, SmppsimApiConfig config,
			HTTPSclient httpsClient) {
		this.responseObserver = responseObserver;
		this.config = config;
		this.httpsClient = httpsClient;
	}

	@Override
	public void onNext(SingleUSSDRequest singleUSSDRequest) {
		Builder response = USSDResponse.newBuilder();
		try {
			if (!streamingStarted) {
				log.info("Streaming started");
				streamingStarted = true;
				initialMsisdn = singleUSSDRequest.getMsisdn();
				userMessageReference = random.nextInt(config.getRendomRange());

				// Validate MSISDN length using the utility method
				if (!Utils.isValidMsisdn(singleUSSDRequest.getMsisdn())) {
					Utils.handleInvalidMsisdn(responseObserver);
					return;
				}
			}
			// Check if the current MSISDN is the same as the initial one
			if (!singleUSSDRequest.getMsisdn().equals(initialMsisdn)) {
				handleMsisdnMismatch(singleUSSDRequest.getMsisdn());
				return;
			}

			if (!Utils.isValidSelection(singleUSSDRequest.getShortMessage())) {
				handleInvalidShortMessage();
				return;
			}

			String urlString = Utils.paramToUrlString(config.getShortCode(), singleUSSDRequest.getMsisdn(),
					userMessageReference, generateString(), config);
			httpsClient.sendSingleSMS(urlString);

			handleUssdResponce(SUCCESS, response);

		} catch (InterruptedException e) {
			log.error("Interrupted Exception :: ", e);
			Thread.currentThread().interrupt();
			handleUssdResponce(INTERNAL_SERVER_ERROR, response);
		} catch (Exception e) {
			log.error("Error occurred :: ", e);
			handleUssdResponce(Utils.getStatusForException(e), response);
		}
		this.responseObserver.onNext(response.build());
	}

	@Override
	public void onError(Throwable throwable) {
		log.error("Error processing request", throwable);
	}

	@Override
	public void onCompleted() {
		log.info("Streaming closed");
		this.responseObserver.onCompleted();
	}

	private void handleMsisdnMismatch(String receivedMsisdn) {
		log.error("MSISDN does not match with initial MSISDN, closing streaming");
		Status status = Status.INVALID_ARGUMENT.withDescription("MSISDN does not match with initial MSISDN")
				.augmentDescription("Received: " + receivedMsisdn + ", Expected: " + initialMsisdn)
				.augmentDescription("errorCode: " + MSISDN_MISMATCH.getStatusCode())
				.augmentDescription("errorMessage: " + MSISDN_MISMATCH.getStatusMessage())
				.augmentDescription("errorDescription: " + MSISDN_MISMATCH.getDescription());
		responseObserver.onError(status.asRuntimeException());
	}

	private void handleInvalidShortMessage() {
		log.error("Short message is null or empty, closing streaming");
		Status status = Status.INVALID_ARGUMENT.withDescription("Short message is null or empty")
				.augmentDescription("errorCode: " + INVALID_SHORT_MESSAGE.getStatusCode())
				.augmentDescription("errorMessage: " + INVALID_SHORT_MESSAGE.getStatusMessage())
				.augmentDescription("errorDescription: " + INVALID_SHORT_MESSAGE.getDescription());
		responseObserver.onError(status.asRuntimeException());
	}

	private String generateString() {
		if (firstRequest) {
			firstRequest = false;
			return "0012";
		} else {
			return "0001";
		}
	}

	private void handleUssdResponce(USSDStatusCode statusCode, Builder response) {
		response.setStatusCode(statusCode.getStatusCode());
		response.setStatusMessage(statusCode.getStatusMessage());
		response.setStatusDescription(statusCode.getDescription());
	}

}
