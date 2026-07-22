package serenityrest.screenplay.tasks.recaudo;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import serenityrest.utils.ApiEndpoints;
import serenityrest.utils.TestData;

/**
 * TX-03 Paso 1 — Consulta de factura (orquestador Everest)
 * POST /everest/orq/consultas/api/v1/consulta
 * X-RqUID: 003001 | X-Channel: CBV | X-CompanyId: 00010016
 *
 * Respuesta esperada (validada en vivo):
 *   HTTP 200
 *   msgRsHdr.status.statusCode    = "200"
 *   data.Agreement.Name           = "ETB EMPRESA TELEFONOS BOGOTA"
 *   data.TotalCurAmt.Amt          = 10000.0
 *   data.AcctBal[0].Desc          = "ValorPrincipal"
 *
 * DEBE ejecutarse SIEMPRE antes de PagarFactura.
 * PROHIBIDO Tasks.instrumented() en API tests (sin WebDriver activo).
 */
public class ConsultarFactura implements Task {

    private final String xRqUID;

    private ConsultarFactura(String xRqUID) {
        this.xRqUID = xRqUID;
    }

    /** Factory method — consulta TX-03 con X-RqUID 003001 */
    public static ConsultarFactura paraTx03() {
        return new ConsultarFactura("003001");
    }

    /** Factory method genérico con X-RqUID personalizado */
    public static ConsultarFactura conRqUID(String xRqUID) {
        return new ConsultarFactura(xRqUID);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Consultas.CONSULTA_FACTURA)
                .with(requestSpec -> requestSpec
                    .headers(TestData.consultaFacturaHeaders(xRqUID))
                    .body(TestData.consultaFacturaPayload(xRqUID)))
        );
    }
}
