package serenityrest.screenplay.tasks.users;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import serenityrest.utils.ApiEndpoints;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetUser implements Task {

    private final Integer userId;

    private GetUser(Integer userId) {
        this.userId = userId;
    }

    public static GetUser withId(int userId) {
        return instrumented(GetUser.class, userId);
    }

    @Override
    @Step("{0} obtiene el usuario con ID #userId de JSONPlaceholder")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Get.resource(ApiEndpoints.Users.BY_ID)
               .with(request -> request.pathParam("id", userId))
        );
    }
}