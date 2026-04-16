package cat.udl.eps.softarch.fll.steps.ranking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import cat.udl.eps.softarch.fll.domain.Ranking;
import cat.udl.eps.softarch.fll.domain.Team;
import cat.udl.eps.softarch.fll.repository.ranking.RankingRepository;
import cat.udl.eps.softarch.fll.repository.team.TeamRepository;
import cat.udl.eps.softarch.fll.steps.app.StepDefs;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

public class ManageRankingStepDefs {

	private final StepDefs stepDefs;
	private final RankingRepository rankingRepository;
	private final TeamRepository teamRepository;

	private Long currentRankingId;

	public ManageRankingStepDefs(StepDefs stepDefs, RankingRepository rankingRepository, TeamRepository teamRepository) {
		this.stepDefs = stepDefs;
		this.rankingRepository = rankingRepository;
		this.teamRepository = teamRepository;
	}

	@Given("rankings exist in the system")
	public void rankingsExistInTheSystem() {
		Team team = Team.create("RankingTeam-" + System.nanoTime(), "Barcelona", 2000, "Junior");
		teamRepository.save(team);

		Ranking ranking = new Ranking();
		ranking.setTeam(team);
		ranking.setPosition(1);
		ranking.setTotalScore(300);
		rankingRepository.save(ranking);
	}

	@Given("a ranking exists for team {string} with position {int} and totalScore {int}")
	public void aRankingExistsForTeamWithPositionAndTotalScore(String teamName, int position, int totalScore) {
		Team team = Team.create(teamName + "-" + System.nanoTime(), "Barcelona", 2000, "Junior");
		teamRepository.save(team);

		Ranking ranking = new Ranking();
		ranking.setTeam(team);
		ranking.setPosition(position);
		ranking.setTotalScore(totalScore);
		Ranking saved = rankingRepository.save(ranking);
		currentRankingId = saved.getId();
	}

	@When("I request the rankings list")
	public void iRequestTheRankingsList() throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(
				get("/rankings")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	@When("I retrieve that ranking by id")
	public void iRetrieveThatRankingById() throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(
				get("/rankings/" + currentRankingId)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	@When("I retrieve ranking with id {int}")
	public void iRetrieveRankingWithId(int id) throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(
				get("/rankings/" + id)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	@And("the response contains position {int}")
	public void theResponseContainsPosition(int position) throws Exception {
		stepDefs.result.andExpect(jsonPath("$.position").value(position));
	}

	@And("the response contains totalScore {int}")
	public void theResponseContainsTotalScore(int totalScore) throws Exception {
		stepDefs.result.andExpect(jsonPath("$.totalScore").value(totalScore));
	}
}
