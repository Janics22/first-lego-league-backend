package cat.udl.eps.softarch.demo.exception;

public class AssignmentValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final AssignmentErrorCode errorCode;

	public AssignmentValidationException(AssignmentErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public AssignmentErrorCode getErrorCode() {
		return errorCode;
	}
}
