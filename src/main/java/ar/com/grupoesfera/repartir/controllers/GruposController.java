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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grupos")
public class GruposController {

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
        Grupo creado = grupos.crear(grupo);
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

    @PutMapping("/{id}")
    public ResponseEntity<Grupo> actualizarNombre(@PathVariable Long id,
            @RequestBody Grupo grupoRequest) {
                Grupo actualizado = grupos.editarNombre(
            id,
            grupoRequest.getNombre()
        );
        return ResponseEntity.ok(actualizado);
    }
    
}
