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
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Patch;
import net.serenitybdd.screenplay.rest.interactions.Post;
import serenityrest.screenplay.abilities.CallTheApi;
import serenityrest.screenplay.questions.TheResponse;
import serenityrest.utils.ApiEndpoints;
import serenityrest.utils.TestData;

public class PostsStepDefinitions {

    @Before
    public void setStage() {
        RestAssured.useRelaxedHTTPSValidation();
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("el actor puede consumir la API de JSONPlaceholder")
    public void elActorPuedeConsumir() {
        OnStage.theActorCalled("API Tester")
               .whoCan(CallTheApi.at(ApiEndpoints.JSONPLACEHOLDER));
    }

    @When("obtiene todos los posts")
    public void obtieneTodosLosPosts() {
        OnStage.theActorInTheSpotlight().attemptsTo(Get.resource(ApiEndpoints.Posts.ALL));
    }

    @When("obtiene el post con ID {int}")
    public void obtieneElPostConId(int postId) {
        OnStage.theActorInTheSpotlight().attemptsTo(
            Get.resource(ApiEndpoints.Posts.BY_ID)
               .with(req -> req.pathParam("id", postId))
        );
    }

    @When("crea un nuevo post con t\u00edtulo {string}")
    public void creaUnNuevoPost(String title) {
        OnStage.theActorInTheSpotlight().attemptsTo(
            Post.to(ApiEndpoints.Posts.ALL)
                .with(req -> req
                    .header("Content-Type", "application/json")
                    .body(TestData.createPostPayload(title, "Body generado automaticamente", 1)))
        );
    }

    @When("crea un post con los datos por defecto")
    public void creaUnPostConDatosPorDefecto() {
        OnStage.theActorInTheSpotlight().attemptsTo(
            Post.to(ApiEndpoints.Posts.ALL)
                .with(req -> req
                    .header("Content-Type", "application/json")
                    .body(TestData.defaultCreatePostPayload()))
        );
    }

    @When("actualiza el post {int} con t\u00edtulo {string}")
    public void actualizaElPost(int postId, String title) {
        OnStage.theActorInTheSpotlight().attemptsTo(
            Patch.to(ApiEndpoints.Posts.BY_ID)
                 .with(req -> req
                     .pathParam("id", postId)
                     .header("Content-Type", "application/json")
                     .body(TestData.patchPostPayload(title)))
        );
    }

    @When("elimina el post con ID {int}")
    public void eliminaElPost(int postId) {
        OnStage.theActorInTheSpotlight().attemptsTo(
            Delete.from(ApiEndpoints.Posts.BY_ID)
                  .with(req -> req.pathParam("id", postId))
        );
    }

    @Then("el status code de la respuesta es {int}")
    public void elStatusCodeEs(int expectedCode) {
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.statusCode(), equalTo(expectedCode)));
    }

    @Then("el post tiene ID {int}")
    public void elPostTieneId(int expectedId) {
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.field("id"), equalTo(expectedId)));
    }

    @Then("el post tiene un t\u00edtulo no nulo")
    public void elPostTieneUnTituloNoNulo() {
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.fieldIsNotNull("title"), is(true)));
    }

    @Then("el post fue creado con un ID asignado")
    public void elPostFueCreadoConId() {
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.statusCode(), equalTo(201)));
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.fieldIsNotNull("id"), is(true)));
    }

    @Then("la respuesta contiene una lista de posts")
    public void laRespuestaContieneListaDePosts() {
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.statusCode(), equalTo(200)));
        OnStage.theActorInTheSpotlight().should(seeThat(TheResponse.fieldIsNotNull(""), is(true)));
    }
}
