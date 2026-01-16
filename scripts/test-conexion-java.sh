#!/bin/bash
# Script para ejecutar el test Java de conexión

echo "=========================================="
echo "Ejecutando Test Java de Conexión Oracle"
echo "=========================================="
echo ""

cd "$(dirname "$0")/.." || exit 1

echo "Compilando y ejecutando test..."
mvn clean test -Dtest=TestConexionOracleSimple

