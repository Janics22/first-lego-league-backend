package cat.udl.eps.softarch.fll.steps.edition;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import cat.udl.eps.softarch.fll.repository.project.ProjectRoomRepository;
import cat.udl.eps.softarch.fll.steps.app.StepDefs;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.containsString;


public class ManageProjectRoomStepDefs {

    private final StepDefs stepDefs;
    private final ProjectRoomRepository projectRoomRepository;

    public ManageProjectRoomStepDefs(StepDefs stepDefs, ProjectRoomRepository projectRoomRepository) {
        this.stepDefs = stepDefs;
        this.projectRoomRepository = projectRoomRepository;
    }

    @Given("the project room system is empty")
    public void clearProjectRoomSystem() {
        projectRoomRepository.deleteAll();
    }

    @When("I request to create a project room with room number {string}")
    public void requestCreateProjectRoom(String roomNumber) throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("roomNumber", roomNumber);
        stepDefs.result = stepDefs.mockMvc.perform(post("/projectRooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stepDefs.mapper.writeValueAsString(body))
                .characterEncoding(StandardCharsets.UTF_8)
                .with(user("admin").roles("ADMIN")));
    }

    @When("I request to create a project room unauthenticated with room number {string}")
    public void requestCreateProjectRoomUnauthenticated(String roomNumber) throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("roomNumber", roomNumber);
        stepDefs.result = stepDefs.mockMvc.perform(post("/projectRooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stepDefs.mapper.writeValueAsString(body))
                .characterEncoding(StandardCharsets.UTF_8)
                .with(anonymous()));
    }

    @When("I request to retrieve project room {string}")
    public void requestRetrieveProjectRoom(String roomNumber) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(get("/projectRooms/" + roomNumber));
    }

    @When("I request to delete project room {string}")
    public void requestDeleteProjectRoom(String roomNumber) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(delete("/projectRooms/" + roomNumber)
                .with(user("admin").roles("ADMIN")));
    }

    @When("I request to delete project room {string} unauthenticated")
    public void requestDeleteProjectRoomUnauthenticated(String roomNumber) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(delete("/projectRooms/" + roomNumber)
                .with(anonymous()));
    }

    @Then("the project room API response status should be {int}")
    public void projectRoomApiResponseStatus(int expectedStatus) throws Exception {
        stepDefs.result.andExpect(status().is(expectedStatus));
    }

    @Then("the response should contain room number {string}")
    public void responseContainsRoomNumber(String roomNumber) throws Exception {
        stepDefs.result.andExpect(jsonPath("$._links.self.href", containsString(roomNumber)));
    }
}
