

docker run -v D:/docker/SmppsimApi/logs:/usr/local/tomcat/logs -v D:/docker/SmppsimApi/config:/usr/local/tomcat/config -p 8080:8080 smppsimapi-img:predev_v1.1



##  Copy the properties file into the image
## COPY config/SmppsimApi.properties /usr/local/tomcat/config/SmppsimApi.properties

docker run -p 8080:8080 smppsimapi-img:predev_v1.1

docker run -v D:/docker/SmppsimApi/logs:/usr/local/tomcat/logs -v D:/docker/SmppsimApi/config:/usr/local/tomcat/config -p 8080:8080 smppsimapi-img:predev_v1.1

docker ps

docker exec -it a6afaa7a88f2 /bin/bash


docker images


docker build -t smppsimapi-img:predev_v1.1 .

docker rmi 8b1b636ea104







# Run a Docker container with port mapping
docker run -p 8080:8080 smppsimapi-img:predev_v1.1

# Run a Docker container with volume mounts and port mapping
docker run -v D:/docker/SmppsimApi/logs:/usr/local/tomcat/logs -v D:/docker/SmppsimApi/config:/usr/local/tomcat/config -p 8080:8080 smppsimapi-img:predev_v1.1

# List all running containers
docker ps

# Execute a command inside a running container
docker exec -it a6afaa7a88f2 /bin/bash

# List all available Docker images
docker images

# Build a Docker image with a specific tag
docker build -t smppsimapi-img:predev_v1.1 .

# Remove a Docker image by Image ID
docker rmi 8b1b636ea104




# Build an Image from Dockerfile
docker build -t custom_image:tag .

# List Docker Images
docker images

# Run a Container with Environment Variables
docker run -d -e ENV_VARIABLE=value custom_image:tag

# List All Containers (including stopped ones)
docker ps -a

# Execute a Command in a Running Container
docker exec -it my_container another_command_to_execute

# Start a Stopped Container
docker start my_container

# Stop the container
docker stop container_name_or_id

# restart the container
docker restart container_name_or_id


# Create a Network
docker network create my_network

# Run a Container in a Specific Network
docker run -d --network my_network --name container_in_network custom_image:tag

# Inspect Container Details
docker inspect container_in_network

# View Logs of a Container
docker logs container_in_network

# Remove a Container
docker stop container_in_network
docker rm container_in_network

# Remove an Image
docker rmi custom_image:tag

# Remove a Network
docker network rm my_network
