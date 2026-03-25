# Fibonacci Active-Passive Microservices System

Este proyecto está dividido en tres carpetas independientes para facilitar su subida a AWS:

## Estructura
- **instancia1 / instancia2**: Servicios de cálculo matemático (Fibonacci).
- **proxi**: Proxy con el algoritmo Activo-Pasivo y la interfaz web.

## Ejecución Local

1. **Instancia 1 (Math Service):**
   ```bash
   cd instancia1
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
   ```

2. **Instancia 2 (Math Service):**
   ```bash
   cd instancia2
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
   ```

3. **Proxy:**
   ```bash
   cd proxi
   export MATH_SERVICE_A_URL=http://localhost:8081
   export MATH_SERVICE_B_URL=http://localhost:8082
   mvn spring-boot:run
   ```

4. Ver aplicación en: `http://localhost:8080/index.html`

## AWS Deployment (EC2)

1. Sube la carpeta **instancia1** a la primera máquina de AWS.
2. Sube la carpeta **instancia2** a la segunda máquina de AWS.
3. Sube la carpeta **proxi** a la tercera máquina de AWS.
4. En el Proxi, configura las variables de entorno con las IPs de las otras máquinas:
   ```bash
   export MATH_SERVICE_A_URL=http://<IP_A>:8081
   export MATH_SERVICE_B_URL=http://<IP_B>:8081
   mvn spring-boot:run
   ```
