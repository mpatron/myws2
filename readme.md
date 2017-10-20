http://wildfly-swarm.io/generator/

mvn wildfly-swarm:run

curl -i http://localhost:8880/api/hello

curl -i -H "Content-Type: application/json" -X POST -d '{"message" : "toto"}' http://localhost:8880/api/hello

Pour JDBC, regarder l'exemple https://github.com/wildfly-swarm/wildfly-swarm-examples/tree/2017.8.1/datasource/datasource-war
https://stackoverflow.com/questions/40294812/how-to-force-maven-to-ignore-repository-from-dependcy-pom-xml