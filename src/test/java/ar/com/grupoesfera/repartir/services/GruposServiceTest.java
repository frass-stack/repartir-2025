package ar.com.grupoesfera.repartir.services;

import ar.com.grupoesfera.repartir.exceptions.GrupoInvalidoException;
import ar.com.grupoesfera.repartir.exceptions.GrupoNoEncontradoException;
import ar.com.grupoesfera.repartir.model.Gasto;
import ar.com.grupoesfera.repartir.model.Grupo;
import ar.com.grupoesfera.repartir.repositories.GruposRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class GruposServiceTest {

    GruposService grupos;

    GruposRepository repositoryMock;

    @BeforeEach
    void crear() {

        grupos = new GruposService();

        repositoryMock = mock(GruposRepository.class);
        grupos.repository = repositoryMock;

        grupos.montos = new MontosService();
    }

    @Test
    void crearGrupo() {

        Grupo nuevoGrupo = new Grupo();
        nuevoGrupo.setNombre("Cena");
        nuevoGrupo.setMiembros(asList("claudia", "tomas", "sandra"));

        grupos.crear(nuevoGrupo);

        verify(repositoryMock).save(nuevoGrupo);
    }

    @Test
    void crearGrupoLanzaExcepcionSiElGrupoNoTieneMiembros() {

        Grupo grupoInvalidoSinMiembros = new Grupo();
        grupoInvalidoSinMiembros.setNombre("Almuerzo");
        grupoInvalidoSinMiembros.setMiembros(emptyList());

        GrupoInvalidoException exception = assertThrows(GrupoInvalidoException.class, () -> {
            grupos.crear(grupoInvalidoSinMiembros);
        });
    
        assertEquals(GrupoInvalidoException.CodigoError.MIEMBROS_INSUFICIENTES, exception.getCodigoError());
    

        verifyNoInteractions(repositoryMock);
    }

    @Test
    void crearGrupoLanzaExcepcionSiElGrupoNoTieneNombre() {

        Grupo grupoInvalidoSinNombre = new Grupo();
        grupoInvalidoSinNombre.setNombre("");
        grupoInvalidoSinNombre.setMiembros(asList("patricia", "guille"));

        GrupoInvalidoException exception = assertThrows(GrupoInvalidoException.class, () -> {
            grupos.crear(grupoInvalidoSinNombre);
        });
    
        assertEquals(GrupoInvalidoException.CodigoError.NOMBRE_INCOMPLETO, exception.getCodigoError());
    

        verifyNoInteractions(repositoryMock);
    }

    @Test
    void crearGrupoLanzaExcepcionSiElNombreTieneMenosDeDosCaracteres() {
        Grupo grupoInvalidoNombreCorto = new Grupo();
        grupoInvalidoNombreCorto.setNombre("A");
        grupoInvalidoNombreCorto.setMiembros(asList("patricia", "guille"));

        GrupoInvalidoException exception = assertThrows(GrupoInvalidoException.class, () -> {
            grupos.crear(grupoInvalidoNombreCorto);
        });
    
        assertEquals(GrupoInvalidoException.CodigoError.NOMBRE_INVALIDO, exception.getCodigoError());
    
        verifyNoInteractions(repositoryMock);
    }

    
    @Test
    void recuperarGrupoPorId() {

        final Long ID_BUSCADO = 45L;
        final Grupo GRUPO = new Grupo();
        final Optional<Grupo> ENCONTRADO = Optional.of(GRUPO);

        when(repositoryMock.findById(ID_BUSCADO)).thenReturn(ENCONTRADO);

        Grupo resultado = grupos.recuperar(ID_BUSCADO);

        assertThat(resultado).isSameAs(GRUPO);
    }

    @Test
    void recuperarGrupoPorIdLanzaExceptionSiNoLoEncuentra() {

        final Long ID_BUSCADO = 98L;
        final Optional<Grupo> NO_ENCONTRADO = Optional.empty();

        when(repositoryMock.findById(ID_BUSCADO)).thenReturn(NO_ENCONTRADO);

        assertThrows(GrupoNoEncontradoException.class, () -> {
            grupos.recuperar(ID_BUSCADO);
        });
    }

    @Test
    void agregarGastoAlGrupoLanzaExcepcionCuandoElGrupoNoExiste() {

        final Long ID_GRUPO_INEXISTENTE = 62L;
        final Optional<Grupo> NO_ENCONTRADO = Optional.empty();
        final Gasto unGasto = new Gasto();

        when(repositoryMock.findById(ID_GRUPO_INEXISTENTE)).thenReturn(NO_ENCONTRADO);

        assertThrows(GrupoNoEncontradoException.class, () -> {
           grupos.agregarGasto(ID_GRUPO_INEXISTENTE, unGasto);
        });

        verify(repositoryMock, never()).save(any());
    }

    @Test
    void agregarGastoDe300AlGrupoDelAlmuerzoSinGastos() {

        final Long ID_ALMUERZO = 231L;
        final Grupo ALMUERZO = crearGrupoAlmuerzoSinGastos(ID_ALMUERZO);
        final Gasto GASTO_POR_300 = crearGastoPor300();
        final BigDecimal $_300_00 = BigDecimal.valueOf(30000, 2);

        when(repositoryMock.findById(ID_ALMUERZO)).thenReturn(Optional.of(ALMUERZO));

        grupos.agregarGasto(ID_ALMUERZO, GASTO_POR_300);

        assertThat(ALMUERZO.getTotal()).isEqualTo($_300_00);
        verify(repositoryMock).save(ALMUERZO);
    }

    private Grupo crearGrupoAlmuerzoSinGastos(final Long ID) {

        Grupo grupo = new Grupo();
        grupo.setId(ID);
        grupo.setNombre("Almuerzo");
        grupo.setMiembros(asList("laura", "rodrigo", "marcos", "valeria"));
        grupo.setTotal(BigDecimal.valueOf(0,2));

        return grupo;
    }

    private Gasto crearGastoPor300() {

        Gasto gasto = new Gasto();
        gasto.setMonto(BigDecimal.valueOf(30000, 2));

        return gasto;
    }

    @Test
    void agregarGastoDe97AlGrupoDelViajeQueTeniaComoTotal112() {

        final Long ID_VIAJE = 89L;
        final Grupo VIAJE = crearGrupoViajeConTotal112(ID_VIAJE);
        final Gasto GASTO_POR_97 = crearGastoPor97();
        final BigDecimal $_209_00 = BigDecimal.valueOf(20900, 2);

        when(repositoryMock.findById(ID_VIAJE)).thenReturn(Optional.of(VIAJE));

        grupos.agregarGasto(ID_VIAJE, GASTO_POR_97);

        assertThat(VIAJE.getTotal()).isEqualTo($_209_00);
        verify(repositoryMock).save(VIAJE);
    }

    private Grupo crearGrupoViajeConTotal112(Long ID) {

        Grupo grupo = new Grupo();
        grupo.setId(ID);
        grupo.setNombre("Viaje");
        grupo.setMiembros(asList("patricia", "guille", "vicky", "luca"));
        grupo.setTotal(BigDecimal.valueOf(11200,2));

        return grupo;
    }

    private Gasto crearGastoPor97() {

        Gasto gasto = new Gasto();
        gasto.setMonto(BigDecimal.valueOf(9700,2));
        return gasto;
    }
}
