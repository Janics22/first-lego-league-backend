package cat.udl.eps.softarch.fll.handler;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import cat.udl.eps.softarch.fll.domain.CompetitionTable;
import cat.udl.eps.softarch.fll.exception.DomainValidationException;

@Component
@RepositoryEventHandler
public class CompetitionTableEventHandler {

	@HandleBeforeCreate
	public void handleCompetitionTableBeforeCreate(CompetitionTable table) {
		if (table.getId() == null || table.getId().isBlank()) {
			throw new DomainValidationException("ID_REQUIRED",
					"When creating a competition table, you must provide an id. For example: {\"id\": \"table-1\"}");
		}
	}
}
