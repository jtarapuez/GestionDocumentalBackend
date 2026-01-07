|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 1**</p>|
| - | :-: | :- |
||**Estándar** ||
Plantilla Quarkus

**Estándar**

PAS-EST-043  

Versión del documento 1.0 

© 2025 Dirección Nacional de Tecnologías de la Información TODOS LOS DERECHOS RESERVADOS 

Queda reservado el derecho de propiedad de este documento, con la facultad de disponer de él, publicarlo, traducirlo o autorizar su traducción, así como reproducirlo total o parcialmente, por cualquier sistema o medio. 

No se permite la reproducción total o parcial de este documento, ni su incorporación a un sistema informático, ni su locación, ni su transmisión en cualquier forma o por cualquier medio, sea este escrito o electrónico, mecánico, por fotocopia, por grabación u otros métodos, sin el permiso previo y escrito de los titulares de los derechos y del copyright. 

FOTOCOPIAR ES DELITO. 

Otros nombres de compañías y productos mencionados en este documento, pueden ser marcas comerciales o marcas registradas por sus respectivos dueños. 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 2**</p>|
| - | :-: | :- |
||**Estándar** ||


|**FIRMAS Y APROBACIONES** ||||
| - | :- | :- | :- |
|**ELABORADO POR:**  ||||
|**Nombre** |**Cargo - Unidad** |**Fecha** |**Firma** |
|Ing. Diego Martínez |<p>Analista TI - Subdirección Nacional de Arquitectura </p><p>y Soluciones </p>|2025-08-26 ||
|**REVISADO POR:**  ||||
|**Nombre – Cargo** |**Cargo - Unidad** |**Fecha** |**Firma** |
|Ing. Jimmy Vera |<p>Analista TI - Subdirección Nacional de Arquitectura </p><p>y Soluciones </p>|2025-08-26 ||
|**APROBADO POR:**  ||||
|**Nombre – Cargo** |**Cargo - Unidad** |**Fecha** |**Firma** |
|Ing. Carlos Aníbal Román Pachar |Subdirector - Subdirección Nacional de Arquitectura y Soluciones |2025-08-26 ||


|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 3**</p>|
| - | :-: | :- |
||**Estándar** ||


|**LISTA DE CAMBIOS** ||||
| - | :- | :- | :- |
|**Versión** |**Fecha** |**Autor** |**Descripción** |
|1\.0 |2025-08-26 |Jimmy Vera, Diego Martinez |Emisión Inicial |



|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 4**</p>|
| - | :-: | :- |
||**Estándar** ||
**TABLA DE CONTENIDOS ![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.002.png)**

Contenido 

1. [**OBJETIVO ........................................................................................................................................6**](#_page5_x68.00_y269.80)
1. [**RESUMEN ........................................................................................................................................6**](#_page5_x68.00_y401.80)
1. [**ARQUITECTURA DE LA PLANTILLA.............................................................................................8**](#_page7_x68.00_y179.80)
1. [**ESTRUCTURA GENERAL DE CARPETAS Y ARCHIVOS ............................................................9**](#_page8_x68.00_y179.80)
1. [**ORGANIZACIÓN DEL CÓDIGO FUENTE .....................................................................................12**](#_page11_x68.00_y179.80)
1. [**ESTRUCTURA DEL PROYECTO ..................................................................................................12**](#_page11_x68.00_y414.80)
1. [**ESTÁNDARES DE NOMBRADO ...................................................................................................12**](#_page11_x68.00_y668.80)
1. [**CONFIGURACIÓN Y EXTENSIBILIDAD .......................................................................................13**](#_page12_x68.00_y207.80)
1. [**EXTENSIONES ...............................................................................................................................13**](#_page12_x68.00_y425.80)
1. [**SEGURIDAD ...................................................................................................................................15** ](#_page14_x68.00_y319.80)[AUTENTICACIÓN VÍA KEYCLOAK (OIDC). ............................................................................................... 15](#_page14_x68.00_y708.80)
- [AUTORIZACIÓN POR ROLES (RBAC). ................................................................................................... 16](#_page15_x68.00_y682.80)
- [MANEJO DE TOKENS JWT. .................................................................................................................. 17](#_page16_x68.00_y359.80)
- [CIFRADO EN TRÁNSITO (TLS) Y EN REPOSO (VAULT)............................................................................ 18](#_page17_x68.00_y179.80)
11. [**BUENAS PRÁCTICAS DE ARQUITECTURA QUARKUS. ...........................................................19**](#_page18_x68.00_y318.80)

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 5**</p>|
| - | :-: | :- |
||**Estándar** ||


12. [**APIS ................................................................................................................................................19**](#_page18_x68.00_y628.80)
12. [**CONCLUSIONES ...........................................................................................................................21**](#_page20_x68.00_y584.80)
12. [**ANEXOS .........................................................................................................................................21**](#_page20_x68.00_y716.80)

Restringido  Ó IESS-Instituto Ecuatoriano de Seguro Social, 2025 



|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 6**</p>|
| - | :-: | :- |
||**Estándar** ||
**Quarkus** 

1. **Objetivo<a name="_page5_x68.00_y269.80"></a>** 

Este documento tiene como objetivo orientar sobre la organización, componentes y mejores prácticas para estructurar proyectos basados en Quarkus empleando una plantilla estándar. Sirve tanto para proyectos institucionales como para desarrollo por proveedores externos. La plantilla se integra en la arquitectura institucional basada en tecnologías de orquestación de contenedores (RKE2/Kubernetes), automatización de despliegues (GitLab), control de versiones y gestión segura de secretos (ArgoCD y Vault). 

2. **Resumen<a name="_page5_x68.00_y401.80"></a>** 

El  presente  documento  define  la  arquitectura  técnica  para  el  desarrollo,  despliegue  y  operación presentando  el bosquejo de un proyecto base está diseñado para construir servicios backend en Quarkus destinados a exponer APIs REST seguras, conectarse a bases de datos como Oracle, Postgresql, entre otras y ejecutarse en contenedores y desplegarse en Kubernetes (RKE2 / OpenShift). Todo esto tomando como referencia la arquitectura base: 


|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 7**</p>|
| - | :-: | :- |
||**Estándar** ||
![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.003.jpeg)



|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 8**</p>|
| - | :-: | :- |
||**Estándar** ||
3. **Arquitectura<a name="_page7_x68.00_y179.80"></a> de la plantilla** 

![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.004.jpeg)

Este diagrama ilustra las capas y componentes principales: 

- Capa de presentación (API REST, controller) 
- Capa de servicio (service) 
- Capa de persistencia y modelo (repository, model) 
- Configuración y dependencias (application.properties, pom.xml) 
- Infraestructura para despliegue (Docker, Kubernetes/OpenShift) 
- Pruebas (test) 
- Archivos  de  soporte  para  integración,  despliegue  y  documentación  (README,  .gitignore, .dockerignore, mvnw, target, etc.) 

El flujo muestra cómo una petición viaja desde la API REST, pasa por la lógica de negocio, accede a la base de datos y finaliza con procesos de pruebas y despliegue sobre contenedores. 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 9**</p>|
| - | :-: | :- |
||**Estándar** ||
4. **Estructura<a name="_page8_x68.00_y179.80"></a> General de Carpetas y Archivos** 

Basado en la convención Maven y adaptado al modelo institucional de microservicios, cada proyecto debe tener la siguiente estructura base: 

nombre-proyecto/ ![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.005.png)

` `├── nombre-proyecto-api/         # Endpoints REST, controladores 

` `├── nombre-proyecto-core/        # Lógica de negocio y servicios 

` `├── nombre-proyecto-domain/      # Modelos de datos, DTOs, entidades JPA  ├── nombre-proyecto-infra/       # Configuración, conectores externos 

` `├── nombre-proyecto-tests/       # Pruebas unitarias e integración 

` `├── Dockerfile                   # Contenedor base 

` `├── helm/                        # Chart de despliegue 

` `├── kustomize/                   # Configuración de ambientes (dev, qa, prod) 

` `└── README.md                    # Descripción técnica y dependencias 

Ejemplo: 

iess-proyecto-base-quarkus-completo/ │ ![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.006.png)

├── .mvn/ 

├── .settings/ 

├── docs/ 

├── infra/ 

│   ├──cloud/ 

│   ├──deploy/ 

│   ├──helm/ 

├── scripts/ 

├── ci/ 

├── src/ 

│   ├── main/ 

│   │   ├── java/ 

│   │   │   └── ec/iess/proyecto/ 

│   │   │       ├── Application.java │   │   │       ├── controller/ 

│   │   │       ├── service/ 

│   │   │       ├── repository/ 

│   │   │       └── model/ 

│   │   └── resources/ 

│   │       └── application.properties │   └── test/ 

│       └── java/ 

├── target/ 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 10**</p>|
| - | :-: | :- |
||**Estándar** ||
├── .classpath ├── .dockerignore ├── .gitignore ├── .project ![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.007.png)

├── mvnw 

├── mvnw.cmd 

├── pom.xml 

└── README.md 

La plantilla incluye los siguientes elementos principales: 

- **.mvn/** 

  Carpeta interna usada por Maven para almacenar información y scripts necesarios para la construcción y empaquetado del proyecto. Es útil para la automatización e integración continua. 

- **.settings/** 

  Configuraciones  específicas  del  entorno  de  desarrollo  (por  ejemplo,  Eclipse  IDE).  No  afecta  la ejecución, pero facilita el trabajo colaborativo y la alineación de configuraciones entre desarrolladores. 

- **/docs/** 

  Documentación del proyecto generada en archivos MarkDown. En esta carpeta se constaran también los lineamientos puntuales del proyecto. 

- **/ci/** 

  Contiene todo lo relacionado con la automatización del ciclo de vida del proyecto:  

- pipelines de CI/CD (gitlab-ci.yml, jenkinsfile) 
- scripts reutilizables de build/test/deploy,  
- Configuración de análisis estático (SonarQube, OWASP, Trivy). 
- Integraciones con Harbor (para imágenes Docker). 
- Plantillas de variables de entorno (.env.example). 
- **/deploy/** 

  Son los despliegues declarativos**,** contiene manifiestos Kubernetes puros, listos para aplicar con kubectl apply  o  integrarse  en  GitOps.  Es  decir  archivos  YAML  nativos  (Deployment,  Service,  Ingress, ConfigMap, Secret, RBAC, Namespace). 

- **/helm/** 

  Facilita empaquetado y despliegue GitOps con ArgoCD, corresponde al despliegue empaquetado y parametrizable,  es  decir  contiene  el  Chart  Helm  del  servicio,  para  despliegue  reproducible  y automatizado en Kubernetes (Chart.yaml, values.yaml). 

- **/scripts/** 

  Archivos de migraciones y preparación de datos. 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 11**</p>|
| - | :-: | :- |
||**Estándar** ||
- **src/** 

  Raíz del código fuente y recursos del proyecto 

- **main/java/** 

  Código fuente principal de la aplicación. 

- **main/resources/** 

  Recursos (archivos de configuración como application.properties, plantillas, archivos estáticos, etc.). 

- **test/java/** 

  Pruebas unitarias y de integración. 

- **target/** 

  Carpeta  generada  automáticamente  durante  la  compilación.  Contiene  los  artefactos  (JAR/WAR), reportes de pruebas, archivos temporales, etc. No debe incluirse en el control de versiones. 

- **.classpath, .project, .settings** 

  Archivos de configuración para integración con IDEs como Eclipse. Permiten importar el proyecto de forma sencilla. 

- **.dockerignore** 

  Define  qué  archivos/directorios  se  excluyen  al  construir  la  imagen  Docker  para  la  aplicación, optimizando el tamaño y seguridad 

- **.gitignore** 

  Lista de archivos y carpetas excluidos del control de versiones Git (por ejemplo, target/, archivos temporales, credenciales locales, etc.).** 

- **mvnw & mvnw.cmd** 

  Scripts que permiten ejecutar Maven sin necesidad de una instalación local, asegurando la portabilidad entre ambientes.** 

- **pom.xml** 

  Archivo de configuración principal del proyecto Maven: 

- Define dependencias, plugins, perfiles y configuración de compilación. 
- Es el corazón del proyecto Quarkus para administrar versiones, dependencias de Quarkus y extensiones. 
- **README.md** 

  Documento introductorio y guía para desarrolladores sobre la finalidad, estructura, dependencias, comandos principales y buenas prácticas del proyecto, puede apoyarse en otros archivos MarkDown que se encuentren en la carpeta /doc/ 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 12**</p>|
| - | :-: | :- |
||**Estándar** ||
5. **Organización<a name="_page11_x68.00_y179.80"></a> del Código Fuente** 

Dentro de src/main/con**troller/** 

Expone APIs REST, recibe peticiones externas y coordina respuestas, usando la extensión Quarkus RESTEasy.( Clases que gestionan las rutas y endpoints REST API).** 

- **service/** 

  Implementa lógica de negocio, reglas y procesamiento de la información. (Lógica de negocio, reglas y transformación de datos).** 

- **repository/** 

  Gestiona la persistencia e interacción con bases de datos usando Jakarta Persistence + Quarkus** 

- **model/** 

  Entidades y objetos de transferencia (DTOs).** 

6. **Estructura<a name="_page11_x68.00_y414.80"></a> del proyecto** 

La estructura de los paquetes se tomará como referencia Domain Driven Design: 

/src/main/java/gob/iess/ 

├── domain/ 

│   ├── model/           # Entidades, Value Objects, Agregados 

│   ├── services/        # Lógica de dominio (reglas de negocio) │   └── events/          # Eventos del dominio 

├── application/ 

│   ├── usecases/        # Casos de uso (coordinan operaciones) │   └── services/        # Orquestación de flujo 

├── infrastructure/ 

│   ├── persistence/     # Repositorios (DB) 

│   ├── rest/            # Controladores REST 

│   └── adapters/        # Integraciones externas 

└── interfaces/ 

`    `└── api/             # DTOs, controladores REST, validaciones 

7. **Estándares<a name="_page11_x68.00_y668.80"></a> de Nombrado** 
- Nombres en minúsculas, sin caracteres especiales. 
- Métodos con notación camelCase; clases en PascalCase. 
- Los nombres deben reflejar claramente el propósito del componente. 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 13**</p>|
| - | :-: | :- |
||**Estándar** ||
ec.gob.iess.<dominio>.<modulo>.<componente> ![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.008.png)

8. **Configuración<a name="_page12_x68.00_y207.80"></a> y Extensibilidad** 
- **application.properties** 

  Define  la  configuración  (puertos,  credenciales,  endpoints,  parámetros  de  logging,  setting  de extensiones Quarkus) 

- **pom.xml** 

  Modulariza dependencias para desarrollo, producción, test, etc. 

- **Dockerfile y .dockerignore** 

  Permite empaquetar la aplicación en imágenes y desplegar en Kubernetes/RKE2/. 

- **Pruebas automatizadas** 

  Es recomendable usar JUnit y Mock frameworks, ubicando los tests en src/test/java.** 

9. **Extensiones<a name="_page12_x68.00_y425.80"></a>**  



|**EXTENSION** |**CONDICION** |**DESCRIPCION** ||||
| - | - | - | :- | :- | :- |
|quarkus-resteasy-reactive |Obligatoria  para  cualquier servicio REST |Implementa  endpoints  HTTP/REST  con soporte JAX-RS de alto rendimiento. ||||
|quarkus-resteasy-reactive- jackson |Si  se  manejan  objetos JSON |Permite  serialización  y  deserialización automática de JSON (usa Jackson). ||||
|quarkus-smallrye-openapi |Obligatoria  si  expones APIs documentadas ![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.009.png)|Genera especificación OpenAPI/Swagger UI  automáticamente  desde  las ![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.010.png)anotaciones. ||||
|quarkus-swagger-ui |Opcional  pero recomendada para QA/dev |Habilita  interfaz  web  interactiva  para probar endpoints REST. ||||
|quarkus-hibernate-orm |Si  se  accede  a  bases relacionales |Proporciona  ORM  basado  en  JPA  y Hibernate. ||||
|quarkus-jdbc-oracle |Si usas Oracle DB |Driver  JDBC  nativo  optimizado  para Oracle 11g/19c. ||||
|quarkus-agroal |Siempre que se use JDBC |Administra pools de conexiones a base de datos. ||||
|quarkus-flyway |Recomendado en entornos CI/CD |Gestiona  migraciones  de  esquema  de base de datos automáticamente. ||||
|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 14**</p>||||
||**Estándar** |||||


|quarkus-arc |Obligatoria (núcleo de CDI) |Framework de inyección de dependencias (CDI) liviano de Quarkus. ||||
| - | - | :- | :- | :- | :- |
|quarkus-config-yaml |Si  usas  configuración externa YAML |Permite  leer  configuraciones  desde archivos .yaml además de .properties. ||||
|quarkus-logging-gelf  o quarkus-logging-json |Si  integras  con  Loki  o Elastic |Envía  logs  en  formato  estructurado (JSON/GELF) para centralización. ||||
|quarkus-smallrye-health |Obligatoria en Kubernetes |Exposición  de  endpoints  /q/health  para readiness y liveness probes. ||||
|quarkus-smallrye-metrics |Obligatoria  en  entornos observables |Exposición  de  métricas  /q/metrics compatibles con Prometheus. ||||
|quarkus-smallrye-fault- tolerance |Si se requiere resiliencia |Implementa patrones @Retry, @Timeout, @CircuitBreaker. ||||
|quarkus-smallrye- opentracing  o  quarkus- ![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.011.png)opentelemetry |Si usas Jaeger o Tempo |Exporta  trazas  distribuidas  de  cada request. ![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.012.png)||||
|quarkus-oidc |Si se usa Keycloak o SSO |Autenticación  y  autorización  basada  en OpenID Connect. ||||
|quarkus-security |Base  para  control  de acceso |Define roles, permisos y anotaciones de seguridad (@RolesAllowed). ||||
|quarkus-container-image-jib |En pipelines CI/CD |Construye  imágenes  Docker  nativas desde el código fuente. ||||
|quarkus-kubernetes |Si despliegas directamente en RKE2 |Genera  manifiestos  Kubernetes automáticamente (deployment.yaml, etc.). ||||
|quarkus-openshift |Si usas OpenShift en lugar de RKE2 |Genera  recursos  específicos  para despliegue en OpenShift. ||||
|quarkus-redis-client |Si usas Redis como cache |Cliente  reactivo  para  interacción  con Redis. ||||
|quarkus-kafka-client |Si  usas  Kafka  para mensajería |Cliente  Kafka  nativo  compatible  con SmallRye Reactive Messaging. ||||
|quarkus-scheduler |Si  se  ejecutan  tareas programadas |Permite  definir  jobs  periódicos (@Scheduled). ||||
|quarkus-mailer |Si  se  requiere  envío  de notificaciones por correo |Cliente  SMTP  configurable  con  soporte asíncrono. ||||
|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 15**</p>||||
||**Estándar** |||||


|quarkus-qute |Si  generas  documentos HTML o textos templados |Motor de plantillas nativo de Quarkus (útil para reportes o correos). |
| - | :- | :- |
|quarkus-test-junit5 |Siempre que haya pruebas unitarias |Extensión  para  ejecutar  tests  JUnit  5 integrados con Quarkus. |
|quarkus-devtools |Entorno de desarrollo local |Provee  el  comando  quarkus  dev  con recarga en caliente |
|uarkus-container-image-jib |En pipelines CI/CD |Construye  imágenes  Docker  nativas desde el código fuente. |

10. **Seguridad<a name="_page14_x68.00_y319.80"></a>** 

Se describen las prácticas recomendadas para asegurar aplicaciones, con el objetivo de garantizar que todas  las  Apis  cuenten  con  un  nivel  de  seguridad,  siguiendo  estándares  y  buenas  prácticas.

![](Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.013.jpeg)

El diagrama muestra una arquitectura de seguridad moderna y robusta para aplicaciones desarrolladas con Quarkus, donde se externaliza la autenticación a Keycloak (OIDC) y se asegura la comunicación y el manejo de credenciales con TLS y un Vault. 

<a name="_page14_x68.00_y708.80"></a>**Autenticación vía Keycloak (OIDC).** 

- Keycloak actúa como Identity Provider (IdP). 
- Quarkus consume tokens OIDC emitidos por Keycloak para autenticar usuarios. 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 16**</p>|
| - | :-: | :- |
||**Estándar** ||
- El flujo recomendado para aplicaciones web y APIs REST es Authorization Code Flow (para aplicaciones con frontend) y Client Credentials Flow (para microservicios). 

Dependencias necesarias en pom.xml:

<dependency> 

`    `<groupId>io.quarkus</groupId> 

`    `<artifactId>quarkus-oidc</artifactId> </dependency> 

Configuración application.properties: 

\# URL del servidor Keycloak quarkus.oidc.auth-server-url=https://keycloak.ejemplo.com/auth/realms/mi-realm quarkus.oidc.client-id=mi-app quarkus.oidc.credentials.secret=MI\_CLIENT\_SECRET quarkus.oidc.token-path=/protocol/openid-connect/token quarkus.oidc.application-type=web-app quarkus.http.auth.proactive=true 

Ejemplo de consumo: 

**@Path**("/secure") 

**public** **class** **SecureResource** { 

`    `**@GET** 

`    `**@RolesAllowed**("user") 

`    `**public** Response **userAccess**() { 

`        `**return** Response.ok("Acceso de usuario permitido").build();     } 

`    `**@GET** 

`    `**@RolesAllowed**("admin") 

`    `**@Path**("/admin") 

`    `**public** Response **adminAccess**() { 

`        `**return** Response.ok("Acceso de administrador permitido").build();     } 

} 

- **Autorización<a name="_page15_x68.00_y682.80"></a> por roles (RBAC).** 
- La autorización se maneja mediante roles asignados en Keycloak. 
- Quarkus valida estos roles usando @RolesAllowed. 
- Se pueden definir roles por endpoint o por método. 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 17**</p>|
| - | :-: | :- |
||**Estándar** ||
Configuración de roles en Keycloak: 

1. Crear roles a nivel de realm o cliente. 
1. Asignar roles a usuarios o grupos. 

Configuración application.properties:  quarkus.oidc.roles.source=real 

realm: Roles definidos en el realm. client: Roles definidos a nivel de cliente 

- **Manejo<a name="_page16_x68.00_y359.80"></a> de tokens JWT.** 
- JWT se utiliza como bearer token para la autenticación. 
- Contiene información de usuario y roles. 
- Se recomienda verificar: 
  - Firma del token. 
  - Tiempo de expiración. 
  - Audiencia (aud) y emisor (iss). 

Configuración application.properties:

quarkus.oidc.token.issuer=https://keycloak.ejemplo.com/auth/realms/mi- realmquarkus.oidc.token.audience=mi-app 

Ejemplo de verificación 

**@Path**("/jwt") 

**public** **class** **JwtResource** { 

`    `**@Inject** 

`    `JsonWebToken jwt; 

`    `**@GET** 

`    `**public** String **info**() { 

`        `**return** "Usuario autenticado: " + jwt.getName() + ", Roles: " + jwt.getGroups();     } 

} 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 18**</p>|
| - | :-: | :- |
||**Estándar** ||
- **Cifrado<a name="_page17_x68.00_y179.80"></a> en tránsito (TLS) y en reposo (Vault).** 

**Cifrado en tránsito (TLS/HTTPS)** 

- Todas las conexiones externas e internas deben usar TLS 1.2 o superior. 

Configuración application.properties: 

quarkus.http.ssl-port=8443 quarkus.http.ssl.certificate.key-store-file=keystore.p12 quarkus.http.ssl.certificate.key-store-password=changeit quarkus.http.ssl.certificate.key-store-type=PKCS12 

**Cifrado en reposo** 

- Usar **Vault** para almacenar secretos sensibles (contraseñas, certificados, tokens). 
- Integración con Quarkus: 

<dependency> 

`    `<groupId>io.quarkus</groupId> 

`    `<artifactId>quarkus-vault</artifactId> </dependency> 

Configuración application.properties: 

quarkus.vault.url=https://vault.ejemplo.com quarkus.vault.authentication=token quarkus.vault.token=MI\_VAULT\_TOKEN quarkus.vault.kv-secret-engine-version=2 

Ejemplo Acceso programático: 

**public** **class** **SecretService** { 

`    `**@Inject** 

`    `VaultKVSecretEngine kvSecretEngine; 

`    `**public** String **getDbPassword**() { 

`        `**return** kvSecretEngine.readSecret("database/password").get().get("value");     } 

} 

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 19**</p>|
| - | :-: | :- |
||**Estándar** ||
**Recomendaciones de seguridad** 

- Nunca hardcodear claves secretas en el código. 
- Usar HTTPS en todos los entornos (desarrollo, QA, producción). 
- Validar siempre JWT y roles en el backend. 
- Mantener Keycloak actualizado. 
- Registrar intentos fallidos y auditar accesos críticos. 
- Rotar tokens y secretos regularmente. 
11. **Buenas<a name="_page18_x68.00_y318.80"></a> Prácticas de Arquitectura Quarkus.** 
- Microservicios:  Separar  la  lógica  por  contextos  de  negocio,  cada  uno  en  su  propio repositorio/proyecto si aplica. 
- Despliegue  en  contenedores:  Orquestación  por  Kubernetes/RKE2/,  siguiendo  la  estructura  y archivos Docker incluidos o sugeridos. 
- Integración Continua y Entrega Continua (CI/CD): Automatizar pruebas, construcción y despliegue usando pipelines. 
- Seguridad: Usar extensiones Quarkus para autenticación y autorización, gestionando secretos fuera del código fuente.. 
- Observabilidad: Integra herramientas de monitoreo como Prometheus y Grafana, además de logging centralizado con ELK. 
- Configuración y propiedades externas: Modularizar la configuración para facilitar adaptación entre ambientes dev, test y prod. 
- Documentación: Mantener README.md y documentación de APIs (OpenAPI/Swagger). 
- Evitar cargas pesadas directas: usar vistas materializadas o réplica de lectura para consultas 

  masivas. 

12. **APIs<a name="_page18_x68.00_y628.80"></a>** 
- Usar JWT o OAuth2/OIDC via quarkus-oidc o quarkus-smallrye-jwt. 
- TLS obligatorio en prod. 
- Roles declarados con @RolesAllowed. 
- Usar contratos JSON uniformes  

|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 20**</p>|
| - | :-: | :- |
||**Estándar** ||
{ 

`  `"data": { ... },  

`  `"meta": { 

`    `"timestamp": "2025-10-13T14:00:00Z",     "path": "/api/v1/usuarios/123" 

`  `}, 

`  `"error": { 

`    `"code": **null**, 

`    `"message": **null**, 

`    `"details": **null** 

`  `} 

} 

- data: contenido principal o resultado. 
- meta: información adicional (paginación, request-id, tiempos, etc.). 
- error: solo presente cuando hay error. 
- En servicios de streaming, data puede ser reemplazado por un stream JSON lines (NDJSON) o SSE (Server Sent Events). 
- Usar OpenAPI y exponer swagger-ui solo en entornos no productivos 
- Agregar en cada respuesta un requestId o traceId en meta, alineado con observabilidad (Jaeger, Prometheus, etc.). 
- Definir versión en la ruta (/api/v1/...) 

Códigos de respuestas  



|**CODIGO HTTP** |**MENSAJE** |**DESCRIPCION** |||
| - | - | - | :- | :- |
|200 |ok |Respuesta  exitosa  estándar  (GET,  PUT, PATCH). |||
|201 |Created |Recurso  creado  (POST).  Incluye  header Location |||
|202 |Accepted |Solicitud  aceptada  para  procesamiento asíncrono |||
|204 |No Content |Operación  exitosa  sin  cuerpo  (DELETE, PUT vacío) |||
|400 |Bad Request (validación) |Error de validación o parámetros inválidos. |||
|401 |Unauthorized |Token inválido o ausente. |||
|![ref1]|<p>**DIRECCIÓN NACIONAL DE TECNOLOGÍAS DE LA INFORMACIÓN** </p><p>**SUBDIRECCIÓN NACIONAL DE ARQUITECTURA Y SOLUCIONES** </p>|<p>**COD: ARQ\_Estandar\_JEE** </p><p>**FECHA: 18/09/2023 VERSIÓN:** 2.0 **PÁG.: 21**</p>|||
||**Estándar** ||||


|403 |Forbidden |Usuario  autenticado  sin  permisos suficientes. |
| - | - | :- |
|404 |Not Found |Recurso no existente. |
|422 |Unprocessable Entity |Validaciones de negocio fallidas. |
|429 |Too Many Requests |Límite de peticiones excedido (rate limiting). |
|500 |Internal |Error interno no controlado. |
|503 |Service Unavailable |Dependencia o backend caído. |

- Implementar un @Provider con ExceptionMapper<Throwable> que formatee errores según el contrato JSON: 

**@Provider** 

**public** **class** **ErrorMapper** **implements** ExceptionMapper<Throwable> { 

`    `**public** Response **toResponse**(Throwable ex) { 

`        `**var** error = Map.of( 

`            `"code", **500**, 

`            `"message", ex.getMessage() 

`        `); 

`        `**return** Response.status(Response.Status.INTERNAL\_SERVER\_ERROR) 

.entity(Map.of("data", **null**, "meta", Map.of(), "error", error))                        .build(); 

`    `} 

} 

- Para pruebas unitarias usar @QuarkusTest, contrato (contract testing) con RestAssured. 
- Usar Mock de dependencias externas con quarkus-wiremock. 
13. **Conclusiones<a name="_page20_x68.00_y584.80"></a>** 
- Desarrollo rápido y modular con Quarkus (arranque rápido, bajo consumo). 
- Despliegue automatizado y reproducible mediante GitOps. 
- Seguridad unificada y auditable. 
- Escalabilidad horizontal y resiliencia. 
- Observabilidad integral en tiempo real. 
14. **Anexos<a name="_page20_x68.00_y716.80"></a>** 

N/A 

[ref1]: Aspose.Words.57cceda1-0d28-4e80-a56d-9b4155afe8d0.001.png
