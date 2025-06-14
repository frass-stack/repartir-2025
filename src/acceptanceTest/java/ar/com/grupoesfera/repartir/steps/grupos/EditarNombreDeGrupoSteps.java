package ar.com.grupoesfera.repartir.steps.grupos;

import ar.com.grupoesfera.repartir.steps.CucumberSteps;
import io.cucumber.java.es.Y;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import org.openqa.selenium.StaleElementReferenceException;

@DisplayName("Editar el nombre de un grupo existente")
public class EditarNombreDeGrupoSteps extends CucumberSteps {

    private String nombreOriginal;
    private String nuevoNombre;

    @Y("existe un grupo con nombre {string} indicando que sus miembros son {string} y {string}")
    public void existeGrupoConNombreMiembros(String nombre, String m1, String m2) {
        this.nombreOriginal = nombre;
        // sólo abrimos el diálogo de nuevo grupo (ya estamos logueados)
        driver.findElement(By.id("crearGruposButton")).click();
        driver.findElement(By.id("nombreGrupoNuevoInput")).sendKeys(nombreOriginal);

        WebElement miembrosInput = driver.findElement(By.id("miembrosGrupoNuevoInput"));
        miembrosInput.sendKeys(m1);
        miembrosInput.sendKeys(Keys.ENTER);
        miembrosInput.sendKeys(m2);
        miembrosInput.sendKeys(Keys.ENTER);

        driver.findElement(By.id("guardarGrupoNuevoButton")).click();
        new WebDriverWait(driver, Duration.of(5, ChronoUnit.SECONDS))
            .until(visibilityOfElementLocated(By.id("mensajesToast")));
    }

    @Cuando("el usuario edita el nombre de ese grupo a {string}")
    public void elUsuarioEditaElNombreDeEseGrupo(String edit) {
        this.nuevoNombre = edit;
        var wait = new WebDriverWait(driver, Duration.of(5, ChronoUnit.SECONDS));
        // Esperar a que la tabla tenga al menos 2 filas (cabecera + datos)
        wait.until(numberOfElementsToBeMoreThan(By.cssSelector("app-grupos table tr"), 1));

        // Buscar la fila del grupo por nombre
        List<WebElement> filas = driver.findElements(By.cssSelector("app-grupos table tr"));
        WebElement fila = filas.stream()
            .filter(f -> {
                List<WebElement> celdas = f.findElements(By.tagName("td"));
                return celdas.size() > 1 && celdas.get(1).getText().equals(nombreOriginal);
            })
            .findFirst()
            .orElseThrow(() -> new AssertionError("No encontré la fila del grupo '" + nombreOriginal + "'"));

        WebElement pButtonHost = fila.findElement(By.cssSelector("p-button[id^='editarGrupoButton-']"));
        WebElement btnEditar = pButtonHost.findElement(By.tagName("button"));
        wait.until(elementToBeClickable(btnEditar)).click();

        WebElement nombreInput = driver.findElement(By.id("nombreGrupoNuevoInput"));
        nombreInput.clear();
        nombreInput.sendKeys(nuevoNombre);
        driver.findElement(By.id("guardarGrupoNuevoButton")).click();

        wait.until(visibilityOfElementLocated(By.id("mensajesToast")));
    }

    @Entonces("debería visualizar dentro del listado el grupo con nombre {string}")
    public void deberiaVisualizarGrupoConNombre(String esperado) {
        var wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
        
        wait.until(numberOfElementsToBeMoreThan(By.cssSelector("app-grupos table tr"), 1));
        
        wait.until(presenceOfElementLocated(By.xpath("//td[contains(text(), '" + esperado + "')]")));
        
        List<WebElement> filas = driver.findElements(By.cssSelector("app-grupos table tr"));
        WebElement fila = filas.stream()
            .filter(f -> {
                try {
                    List<WebElement> celdas = f.findElements(By.tagName("td"));
                    return celdas.size() > 1 && celdas.get(1).getText().equals(esperado);
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            })
            .findFirst()
            .orElseThrow(() -> new AssertionError("No se mostró grupo con nombre " + esperado));

        List<WebElement> celdas = fila.findElements(By.tagName("td"));
        assertThat(celdas.get(1).getText()).isEqualTo(esperado);
    }
}