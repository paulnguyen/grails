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
	java -jar build/libs/hellograils-0.1.war

docker-build: build
	docker build -t hellograils .
	docker images

docker: docker-build
	docker build -t hellograils .
	docker run --name hellograils -td hellograils
	docker exec -it hellograils bash 	

docker-clean:
	docker stop hellograils
	docker rm hellograils
	docker rmi hellograils

docker-run:
	docker run --name hellograils -td -p 8080:8080 hellograils
	docker ps

docker-network:
	docker network inspect host
	docker network inspect bridge

docker-stop:
	docker stop hellograils
	docker rm hellograils

docker-shell:
	docker exec -it hellograils bash 

