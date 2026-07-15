package serenityrest.screenplay.tasks.recaudo;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import serenityrest.utils.ApiEndpoints;
import serenityrest.utils.TestData;

/**
 * TX-03 Paso 2 — Recaudo de convenios / pago de factura (Efectivo)
 * POST /api/v1/pagos/pago-factura
 * X-RqUID: 003001 | X-Channel: ATM | X-CompanyId: BANCO_BOGOTA
 *
 * Respuesta esperada (validada en vivo):
 *   HTTP 200
 *   msgRsHdr.status.statusCode = "200"
 *   msgRsHdr.status.severity   = "Info"
 *   msgRsHdr.status.statusDesc = "Transaccion exitosa"
 *   endDt                      = timestamp (notNullValue)
 *
 * SIEMPRE se ejecuta después de ConsultarFactura (flujo de dos pasos).
 * PROHIBIDO Tasks.instrumented() en API tests (sin WebDriver activo).
 */
public class PagarFactura implements Task {

    private PagarFactura() {}

    /** Factory method — pago de factura TX-03 con datos derivados de la consulta */
    public static PagarFactura conDatosEstandar() {
        return new PagarFactura();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Pagos.PAGO_FACTURA)
                .with(requestSpec -> requestSpec
                    .headers(TestData.pagoFacturaHeaders())
                    .body(TestData.pagoFacturaPayload()))
        );
    }
}
