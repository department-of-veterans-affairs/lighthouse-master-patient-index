#!/usr/bin/env bash
set -euo pipefail

cd $(dirname $0)

SERVER_KEYSTORE=mock-mpi-server.keystore
SERVER_TRUSTSTORE=mock-mpi-server.truststore
SERVER_CERT=mock-mpi-server.cert

CLIENT_KEYSTORE=mock-mpi-client.keystore
CLIENT_TRUSTSTORE=mock-mpi-client.truststore
CLIENT_CERT=mock-mpi-client.cert

PASSWORD=password123

TEN_YEARS=3650

for file in \
  $SERVER_KEYSTORE \
  $SERVER_TRUSTSTORE \
  $SERVER_CERT \
  $CLIENT_KEYSTORE \
  $CLIENT_TRUSTSTORE \
  $CLIENT_CERT
do
  if [ -f $file ]; then rm $file; fi
done
set -x
keytool -genkey -alias mock-mpi-server -keyalg RSA -keystore $SERVER_KEYSTORE \
  -dname "cn=Unknown, ou=Unknown, o=Unknown, c=Unknown" \
  -validity $TEN_YEARS \
  -keypass $PASSWORD \
  -storepass $PASSWORD
keytool -export -alias mock-mpi-server -keystore $SERVER_KEYSTORE -file $SERVER_CERT \
  -keypass $PASSWORD \
  -storepass $PASSWORD



keytool -genkey -alias mock-mpi-client -keyalg RSA -keystore $CLIENT_KEYSTORE \
  -dname "cn=Unknown, ou=Unknown, o=Unknown, c=Unknown" \
  -validity $TEN_YEARS \
  -keypass $PASSWORD \
  -storepass $PASSWORD

keytool -export -alias mock-mpi-client -keystore $CLIENT_KEYSTORE -file $CLIENT_CERT \
  -keypass $PASSWORD \
  -storepass $PASSWORD

keytool -import -alias mock-mpi-server -keystore $CLIENT_TRUSTSTORE -file $SERVER_CERT \
  -noprompt \
  -keypass $PASSWORD \
  -storepass $PASSWORD

keytool -import -alias mock-mpi-client -keystore $SERVER_TRUSTSTORE -file $CLIENT_CERT \
  -noprompt \
  -keypass $PASSWORD \
  -storepass $PASSWORD




