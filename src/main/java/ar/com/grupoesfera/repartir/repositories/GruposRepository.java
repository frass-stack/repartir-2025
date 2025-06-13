package ar.com.grupoesfera.repartir.repositories;

import ar.com.grupoesfera.repartir.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GruposRepository extends JpaRepository<Grupo, Long> {

    boolean existsByNombre(String nombre);

}
