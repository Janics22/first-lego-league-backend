package cat.udl.eps.softarch.fll.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cat.udl.eps.softarch.fll.controller.dto.ApiErrorResponse;
import cat.udl.eps.softarch.fll.exception.EditionTeamRegistrationException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class EditionTeamRegistrationExceptionHandler {

	@ExceptionHandler(EditionTeamRegistrationException.class)
	public ResponseEntity<ApiErrorResponse> handleRegistrationException(
			EditionTeamRegistrationException e,
			HttpServletRequest request) {
		HttpStatus status = resolveStatus(e.getError());
		return ResponseEntity.status(status)
				.body(ApiErrorResponse.of(e.getError(), e.getMessage(), request.getRequestURI()));
	}

	private HttpStatus resolveStatus(String error) {
		return switch (error) {
			case "EDITION_NOT_FOUND", "TEAM_NOT_FOUND" -> HttpStatus.NOT_FOUND;
			case "MAX_TEAMS_REACHED", "TEAM_ALREADY_REGISTERED" -> HttpStatus.CONFLICT;
			case "EDITION_OPERATION_NOT_ALLOWED" -> HttpStatus.UNPROCESSABLE_CONTENT;
			default -> HttpStatus.BAD_REQUEST;
		};
	}
}


