package serenityrest.screenplay.tasks.todos;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import serenityrest.utils.ApiEndpoints;

/**
 * GetTodo
 * ─────────────────────────────────────────────────────────────────────────────
 * Task: Obtener un todo por ID desde JSONPlaceholder.
 * Patrón Screenplay: Task orquesta la interacción GET.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class GetTodo implements Task {

    private final int todoId;

    private GetTodo(int todoId) {
        this.todoId = todoId;
    }

    /**
     * Factory method: obtener todo por ID.
     *
     * @param id ID del todo a consultar
     * @return instancia de GetTodo
     */
    public static GetTodo withId(int id) {
        return new GetTodo(id);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource(ApiEndpoints.Todos.BY_ID)
                        .with(request -> request
                                .pathParam("id", todoId)
                                .baseUri(ApiEndpoints.JSONPLACEHOLDER)
                                .header("Accept", "application/json")
                        )
        );
    }
}
