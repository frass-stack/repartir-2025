package ar.com.grupoesfera.repartir.steps.grupos;

import ar.com.grupoesfera.repartir.steps.CucumberSteps;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.junit.jupiter.api.DisplayName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

@DisplayName("Nombre que los identifica")
public class NombreQueLosIdentificaSteps extends CucumberSteps {

    private String nombreIndicado;

    @Cuando("el usuario crea un grupo indicando el nombre {string}")
    public void elUsuarioCreaUnGrupoIndicandoElNombre(String nombre) {

        nombreIndicado = nombre;

        var wait = new WebDriverWait(driver, Duration.of(2, ChronoUnit.SECONDS));
        var crearGruposButton = wait.until(elementToBeClickable(By.id("crearGruposButton")));
        crearGruposButton.click();

        driver.findElement(By.id("nombreGrupoNuevoInput")).sendKeys(nombreIndicado);

        var miembrosInput = driver.findElement(By.id("miembrosGrupoNuevoInput"));
        miembrosInput.sendKeys("Victor");
        miembrosInput.sendKeys(Keys.ENTER);
        miembrosInput.sendKeys("Brenda");
        miembrosInput.sendKeys(Keys.ENTER);

        driver.findElement(By.id("guardarGrupoNuevoButton")).click();

        wait.until(visibilityOfElementLocated(By.id("mensajesToast")));
    }

    @Entonces("debería visualizar dentro del listado el grupo con el nombre indicado")
    public void deberiaVisualizarDentroDelListadoElGrupoConElNombreIndicado() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> grupoTR = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
            By.cssSelector("app-grupos table tr"), 1));

        assertThat(grupoTR).hasSizeGreaterThan(1);

        List<WebElement> campoTDs = grupoTR.get(1).findElements(By.tagName("td"));
        assertThat(campoTDs.get(0).getText()).isNotEmpty();
        assertThat(campoTDs.get(1).getText()).isEqualTo(nombreIndicado);
    }

    @Cuando("el usuario intenta crear un grupo sin indicar su nombre")
    public void elUsuarioIntentaCrearUnGrupoSinIndicarSuNombre() {

        var crearGruposButton = driver.findElement(By.id("crearGruposButton"));
        crearGruposButton.click();

        var miembrosInput = driver.findElement(By.id("miembrosGrupoNuevoInput"));
        miembrosInput.sendKeys("Carla");
        miembrosInput.sendKeys(Keys.ENTER);
        miembrosInput.sendKeys("Miguel");
        miembrosInput.sendKeys(Keys.ENTER);

        driver.findElement(By.id("guardarGrupoNuevoButton")).click();
    }

    @Entonces("no debería crear el grupo sin nombre")
    public void noDeberiaCrearElGrupoSinNombre() {

        // TODO
    }

    @Y("debería ser informado que no puede crear un grupo sin nombre")
    public void deberiaSerInformadoQueNoPuedeCrearUnGrupoSinNombre() {
        shouldShowAnError("No se puede guardar");
    }

    @Dado("que existe un grupo llamado {string}")
    public void que_existe_un_grupo_llamado(String nombre) {
        var wait = new WebDriverWait(driver, Duration.of(2, ChronoUnit.SECONDS));
        var crearGruposButton = wait.until(elementToBeClickable(By.id("crearGruposButton")));
        crearGruposButton.click();

        driver.findElement(By.id("nombreGrupoNuevoInput")).sendKeys(nombre);

        var miembrosInput = driver.findElement(By.id("miembrosGrupoNuevoInput"));
        miembrosInput.sendKeys("Victor");
        miembrosInput.sendKeys(Keys.ENTER);
        miembrosInput.sendKeys("Brenda");
        miembrosInput.sendKeys(Keys.ENTER);

        driver.findElement(By.id("guardarGrupoNuevoButton")).click();

        wait.until(visibilityOfElementLocated(By.id("mensajesToast")));
    }

    @Cuando("intento crear un grupo con el nombre {string}")
    public void intento_crear_un_grupo_con_el_nombre(String nombre) {
        var wait = new WebDriverWait(driver, Duration.of(2, ChronoUnit.SECONDS));
        var crearGruposButton = wait.until(elementToBeClickable(By.id("crearGruposButton")));
        crearGruposButton.click();

        driver.findElement(By.id("nombreGrupoNuevoInput")).sendKeys(nombre);

        var miembrosInput = driver.findElement(By.id("miembrosGrupoNuevoInput"));
        miembrosInput.sendKeys("Oscar");
        miembrosInput.sendKeys(Keys.ENTER);
        miembrosInput.sendKeys("Laura");
        miembrosInput.sendKeys(Keys.ENTER);

        driver.findElement(By.id("guardarGrupoNuevoButton")).click();

        wait.until(visibilityOfElementLocated(By.id("mensajesToast")));
    }

    @Entonces("debería ser informado que ya existe un grupo con ese nombre")
    public void deberia_ser_informado_que_ya_existe_un_grupo_con_ese_nombre() {
        var wait = new WebDriverWait(driver, Duration.of(2, ChronoUnit.SECONDS));
        var mensajesToast = wait.until(visibilityOfElementLocated(By.id("mensajesToast")));
        String texto = mensajesToast.getText();
        assertThat(texto).contains("Ya existe un grupo con ese nombre");
    }

    @Entonces("debería ser informado que el nombre del grupo debe tener al menos {int} caracteres")
    public void deberia_ser_informado_que_el_nombre_del_grupo_debe_tener_al_menos_caracteres(Integer cantidad) {
        var wait = new WebDriverWait(driver, Duration.of(2, ChronoUnit.SECONDS));
        var mensajesToast = wait.until(visibilityOfElementLocated(By.id("mensajesToast")));
        String texto = mensajesToast.getText();
        assertThat(texto).contains("El nombre del grupo debe tener al menos " + cantidad + " caracteres");
    }
    
}
