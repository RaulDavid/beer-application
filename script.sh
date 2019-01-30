mvn clean spring-boot:run
docker rmi -f beer-image
docker rm  -f beer
docker build -t beer-image .
docker run  -p 8080:8080 -t beer-image
