package serenityrest.screenplay.tasks.users;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import serenityrest.utils.ApiEndpoints;
import serenityrest.utils.TestData;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * LoginUser
 * ─────────────────────────────────────────────────────────────────────────────
 * Task: autenticarse en ReqRes API y obtener el token.
 * Tras ejecutar esta Task, el token queda disponible via:
 *   SerenityRest.lastResponse().path("token")
 *
 * Uso:
 *   theActor.attemptsTo(LoginUser.withDefaultCredentials());
 *   String token = theActor.asksFor(TheResponse.field("token"));
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class LoginUser implements Task {

    private final String email;
    private final String password;

    private LoginUser(String email, String password) {
        this.email    = email;
        this.password = password;
    }

    /** Login con credenciales desde variables de entorno (REQRES_EMAIL, REQRES_PASSWORD) */
    public static LoginUser withDefaultCredentials() {
        String email    = System.getenv("REQRES_EMAIL")    != null ? System.getenv("REQRES_EMAIL")    : "eve.holt@reqres.in";
        String password = System.getenv("REQRES_PASSWORD") != null ? System.getenv("REQRES_PASSWORD") : "cityslicka";
        return instrumented(LoginUser.class, email, password);
    }

    /** Login con credenciales explícitas */
    public static LoginUser with(String email, String password) {
        return instrumented(LoginUser.class, email, password);
    }

    @Override
    @Step("{0} realiza login con email=#email")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Auth.LOGIN)
                .with(request -> request
                    .baseUri(ApiEndpoints.REQRES)
                    .header("Content-Type", "application/json")
                    .header("x-api-key", getApiKey())
                    .body(TestData.loginPayload(email, password)))
        );
    }

    private static String getApiKey() {
        String key = System.getenv("REQRES_API_KEY");
        return key != null ? key : "reqres-free-v1";
    }
}
