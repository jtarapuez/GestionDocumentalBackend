#!/bin/bash

# =====================================================
# SCRIPT PARA EJECUTAR TODOS LOS SCRIPTS DE PRUEBA
# =====================================================
# Descripción: Ejecuta todos los scripts SQL en orden
# Uso: ./ejecutar_todos.sh
# =====================================================

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuración de conexión
DB_USER="DOCUMENTAL_OWNER"
DB_PASS="DOC87desa"
DB_HOST="192.168.29.208"
DB_PORT="1539"
DB_SERVICE="PDBIESS_DESA"
DB_CONNECTION="${DB_USER}/${DB_PASS}@${DB_HOST}:${DB_PORT}/${DB_SERVICE}"

# Configurar codificación UTF-8 para caracteres especiales (tildes, ñ, etc.)
export NLS_LANG=SPANISH_ECUADOR.UTF8

echo -e "${GREEN}=====================================================${NC}"
echo -e "${GREEN}  EJECUTANDO SCRIPTS DE DATOS DE PRUEBA${NC}"
echo -e "${GREEN}=====================================================${NC}"
echo ""

# Directorio de scripts
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Función para ejecutar un script
ejecutar_script() {
    local script=$1
    local descripcion=$2
    
    echo -e "${YELLOW}Ejecutando: ${descripcion}${NC}"
    echo "Archivo: $script"
    echo ""
    
    # Ejecutar con NLS_LANG configurado para UTF-8
    if NLS_LANG=SPANISH_ECUADOR.UTF8 sqlplus -S "${DB_CONNECTION}" @"${SCRIPT_DIR}/${script}" > /tmp/sql_output.log 2>&1; then
        echo -e "${GREEN}✓ ${descripcion} - EXITOSO${NC}"
        echo ""
        return 0
    else
        echo -e "${RED}✗ ${descripcion} - ERROR${NC}"
        echo "Ver logs en /tmp/sql_output.log"
        cat /tmp/sql_output.log | tail -20
        echo ""
        return 1
    fi
}

# Ejecutar scripts en orden
echo -e "${YELLOW}Paso 0/5: Insertar Catálogo de Secciones Documentales${NC}"
ejecutar_script "05_insertar_catalogos_secciones.sql" "Catálogo de Secciones Documentales (Áreas IESS)" || exit 1

echo -e "${YELLOW}Paso 1/5: Insertar Secciones${NC}"
ejecutar_script "01_insertar_secciones.sql" "Secciones Documentales" || exit 1

echo -e "${YELLOW}Paso 2/5: Insertar Series${NC}"
ejecutar_script "02_insertar_series.sql" "Series Documentales" || exit 1

echo -e "${YELLOW}Paso 3/5: Insertar Subseries${NC}"
ejecutar_script "03_insertar_subseries.sql" "Subseries Documentales" || exit 1

echo -e "${YELLOW}Paso 4/5: Insertar Inventarios${NC}"
ejecutar_script "04_insertar_inventarios.sql" "Inventarios Documentales" || exit 1

echo ""
echo -e "${GREEN}=====================================================${NC}"
echo -e "${GREEN}  ✓ TODOS LOS SCRIPTS EJECUTADOS EXITOSAMENTE${NC}"
echo -e "${GREEN}=====================================================${NC}"
echo ""
echo "Datos de prueba insertados:"
echo "  - 1 Catálogo de Secciones Documentales (60+ áreas IESS)"
echo "  - 3 Secciones de prueba"
echo "  - 3 Series"
echo "  - 4 Subseries"
echo "  - 5 Inventarios"
echo ""
echo "Puedes verificar los datos con las consultas en README.md"

