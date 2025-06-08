package ar.com.grupoesfera.repartir.services;

import ar.com.grupoesfera.repartir.exceptions.GrupoInvalidoException;
import ar.com.grupoesfera.repartir.exceptions.GrupoNoEncontradoException;
import ar.com.grupoesfera.repartir.exceptions.GrupoInvalidoException.CodigoError;
import ar.com.grupoesfera.repartir.model.Gasto;
import ar.com.grupoesfera.repartir.model.Grupo;
import ar.com.grupoesfera.repartir.repositories.GruposRepository;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GruposService {

    private static final Logger logger = LoggerFactory.getLogger(GruposService.class);

    @Autowired
    GruposRepository repository;

    @Autowired
    MontosService montos;

    public List<Grupo> listarGrupos() {

        return  repository.findAll();
    }

    public Grupo crear(Grupo nuevoGrupo) {
        logger.info("Intentando crear grupo con nombre: '{}' y miembros: {}", nuevoGrupo.getNombre(), nuevoGrupo.getMiembros());
        validar(nuevoGrupo);
        logger.info("Validación exitosa para el grupo: '{}'", nuevoGrupo.getNombre());
        guardar(nuevoGrupo);
        logger.info("Grupo guardado: '{}'", nuevoGrupo.getNombre());
        return nuevoGrupo;
    }

    private void guardar(Grupo nuevoGrupo) {
        montos.inicializarTotal(nuevoGrupo);
        repository.save(nuevoGrupo);
    }

    private void validar(Grupo nuevoGrupo) {
        logger.info("Validando grupo con nombre: '{}' y miembros: {}", nuevoGrupo.getNombre(), nuevoGrupo.getMiembros());
        if (!nuevoGrupo.estaFormado()) {
            logger.warn("Validación fallida: El grupo no tiene suficientes miembros");
            throw new GrupoInvalidoException(GrupoInvalidoException.CodigoError.MIEMBROS_INSUFICIENTES);
        }
        if (Strings.isBlank(nuevoGrupo.getNombre())) {
            logger.warn("Validación fallida: El grupo no tiene nombre");
            throw new GrupoInvalidoException(GrupoInvalidoException.CodigoError.NOMBRE_INCOMPLETO);
        }
        if (nuevoGrupo.getNombre().length() < 2) {
            logger.warn("Validación fallida: El nombre del grupo tiene menos de 2 caracteres");
            throw new GrupoInvalidoException(GrupoInvalidoException.CodigoError.NOMBRE_INVALIDO);
        }
        if (repository.findByNombreIgnoreCase(nuevoGrupo.getNombre()).isPresent()) {
            logger.warn("Validación fallida: Ya existe un grupo con ese nombre");
            throw new GrupoInvalidoException(GrupoInvalidoException.CodigoError.NOMBRE_DUPLICADO);
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

}
