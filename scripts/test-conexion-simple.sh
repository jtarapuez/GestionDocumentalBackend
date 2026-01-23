#!/bin/bash
# Script simple para probar conexión a Oracle
# Uso: ./scripts/test-conexion-simple.sh

echo "=========================================="
echo "Test SIMPLE de Conexión a Oracle"
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

# 1. Verificar conectividad de red
echo "1. Verificando conectividad de red..."
if command -v nc &> /dev/null; then
    if nc -zv -w 5 $HOST $PORT 2>&1 | grep -q "succeeded"; then
        echo "   ✅ Puerto $PORT accesible en $HOST"
    else
        echo "   ❌ ERROR: No se puede conectar al puerto $PORT en $HOST"
        echo "   Verifica:"
        echo "     - Que el servidor Oracle esté corriendo"
        echo "     - Que tengas acceso de red"
        echo "     - Que el firewall permita conexiones"
        exit 1
    fi
else
    echo "   ⚠️  nc (netcat) no disponible, saltando verificación de red"
fi

# 2. Verificar si sqlplus está disponible
echo ""
echo "2. Verificando si sqlplus está disponible..."
if command -v sqlplus &> /dev/null; then
    echo "   ✅ sqlplus encontrado"
    echo ""
    echo "3. Probando conexión con sqlplus..."
    echo "   (Esto puede tomar unos segundos...)"
    
    sqlplus -S "${USER}/${PASS}@${HOST}:${PORT}/${SERVICE}" << SQL 2>&1 | tee /tmp/test-oracle-output.txt
SET PAGESIZE 0
SET FEEDBACK OFF
SET HEADING OFF
SELECT '=== CONEXION EXITOSA ===' FROM DUAL;
SELECT 'Fecha del servidor: ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS') FROM DUAL;
SELECT 'Usuario conectado: ' || USER FROM DUAL;
SELECT 'Total tablas GDOC: ' || COUNT(*) FROM USER_TABLES WHERE TABLE_NAME LIKE 'GDOC_%';
EXIT;
SQL
    
    EXIT_CODE=$?
    
    if [ $EXIT_CODE -eq 0 ]; then
        if grep -q "CONEXION EXITOSA" /tmp/test-oracle-output.txt; then
            echo ""
            echo "=========================================="
            echo "✅ CONEXIÓN EXITOSA"
            echo "=========================================="
            echo ""
            echo "La base de datos está accesible y las credenciales son correctas."
            rm -f /tmp/test-oracle-output.txt
            exit 0
        else
            echo ""
            echo "❌ ERROR: La conexión falló"
            echo "Revisa el output anterior para más detalles"
            rm -f /tmp/test-oracle-output.txt
            exit 1
        fi
    else
        echo ""
        echo "❌ ERROR: No se pudo conectar (código de salida: $EXIT_CODE)"
        echo ""
        echo "Posibles causas:"
        echo "  - Credenciales incorrectas"
        echo "  - Service name incorrecto"
        echo "  - Servidor Oracle no disponible"
        echo "  - Problemas de red/firewall"
        rm -f /tmp/test-oracle-output.txt
        exit 1
    fi
else
    echo "   ❌ sqlplus NO está disponible"
    echo ""
    echo "Para probar la conexión completa, necesitas:"
    echo "  1. Instalar Oracle Instant Client"
    echo "  2. O usar el test Java: mvn test -Dtest=TestConexionOracleSimple"
    echo ""
    echo "Por ahora solo se verificó la conectividad de red."
    echo "Si el paso 1 fue exitoso, el puerto está abierto."
    exit 0
fi





