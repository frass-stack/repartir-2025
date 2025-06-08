package ar.com.grupoesfera.repartir.controllers;

import ar.com.grupoesfera.repartir.exceptions.GrupoInvalidoException;
import ar.com.grupoesfera.repartir.model.Gasto;
import ar.com.grupoesfera.repartir.model.Grupo;
import ar.com.grupoesfera.repartir.repositories.GruposRepository;
import ar.com.grupoesfera.repartir.exceptions.GrupoNoEncontradoException;
import ar.com.grupoesfera.repartir.services.GruposService;

import org.apache.catalina.filters.AddDefaultCharsetFilter.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grupos")
public class GruposController {

    private static final Logger logger = LoggerFactory.getLogger(GruposController.class);

    private GruposService grupos;

    @Autowired
    public void setGruposService(GruposService gruposService) {
        this.grupos = gruposService;
    }

    @GetMapping
    public ResponseEntity<List<Grupo>> listar() {

        ResponseEntity<List<Grupo>> response;

        try {

            response = ResponseEntity.ok(grupos.listarGrupos());

        } catch (Exception e) {
            response = ResponseEntity.internalServerError().build();
        }

        return  response;
    }

    @PostMapping
    public ResponseEntity<Grupo> crear(@RequestBody Grupo grupo) {
        logger.info("Petición para crear grupo: '{}' con miembros: {}", grupo.getNombre(), grupo.getMiembros());
        Grupo creado = grupos.crear(grupo);
        logger.info("Grupo creado exitosamente: '{}'", grupo.getNombre());
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grupo> recuperar(@PathVariable Long id) {

        ResponseEntity<Grupo> response;

        try {

            response = ResponseEntity.ok(grupos.recuperar(id));

        } catch (GrupoNoEncontradoException e) {

            response = ResponseEntity.notFound().build();

        } catch (Exception e) {

            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    @PostMapping("/{id}/gastos")
    public ResponseEntity<Grupo> agregarGasto(@PathVariable Long id, @RequestBody Gasto gasto) {

        ResponseEntity<Grupo> response;

        Grupo grupo = grupos.agregarGasto(id, gasto);
        response = ResponseEntity.ok(grupo);

        return response;
    }

    @ExceptionHandler(GrupoInvalidoException.class)
    public ResponseEntity<Map<String, String>> manejarGrupoInvalido(GrupoInvalidoException ex) {
        logger.warn("Error de validación al crear grupo: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getCodigoError().name());
        error.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
