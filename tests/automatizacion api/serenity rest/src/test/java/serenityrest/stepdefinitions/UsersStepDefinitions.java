package serenityrest.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.interactions.Get;
import serenityrest.screenplay.abilities.CallTheApi;
import serenityrest.screenplay.questions.TheResponse;
import serenityrest.utils.ApiEndpoints;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.*;

public class UsersStepDefinitions {

    private Actor theActor;

    @Before
    public void setStage() {
        RestAssured.useRelaxedHTTPSValidation();
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("el actor puede consumir la API de JSONPlaceholder para usuarios")
    public void elActorPuedeConsumir() {
        theActor = OnStage.theActorCalled("API Tester");
        theActor.whoCan(CallTheApi.at(ApiEndpoints.JSONPLACEHOLDER));
    }

    @When("obtiene el usuario con ID {int}")
    public void obtieneElUsuarioConId(int userId) {
        theActor.attemptsTo(
            Get.resource(ApiEndpoints.Users.BY_ID)
               .with(req -> req.pathParam("id", userId))
        );
    }

    @Then("el status code del usuario es {int}")
    public void elStatusCodeDelUsuarioEs(int expectedCode) {
        theActor.should(seeThat(TheResponse.statusCode(), equalTo(expectedCode)));
    }

    @Then("el usuario tiene ID {int}")
    public void elUsuarioTieneId(int expectedId) {
        theActor.should(seeThat(TheResponse.field("id"), equalTo(expectedId)));
    }

    @Then("el usuario tiene email no nulo")
    public void elUsuarioTieneEmailNoNulo() {
        theActor.should(seeThat(TheResponse.fieldIsNotNull("email"), is(true)));
    }
}