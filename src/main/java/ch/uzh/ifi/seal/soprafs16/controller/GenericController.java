package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

public abstract class GenericController {

	private static final Logger genLogger = LoggerFactory.getLogger(GenericController.class);

	@ExceptionHandler(TransactionSystemException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public void handleTransactionSystemException(Exception exception) {
		genLogger.error("", exception);
	}

	@ExceptionHandler(InvalidInputException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "Invalid input argument")
	public ErrorResource handleInvalidInputException(Exception exception) {
		genLogger.error("", exception);
		genLogger.info("handleInvalidInputException ....");

		return new ErrorResource("Invalid input argument", exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(Exception exception) {
		genLogger.error("", exception);
	}
}
