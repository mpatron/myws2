# README

## 0 - Status
[![Build Status](https://travis-ci.org/mpatron/myws2.svg?branch=master)](https://travis-ci.org/mpatron/myws2)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Avant de commencer, voir le status du code en cours sur travis-ci :
https://travis-ci.org/mpatron/myws2

## 1 - Lancement de l'application
Deux modes pour lancer l'application :
```bash
git clone git@github.com:mpatron/myws2.git
cd myws2
git checkout tags/v1.5

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
Accéder à swagger-ui par : 
```bash
http://localhost:8880/swagger-ui/
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
* https://github.com/for-GET/http-decision-diagram

