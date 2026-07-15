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
 * StepDefinitions — TX-03 Recaudo de convenios (Efectivo)
 * Flujo de dos pasos: ConsultarFactura → PagarFactura
 *
 * Invariantes de red corporativa NTT:
 *   - RestAssured.useRelaxedHTTPSValidation() en @Before (proxy MITM)
 *   - PROHIBIDO Tasks.instrumented() en API tests
 */
public class RecaudoStepDefinitions {

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

    @Dado("el actor est\u00e1 autorizado para operar en la API de recaudo")
    public void elActorEstaAutorizadoParaRecaudo() {
        // La ability CallAnApi ya fue configurada en @Before
    }

    // ── Cuando — Paso 1: Consulta ─────────────────────────────────────────────
    // Llamada REST directa — PROHIBIDO Tasks.instrumented() en API tests sin WebDriver

    @Cuando("consulta la factura del convenio TX-03")
    public void consultaLaFacturaDelConvenio() {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Consultas.CONSULTA_FACTURA)
                .with(requestSpec -> requestSpec
                    .headers(TestData.consultaFacturaHeaders("003001"))
                    .body(TestData.consultaFacturaPayload()))
        );
    }

    // ── Cuando — Paso 2: Pago ─────────────────────────────────────────────────

    @Cuando("realiza el pago de la factura del convenio")
    public void realizaElPagoDeLaFactura() {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Pagos.PAGO_FACTURA)
                .with(requestSpec -> requestSpec
                    .headers(TestData.pagoFacturaHeaders())
                    .body(TestData.pagoFacturaPayload()))
        );
    }

    // ── Entonces — Validaciones de Consulta (Paso 1) ─────────────────────────

    @Entonces("la consulta de factura es exitosa con c\u00f3digo {string}")
    public void laConsultaDeFacturaEsExitosa(String codigoEsperado) {
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

    @Entonces("la respuesta contiene el nombre del convenio")
    public void laRespuestaContieneElNombreDelConvenio() {
        assertThat(
            "data.Agreement.Name debe estar presente",
            actor.asksFor(TheResponse.fieldIsNotNull("data.Agreement.Name")),
            is(true)
        );
    }

    @Entonces("la respuesta contiene el monto total a pagar")
    public void laRespuestaContieneElMontoTotalAPagar() {
        assertThat(
            "data.TotalCurAmt.Amt debe estar presente",
            actor.asksFor(TheResponse.fieldIsNotNull("data.TotalCurAmt.Amt")),
            is(true)
        );
    }

    @Entonces("los saldos de la factura est\u00e1n presentes")
    public void losSaldosDeLaFacturaEstanPresentes() {
        assertThat(
            "data.AcctBal debe estar presente",
            actor.asksFor(TheResponse.fieldIsNotNull("data.AcctBal")),
            is(true)
        );
    }

    // ── Entonces — Validaciones de Pago (Paso 2) ─────────────────────────────

    @Entonces("el pago de la factura es exitoso con c\u00f3digo {string}")
    public void elPagoDeLaFacturaEsExitoso(String codigoEsperado) {
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

    @Entonces("la severidad del recaudo es {string}")
    public void laSeveridadDelRecaudoEs(String severidadEsperada) {
        assertThat(
            "msgRsHdr.status.severity debe ser " + severidadEsperada,
            actor.asksFor(TheResponse.fieldAsString("msgRsHdr.status.severity")),
            equalTo(severidadEsperada)
        );
    }

    @Entonces("la descripci\u00f3n del recaudo es {string}")
    public void laDescripcionDelRecaudoEs(String descripcionEsperada) {
        assertThat(
            "msgRsHdr.status.statusDesc debe ser " + descripcionEsperada,
            actor.asksFor(TheResponse.fieldAsString("msgRsHdr.status.statusDesc")),
            equalTo(descripcionEsperada)
        );
    }

    @Entonces("el campo endDt del recaudo est\u00e1 presente")
    public void elCampoEndDtDelRecaudoEstaPresente() {
        assertThat(
            "endDt debe estar presente en la respuesta",
            actor.asksFor(TheResponse.fieldIsNotNull("endDt")),
            is(true)
        );
    }
}
