package cat.udl.eps.softarch.fll.ranking;

import cat.udl.eps.softarch.fll.domain.match.Match;
import cat.udl.eps.softarch.fll.domain.match.MatchResult;
import cat.udl.eps.softarch.fll.domain.ranking.Ranking;
import cat.udl.eps.softarch.fll.domain.team.Team;
import cat.udl.eps.softarch.fll.service.ranking.TotalScoreRankingCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TotalScoreRankingCalculatorTest {

	private TotalScoreRankingCalculator calculator;

	private Team teamAlpha;
	private Team teamBravo;
	private Team teamCharlie;
	private Match match;

	@BeforeEach
	void setUp() {
		calculator = new TotalScoreRankingCalculator();

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
		MatchResult result = MatchResult.create(100, match, teamAlpha);

		List<Ranking> rankings = calculator.calculate(List.of(result));

		assertEquals(1, rankings.size());
		assertEquals("Alpha", rankings.get(0).getTeam().getId());
		assertEquals(100, rankings.get(0).getTotalScore());
		assertEquals(1, rankings.get(0).getPosition());
	}

	@Test
	void calculateShouldSumScoresForSameTeam() {
		MatchResult r1 = MatchResult.create(50, match, teamAlpha);
		MatchResult r2 = MatchResult.create(70, match, teamAlpha);
		MatchResult r3 = MatchResult.create(30, match, teamAlpha);

		List<Ranking> rankings = calculator.calculate(List.of(r1, r2, r3));

		assertEquals(1, rankings.size());
		assertEquals("Alpha", rankings.get(0).getTeam().getId());
		assertEquals(150, rankings.get(0).getTotalScore());
		assertEquals(1, rankings.get(0).getPosition());
	}

	@Test
	void calculateShouldRankTeamsByTotalScoreDescending() {
		MatchResult rAlpha = MatchResult.create(80, match, teamAlpha);
		MatchResult rBravo = MatchResult.create(120, match, teamBravo);
		MatchResult rCharlie = MatchResult.create(60, match, teamCharlie);

		List<Ranking> rankings = calculator.calculate(List.of(rAlpha, rBravo, rCharlie));

		assertEquals(3, rankings.size());

		assertEquals("Bravo", rankings.get(0).getTeam().getId());
		assertEquals(120, rankings.get(0).getTotalScore());
		assertEquals(1, rankings.get(0).getPosition());

		assertEquals("Alpha", rankings.get(1).getTeam().getId());
		assertEquals(80, rankings.get(1).getTotalScore());
		assertEquals(2, rankings.get(1).getPosition());

		assertEquals("Charlie", rankings.get(2).getTeam().getId());
		assertEquals(60, rankings.get(2).getTotalScore());
		assertEquals(3, rankings.get(2).getPosition());
	}

	@Test
	void calculateShouldAggregateMultipleResultsAcrossTeams() {
		MatchResult rAlpha1 = MatchResult.create(50, match, teamAlpha);
		MatchResult rAlpha2 = MatchResult.create(30, match, teamAlpha);
		MatchResult rBravo1 = MatchResult.create(90, match, teamBravo);
		MatchResult rBravo2 = MatchResult.create(10, match, teamBravo);

		List<Ranking> rankings = calculator.calculate(
			List.of(rAlpha1, rAlpha2, rBravo1, rBravo2));

		assertEquals(2, rankings.size());

		assertEquals("Bravo", rankings.get(0).getTeam().getId());
		assertEquals(100, rankings.get(0).getTotalScore());
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
