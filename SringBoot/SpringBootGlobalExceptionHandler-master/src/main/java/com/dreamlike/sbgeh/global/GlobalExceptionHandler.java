/**
 * 
 */
package com.dreamlike.sbgeh.global;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.dreamlike.sbgeh.bean.Response;
import com.dreamlike.sbgeh.util.RespUtil;

/**
 * @author Broly
 *
 */
@EnableWebMvc
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { NoHandlerFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Response noHandlerFoundException(Exception ex) {
		return RespUtil.make(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Response constraintViolationException(ConstraintViolationException ex) {
		return RespUtil.make(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response defaultErrorHandler(HttpServletRequest request, Exception exception) throws Exception {
		return RespUtil.make(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	}

}
