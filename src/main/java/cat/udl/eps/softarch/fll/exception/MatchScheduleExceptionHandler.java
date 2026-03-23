package cat.udl.eps.softarch.fll.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cat.udl.eps.softarch.fll.controller.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class MatchScheduleExceptionHandler {

	@ExceptionHandler(MatchScheduleException.class)
	public ResponseEntity<ApiErrorResponse> handleMatchScheduleException(
			MatchScheduleException ex,
			HttpServletRequest request) {
		HttpStatus status = switch (ex.getErrorCode()) {
			case INVALID_TIME_RANGE -> HttpStatus.UNPROCESSABLE_CONTENT;
			case TABLE_TIME_OVERLAP -> HttpStatus.CONFLICT;
		};
		return ResponseEntity.status(status)
				.body(ApiErrorResponse.of(ex.getErrorCode().name(), ex.getMessage(), request.getRequestURI()));
	}
}
