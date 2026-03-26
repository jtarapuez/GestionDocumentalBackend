package ec.gob.iess.gestiondocumental.application.inventario;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InventarioDocumentalRegistroMapperTest {

    private InventarioDocumentalRegistroMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new InventarioDocumentalRegistroMapper();
    }

    @Nested
    @DisplayName("toNuevoInventario")
    class ToNuevo {

        @Test
        @DisplayName("mapea campos básicos, estado Registrado y metadatos de auditoría")
        void mapeoBasico() {
            InventarioDocumentalRequest r = new InventarioDocumentalRequest();
            r.setIdSeccion(1L);
            r.setIdSerie(2L);
            r.setIdSubserie(3L);
            r.setNumeroExpediente("EXP-99");
            r.setTipoArchivo("Físico");
            r.setSupervisor("SUP-1");
            r.setObservaciones("nota");

            LocalDateTime ahora = LocalDateTime.of(2026, 3, 23, 10, 0);
            InventarioDocumental inv = mapper.toNuevoInventario(r, "1712345678", "10.0.0.1", ahora);

            assertThat(inv.getIdSeccion()).isEqualTo(1L);
            assertThat(inv.getIdSerie()).isEqualTo(2L);
            assertThat(inv.getIdSubserie()).isEqualTo(3L);
            assertThat(inv.getNumeroExpediente()).isEqualTo("EXP-99");
            assertThat(inv.getTipoArchivo()).isEqualTo("Físico");
            assertThat(inv.getSupervisor()).isEqualTo("SUP-1");
            assertThat(inv.getObservaciones()).isEqualTo("nota");
            assertThat(inv.getOperador()).isEqualTo("1712345678");
            assertThat(inv.getEstadoInventario()).isEqualTo("Registrado");
            assertThat(inv.getFechaCambioEstado()).isEqualTo(ahora);
            assertThat(inv.getCedulaUsuarioCambio()).isEqualTo("1712345678");
            assertThat(inv.getUsuCreacion()).isEqualTo("1712345678");
            assertThat(inv.getFecCreacion()).isEqualTo(ahora);
            assertThat(inv.getIpEquipo()).isEqualTo("10.0.0.1");
        }

        @Test
        @DisplayName("aplica archivo pasivo y posición RAC")
        void archivoPasivo() {
            InventarioDocumentalRequest r = new InventarioDocumentalRequest();
            r.setNumeroExpediente("EXP-P");
            r.setTipoArchivo("PASIVO");
            r.setNumeroRac(9);
            r.setNumeroFila(8);

            InventarioDocumental inv = mapper.toNuevoInventario(r, "1712345678", "127.0.0.1", LocalDateTime.now());

            assertThat(inv.getPosicionPasivo()).isEqualTo("09.08.00.00.00");
            assertThat(inv.getNumeroRac()).isEqualTo(9);
            assertThat(inv.getNumeroFila()).isEqualTo(8);
        }
    }

    @Nested
    @DisplayName("aplicarParchesDesdeRequest")
    class Parches {

        @Test
        @DisplayName("solo actualiza campos no nulos del request")
        void parcial() {
            InventarioDocumental inv = new InventarioDocumental();
            inv.setNumeroExpediente("OLD");
            inv.setNumeroCedula("111");
            inv.setTipoArchivo("Físico");

            InventarioDocumentalRequest r = new InventarioDocumentalRequest();
            r.setNumeroExpediente("NEW");

            mapper.aplicarParchesDesdeRequest(inv, r);

            assertThat(inv.getNumeroExpediente()).isEqualTo("NEW");
            assertThat(inv.getNumeroCedula()).isEqualTo("111");
            assertThat(inv.getTipoArchivo()).isEqualTo("Físico");
        }

        @Test
        @DisplayName("actualiza fechas y archivo pasivo cuando vienen en el request")
        void conPasivo() {
            InventarioDocumental inv = new InventarioDocumental();
            inv.setNumeroExpediente("E1");

            InventarioDocumentalRequest r = new InventarioDocumentalRequest();
            r.setFechaDesde(LocalDate.of(2025, 1, 1));
            r.setTipoArchivo("PASIVO");
            r.setNumeroRac(4);

            mapper.aplicarParchesDesdeRequest(inv, r);

            assertThat(inv.getFechaDesde()).isEqualTo(LocalDate.of(2025, 1, 1));
            assertThat(inv.getTipoArchivo()).isEqualTo("PASIVO");
            assertThat(inv.getPosicionPasivo()).isEqualTo("04.00.00.00.00");
            assertThat(inv.getNumeroRac()).isEqualTo(4);
        }
    }
}
