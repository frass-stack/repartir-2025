package ar.com.grupoesfera.repartir.steps.grupos;

import ar.com.grupoesfera.repartir.exceptions.GrupoInvalidoException;
import ar.com.grupoesfera.repartir.model.Grupo;
import ar.com.grupoesfera.repartir.services.GruposService;
import ar.com.grupoesfera.repartir.steps.FastCucumberSteps;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class NombresUnicosDeGrupoSteps extends FastCucumberSteps {

    private boolean errorRecibido = false;
    private String mensajeError;
    @Autowired
    private GruposService gruposService;
    
    @Dado("que existe un grupo con nombre {string}")
    public void queExisteUnGrupoConNombre(String nombre) {
        // Crear y guardar un grupo con el nombre indicado
        Grupo grupo = new Grupo();
        grupo.setNombre(nombre);
        
        // Añadir miembros suficientes para que el grupo sea válido
        List<String> miembros = Arrays.asList("Juan", "Pedro");
        grupo.setMiembros(miembros);
        
        // Guardar el grupo en la base de datos
        gruposService.crear(grupo);
    }
    
    @Cuando("el usuario intenta crear otro grupo con nombre {string}")
    public void elUsuarioIntentaCrearOtroGrupoConNombre(String nombre) {
        // Intentar crear otro grupo con el mismo nombre
        Grupo nuevoGrupo = new Grupo();
        nuevoGrupo.setNombre(nombre);
        
        // Añadir miembros suficientes para que el grupo sea válido por la regla anterior
        List<String> miembros = Arrays.asList("Ana", "Luis");
        nuevoGrupo.setMiembros(miembros);
        
        try {
            gruposService.crear(nuevoGrupo);
        } catch (GrupoInvalidoException e) {
            errorRecibido = true;
            mensajeError = e.getMessage();
        } catch (Exception e) {
            errorRecibido = true;
            mensajeError = e.getMessage();
        }
    }
    
    @Entonces("debería recibir un error por nombre repetido")
    public void deberiaRecibirUnErrorPorNombreRepetido() {
        // Verificar que se recibió un error adecuado
        assertThat(errorRecibido).isTrue();
        assertThat(mensajeError).contains("nombre"); // El mensaje debería mencionar el nombre
    }
}