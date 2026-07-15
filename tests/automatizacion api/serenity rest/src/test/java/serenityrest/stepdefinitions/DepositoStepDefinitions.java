package serenityrest.stepdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
 * StepDefinitions — TX-02 Dep\u00f3sitos y consignaciones (Efectivo)
 *
 * Invariantes de red corporativa NTT:
 *   - RestAssured.useRelaxedHTTPSValidation() en @Before (proxy MITM)
 *   - PROHIBIDO Tasks.instrumented() en API tests
 */
public class DepositoStepDefinitions {

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

    @Dado("el actor est\u00e1 autorizado para operar en la API de dep\u00f3sitos")
    public void elActorEstaAutorizadoParaDepositos() {
        // La ability CallAnApi ya fue configurada en @Before
    }

    // ── Cuando ───────────────────────────────────────────────────────────────

    // Llamada REST directa — PROHIBIDO Tasks.instrumented() en API tests sin WebDriver
    @Cuando("realiza un dep\u00f3sito en efectivo")
    public void realizaDepositoEnEfectivo() {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Pagos.DEPOSITO)
                .with(requestSpec -> requestSpec
                    .headers(TestData.depositoHeaders())
                    .body(TestData.depositoPayload()))
        );
    }

    // ── Entonces ─────────────────────────────────────────────────────────────

    @Entonces("la transacci\u00f3n de dep\u00f3sito es exitosa con c\u00f3digo {string}")
    public void laTransaccionDepositoEsExitosa(String codigoEsperado) {
        assertThat(
            "HTTP status code debe ser 200",
            actor.asksFor(TheResponse.statusCode()),
            equalTo(200)
        );
        assertThat(
            "msgRsHdr.status.statusCode debe ser " + codigoEsperado,
            actor.asksFor(TheResponse.fieldAsString("msgRsHdr.status.statusCode")),
            equalTo(codigoEsperado)
        );
    }

    @Entonces("la severidad del dep\u00f3sito es {string}")
    public void laSeveridadDelDepositoEs(String severidadEsperada) {
        assertThat(
            "msgRsHdr.status.severity debe ser " + severidadEsperada,
            actor.asksFor(TheResponse.fieldAsString("msgRsHdr.status.severity")),
            equalTo(severidadEsperada)
        );
    }

    @Entonces("la descripci\u00f3n del dep\u00f3sito es {string}")
    public void laDescripcionDelDepositoEs(String descripcionEsperada) {
        assertThat(
            "msgRsHdr.status.statusDesc debe ser " + descripcionEsperada,
            actor.asksFor(TheResponse.fieldAsString("msgRsHdr.status.statusDesc")),
            equalTo(descripcionEsperada)
        );
    }

    @Entonces("el campo endDt del dep\u00f3sito est\u00e1 presente")
    public void elCampoEndDtDelDepositoEstaPresente() {
        assertThat(
            "endDt debe estar presente en la respuesta",
            actor.asksFor(TheResponse.fieldIsNotNull("endDt")),
            is(true)
        );
    }
}
