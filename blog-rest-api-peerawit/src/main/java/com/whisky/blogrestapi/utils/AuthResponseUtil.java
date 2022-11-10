package com.whisky.blogrestapi.utils;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.whisky.blogrestapi.payload.UnAuthorizedResponse;

public class AuthResponseUtil {

	public static void setResponseDataToUnAuthorizationBean(String message,
			HttpServletRequest request, UnAuthorizedResponse unAuthorizedResponse) {
		unAuthorizedResponse.setTimestamp(new Date());
		unAuthorizedResponse.setMessage(message);
		unAuthorizedResponse.setDetails("uri=" + request.getRequestURI());
	}

	public static void sendResponse(UnAuthorizedResponse unAuthorizedResponse,
			HttpServletResponse response, HttpStatus httpStatus) throws IOException {
		
		ObjectWriter ow = new ObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(unAuthorizedResponse);

		response.setContentType(MediaType.APPLICATION_JSON.toString());
		response.getWriter().write(json);
		response.setStatus(httpStatus.value());
		
	}

}
