#!/bin/bash
# Test simple de conexión a Oracle

echo "=========================================="
echo "Test de Conexión a Oracle"
echo "=========================================="
echo ""

HOST="192.168.29.208"
PORT="1539"
SERVICE="PDBIESS_DESA"
USER="DOCUMENTAL_OWNER"
PASS="DOC87desa"

echo "Credenciales:"
echo "  Host: $HOST"
echo "  Port: $PORT"
echo "  Service: $SERVICE"
echo "  User: $USER"
echo ""

# Verificar conectividad de red
echo "1. Verificando conectividad de red al puerto $PORT..."
if command -v nc &> /dev/null; then
    if timeout 5 nc -zv $HOST $PORT 2>&1 | grep -q "succeeded"; then
        echo "   ✅ Puerto $PORT accesible en $HOST"
    else
        echo "   ❌ ERROR: No se puede conectar al puerto $PORT en $HOST"
        echo "   Verifica que el servidor Oracle esté corriendo"
        exit 1
    fi
else
    echo "   ⚠️  nc (netcat) no disponible"
fi

# Verificar sqlplus
echo ""
echo "2. Verificando sqlplus..."
if command -v sqlplus &> /dev/null; then
    echo "   ✅ sqlplus encontrado"
    echo ""
    echo "3. Probando conexión..."
    
    sqlplus -S "${USER}/${PASS}@${HOST}:${PORT}/${SERVICE}" << 'SQL'
SET PAGESIZE 0
SET FEEDBACK OFF
SET HEADING OFF
SELECT '=== CONEXION EXITOSA ===' FROM DUAL;
SELECT 'Fecha: ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS') FROM DUAL;
SELECT 'Usuario: ' || USER FROM DUAL;
SELECT 'Tablas GDOC: ' || COUNT(*) FROM USER_TABLES WHERE TABLE_NAME LIKE 'GDOC_%';
EXIT;
SQL

    if [ $? -eq 0 ]; then
        echo ""
        echo "✅ CONEXIÓN EXITOSA"
        exit 0
    else
        echo ""
        echo "❌ ERROR: No se pudo conectar"
        exit 1
    fi
else
    echo "   ❌ sqlplus NO está disponible"
    echo ""
    echo "Para probar la conexión completa necesitas Oracle Instant Client"
    echo "Por ahora solo se verificó la conectividad de red."
    exit 0
fi





