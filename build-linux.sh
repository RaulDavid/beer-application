docker stop beer-container
docker rmi -f beer-image
docker rm  -f beer-container
docker build -t beer-image .
docker run  -p 8080:8080 --name beer-container -t beer-image
