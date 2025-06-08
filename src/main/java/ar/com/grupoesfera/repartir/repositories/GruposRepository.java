package ar.com.grupoesfera.repartir.repositories;

import ar.com.grupoesfera.repartir.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GruposRepository extends JpaRepository<Grupo, Long> {

    Optional<Grupo> findByNombreIgnoreCase(String nombre);

}
