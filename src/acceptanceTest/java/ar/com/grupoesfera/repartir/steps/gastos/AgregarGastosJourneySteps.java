package ar.com.grupoesfera.repartir.steps.gastos;

import ar.com.grupoesfera.repartir.steps.CucumberSteps;
import ar.com.grupoesfera.repartir.steps.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class AgregarGastosJourneySteps extends CucumberSteps {

    @Step("existe el grupo #{int} {string} sin gastos")
    public void existeElGrupoSinGastos(int idGrupo, String nombre) {

        baseDeDatos.tieneElGrupoSinGastos(idGrupo, nombre);
    }

    @Step("el usuario selecciona agregar gasto al grupo #{int}")
    public void elUsuarioSeleccionaAgregarGastoAlGrupo(int idGrupo) {

        var wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(2));
        var agregarGastoButton = wait.until(visibilityOfElementLocated(By.id("agregarGastoGruposButton-" + idGrupo)));
        agregarGastoButton.click();
    }

    @Step("completa con el monto de $ {string}")
    public void completaConElMontoDe(String monto) {

        var montoInput = driver.findElement(By.id("montoGastoNuevoInput"));
        montoInput.clear();
        montoInput.sendKeys(monto);
    }

    @Step("guarda el gasto")
    public void guardaElGasto() {

        var agregarGastoButton = driver.findElement(By.id("guardarGastoNuevoButton"));
        agregarGastoButton.click();
    }

    @Step("ve la confirmación {string}")
    public void veLaConfirmación(String mensaje) {

        var wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(2));
        var mensajesToast = wait.until(visibilityOfElementLocated(By.id("mensajesToast")));
        assertThat(mensajesToast.getText())
                .contains("Éxito")
                .contains(mensaje);
    }

    @Step("ve el total del grupo #{int} actualizado a {string}")
    public void veElTotalDelGrupoActualidadoA$(int idGrupo, String monto) {

        var grupoTR = driver.findElements(By.cssSelector("app-grupos table tr"));

        var campoTDs = grupoTR.get(1).findElements(By.tagName("td"));
        assertThat(campoTDs.get(0).getText()).isNotEmpty();
        assertThat(campoTDs.get(2).getText()).isEqualTo(monto);
    }
}
