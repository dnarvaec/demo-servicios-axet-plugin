package serenityrest.screenplay.tasks.deposito;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import serenityrest.utils.ApiEndpoints;
import serenityrest.utils.TestData;

/**
 * TX-02 — Depósitos y consignaciones (Efectivo)
 * POST /api/v1/pagos/deposito
 * X-RqUID: 002001 | X-Channel: ATM | X-CompanyId: BANCO_BOGOTA
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
public class RealizarDeposito implements Task {

    private RealizarDeposito() {}

    /** Factory method — depósito en efectivo con datos de prueba estándar */
    public static RealizarDeposito conDatosEstandar() {
        return new RealizarDeposito();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Pagos.DEPOSITO)
                .with(requestSpec -> requestSpec
                    .headers(TestData.depositoHeaders())
                    .body(TestData.depositoPayload()))
        );
    }
}
