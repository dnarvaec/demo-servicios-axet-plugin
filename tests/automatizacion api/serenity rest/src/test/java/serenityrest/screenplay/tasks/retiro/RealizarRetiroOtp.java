package serenityrest.screenplay.tasks.retiro;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import serenityrest.utils.ApiEndpoints;
import serenityrest.utils.TestData;

/**
 * TX-01 — Retiro de efectivo con OTP
 * POST /api/v1/pagos/retiro
 * X-RqUID: 001001 | X-Channel: ATM | X-CompanyId: BANCO_BOGOTA
 *
 * Respuesta esperada (validada en vivo):
 *   HTTP 200
 *   msgRsHdr.status.statusCode = "200"
 *   msgRsHdr.status.severity   = "Info"
 *   msgRsHdr.status.statusDesc = "Transaccion exitosa"
 *   endDt                      = timestamp (notNullValue)
 *
 * PROHIBIDO Tasks.instrumented() en API tests (sin WebDriver activo).
 */
public class RealizarRetiroOtp implements Task {

    private RealizarRetiroOtp() {}

    /** Factory method — retiro con datos de prueba estándar */
    public static RealizarRetiroOtp conDatosEstandar() {
        return new RealizarRetiroOtp();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Pagos.RETIRO)
                .with(requestSpec -> requestSpec
                    .headers(TestData.retiroHeaders())
                    .body(TestData.retiroOtpPayload()))
        );
    }
}
