# README

## 1 - Lancement de l'application
Deux modes pour lancer l'application :
```bash
git clone git@github.com:mpatron/myws2.git
cd myws2
git checkout tags/v1.4

java -jar target\demo-swarm.jar
# ou
mvn wildfly-swarm:run
```

## 2 - Tests
Tester l'application en mode console :
```bash
curl -X GET -i http://localhost:8880/api/hello
curl -X POST -i -H "Content-Type: application/json" -d '{"message" : "toto"}' http://localhost:8880/api/hello
curl -X GET http://localhost:8880/api/swagger.json
```

## 3 - Swagger
Lancer dans une console séparée : 
```bash
java -jar .\swagger-ui-2017.10.0-swarm.jar
```
Accéder à swagger-ui par : 
```bash
http://localhost:8080/swagger-ui
```
Dans la page web de swagger-ui, mentionner l'url suivant :
```bash
http://localhost:8880/api/swagger.json
```

## 4 - Sources
* fork it on : https://github.com/mpatron/myws2

## Annexes
* http://wildfly-swarm.io/generator/
* https://github.com/wildfly-swarm/wildfly-swarm-examples/tree/2017.8.1/datasource/datasource-war
* https://stackoverflow.com/questions/40294812/how-to-force-maven-to-ignore-repository-from-dependcy-pom-xml
* http://wildfly-swarm.io/posts/wildfly-swarm-s-got-swagger/

