#!/bin/bash
touch .env

echo "Insert password: "
read pwd1

echo "BACKEND_DB_CONN_ADDR=127.0.0.1" > .env
echo "BACKEND_DB_CONN_PASS=$pwd1" >> .env

echo "spring.datasource.password=$pwd1" > ./src/springjpa11/src/main/resources/application_password.properties

sed s/localhost/172.18.0.1/g ./src/springjpa11/src/main/resources/application.properties | tee ./src/springjpa11/src/main/resources/application.properties
sed s/mesh.NETWORK_ADDR=127.0.0.1/mesh.NETWORK_ADDR=172.18.0.1/g ./src/springjpa11/src/main/resources/application_connection_urls.properties | tee ./src/springjpa11/src/main/resources/application_connection_urls.properties

echo "Done."
cat .env
cat ./src/springjpa11/src/main/resources/*
