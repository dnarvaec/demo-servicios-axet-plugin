package serenityrest.screenplay.tasks.posts;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;
import net.serenitybdd.screenplay.rest.interactions.Patch;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import serenityrest.utils.ApiEndpoints;
import serenityrest.utils.TestData;

import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * CreatePost — Task: POST /posts
 * UpdatePost — Task: PUT /posts/{id}
 * PatchPost  — Task: PATCH /posts/{id}
 * DeletePost — Task: DELETE /posts/{id}
 * ─────────────────────────────────────────────────────────────────────────────
 * Todas las Tasks de escritura sobre el recurso /posts en un mismo archivo
 * para cohesión. Las Tasks SOLO ejecutan la llamada, sin aserciones.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class CreatePost implements Task {

    private final Map<String, Object> payload;

    private CreatePost(Map<String, Object> payload) {
        this.payload = payload;
    }

    /** Crea un post con payload personalizado */
    public static CreatePost with(Map<String, Object> payload) {
        return instrumented(CreatePost.class, payload);
    }

    /** Crea un post con datos de demo por defecto */
    public static CreatePost withDefaults() {
        return instrumented(CreatePost.class, TestData.defaultCreatePostPayload());
    }

    /** Crea un post con un título específico */
    public static CreatePost withTitle(String title) {
        return instrumented(CreatePost.class,
            TestData.createPostPayload(title, "Body generado automáticamente", 1));
    }

    @Override
    @Step("{0} crea un nuevo post con title={payload.title}")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to(ApiEndpoints.Posts.ALL)
                .with(request -> request
                    .header("Content-Type", "application/json")
                    .body(payload))
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Task interna: PUT /posts/{id}
    // Uso: theActor.attemptsTo(CreatePost.Update.post(1).with(payload));
    // ─────────────────────────────────────────────────────────────────────────
    public static class Update implements Task {
        private final int postId;
        private final Map<String, Object> payload;

        private Update(int postId, Map<String, Object> payload) {
            this.postId = postId;
            this.payload = payload;
        }

        public static Update post(int postId) {
            return new Update(postId, TestData.defaultCreatePostPayload());
        }

        public Update with(Map<String, Object> payload) {
            return new Update(this.postId, payload);
        }

        @Override
        @Step("{0} actualiza (PUT) el post con ID #postId")
        public <T extends Actor> void performAs(T actor) {
            actor.attemptsTo(
                Put.to(ApiEndpoints.Posts.BY_ID)
                   .with(request -> request
                       .pathParam("id", postId)
                       .header("Content-Type", "application/json")
                       .body(payload))
            );
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Task interna: PATCH /posts/{id}
    // Uso: theActor.attemptsTo(CreatePost.PartialUpdate.post(1).withTitle("Nuevo"));
    // ─────────────────────────────────────────────────────────────────────────
    public static class PartialUpdate implements Task {
        private final int postId;
        private final Map<String, Object> patch;

        private PartialUpdate(int postId, Map<String, Object> patch) {
            this.postId = postId;
            this.patch = patch;
        }

        public static PartialUpdate post(int postId) {
            return new PartialUpdate(postId, Map.of());
        }

        public PartialUpdate withTitle(String title) {
            return new PartialUpdate(this.postId, TestData.patchPostPayload(title));
        }

        @Override
        @Step("{0} actualiza parcialmente (PATCH) el post con ID #postId")
        public <T extends Actor> void performAs(T actor) {
            actor.attemptsTo(
                Patch.to(ApiEndpoints.Posts.BY_ID)
                     .with(request -> request
                         .pathParam("id", postId)
                         .header("Content-Type", "application/json")
                         .body(patch))
            );
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Task interna: DELETE /posts/{id}
    // Uso: theActor.attemptsTo(CreatePost.Remove.post(1));
    // ─────────────────────────────────────────────────────────────────────────
    public static class Remove implements Task {
        private final int postId;

        private Remove(int postId) { this.postId = postId; }

        public static Remove post(int postId) {
            return instrumented(Remove.class, postId);
        }

        @Override
        @Step("{0} elimina (DELETE) el post con ID #postId")
        public <T extends Actor> void performAs(T actor) {
            actor.attemptsTo(
                Delete.from(ApiEndpoints.Posts.BY_ID)
                      .with(request -> request.pathParam("id", postId))
            );
        }
    }
}
