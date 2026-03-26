package ec.gob.iess.gestiondocumental.application.inventario;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ArchivoPasivoPosicionTest {

    @Nested
    @DisplayName("esArchivoPasivo")
    class EsArchivoPasivo {

        @Test
        @DisplayName("acepta PASIVO y Archivo pasivo")
        void aceptaValoresConocidos() {
            assertThat(ArchivoPasivoPosicion.esArchivoPasivo("PASIVO")).isTrue();
            assertThat(ArchivoPasivoPosicion.esArchivoPasivo("Archivo pasivo")).isTrue();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { " ", "Físico", "pasivo", "PASIVO " })
        @DisplayName("rechaza null, vacío y otros tipos")
        void rechazaOtros(String tipo) {
            assertThat(ArchivoPasivoPosicion.esArchivoPasivo(tipo)).isFalse();
        }
    }

    @Nested
    @DisplayName("debeAplicarUbicacionPasiva")
    class DebeAplicar {

        @Test
        @DisplayName("true solo con tipo pasivo y numeroRac no nulo")
        void requierePasivoYRac() {
            InventarioDocumentalRequest r = new InventarioDocumentalRequest();
            r.setTipoArchivo("PASIVO");
            r.setNumeroRac(1);
            assertThat(ArchivoPasivoPosicion.debeAplicarUbicacionPasiva(r)).isTrue();

            r.setNumeroRac(null);
            assertThat(ArchivoPasivoPosicion.debeAplicarUbicacionPasiva(r)).isFalse();

            r.setNumeroRac(1);
            r.setTipoArchivo("Físico");
            assertThat(ArchivoPasivoPosicion.debeAplicarUbicacionPasiva(r)).isFalse();
        }

        @Test
        @DisplayName("request null no aplica")
        void requestNull() {
            assertThat(ArchivoPasivoPosicion.debeAplicarUbicacionPasiva(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("resolverPosicionPasivo")
    class Resolver {

        @Test
        @DisplayName("prioriza literal no vacío del request")
        void usaLiteral() {
            InventarioDocumentalRequest r = reqPasivo(5);
            r.setPosicionPasivo("  01.02.03.04.05  ");
            assertThat(ArchivoPasivoPosicion.resolverPosicionPasivo(r)).isEqualTo("01.02.03.04.05");
        }

        @Test
        @DisplayName("formatea RAC y componentes nulos como cero")
        void formateaConCeros() {
            InventarioDocumentalRequest r = reqPasivo(3);
            r.setPosicionPasivo(null);
            assertThat(ArchivoPasivoPosicion.resolverPosicionPasivo(r)).isEqualTo("03.00.00.00.00");
        }

        @Test
        @DisplayName("rellena todos los segmentos")
        void formateaCompleto() {
            InventarioDocumentalRequest r = reqPasivo(12);
            r.setNumeroFila(3);
            r.setNumeroColumna(4);
            r.setNumeroPosicion(5);
            r.setBodega(6);
            assertThat(ArchivoPasivoPosicion.resolverPosicionPasivo(r)).isEqualTo("12.03.04.05.06");
        }
    }

    @Nested
    @DisplayName("copiarCamposPasivoAlDominio")
    class Copiar {

        @Test
        @DisplayName("no modifica destino si no aplica pasivo+RAC")
        void noOpSiNoAplica() {
            InventarioDocumental d = new InventarioDocumental();
            d.setPosicionPasivo("viejo");
            InventarioDocumentalRequest r = new InventarioDocumentalRequest();
            r.setTipoArchivo("Físico");
            r.setNumeroRac(1);
            ArchivoPasivoPosicion.copiarCamposPasivoAlDominio(d, r);
            assertThat(d.getPosicionPasivo()).isEqualTo("viejo");
        }

        @Test
        @DisplayName("copia posición y RAC cuando aplica")
        void copiaCampos() {
            InventarioDocumental d = new InventarioDocumental();
            InventarioDocumentalRequest r = reqPasivo(7);
            r.setNumeroFila(1);
            r.setNumeroColumna(2);
            r.setNumeroPosicion(3);
            r.setBodega(4);
            ArchivoPasivoPosicion.copiarCamposPasivoAlDominio(d, r);
            assertThat(d.getPosicionPasivo()).isEqualTo("07.01.02.03.04");
            assertThat(d.getNumeroRac()).isEqualTo(7);
            assertThat(d.getNumeroFila()).isEqualTo(1);
            assertThat(d.getNumeroColumna()).isEqualTo(2);
            assertThat(d.getNumeroPosicion()).isEqualTo(3);
            assertThat(d.getBodega()).isEqualTo(4);
        }

        @Test
        @DisplayName("legacy Archivo pasivo")
        void legacy() {
            InventarioDocumental d = new InventarioDocumental();
            InventarioDocumentalRequest r = new InventarioDocumentalRequest();
            r.setTipoArchivo("Archivo pasivo");
            r.setNumeroRac(2);
            ArchivoPasivoPosicion.copiarCamposPasivoAlDominio(d, r);
            assertThat(d.getPosicionPasivo()).isEqualTo("02.00.00.00.00");
        }
    }

    private static InventarioDocumentalRequest reqPasivo(int rac) {
        InventarioDocumentalRequest r = new InventarioDocumentalRequest();
        r.setTipoArchivo("PASIVO");
        r.setNumeroRac(rac);
        return r;
    }
}
