#!/bin/bash

jar -cvf turing_simulator.jar -C turing_simulator_extracted_jar .
sh applet_signer.sh
