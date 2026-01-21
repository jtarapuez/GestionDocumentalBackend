#!/bin/bash
# Script para validar conexión a Oracle antes de iniciar la aplicación
# Uso: ./scripts/validar-conexion-bd.sh

HOST="192.168.29.208"
PORT="1539"
SERVICE="PDBIESS_DESA"
USER="DOCUMENTAL_OWNER"
PASS="DOC87desa"

echo "=========================================="
echo "Validando conexión a Base de Datos Oracle"
echo "=========================================="
echo "Host: $HOST"
echo "Port: $PORT"
echo "Service: $SERVICE"
echo "User: $USER"
echo ""

# Verificar conectividad de red
echo "1. Verificando conectividad de red..."
if nc -zv $HOST $PORT 2>&1 | grep -q "succeeded"; then
    echo "   ✅ Puerto $PORT accesible en $HOST"
else
    echo "   ❌ ERROR: No se puede conectar al puerto $PORT en $HOST"
    echo "   Verifica:"
    echo "   - Que el servidor Oracle esté corriendo"
    echo "   - Que el firewall permita conexiones al puerto $PORT"
    echo "   - Que tengas acceso de red al servidor"
    exit 1
fi

# Verificar si sqlplus está disponible para prueba más completa
if command -v sqlplus &> /dev/null; then
    echo ""
    echo "2. Probando conexión con sqlplus..."
    sqlplus -S "${USER}/${PASS}@${HOST}:${PORT}/${SERVICE}" << SQL 2>&1 | grep -v "^$" | head -5
SELECT 'Conexión exitosa!' AS RESULTADO FROM DUAL;
SELECT COUNT(*) AS TOTAL_CATALOGOS FROM GDOC_CATALOGOS_T;
EXIT;
SQL
    if [ $? -eq 0 ]; then
        echo "   ✅ Conexión a Oracle exitosa"
    else
        echo "   ❌ ERROR: No se pudo autenticar con las credenciales"
        echo "   Verifica usuario y contraseña"
        exit 1
    fi
else
    echo ""
    echo "2. sqlplus no disponible - solo se verificó conectividad de red"
    echo "   ⚠️  Para validación completa, instala Oracle Client con sqlplus"
fi

echo ""
echo "=========================================="
echo "✅ Validación completada"
echo "=========================================="
echo "La aplicación puede intentar conectarse a la BD"
echo ""



