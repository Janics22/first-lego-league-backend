package cat.udl.eps.softarch.fll.steps.edition;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import cat.udl.eps.softarch.fll.steps.app.AuthenticationStepDefs;
import cat.udl.eps.softarch.fll.steps.app.StepDefs;
import org.springframework.http.MediaType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchEditionByVenueNameStepDefs {

	private final StepDefs stepDefs;

	public SearchEditionByVenueNameStepDefs(StepDefs stepDefs) {
		this.stepDefs = stepDefs;
    }

	@Given("an edition exists with year {int}, venue name {string} and description {string}")
	public void an_edition_exists(int year, String venueName, String description) throws Exception {
		Map<String, Object> body = new HashMap<>();
		body.put("year", year);
		body.put("venueName", venueName);
		body.put("description", description);

		stepDefs.mockMvc.perform(post("/editions")
				.contentType(MediaType.APPLICATION_JSON)
 				.content(stepDefs.mapper.writeValueAsString(body))
				.characterEncoding(StandardCharsets.UTF_8)
				.with(AuthenticationStepDefs.authenticate()))
				.andExpect(status().isCreated());
	}

	@When("I search for an edition by venue name {string}")
	public void i_search_for_an_edition_by_venue_name(String venueName) throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(get("/editions/search")
				.param("venueName", venueName)
				.with(AuthenticationStepDefs.authenticate()));
	}

	@Then("the edition search response status should be {int}")
	public void the_edition_search_response_status_should_be(int expectedStatus) throws Exception {
		stepDefs.result.andExpect(status().is(expectedStatus));
	}

	@Then("the edition search response should contain an edition with venue name {string}")
	public void the_edition_search_response_should_contain(String expectedVenueName) throws Exception {
		stepDefs.result.andExpect(jsonPath("$._embedded.editions[0].venueName").value(expectedVenueName));
	}

	@Then("the edition search response should be empty")
	public void the_edition_search_response_should_be_empty() throws Exception {
		stepDefs.result.andExpect(jsonPath("$._embedded.editions", hasSize(0)));
	}
}