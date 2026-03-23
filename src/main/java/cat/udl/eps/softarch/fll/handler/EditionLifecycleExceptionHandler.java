package cat.udl.eps.softarch.fll.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cat.udl.eps.softarch.fll.controller.dto.ApiErrorResponse;
import cat.udl.eps.softarch.fll.exception.EditionLifecycleException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class EditionLifecycleExceptionHandler {

	@ExceptionHandler(EditionLifecycleException.class)
	public ResponseEntity<ApiErrorResponse> handleEditionLifecycleException(
			EditionLifecycleException exception,
			HttpServletRequest request) {
		HttpStatus status = resolveStatus(exception.getError());
		return ResponseEntity.status(status)
				.body(ApiErrorResponse.of(exception.getError(), exception.getMessage(), request.getRequestURI()));
	}

	private HttpStatus resolveStatus(String error) {
		return switch (error) {
			case "EDITION_NOT_FOUND" -> HttpStatus.NOT_FOUND;
			case "INVALID_EDITION_STATE_TRANSITION" -> HttpStatus.CONFLICT;
			case "EDITION_OPERATION_NOT_ALLOWED" -> HttpStatus.UNPROCESSABLE_CONTENT;
			default -> HttpStatus.BAD_REQUEST;
		};
	}
}
