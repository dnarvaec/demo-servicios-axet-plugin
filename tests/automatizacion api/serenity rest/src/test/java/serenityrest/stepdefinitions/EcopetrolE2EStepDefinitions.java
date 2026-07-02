package serenityrest.stepdefinitions;

import java.nio.file.Paths;

import org.assertj.core.api.Assertions;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import serenityrest.screenplay.pages.LoginPage;
import serenityrest.screenplay.pages.RolSelectionPage;
import serenityrest.screenplay.tasks.ecopetrol.SeleccionarRol;

/**
 * Step Definitions — E2E Ciclo de Vida App Central Ecopetrol
 * Combina automatización UI (Playwright) con verificaciones de estado.
 */
public class EcopetrolE2EStepDefinitions {

    private Actor actor;
    private Page  page;

    @Before("@ecopetrol")
    public void setStage() {
        RestAssured.useRelaxedHTTPSValidation();
        OnStage.setTheStage(new OnlineCast());
    }

    @After("@ecopetrol")
    public void tearDown() {
        if (actor != null) {
            try {
                BrowseTheWebWithPlaywright.as(actor).getBrowser().close();
            } catch (Exception ignored) {
                // El browser puede ya estar cerrado
            }
        }
        OnStage.drawTheCurtain();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  GIVEN
    // ──────────────────────────────────────────────────────────────────────────

    @Given("el actor inicia el navegador con Playwright")
    public void elActorIniciaElNavegador() {
        actor = OnStage.theActorCalled("E2E Tester");
        // Usar Chrome del sistema para evitar problemas de descarga en redes corporativas
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setExecutablePath(Paths.get("C:/Program Files/Google/Chrome/Application/chrome.exe"))
                .setHeadless(true);
        actor.whoCan(BrowseTheWebWithPlaywright.withOptions(options));
        page  = BrowseTheWebWithPlaywright.as(actor).getCurrentPage();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  WHEN
    // ──────────────────────────────────────────────────────────────────────────

    @When("el actor ingresa usuario {string} y contrase\u00f1a {string}")
    public void elActorIngresaCredenciales(String usuario, String contrasena) {
        page.navigate("https://app-central-ecopetrol-geeqdnayfth9d7cx.centralus-01.azurewebsites.net/login");
        page.waitForSelector(LoginPage.CAMPO_USUARIO);
        page.fill(LoginPage.CAMPO_USUARIO,  usuario);
        page.fill(LoginPage.CAMPO_PASSWORD, contrasena);
        // Esperar a que Angular procese los eventos de binding antes del clic
        page.waitForTimeout(500);
    }

    @When("hace clic en {string}")
    public void haceClicEn(String textoBoton) {
        // Esperar que el botón esté habilitado (Angular reactive forms activan el botón al llenar campos)
        page.waitForSelector(LoginPage.BOTON_LOGIN);
        page.click(LoginPage.BOTON_LOGIN);
    }

    @When("selecciona el rol L\u00edder")
    public void seleccionaElRolLider() {
        actor.attemptsTo(SeleccionarRol.lider());
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  THEN
    // ──────────────────────────────────────────────────────────────────────────

    @Then("debe ver la pantalla de bienvenida con los roles disponibles")
    public void debeVerLaPantallaDeBienvenida() {
        page.waitForSelector(RolSelectionPage.TITULO_BIENVENIDOS, new Page.WaitForSelectorOptions().setTimeout(15000));
        Assertions.assertThat(page.isVisible(RolSelectionPage.TITULO_BIENVENIDOS))
                  .as("La pantalla de bienvenida debe estar visible")
                  .isTrue();
        Assertions.assertThat(page.isVisible(RolSelectionPage.TEXTO_ELIJA_ROL))
                  .as("El texto 'Elija su rol de usuario' debe estar visible")
                  .isTrue();
    }

    @And("el rol {string} est\u00e1 disponible y puede ser seleccionado")
    public void elRolEstaDisponible(String nombreRol) {
        String selector = "text=" + nombreRol;
        Assertions.assertThat(page.isVisible(selector))
                  .as("El card del rol '" + nombreRol + "' debe estar visible")
                  .isTrue();
    }

    @Then("accede al dashboard correspondiente al rol L\u00edder")
    public void accedeAlDashboard() {
        // Después de seleccionar el rol, esperar a que la URL o el contenido cambie
        page.waitForLoadState();
        String urlActual = page.url();
        Assertions.assertThat(urlActual)
                  .as("La URL debe haber cambiado desde la pantalla de bienvenida")
                  .isNotEqualTo("https://app-central-ecopetrol-geeqdnayfth9d7cx.centralus-01.azurewebsites.net/login");
    }

    @Then("la pantalla de login permanece visible o muestra un mensaje de error")
    public void laPantallaDeLoginPermanece() {
        // Esperar un momento para que la app responda al intento de login
        page.waitForTimeout(3000);
        boolean loginSigueVisible = page.isVisible(LoginPage.CAMPO_USUARIO) ||
                                    page.isVisible(LoginPage.CAMPO_PASSWORD);
        boolean errorVisible = page.isVisible("text=incorrecto") ||
                               page.isVisible("text=inv\u00e1lido") ||
                               page.isVisible("text=error") ||
                               page.isVisible("text=Error") ||
                               page.isVisible(".p-message-error");

        Assertions.assertThat(loginSigueVisible || errorVisible)
                  .as("Con credenciales incorrectas, debe mostrarse el login o un mensaje de error")
                  .isTrue();
    }
}
