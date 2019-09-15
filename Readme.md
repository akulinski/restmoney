# Prerequisites
* JDK 11

Before running project from IDE build it using gradle. Dagger has to do its magic and create DaggerApplicationComponent.

## Test

```shell script
./gradlew clean - In windows use gradlew.bat
```

 ```shell script
./gradlew test - In windows use gradlew.bat
```
## How to run

 ```shell script
./gradlew clean      - In windows use gradlew.bat
```

```shell script
./gradlew shadowJar
```

```shell script
java -jar build/libs/restmoney-1.0-all.jar 
```


## Examples

```shell script
curl -X POST \
  http://localhost:8080/api/v1/create-account \
  -H 'Content-Type: application/json' \
  -d '{
	"accountNumber":"123456789220",
	"balance":1000
}'
```

```shell script
    curl -X GET \
      http://localhost:8080/api/v1/get-all-accounts 
```

```shell script

    curl -X POST \
      http://localhost:8080/api/v1/transfer \
      -H 'Content-Type: application/json' \
      -d '{
    	"fromBankAccount":"xxxxxx",
    	"toBankAccount":"xxxx",
    	"amount":100
    }'
```

```shell script

curl -X GET \
  http://localhost:8080/api/v1/find-by-id/1 

```
