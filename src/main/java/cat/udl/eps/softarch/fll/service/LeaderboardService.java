package cat.udl.eps.softarch.fll.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import cat.udl.eps.softarch.fll.controller.dto.LeaderboardItemResponse;
import cat.udl.eps.softarch.fll.controller.dto.LeaderboardPageResponse;
import cat.udl.eps.softarch.fll.repository.EditionRepository;
import cat.udl.eps.softarch.fll.repository.MatchResultRepository;
import cat.udl.eps.softarch.fll.repository.projection.LeaderboardRowProjection;

@Service
public class LeaderboardService {

	private final EditionRepository editionRepository;
	private final MatchResultRepository matchResultRepository;

	public LeaderboardService(EditionRepository editionRepository, MatchResultRepository matchResultRepository) {
		this.editionRepository = editionRepository;
		this.matchResultRepository = matchResultRepository;
	}

	public LeaderboardPageResponse getEditionLeaderboard(Long editionId, int page, int size) {
		if (page < 0 || size < 1 || size > 100) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Invalid pagination: page must be >= 0 and size must be between 1 and 100");
		}

		if (!editionRepository.existsById(editionId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Edition not found: " + editionId);
		}

		Page<LeaderboardRowProjection> resultPage = matchResultRepository.findLeaderboardByEditionId(
				editionId, PageRequest.of(page, size));

		List<LeaderboardItemResponse> items = mapItems(resultPage, page, size);

		return new LeaderboardPageResponse(
				editionId,
				page,
				size,
				resultPage.getTotalElements(),
				items);
	}

	private List<LeaderboardItemResponse> mapItems(Page<LeaderboardRowProjection> resultPage, int page, int size) {
		int basePosition = page * size;
		List<LeaderboardRowProjection> rows = resultPage.getContent();

		return java.util.stream.IntStream.range(0, rows.size())
				.mapToObj(index -> {
					LeaderboardRowProjection row = rows.get(index);
					return new LeaderboardItemResponse(
							basePosition + index + 1,
							row.getTeamId(),
							row.getTeamName(),
							row.getTotalScore(),
							row.getMatchesPlayed());
				})
				.toList();
	}
}
