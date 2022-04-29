# Mendel

## Ejecutar con Docker

* Java version --> 17
```bash
Make sure you have java 17 configured in your shell
```

* Build JAR
```bash
mvn clean install
```

* Build docker image
```bash
docker build -t mendel .
```

* Run container
```bash
docker run -p 8080:8080 mendel
```

## Casos de uso

### Create Transaction

```bash
curl --location --request PUT 'localhost:8080/api/v1/transactions/1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "amount": 210.0,
    "type": "test",
    "parent_id": 1
}'
```

### Get Transaction IDs by type
```bash
curl --location --request GET 'localhost:8080/api/v1/transactions/types/test'
```

### sum Transaction Amounts Transitively
```bash
curl --location --request GET 'localhost:8080/api/v1/transactions/sum/1'
```