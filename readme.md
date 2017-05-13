http://wildfly-swarm.io/generator/

mvn wildfly-swarm:run

curl -i http://localhost:8080/api/hello

curl -i -H "Content-Type: application/json" -X POST -d '{"message" : "toto"}' http://localhost:8080/api/hello