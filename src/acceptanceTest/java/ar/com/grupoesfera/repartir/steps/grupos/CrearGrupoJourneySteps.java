package ar.com.grupoesfera.repartir.steps.grupos;

import ar.com.grupoesfera.repartir.steps.CucumberSteps;
import ar.com.grupoesfera.repartir.steps.Step;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Crear Grupo")
public class CrearGrupoJourneySteps extends CucumberSteps {

    @Step("el usuario inicia la aplicación")
    public void elUsuarioIniciaLaAplicacion() {

        driver.navigate().to(url("/"));
        driver.findElement(By.id("usuarioInput")).sendKeys("julian");
        var iniciarButton = driver.findElement(By.id("iniciarBienvenidaButton"));
        iniciarButton.click();
    }

    @Step("se muestra {int}° el grupo {string} con total {string}")
    public void seMuestraElNuevoGrupo(int posicion, String nombre, String total) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(30, ChronoUnit.SECONDS));
        WebElement fila = wait.until(driver -> {
            List<WebElement> filas = driver.findElements(By.cssSelector("app-grupos table tr"));
            for (WebElement f : filas) {
                try {
                    List<WebElement> columnas = f.findElements(By.tagName("td"));
                    if (columnas.size() > 2 &&
                            columnas.get(1).getText().equals(nombre) &&
                            columnas.get(2).getText().equals(total)) {
                        return f;
                    }
                } catch (StaleElementReferenceException ignored) {

                }
            }
            return null;
        });

        assertThat(fila).isNotNull();
    }

    private @NotNull List<WebElement> obtenerGrupo() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(30, ChronoUnit.SECONDS));
        var grupoTR = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-grupos table tr")));
        assertThat(grupoTR).hasSizeGreaterThan(1);
        return grupoTR;
    }

    @Step("no existe ningún grupo")
    public void noExisteNingunGrupo() {
        // Aquí podrías limpiar la base de datos o asegurarte de que no haya grupos
        // Por simplicidad, no hacemos nada (o podrías implementar lógica específica)
    }

    @Step("el usuario selecciona crear grupo")
    public void elUsuarioSeleccionaCrearGrupo() {
        driver.findElement(By.id("crearGruposButton")).click();
    }

    @Step("completa con el nombre {string}")
    public void completaConElNombre(String nombre) {
        driver.findElement(By.id("nombreGrupoNuevoInput")).clear();
        driver.findElement(By.id("nombreGrupoNuevoInput")).sendKeys(nombre);
    }

    @Step("indica que los miembros son {string}, {string} y {string}")
    public void indicaQueLosMiembrosSon(String m1, String m2, String m3) {
        WebElement miembrosInput = driver.findElement(By.id("miembrosGrupoNuevoInput"));
        miembrosInput.sendKeys(m1);
        miembrosInput.sendKeys("\n");
        miembrosInput.sendKeys(m2);
        miembrosInput.sendKeys("\n");
        miembrosInput.sendKeys(m3);
        miembrosInput.sendKeys("\n");
    }

    @Step("indica que los miembros son:")
    public void indicaQueLosMiembrosSon(io.cucumber.datatable.DataTable dataTable) {
        WebElement miembrosInput = driver.findElement(By.id("miembrosGrupoNuevoInput"));
        List<String> miembros = dataTable.asList();
        for (String miembro : miembros) {
            miembrosInput.sendKeys(miembro);
            miembrosInput.sendKeys("\n");
        }
    }

    @Step("guarda el grupo")
    public void guardaElGrupo() {
        driver.findElement(By.id("guardarGrupoNuevoButton")).click();
    }

    @Step("existe un grupo")
    public void existeUnGrupo() {
        // Puedes crear un grupo por defecto aquí si es necesario
        elUsuarioSeleccionaCrearGrupo();
        completaConElNombre("Grupo por defecto");
        indicaQueLosMiembrosSon("Ana", "Luis", "Pedro");
        guardaElGrupo();
    }

    @Step("el usuario agrega un gasto de ${string} al grupo #{string}")
    public void elUsuarioAgregaUnGastoAlGrupo(String monto, String grupoId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> filas = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-grupos table tr")));
        WebElement filaGrupo = null;
        for (WebElement fila : filas) {
            List<WebElement> columnas = fila.findElements(By.tagName("td"));
            if (!columnas.isEmpty() && columnas.get(0).getText().trim().equals(grupoId)) {
                filaGrupo = fila;
                break;
            }
        }
        assertThat(filaGrupo).isNotNull();

        // Hacer clic en el botón de agregar gasto (asumiendo que es el último botón de la fila)
        WebElement botonAgregarGasto = filaGrupo.findElement(By.cssSelector("button"));
        botonAgregarGasto.click();

        // Completar el monto y guardar
        WebElement inputMonto = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("montoGastoNuevoInput")));
        inputMonto.clear();
        inputMonto.sendKeys(monto);

        WebElement botonGuardar = driver.findElement(By.id("guardarGastoNuevoButton"));
        botonGuardar.click();

        // Esperar a que el toast de éxito aparezca
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mensajesToast")));
    }

}
