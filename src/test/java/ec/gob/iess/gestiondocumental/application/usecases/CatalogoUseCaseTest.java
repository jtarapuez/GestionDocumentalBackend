package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.application.port.in.CatalogoUseCasePort;
import ec.gob.iess.gestiondocumental.application.port.out.CatalogoDetalleRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.CatalogoRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SeccionDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.Catalogo;
import ec.gob.iess.gestiondocumental.domain.model.CatalogoDetalle;
import ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoDetalleResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SeccionDocumentalResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios del caso de uso de catálogos (puertos mockeados).
 */
@QuarkusTest
class CatalogoUseCaseTest {

    @InjectMock
    CatalogoRepositoryPort catalogoRepositoryPort;

    @InjectMock
    CatalogoDetalleRepositoryPort catalogoDetalleRepositoryPort;

    @InjectMock
    SeccionDocumentalRepositoryPort seccionDocumentalRepositoryPort;

    @Inject
    CatalogoUseCasePort useCase;

    private static Catalogo catalogoDominio(Long id, String codigo) {
        Catalogo c = new Catalogo();
        c.setId(id);
        c.setCodigo(codigo);
        c.setDescripcion("Desc " + codigo);
        c.setEstado("A");
        return c;
    }

    private static CatalogoDetalle detalleDominio(Long id, String codigo, Long idCatalogo) {
        CatalogoDetalle d = new CatalogoDetalle();
        d.setId(id);
        d.setCodigo(codigo);
        d.setDescripcion("Detalle " + codigo);
        d.setIdCatalogo(idCatalogo);
        d.setEstado("A");
        return d;
    }

    private static SeccionDocumental seccionDominio(Long id, String nombre) {
        SeccionDocumental s = new SeccionDocumental();
        s.setId(id);
        s.setNombre(nombre);
        s.setDescripcion("Descripción");
        s.setEstadoRegistro("A");
        return s;
    }

    @Test
    @DisplayName("listarCatalogos devuelve lista del puerto")
    void listarCatalogosDevuelveLista() {
        Catalogo c1 = catalogoDominio(1L, "TIPO_SOPORTE");
        Catalogo c2 = catalogoDominio(2L, "TIPO_ARCHIVO");
        when(catalogoRepositoryPort.findActivos()).thenReturn(List.of(c1, c2));

        List<CatalogoResponse> result = useCase.listarCatalogos();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCodigo()).isEqualTo("TIPO_SOPORTE");
        assertThat(result.get(1).getCodigo()).isEqualTo("TIPO_ARCHIVO");
        verify(catalogoRepositoryPort).findActivos();
    }

    @Test
    @DisplayName("obtenerCatalogoPorCodigo devuelve vacío cuando no existe")
    void obtenerPorCodigoVacioSiNoExiste() {
        when(catalogoRepositoryPort.findByCodigo("NO_EXISTE")).thenReturn(Optional.empty());

        Optional<CatalogoResponse> result = useCase.obtenerCatalogoPorCodigo("NO_EXISTE");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("obtenerCatalogoPorCodigo devuelve respuesta cuando existe")
    void obtenerPorCodigoRespuestaCuandoExiste() {
        Catalogo c = catalogoDominio(1L, "TIPO_SOPORTE");
        when(catalogoRepositoryPort.findByCodigo("TIPO_SOPORTE")).thenReturn(Optional.of(c));

        Optional<CatalogoResponse> result = useCase.obtenerCatalogoPorCodigo("TIPO_SOPORTE");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getCodigo()).isEqualTo("TIPO_SOPORTE");
    }

    @Test
    @DisplayName("listarDetallesPorCatalogo devuelve lista del puerto")
    void listarDetallesPorCatalogo() {
        CatalogoDetalle d1 = detalleDominio(1L, "FISICO", 1L);
        when(catalogoDetalleRepositoryPort.findActivosByCodigoCatalogo("TIPO_SOPORTE")).thenReturn(List.of(d1));

        List<CatalogoDetalleResponse> result = useCase.listarDetallesPorCatalogo("TIPO_SOPORTE");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCodigo()).isEqualTo("FISICO");
        assertThat(result.get(0).getIdCatalogo()).isEqualTo(1L);
    }

    @Test
    @DisplayName("listarSecciones devuelve lista del puerto")
    void listarSecciones() {
        SeccionDocumental s1 = seccionDominio(1L, "Sección A");
        when(seccionDocumentalRepositoryPort.findActivas()).thenReturn(List.of(s1));

        List<SeccionDocumentalResponse> result = useCase.listarSecciones();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Sección A");
        verify(seccionDocumentalRepositoryPort).findActivas();
    }
}
