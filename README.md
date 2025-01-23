# Star Wars API Integration

Este proyecto es una API RESTful que integra con la API de Star Wars (SWAPI) y proporciona endpoints seguros para consultar información sobre personajes, películas, naves espaciales y vehículos.

## Características

- Integración con SWAPI (Star Wars API)
- Autenticación JWT
- Endpoints protegidos
- Paginación de resultados
- Búsqueda por ID y nombre
- Pruebas unitarias y de integración
- Dockerizado para fácil despliegue

## Requisitos Previos

Para desarrollo local:
- Java 8 JDK
- Maven 3.6+
- Un IDE compatible con Java (recomendado: IntelliJ IDEA, Eclipse, VS Code)

Para despliegue con Docker:
- Docker
- Docker Compose

## Ejecución con Docker

1. Construir y ejecutar con Docker Compose:
```bash
docker-compose up --build
```

2. Para ejecutar en segundo plano:
```bash
docker-compose up -d
```

3. Para detener los contenedores:
```bash
docker-compose down
```

## Uso con Docker Hub

1. Para subir la imagen a Docker Hub:
```bash
# Login en Docker Hub
docker login

# Construir la imagen
docker-compose build

# Subir la imagen
docker-compose push
```

2. Para usar la imagen desde Docker Hub:
```bash
# Reemplaza 'tuusuario' con el nombre de usuario correcto
docker pull tuusuario/starwars-api:latest
docker run -p 8080:8080 tuusuario/starwars-api:latest
```

La aplicación estará disponible en `http://localhost:8080`

## Configuración del Proyecto (Desarrollo Local)

1. Clonar el repositorio:
```bash
git clone [URL_DEL_REPOSITORIO]
cd pruebaTecnicaConexa
```

2. Compilar el proyecto:
```bash
mvn clean install
```

3. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Configuración

Las principales configuraciones se encuentran en `application.properties`:

```properties
# JWT Configuration
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000

# Server Configuration
server.port=8080
```

## Documentación de la API

La API proporciona acceso a datos de Star Wars a través de endpoints RESTful protegidos con autenticación JWT.

### Autenticación

#### Registro de Usuario
```http
POST /api/v1/auth/register
```
**Request Body:**
```json
{
    "username": "string",
    "password": "string"
}
```
**Response:**
```json
{
    "message": "User registered successfully"
}
```

#### Login
```http
POST /api/v1/auth/login
```
**Request Body:**
```json
{
    "username": "string",
    "password": "string"
}
```
**Response:**
```json
{
    "token": "string",
    "type": "Bearer"
}
```

### Recursos

Todos los endpoints de recursos requieren autenticación JWT y soportan los siguientes parámetros de consulta:
- `page`: Número de página (por defecto: 1)
- `id`: ID específico del recurso
- `name`/`title`: Término de búsqueda

#### People (Personas)

```http
GET /api/v1/people?page=1
GET /api/v1/people?id={id}
GET /api/v1/people?name={name}
```

**Ejemplo de Respuesta:**
```json
{
    "results": [
        {
            "uid": "string",
            "properties": {
                "name": "string",
                "height": "string",
                "mass": "string",
                "birth_year": "string"
            }
        }
    ],
    "total_records": 0,
    "total_pages": 0,
    "next": "string",
    "previous": "string"
}
```

#### Films (Películas)

```http
GET /api/v1/films?page=1
GET /api/v1/films?id={id}
GET /api/v1/films?title={title}
```

**Ejemplo de Respuesta:**
```json
{
    "results": [
        {
            "uid": "string",
            "properties": {
                "title": "string",
                "episode_id": 0,
                "opening_crawl": "string",
                "director": "string",
                "producer": "string",
                "release_date": "string"
            }
        }
    ],
    "total_records": 0,
    "total_pages": 0,
    "next": "string",
    "previous": "string"
}
```

#### Starships (Naves Espaciales)

```http
GET /api/v1/starships?page=1
GET /api/v1/starships?id={id}
GET /api/v1/starships?name={name}
```

**Ejemplo de Respuesta:**
```json
{
    "results": [
        {
            "uid": "string",
            "properties": {
                "name": "string",
                "model": "string",
                "manufacturer": "string",
                "cost_in_credits": "string",
                "length": "string",
                "crew": "string",
                "passengers": "string"
            }
        }
    ],
    "total_records": 0,
    "total_pages": 0,
    "next": "string",
    "previous": "string"
}
```

#### Vehicles (Vehículos)

```http
GET /api/v1/vehicles?page=1
GET /api/v1/vehicles?id={id}
GET /api/v1/vehicles?name={name}
```

**Ejemplo de Respuesta:**
```json
{
    "results": [
        {
            "uid": "string",
            "properties": {
                "name": "string",
                "model": "string",
                "manufacturer": "string",
                "cost_in_credits": "string",
                "length": "string",
                "crew": "string",
                "passengers": "string"
            }
        }
    ],
    "total_records": 0,
    "total_pages": 0,
    "next": "string",
    "previous": "string"
}
```

### Paginación

Todos los endpoints **?page=** devuelven listas paginadas con un limite de 10.

### Manejo de Errores

**Ejemplo de Respuesta de Error:**
```json
{
    "timestamp": "2024-01-22T12:00:00Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid request parameters",
    "path": "/api/v1/people"
}
```

## Autenticación

La API utiliza autenticación JWT. Para acceder a los endpoints protegidos:

1. Registrar un usuario:
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario","password":"contraseña"}'
```

2. Iniciar sesión:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario","password":"contraseña"}'
```

3. Usar el token JWT recibido en las llamadas subsiguientes:
```bash
curl -X GET http://localhost:8080/api/v1/people \
  -H "Authorization: Bearer {token}"
```

## Pruebas

El proyecto incluye pruebas unitarias y de integración. Para ejecutarlas:

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar solo pruebas unitarias
mvn test -Dtest=*Test

# Ejecutar solo pruebas de integración
mvn test -Dtest=*IntegrationTest
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/pruebaTecnicaConexa/demo/
│   │       ├── config/          # Configuraciones de Spring
│   │       ├── controller/      # Controladores REST
│   │       ├── model/          # Modelos de datos
│   │       ├── security/       # Configuración de seguridad
│   │       └── service/        # Servicios
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/pruebaTecnicaConexa/demo/
            ├── service/         # Pruebas unitarias
            └── integration/     # Pruebas de integración
```

## Tecnologías Utilizadas

- Spring Boot 2.7.18
- Spring Security
- JWT (JSON Web Tokens)
- WebClient
- Lombok
- JUnit 5
- Mockito
- Maven

## Despliegue en Railway

1. Crear una cuenta en [Railway](https://railway.app/)

2. Instalar Railway CLI (opcional):
```bash
npm i -g @railway/cli
```

3. Login en Railway:
```bash
railway login
```

4. Iniciar nuevo proyecto:
```bash
railway init
```

5. Desplegar la aplicación:
```bash
railway up
```

Alternativamente, puedes desplegar directamente desde GitHub:

1. Ve a [Railway](https://railway.app/)
2. Inicia sesión con tu cuenta de GitHub
3. Crea un nuevo proyecto
4. Selecciona "Deploy from GitHub repo"
5. Selecciona este repositorio
6. Railway detectará automáticamente el Dockerfile y desplegará la aplicación

Variables de entorno necesarias en Railway:
- `JWT_SECRET`
- `JWT_EXPIRATION`
- `SPRING_PROFILES_ACTIVE=prod`

### Monitoreo y Logs

- Puedes ver los logs en tiempo real desde el dashboard de Railway
- Railway proporciona métricas básicas de CPU y memoria
- Puedes configurar alertas para cuando la aplicación tenga problemas

### URLs y Dominios

Railway asignará automáticamente un subdominio para tu aplicación. Por ejemplo:
`https://tu-app.railway.app`

También puedes configurar tu propio dominio personalizado en la sección de configuración del proyecto.