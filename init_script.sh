#!/bin/bash
touch .env

echo "Insert password: "
read pwd1
echo "Insert secret store key: "
read sec1 # for this arg, visit secret store or in my case the private repo / drive url

echo "BACKEND_DB_CONN_ADDR=172.18.0.1" > .env
echo "BACKEND_DB_CONN_PASS=$pwd1" >> .env

echo "spring.datasource.password=$pwd1" > ./src/springjpa11/src/main/resources/application_password.properties

sed s/localhost/172.18.0.1/g ./src/springjpa11/src/main/resources/application.properties | tee ./src/springjpa11/src/main/resources/application.properties
sed s/mesh.NETWORK_ADDR=127.0.0.1/mesh.NETWORK_ADDR=172.18.0.1/g ./src/springjpa11/src/main/resources/application_connection_urls.properties | tee ./src/springjpa11/src/main/resources/application_connection_urls.properties
sed s/REACT_APP_ADDRESS=127.0.0.1/REACT_APP_ADDRESS=172.18.0.1/g ./src/frontend/.env | tee ./src/frontend/.env

gpg -o appKey.txt.retrieve --passphrase "$sec1" --batch --yes -d appKey.txt.gpg # if gpg is not initialized, might fail
cp appKey.txt.retrieve ./src/contentscanner/src/main/resources/keyValue.txt

# to restrict necessary permissions, no cleanup is performed.
# rm ./*.retrieve 
# rm ./*.gpg


echo "Done."
cat .env
cat ./src/springjpa11/src/main/resources/*
