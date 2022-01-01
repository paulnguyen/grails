all: clean 

clean:
	find . -name "*.class" -exec rm -rf {} \;
	rm -f build/libs/*
	rm -f *.db

compile:
	grails compile

build: compile
	grails war

run: compile
	echo Starting Grails at:  http://localhost:8080
	grails run-app

run-test: compile
	echo Starting Grails at:  http://localhost:8080
	grails test run-app	

run-boot: build
	echo Starting Grails at:  http://localhost:8080
	java -jar build/libs/gumball-v2-0.1.war

docker-build: build
	docker build -t gumball:v2 .
	docker images

docker: docker-build
	docker build -t gumball:v2 .
	docker run --network gumball --name gumball -td gumball:v2
	docker exec -it gumball bash

docker-push:
	docker login
	docker build -t $(account)/gumball:v2 -t $(account)/gumball:v2 .
	docker push $(account)/gumball:v2 

docker-clean:
	docker stop gumball
	docker rm gumball
	docker rmi gumball:v2

docker-run:
	docker run --network gumball --name gumball -td -p 8080:8080 gumball:v2
	docker ps

docker-create-network:
	docker network create gumball

docker-network:
	docker network inspect host
	docker network inspect bridge

docker-stop:
	docker stop gumball
	docker rm gumball

docker-shell:
	docker exec -it gumball bash

mysql-clean:
	docker stop mysql
	docker rm mysql

mysql-run:
	docker run -d --network gumball --name mysql -td -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password mysql:5.7

mysql-shell:
	docker exec -it mysql bash 


jumpbox-clean:
	docker stop jumpbox
	docker rm jumpbox

jumpbox-run:
	docker run -d --network gumball --name jumpbox ubuntu sleep 3600

jumpbox-shell:
	docker exec -it jumpbox bash 



