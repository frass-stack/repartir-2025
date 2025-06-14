package ar.com.grupoesfera.repartir.services;

import ar.com.grupoesfera.repartir.exceptions.GrupoInvalidoException;
import ar.com.grupoesfera.repartir.exceptions.GrupoNoEncontradoException;
import ar.com.grupoesfera.repartir.exceptions.GrupoInvalidoException.CodigoError;
import ar.com.grupoesfera.repartir.model.Gasto;
import ar.com.grupoesfera.repartir.model.Grupo;
import ar.com.grupoesfera.repartir.repositories.GruposRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GruposService {

    @Autowired
    GruposRepository repository;

    @Autowired
    MontosService montos;

    public List<Grupo> listarGrupos() {

        return  repository.findAll();
    }

    public Grupo crear(Grupo nuevoGrupo) {

        validar(nuevoGrupo);
        guardar(nuevoGrupo);

        return nuevoGrupo;
    }

    private void guardar(Grupo nuevoGrupo) {
        montos.inicializarTotal(nuevoGrupo);
        repository.save(nuevoGrupo);
    }

    private void validar(Grupo nuevoGrupo) {
        if (!nuevoGrupo.estaFormado()) {
            throw new GrupoInvalidoException(GrupoInvalidoException.CodigoError.MIEMBROS_INSUFICIENTES);
        }

        if (Strings.isBlank(nuevoGrupo.getNombre())) {
            throw new GrupoInvalidoException(GrupoInvalidoException.CodigoError.NOMBRE_INCOMPLETO);
        }

        if (repository.existsByNombreIgnoreCase(nuevoGrupo.getNombre())) {
            throw new GrupoInvalidoException(GrupoInvalidoException.CodigoError.NOMBRE_REPETIDO);
        }
    }

    public Grupo recuperar(Long id) {

        Optional<Grupo> grupoBuscado = repository.findById(id);

        if (!grupoBuscado.isPresent()) {
            throw new GrupoNoEncontradoException();
        }

        return grupoBuscado.get();
    }

    public Grupo agregarGasto(Long id, Gasto gasto) {

        Grupo grupo = recuperar(id);
        montos.acumularAlTotal(grupo, gasto);
        repository.save(grupo);
        return grupo;
    }

    public Grupo editarNombre(Long id, String nuevoNombre) {
        Grupo g = recuperar(id);
        if (Strings.isBlank(nuevoNombre)) {
            throw new GrupoInvalidoException(GrupoInvalidoException.CodigoError.NOMBRE_INCOMPLETO);
        }
        if (!g.getNombre().equalsIgnoreCase(nuevoNombre) && repository.existsByNombreIgnoreCase(nuevoNombre)) {
            throw new GrupoInvalidoException(GrupoInvalidoException.CodigoError.NOMBRE_REPETIDO);
        }
        g.setNombre(nuevoNombre);
        return repository.save(g);
    }

}
