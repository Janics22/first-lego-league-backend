package cat.udl.eps.softarch.fll.ranking;

import cat.udl.eps.softarch.fll.domain.match.Match;
import cat.udl.eps.softarch.fll.domain.match.MatchResult;
import cat.udl.eps.softarch.fll.domain.ranking.Ranking;
import cat.udl.eps.softarch.fll.domain.team.Team;
import cat.udl.eps.softarch.fll.service.ranking.BestRoundRankingCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BestRoundRankingCalculatorTest {

	private BestRoundRankingCalculator calculator;

	private Team teamAlpha;
	private Team teamBravo;
	private Team teamCharlie;
	private Match match;

	@BeforeEach
	void setUp() {
		calculator = new BestRoundRankingCalculator();

		teamAlpha = Team.create("Alpha", "City-A", 2000, "Junior");
		teamBravo = Team.create("Bravo", "City-B", 2001, "Junior");
		teamCharlie = Team.create("Charlie", "City-C", 2002, "Junior");

		match = new Match();
		match.setId(1L);
	}

	@Test
	void calculateShouldReturnEmptyListWhenNoResults() {
		List<Ranking> rankings = calculator.calculate(Collections.emptyList());

		assertTrue(rankings.isEmpty());
	}

	@Test
	void calculateShouldReturnSingleRankingForSingleResult() {
		MatchResult result = MatchResult.create(75, match, teamAlpha);

		List<Ranking> rankings = calculator.calculate(List.of(result));

		assertEquals(1, rankings.size());
		assertEquals("Alpha", rankings.get(0).getTeam().getId());
		assertEquals(75, rankings.get(0).getTotalScore());
		assertEquals(1, rankings.get(0).getPosition());
	}

	@Test
	void calculateShouldKeepMaxScoreForSameTeam() {
		MatchResult r1 = MatchResult.create(40, match, teamAlpha);
		MatchResult r2 = MatchResult.create(90, match, teamAlpha);
		MatchResult r3 = MatchResult.create(60, match, teamAlpha);

		List<Ranking> rankings = calculator.calculate(List.of(r1, r2, r3));

		assertEquals(1, rankings.size());
		assertEquals("Alpha", rankings.get(0).getTeam().getId());
		assertEquals(90, rankings.get(0).getTotalScore());
		assertEquals(1, rankings.get(0).getPosition());
	}

	@Test
	void calculateShouldRankTeamsByBestRoundDescending() {
		MatchResult rAlpha = MatchResult.create(70, match, teamAlpha);
		MatchResult rBravo = MatchResult.create(110, match, teamBravo);
		MatchResult rCharlie = MatchResult.create(50, match, teamCharlie);

		List<Ranking> rankings = calculator.calculate(List.of(rAlpha, rBravo, rCharlie));

		assertEquals(3, rankings.size());

		assertEquals("Bravo", rankings.get(0).getTeam().getId());
		assertEquals(110, rankings.get(0).getTotalScore());
		assertEquals(1, rankings.get(0).getPosition());

		assertEquals("Alpha", rankings.get(1).getTeam().getId());
		assertEquals(70, rankings.get(1).getTotalScore());
		assertEquals(2, rankings.get(1).getPosition());

		assertEquals("Charlie", rankings.get(2).getTeam().getId());
		assertEquals(50, rankings.get(2).getTotalScore());
		assertEquals(3, rankings.get(2).getPosition());
	}

	@Test
	void calculateShouldPickMaxScoreAcrossMultipleResultsPerTeam() {
		MatchResult rAlpha1 = MatchResult.create(30, match, teamAlpha);
		MatchResult rAlpha2 = MatchResult.create(80, match, teamAlpha);
		MatchResult rBravo1 = MatchResult.create(95, match, teamBravo);
		MatchResult rBravo2 = MatchResult.create(60, match, teamBravo);

		List<Ranking> rankings = calculator.calculate(
			List.of(rAlpha1, rAlpha2, rBravo1, rBravo2));

		assertEquals(2, rankings.size());

		assertEquals("Bravo", rankings.get(0).getTeam().getId());
		assertEquals(95, rankings.get(0).getTotalScore());
		assertEquals(1, rankings.get(0).getPosition());

		assertEquals("Alpha", rankings.get(1).getTeam().getId());
		assertEquals(80, rankings.get(1).getTotalScore());
		assertEquals(2, rankings.get(1).getPosition());
	}

	@Test
	void calculateShouldBreakTiesByTeamIdAscending() {
		MatchResult rAlpha = MatchResult.create(100, match, teamAlpha);
		MatchResult rBravo = MatchResult.create(100, match, teamBravo);

		List<Ranking> rankings = calculator.calculate(List.of(rAlpha, rBravo));

		assertEquals(2, rankings.size());

		assertEquals("Alpha", rankings.get(0).getTeam().getId());
		assertEquals(1, rankings.get(0).getPosition());

		assertEquals("Bravo", rankings.get(1).getTeam().getId());
		assertEquals(2, rankings.get(1).getPosition());
	}

	@Test
	void calculateShouldUseMaxNotSumForMultipleResults() {
		MatchResult rAlpha1 = MatchResult.create(20, match, teamAlpha);
		MatchResult rAlpha2 = MatchResult.create(30, match, teamAlpha);
		MatchResult rBravo = MatchResult.create(40, match, teamBravo);

		List<Ranking> rankings = calculator.calculate(
			List.of(rAlpha1, rAlpha2, rBravo));

		assertEquals(2, rankings.size());

		assertEquals("Bravo", rankings.get(0).getTeam().getId());
		assertEquals(40, rankings.get(0).getTotalScore());
		assertEquals(1, rankings.get(0).getPosition());

		assertEquals("Alpha", rankings.get(1).getTeam().getId());
		assertEquals(30, rankings.get(1).getTotalScore());
		assertEquals(2, rankings.get(1).getPosition());
	}

	@Test
	void calculateShouldHandleZeroScoresCorrectly() {
		MatchResult rAlpha = MatchResult.create(0, match, teamAlpha);
		MatchResult rBravo = MatchResult.create(50, match, teamBravo);

		List<Ranking> rankings = calculator.calculate(List.of(rAlpha, rBravo));

		assertEquals(2, rankings.size());

		assertEquals("Bravo", rankings.get(0).getTeam().getId());
		assertEquals(50, rankings.get(0).getTotalScore());
		assertEquals(1, rankings.get(0).getPosition());

		assertEquals("Alpha", rankings.get(1).getTeam().getId());
		assertEquals(0, rankings.get(1).getTotalScore());
		assertEquals(2, rankings.get(1).getPosition());
	}
}
