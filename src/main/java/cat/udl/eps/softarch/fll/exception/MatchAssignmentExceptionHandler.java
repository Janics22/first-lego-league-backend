package cat.udl.eps.softarch.fll.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cat.udl.eps.softarch.fll.controller.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class MatchAssignmentExceptionHandler {

	@ExceptionHandler(MatchAssignmentException.class)
	public ResponseEntity<ApiErrorResponse> handleMatchAssignmentException(
			MatchAssignmentException ex,
			HttpServletRequest request) {
		HttpStatus status = switch (ex.getErrorCode()) {
			case ROUND_NOT_FOUND, MATCH_NOT_FOUND, REFEREE_NOT_FOUND -> HttpStatus.NOT_FOUND;
			case AVAILABILITY_CONFLICT, MATCH_ALREADY_HAS_REFEREE, DUPLICATE_MATCH_IN_BATCH -> HttpStatus.CONFLICT;
			case INVALID_ID_FORMAT -> HttpStatus.BAD_REQUEST;
			case INVALID_ROLE, INVALID_MATCH_STATE -> HttpStatus.UNPROCESSABLE_CONTENT;
		};

		BatchErrorDetails details = ex.hasBatchDetails()
				? new BatchErrorDetails(ex.getIndex(), ex.getMatchId(), ex.getRefereeId(), ex.getErrorCode().name())
				: null;
		ApiErrorResponse body = ApiErrorResponse.of(
				ex.hasBatchDetails() ? "BATCH_ASSIGNMENT_FAILED" : ex.getErrorCode().name(),
				ex.hasBatchDetails() ? "Assignment failed at index " + ex.getIndex() : ex.getMessage(),
				request.getRequestURI(),
				details);
		return ResponseEntity.status(status).body(body);
	}

	public record BatchErrorDetails(Integer index, String matchId, String refereeId, String cause) {}
}
