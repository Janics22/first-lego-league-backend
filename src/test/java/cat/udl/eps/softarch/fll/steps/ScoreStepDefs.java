package cat.udl.eps.softarch.fll.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.MediaType;
import cat.udl.eps.softarch.fll.domain.Match;
import cat.udl.eps.softarch.fll.domain.Round;
import cat.udl.eps.softarch.fll.domain.Team;
import cat.udl.eps.softarch.fll.repository.MatchRepository;
import cat.udl.eps.softarch.fll.repository.RoundRepository;
import cat.udl.eps.softarch.fll.repository.TeamRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import net.minidev.json.JSONObject;

public class ScoreStepDefs {

	private final StepDefs stepDefs;
	private final RoundRepository roundRepository;
	private final MatchRepository matchRepository;
	private final TeamRepository teamRepository;

	private Long roundId;
	private String roundScoresUrl;
	private String participatingTeamUri;
	private String nonParticipatingTeamUri;
	private String matchUri;

	private final Map<String, String> teamUriByName = new HashMap<>();

	public ScoreStepDefs(
			StepDefs stepDefs,
			RoundRepository roundRepository,
			MatchRepository matchRepository,
			TeamRepository teamRepository
	) {
		this.stepDefs = stepDefs;
		this.roundRepository = roundRepository;
		this.matchRepository = matchRepository;
		this.teamRepository = teamRepository;
	}

	@Given("The score dependencies exist")
	public void theScoreDependenciesExist() throws Exception {
		String suffix = UUID.randomUUID().toString().substring(0, 8);
		Round round = createRound();

		this.roundId = round.getId();
		this.roundScoresUrl = "/rounds/" + this.roundId + "/scores";

		participatingTeamUri = createTeam("TeamA-" + suffix);
		teamUriByName.put("TeamA", participatingTeamUri);

		nonParticipatingTeamUri = createTeam("TeamB-" + suffix);
		teamUriByName.put("TeamB", nonParticipatingTeamUri);

		Match match = new Match();
		match.setRound(round);
		match = matchRepository.save(match);
		matchUri = "http://localhost/matches/" + match.getId();

		JSONObject matchResultJson = new JSONObject();
		matchResultJson.put("score", 0);
		matchResultJson.put("team", participatingTeamUri);
		matchResultJson.put("match", matchUri);

		var mrRes = stepDefs.mockMvc.perform(
						post("/matchResults")
								.contentType(MediaType.APPLICATION_JSON)
								.content(matchResultJson.toString())
								.characterEncoding(StandardCharsets.UTF_8)
								.with(AuthenticationStepDefs.authenticate()))
				.andReturn()
				.getResponse();

		if (mrRes.getStatus() != 201) {
			throw new RuntimeException(
					"ERROR CREATING MATCH RESULT. Status: "
							+ mrRes.getStatus()
							+ " Body: "
							+ mrRes.getContentAsString()
			);
		}
	}

	@Given("a round exists with id {int} and a team {string} participates in round {int}")
	public void aRoundExistsAndATeamParticipatesInRound(
			int ignoredRoundId,
			String teamName,
			int ignoredRoundIdAgain
	) throws Exception {
		teamUriByName.clear();

		Round round = createRound();
		this.roundId = round.getId();
		this.roundScoresUrl = "/rounds/" + this.roundId + "/scores";

		participatingTeamUri = createTeam(teamName + "-" + UUID.randomUUID().toString().substring(0, 8));
		teamUriByName.put(teamName, participatingTeamUri);

		createMatchResult(round, participatingTeamUri, 0);
	}

	@Given("a round exists with id {int}")
	public void aRoundExistsWithId(int ignoredRoundId) {
		teamUriByName.clear();

		Round round = createRound();
		this.roundId = round.getId();
		this.roundScoresUrl = "/rounds/" + this.roundId + "/scores";
	}

	@Given("a team {string} exists but does not participate in round {int}")
	public void aTeamExistsButDoesNotParticipateInRound(String teamName, int ignoredRoundId) {
		nonParticipatingTeamUri = createTeam(teamName + "-" + UUID.randomUUID().toString().substring(0, 8));
		teamUriByName.put(teamName, nonParticipatingTeamUri);
	}

	@When("I submit {int} points for the participating team")
	public void iSubmitPointsForParticipatingTeam(int points) throws Exception {
		JSONObject payload = new JSONObject();
		payload.put("team", participatingTeamUri);
		payload.put("points", points);

		stepDefs.result = stepDefs.mockMvc.perform(
				post(roundScoresUrl)
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload.toString())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()));
	}

	@When("I submit {int} points for a non participating team")
	public void iSubmitPointsForNonParticipatingTeam(int points) throws Exception {
		JSONObject payload = new JSONObject();
		payload.put("team", nonParticipatingTeamUri);
		payload.put("points", points);

		stepDefs.result = stepDefs.mockMvc.perform(
				post(roundScoresUrl)
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload.toString())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()));
	}

	@When("I submit {int} points for team {string} in round {int}")
	public void iSubmitPointsForTeamInRound(int points, String teamReference, int ignoredRoundId) throws Exception {
		String teamUri = resolveTeamUri(teamReference);

		JSONObject payload = new JSONObject();
		payload.put("team", teamUri);
		payload.put("points", points);

		stepDefs.result = stepDefs.mockMvc.perform(
				post(roundScoresUrl)
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload.toString())
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()));
	}

	@When("I request the scores for the round")
	public void iRequestTheScoresForTheRound() throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(
				get(roundScoresUrl)
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()));
	}

	@When("I request the scores for round {int}")
	public void iRequestTheScoresForRound(int ignoredRoundId) throws Exception {
		iRequestTheScoresForTheRound();
	}

	private Round createRound() {
		String suffix = UUID.randomUUID().toString().substring(0, 8);

		Round round = new Round();
		round.setNumber(Math.abs(suffix.hashCode() % 10000) + 1);

		return roundRepository.save(round);
	}

	private String createTeam(String name) {
		Team team = new Team(name);
		team.setCity("Igualada");
		team.setFoundationYear(2000);
		team.setCategory("Junior");
		team.setInscriptionDate(LocalDate.now());

		Team saved = teamRepository.save(team);
		return "/teams/" + saved.getId();
	}

	private void createMatchResult(Round round, String teamUri, int initialScore) throws Exception {
		Match match = new Match();
		match.setRound(round);
		match = matchRepository.save(match);
		matchUri = "http://localhost/matches/" + match.getId();

		JSONObject matchResultJson = new JSONObject();
		matchResultJson.put("score", initialScore);
		matchResultJson.put("team", teamUri);
		matchResultJson.put("match", matchUri);

		var mrRes = stepDefs.mockMvc.perform(
						post("/matchResults")
								.contentType(MediaType.APPLICATION_JSON)
								.content(matchResultJson.toString())
								.characterEncoding(StandardCharsets.UTF_8)
								.with(AuthenticationStepDefs.authenticate()))
				.andReturn()
				.getResponse();

		if (mrRes.getStatus() != 201) {
			throw new RuntimeException(
					"ERROR CREATING MATCH RESULT. Status: "
							+ mrRes.getStatus()
							+ " Body: "
							+ mrRes.getContentAsString()
			);
		}
	}

	private String resolveTeamUri(String teamReference) {
		if (teamReference.startsWith("/teams/")) {
			String alias = teamReference.substring("/teams/".length());
			String mappedUri = teamUriByName.get(alias);
			if (mappedUri != null) {
				return mappedUri;
			}
		}
		return teamReference;
	}
}
