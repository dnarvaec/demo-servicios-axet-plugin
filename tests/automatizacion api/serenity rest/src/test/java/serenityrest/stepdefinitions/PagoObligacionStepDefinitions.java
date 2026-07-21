package serenityrest.stepdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.restassured.RestAssured;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import serenityrest.screenplay.questions.TheResponse;
import serenityrest.utils.ApiEndpoints;
import serenityrest.utils.TestData;

/**
 * StepDefinitions — TX-04 Pago de obligaciones y TC Aval (Efectivo)
 *
 * Invariantes de red corporativa NTT:
 *   - RestAssured.useRelaxedHTTPSValidation() en @Before (proxy MITM)
 *   - PROHIBIDO Tasks.instrumented() en API tests
 */
public class PagoObligacionStepDefinitions {

    private Actor actor;

    @Before
    public void configurarEscenario() {
        // Obligatorio en red NTT/corporativa — proxy MITM con certificado propio
        RestAssured.useRelaxedHTTPSValidation();
        OnStage.setTheStage(Cast.ofStandardActors());
        actor = OnStage.theActorCalled("API Tester");
        actor.whoCan(CallAnApi.at(ApiEndpoints.API_BASE_URL));
    }

    @After
    public void cerrarEscenario() {
        OnStage.drawTheCurtain();
    }

    // ── Dado ──────────────────────────────────────────────────────────────────

    @Dado("el actor est\u00e1 autorizado para operar en la API de pago de obligaciones")
    public void elActorEstaAutorizadoParaPagoObligaciones() {
        // La ability CallAnApi ya fue configurada en @Before
    }

    // ── Cuando ───────────────────────────────────────────────────────────────

    // Llamada REST directa — PROHIBIDO Tasks.instrumented() en API tests sin WebDriver
    @Cuando("realiza el pago de la obligaci\u00f3n TC Aval")
    public void realizaElPagoDeLaObligacion() {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Pagos.PAGO_OBLIGACIONES)
                .with(requestSpec -> requestSpec
                    .headers(TestData.pagoObligacionHeaders())
                    .body(TestData.pagoObligacionPayload()))
        );
    }

    // ── Entonces ─────────────────────────────────────────────────────────────

    @Entonces("el pago de obligaci\u00f3n es exitoso con HTTP {int}")
    public void elPagoDeObligacionEsExitosoConHttp(int codigoEsperado) {
        assertThat(
            "HTTP status code debe ser " + codigoEsperado,
            actor.asksFor(TheResponse.statusCode()),
            equalTo(codigoEsperado)
        );
    }
}
