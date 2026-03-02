package cat.udl.eps.softarch.demo.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AssignRefereeRequest(
		@NotBlank(message = "matchId is mandatory") String matchId,
		@NotBlank(message = "refereeId is mandatory") String refereeId) {
}
