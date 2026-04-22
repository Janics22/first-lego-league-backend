package cat.udl.eps.softarch.fll.ranking;

import cat.udl.eps.softarch.fll.domain.match.Match;
import cat.udl.eps.softarch.fll.domain.match.MatchResult;
import cat.udl.eps.softarch.fll.domain.ranking.Ranking;
import cat.udl.eps.softarch.fll.domain.team.Team;
import cat.udl.eps.softarch.fll.repository.match.MatchResultRepository;
import cat.udl.eps.softarch.fll.repository.ranking.RankingRepository;
import cat.udl.eps.softarch.fll.service.ranking.RankingCalculator;
import cat.udl.eps.softarch.fll.service.ranking.RankingService;
import cat.udl.eps.softarch.fll.service.ranking.TotalScoreRankingCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

	@Mock
	private MatchResultRepository matchResultRepository;

	@Mock
	private RankingRepository rankingRepository;

	@Mock
	private RankingCalculator rankingCalculator;

	@InjectMocks
	private RankingService rankingService;

	@Captor
	private ArgumentCaptor<List<Ranking>> rankingCaptor;

	private Team teamAlpha;
	private Team teamBravo;
	private Match match;

	@BeforeEach
	void setUp() {
		teamAlpha = Team.create("Alpha", "City-A", 2000, "Junior");
		teamBravo = Team.create("Bravo", "City-B", 2001, "Junior");

		match = new Match();
		match.setId(1L);
	}

	@Test
	void recalculateRankingShouldFetchAllResultsAndDelegateToCalculator() {
		List<MatchResult> results = List.of(
			MatchResult.create(100, match, teamAlpha),
			MatchResult.create(80, match, teamBravo)
		);
		when(matchResultRepository.findAll()).thenReturn(results);

		Ranking r1 = new Ranking();
		r1.setTeam(teamAlpha);
		r1.setTotalScore(100);
		r1.setPosition(1);
		Ranking r2 = new Ranking();
		r2.setTeam(teamBravo);
		r2.setTotalScore(80);
		r2.setPosition(2);
		when(rankingCalculator.calculate(results)).thenReturn(List.of(r1, r2));

		rankingService.recalculateRanking();

		verify(matchResultRepository).findAll();
		verify(rankingCalculator).calculate(results);
	}

	@Test
	void recalculateRankingShouldDeleteOldRankingsBeforeSavingNew() {
		when(matchResultRepository.findAll()).thenReturn(Collections.emptyList());
		when(rankingCalculator.calculate(anyList())).thenReturn(Collections.emptyList());

		rankingService.recalculateRanking();

		InOrder inOrder = inOrder(rankingRepository);
		inOrder.verify(rankingRepository).deleteAllInBatch();
		inOrder.verify(rankingRepository).saveAll(anyList());
	}

	@Test
	void recalculateRankingShouldPersistComputedRankings() {
		List<MatchResult> results = List.of(
			MatchResult.create(50, match, teamAlpha),
			MatchResult.create(90, match, teamBravo)
		);
		when(matchResultRepository.findAll()).thenReturn(results);

		Ranking r1 = new Ranking();
		r1.setTeam(teamBravo);
		r1.setTotalScore(90);
		r1.setPosition(1);
		Ranking r2 = new Ranking();
		r2.setTeam(teamAlpha);
		r2.setTotalScore(50);
		r2.setPosition(2);
		List<Ranking> expectedRankings = List.of(r1, r2);
		when(rankingCalculator.calculate(results)).thenReturn(expectedRankings);

		rankingService.recalculateRanking();

		verify(rankingRepository).saveAll(rankingCaptor.capture());
		List<Ranking> savedRankings = rankingCaptor.getValue();

		assertEquals(2, savedRankings.size());
		assertEquals("Bravo", savedRankings.get(0).getTeam().getId());
		assertEquals(90, savedRankings.get(0).getTotalScore());
		assertEquals(1, savedRankings.get(0).getPosition());
		assertEquals("Alpha", savedRankings.get(1).getTeam().getId());
		assertEquals(50, savedRankings.get(1).getTotalScore());
		assertEquals(2, savedRankings.get(1).getPosition());
	}

	@Test
	void recalculateRankingShouldHandleEmptyResultsGracefully() {
		when(matchResultRepository.findAll()).thenReturn(Collections.emptyList());
		when(rankingCalculator.calculate(Collections.emptyList())).thenReturn(Collections.emptyList());

		rankingService.recalculateRanking();

		verify(rankingRepository).deleteAllInBatch();
		verify(rankingRepository).saveAll(rankingCaptor.capture());

		assertEquals(0, rankingCaptor.getValue().size());
	}

	@Test
	void recalculateRankingShouldWorkWithRealTotalScoreCalculator() {
		TotalScoreRankingCalculator realCalculator = new TotalScoreRankingCalculator();
		RankingService serviceWithRealCalc = new RankingService(
			matchResultRepository, rankingRepository, realCalculator);

		MatchResult rAlpha1 = MatchResult.create(40, match, teamAlpha);
		MatchResult rAlpha2 = MatchResult.create(60, match, teamAlpha);
		MatchResult rBravo = MatchResult.create(90, match, teamBravo);

		when(matchResultRepository.findAll()).thenReturn(List.of(rAlpha1, rAlpha2, rBravo));

		serviceWithRealCalc.recalculateRanking();

		verify(rankingRepository).deleteAllInBatch();
		verify(rankingRepository).saveAll(rankingCaptor.capture());

		List<Ranking> saved = rankingCaptor.getValue();
		assertEquals(2, saved.size());

		assertEquals("Alpha", saved.get(0).getTeam().getId());
		assertEquals(100, saved.get(0).getTotalScore());
		assertEquals(1, saved.get(0).getPosition());

		assertEquals("Bravo", saved.get(1).getTeam().getId());
		assertEquals(90, saved.get(1).getTotalScore());
		assertEquals(2, saved.get(1).getPosition());
	}
}
