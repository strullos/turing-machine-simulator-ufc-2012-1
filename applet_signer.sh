#!/bin/bash

rm teoria_ufc_keystore
keytool -genkey -keystore teoria_ufc_keystore -alias teoria_ufc -dname "CN=Teoria, OU=UFC, O=UFC, L=Fortaleza,S=CE, C=BR" -keypass teoria -storepass teoria
keytool -selfcert -keystore teoria_ufc_keystore -alias teoria_ufc -keypass teoria -storepass teoria
jarsigner -keystore teoria_ufc_keystore turing_simulator.jar teoria_ufc -keypass teoria -storepass teoria
echo "Finished! Applet should be signed now!"
