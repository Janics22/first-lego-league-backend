package cat.udl.eps.softarch.demo.exception;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cat.udl.eps.softarch.demo.api.dto.AssignmentErrorResponse;
import cat.udl.eps.softarch.demo.controller.MatchAssignmentController;

@RestControllerAdvice(assignableTypes = MatchAssignmentController.class)
public class MatchAssignmentExceptionHandler {

	@ExceptionHandler(AssignmentValidationException.class)
	public ResponseEntity<AssignmentErrorResponse> handleAssignmentValidationException(
			AssignmentValidationException exception) {
		HttpStatus status = mapStatus(exception.getErrorCode());
		AssignmentErrorResponse response = new AssignmentErrorResponse(
				exception.getErrorCode().name(),
				exception.getMessage());
		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<AssignmentErrorResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(error -> error.getDefaultMessage())
				.orElse("Invalid request payload");
		AssignmentErrorResponse response = new AssignmentErrorResponse(
				AssignmentErrorCode.INVALID_ID_FORMAT.name(),
				message);
		return ResponseEntity.unprocessableEntity().body(response);
	}

	@ExceptionHandler({
			OptimisticLockingFailureException.class,
			PessimisticLockingFailureException.class,
			CannotAcquireLockException.class
	})
	public ResponseEntity<AssignmentErrorResponse> handleConcurrencyException(RuntimeException exception) {
		AssignmentErrorResponse response = new AssignmentErrorResponse(
				AssignmentErrorCode.MATCH_ALREADY_HAS_REFEREE.name(),
				"Concurrent assignment conflict detected");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	private HttpStatus mapStatus(AssignmentErrorCode errorCode) {
		return switch (errorCode) {
			case MATCH_NOT_FOUND, REFEREE_NOT_FOUND -> HttpStatus.NOT_FOUND;
			case MATCH_ALREADY_HAS_REFEREE, AVAILABILITY_CONFLICT -> HttpStatus.CONFLICT;
			case INVALID_ROLE, INVALID_MATCH_STATE, INVALID_ID_FORMAT -> HttpStatus.UNPROCESSABLE_ENTITY;
		};
	}
}
