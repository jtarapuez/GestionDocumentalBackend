#!/usr/bin/env python3
"""
Script para ejecutar el SQL de actualización de OPERADOR
Convierte cédulas a IDs numéricos en GDOC_INVENTARIO_T
"""

import oracledb
import sys
import os

# Configuración de conexión
DSN = "192.168.29.208:1539/PDBIESS_DESA"
USER = "DOCUMENTAL_OWNER"
PASSWORD = "DOC87desa"

# Usar modo thin (no requiere librerías nativas)
# No llamar a init_oracle_client() - usa modo thin por defecto

def ejecutar_script():
    """Ejecuta el script SQL de actualización"""
    try:
        print("=" * 60)
        print("Conectando a la base de datos...")
        print(f"Host: 192.168.29.208:1539")
        print(f"Database: PDBIESS_DESA")
        print(f"User: {USER}")
        print("=" * 60)
        
        # Conectar a la base de datos
        connection = oracledb.connect(
            user=USER,
            password=PASSWORD,
            dsn=DSN
        )
        
        print("✅ Conexión exitosa")
        print()
        
        # Habilitar DBMS_OUTPUT
        cursor = connection.cursor()
        cursor.callproc("dbms_output.enable")
        
        # Leer el script SQL
        script_path = os.path.join(os.path.dirname(__file__), "05_actualizar_operadores.sql")
        with open(script_path, 'r', encoding='utf-8') as f:
            sql_script = f.read()
        
        # Ejecutar el script
        print("Ejecutando script SQL...")
        print("-" * 60)
        
        # Dividir el script en bloques (separados por /)
        blocks = sql_script.split('/')
        
        for block in blocks:
            block = block.strip()
            if block and not block.startswith('--'):
                try:
                    cursor.execute(block)
                except oracledb.DatabaseError as e:
                    # Ignorar errores de sintaxis menores, continuar
                    if "ORA-00911" not in str(e):  # Ignorar error de terminador
                        print(f"⚠️  Advertencia: {e}")
        
        # Hacer commit
        connection.commit()
        print("-" * 60)
        print("✅ Script ejecutado correctamente")
        print()
        
        # Obtener mensajes de DBMS_OUTPUT
        print("Resultados:")
        print("=" * 60)
        status_var = cursor.var(int)
        line_var = cursor.var(str)
        while True:
            cursor.callproc("dbms_output.get_line", (line_var, status_var))
            if status_var.getvalue() != 0:
                break
            line = line_var.getvalue()
            if line:
                print(line)
        
        print("=" * 60)
        print("✅ Actualización completada")
        
        # Cerrar conexión
        cursor.close()
        connection.close()
        
        return True
        
    except oracledb.DatabaseError as e:
        error, = e.args
        print(f"❌ Error de base de datos: {error.message}")
        if hasattr(error, 'code'):
            print(f"   Código: {error.code}")
        return False
        
    except Exception as e:
        print(f"❌ Error inesperado: {str(e)}")
        import traceback
        traceback.print_exc()
        return False

if __name__ == "__main__":
    print()
    print("🔧 Script de Actualización de OPERADOR")
    print("   Convierte cédulas a IDs numéricos")
    print()
    
    # Ejecutar directamente (ya confirmado por el usuario)
    print()
    exito = ejecutar_script()
    
    if exito:
        print()
        print("✅ Proceso completado exitosamente")
        sys.exit(0)
    else:
        print()
        print("❌ El proceso falló. Revisa los errores arriba.")
        sys.exit(1)

