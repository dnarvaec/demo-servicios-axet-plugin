package serenityrest.screenplay.pages;

/**
 * Page Object — Pantalla de Selección de Rol de Ecopetrol
 * Aparece tras el login exitoso: muestra 3 cards (Empleado, Líder, People)
 * Selectores CSS derivados de inspección real.
 */
public class RolSelectionPage {

    /** Card "Líder" — identificado por texto visible */
    public static final String CARD_LIDER      = "text=L\u00edder";

    /** Card "Empleado" */
    public static final String CARD_EMPLEADO   = "text=Empleado";

    /** Card "People" */
    public static final String CARD_PEOPLE     = "text=People";

    /** Contenedor de bienvenida — verifica que se llegó a esta pantalla */
    public static final String TITULO_BIENVENIDOS = "text=Bienvenidos";

    /** Texto instruccional visible en la pantalla */
    public static final String TEXTO_ELIJA_ROL = "text=Elija su rol de usuario";
}
