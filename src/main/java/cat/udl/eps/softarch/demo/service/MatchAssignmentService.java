package cat.udl.eps.softarch.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cat.udl.eps.softarch.demo.api.dto.AssignRefereeResponse;
import cat.udl.eps.softarch.demo.domain.Match;
import cat.udl.eps.softarch.demo.domain.MatchState;
import cat.udl.eps.softarch.demo.domain.Referee;
import cat.udl.eps.softarch.demo.domain.Volunteer;
import cat.udl.eps.softarch.demo.exception.AssignmentErrorCode;
import cat.udl.eps.softarch.demo.exception.AssignmentValidationException;
import cat.udl.eps.softarch.demo.repository.MatchRepository;
import cat.udl.eps.softarch.demo.repository.VolunteerRepository;

@Service
public class MatchAssignmentService {

	private final MatchRepository matchRepository;
	private final VolunteerRepository volunteerRepository;

	public MatchAssignmentService(MatchRepository matchRepository, VolunteerRepository volunteerRepository) {
		this.matchRepository = matchRepository;
		this.volunteerRepository = volunteerRepository;
	}

	@Transactional
	public AssignRefereeResponse assignReferee(String matchId, String refereeId) {
		Long parsedMatchId = parseId(matchId, "matchId");
		Long parsedRefereeId = parseId(refereeId, "refereeId");

		Match match = matchRepository.findByIdForUpdate(parsedMatchId).orElseThrow(
				() -> new AssignmentValidationException(
						AssignmentErrorCode.MATCH_NOT_FOUND,
						"Match " + matchId + " does not exist"));

		if (match.getReferee() != null) {
			throw new AssignmentValidationException(
					AssignmentErrorCode.MATCH_ALREADY_HAS_REFEREE,
					"Match " + matchId + " already has a referee assigned");
		}

		if (match.getState() != MatchState.SCHEDULED) {
			throw new AssignmentValidationException(
					AssignmentErrorCode.INVALID_MATCH_STATE,
					"Match " + matchId + " is not in an assignable state");
		}

		Volunteer volunteer = volunteerRepository.findByIdForUpdate(parsedRefereeId).orElseThrow(
				() -> new AssignmentValidationException(
						AssignmentErrorCode.REFEREE_NOT_FOUND,
						"Volunteer " + refereeId + " does not exist"));

		if (!(volunteer instanceof Referee referee)) {
			throw new AssignmentValidationException(
					AssignmentErrorCode.INVALID_ROLE,
					"Volunteer " + refereeId + " is not a referee");
		}

		boolean hasConflict = matchRepository.existsOverlappingAssignment(
				parsedRefereeId,
				match.getStartTime(),
				match.getEndTime());

		if (hasConflict) {
			throw new AssignmentValidationException(
					AssignmentErrorCode.AVAILABILITY_CONFLICT,
					"Referee " + refereeId + " is already assigned to an overlapping match");
		}

		match.setReferee(referee);

		return new AssignRefereeResponse(matchId, refereeId, "ASSIGNED");
	}

	private Long parseId(String rawId, String fieldName) {
		try {
			return Long.parseLong(rawId);
		} catch (NumberFormatException exception) {
			throw new AssignmentValidationException(
					AssignmentErrorCode.INVALID_ID_FORMAT,
					fieldName + " must be a valid numeric string");
		}
	}
}
