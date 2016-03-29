package ch.uzh.ifi.seal.soprafs16.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class GenericController {

	Logger logger = LoggerFactory.getLogger(GenericController.class);

	@ExceptionHandler(TransactionSystemException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public void handleTransactionSystemException(Exception exception, HttpServletRequest request) {
		logger.error("", exception);
	}

	@ExceptionHandler(InvalidInputException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid input argument")
	public ErrorResource handleInvalidInputException(Exception exception, HttpServletRequest request) {
		logger.error("", exception);
		logger.info("handleInvalidInputException ....");

		return new ErrorResource("Invalid input argument", exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(Exception exception, HttpServletRequest request) {
		logger.error("", exception);
	}
}
