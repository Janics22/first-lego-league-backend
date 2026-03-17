package cat.udl.eps.softarch.fll.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cat.udl.eps.softarch.fll.domain.Edition;
import cat.udl.eps.softarch.fll.domain.EditionOperation;
import cat.udl.eps.softarch.fll.domain.EditionState;
import cat.udl.eps.softarch.fll.exception.EditionLifecycleException;
import cat.udl.eps.softarch.fll.repository.edition.EditionRepository;

@Service
public class EditionLifecycleService {

	private final EditionRepository editionRepository;

	public EditionLifecycleService(EditionRepository editionRepository) {
		this.editionRepository = editionRepository;
	}

	@Transactional
	public TransitionResult changeState(Long editionId, EditionState targetState) {
		Edition edition = editionRepository.findByIdForUpdate(editionId)
				.orElseThrow(() -> new EditionLifecycleException(
						"EDITION_NOT_FOUND", "Edition with id " + editionId + " not found"));

		EditionState currentState = edition.getState() == null ? EditionState.DRAFT : edition.getState();
		if (!isValidTransition(currentState, targetState)) {
			throw new EditionLifecycleException(
					"INVALID_EDITION_STATE_TRANSITION",
					"Invalid transition from " + currentState + " to " + targetState);
		}

		edition.setState(targetState);
		editionRepository.save(edition);
		return new TransitionResult(edition.getId(), currentState, targetState);
	}

	public void assertOperationAllowed(Edition edition, EditionOperation operation) {
		EditionState currentState = edition.getState() == null ? EditionState.DRAFT : edition.getState();
		boolean allowed = switch (operation) {
			case TEAM_REGISTRATION -> currentState == EditionState.OPEN;
		};

		if (!allowed) {
			throw new EditionLifecycleException(
					"EDITION_OPERATION_NOT_ALLOWED",
					"Operation " + operation + " is not allowed when edition is in state " + currentState);
		}
	}

	boolean isValidTransition(EditionState currentState, EditionState targetState) {
		return switch (currentState) {
			case DRAFT -> targetState == EditionState.OPEN;
			case OPEN -> targetState == EditionState.CLOSED;
			case CLOSED -> false;
		};
	}

	public record TransitionResult(Long editionId, EditionState previousState, EditionState newState) {
	}
}
