package serenityrest.stepdefinitions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.interactions.Get;
import serenityrest.screenplay.abilities.CallTheApi;
import serenityrest.screenplay.questions.TheResponse;
import serenityrest.utils.ApiEndpoints;

public class TodosStepDefinitions {

    @Before
    public void setStage() {
        RestAssured.useRelaxedHTTPSValidation();
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("el actor puede consumir la API de Todos")
    public void elActorPuedeConsumirTodos() {
        OnStage.theActorCalled("API Tester")
               .whoCan(CallTheApi.at(ApiEndpoints.JSONPLACEHOLDER));
    }

    @When("obtiene el todo con ID {int}")
    public void obtieneElTodoConId(int todoId) {
        OnStage.theActorInTheSpotlight().attemptsTo(
            Get.resource(ApiEndpoints.Todos.BY_ID)
               .with(req -> req
                   .pathParam("id", todoId)
                   .header("Accept", "application/json"))
        );
    }

    @Then("el todo tiene ID {int} y userId {int}")
    public void elTodoTieneIdYUserId(int todoId, int userId) {
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.field("id"), equalTo(todoId)));
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.field("userId"), equalTo(userId)));
    }

    @Then("el todo tiene un t\u00edtulo no nulo")
    public void elTodoTieneUnTituloNoNulo() {
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.fieldIsNotNull("title"), is(true)));
    }

    @Then("el campo completed es de tipo booleano")
    public void elCampoCompletedEsBooleano() {
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.fieldIsNotNull("completed"), is(true)));
    }
}
