# Ejemplos de Endpoints de Consultas

Esta carpeta contiene ejemplos JSON para probar cada filtro de consultas uno por uno.

## Estructura

- `01_filtro_seccion.json` - ✅ Funcionando correctamente
- `02_filtro_tipo_archivo.json` - ❌ Problema: Filtra pasivo pero sale activo
- `03_filtro_operador.json` - ❌ Problema: Valores hardcodeados
- `04_filtro_supervisor.json` - ❌ Problema: Valores hardcodeados

## Cómo usar

1. Probar cada endpoint con Postman o curl usando los ejemplos JSON
2. Verificar que los resultados del backend sean correctos
3. Ajustar el código del frontend según sea necesario

## Notas

- Todos los endpoints usan `POST /api/v1/consultas`
- El backend espera strings para `operador` y `supervisor` (ej: "1", "2")
- El backend espera strings para `tipoArchivo` (ej: "Archivo activo", "Archivo pasivo")
