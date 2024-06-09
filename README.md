# How to build 
``mvn package``

# How to run
``java -jar target/[built_jar].jar [arguments specific to the controller or worker]``

# Usage examples

``java -jar target/controller-1.0-SNAPSHOT.jar -s http://localhost:8080/example/form -w http://localhost:1100 http://localhost:1099 -m POST -n 10 -H "Content-Type: application/json" -t '{"name": "Seb", "surname": "Paw", "about": "I like dogs"}'``