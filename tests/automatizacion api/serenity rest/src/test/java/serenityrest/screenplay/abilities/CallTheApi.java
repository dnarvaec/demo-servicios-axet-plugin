package serenityrest.screenplay.abilities;

import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

/** Factory sobre CallAnApi - abstraccion de la Ability de llamadas HTTP. */
public final class CallTheApi {
    private CallTheApi() {}

    public static CallAnApi at(String baseUrl) {
        return CallAnApi.at(baseUrl);
    }
}