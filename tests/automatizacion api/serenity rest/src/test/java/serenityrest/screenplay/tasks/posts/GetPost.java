package serenityrest.screenplay.tasks.posts;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import serenityrest.utils.ApiEndpoints;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * GetPost
 * Task: obtener posts de JSONPlaceholder.
 * La Task SOLO ejecuta la llamada — no hace aserciones.
 *
 * Uso:
 *   theActor.attemptsTo(GetPost.withId(1));
 *   theActor.attemptsTo(GetPost.all());
 */
public class GetPost implements Task {

    private final Integer postId;  // null = obtener todos

    private GetPost(Integer postId) {
        this.postId = postId;
    }

    /** Obtiene un post específico por ID */
    public static GetPost withId(int postId) {
        return instrumented(GetPost.class, postId);
    }

    /** Obtiene todos los posts */
    public static GetPost all() {
        return instrumented(GetPost.class, (Object) null);
    }

    @Override
    @Step("{0} obtiene post(s) de JSONPlaceholder")
    public <T extends Actor> void performAs(T actor) {
        if (postId == null) {
            actor.attemptsTo(Get.resource(ApiEndpoints.Posts.ALL));
        } else {
            actor.attemptsTo(
                Get.resource(ApiEndpoints.Posts.BY_ID)
                   .with(request -> request.pathParam("id", postId))
            );
        }
    }
}

