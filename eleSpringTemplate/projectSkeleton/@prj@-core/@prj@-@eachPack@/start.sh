#!/bin/sh

APP_ID="@orgPath@.scm.@pack@.api"
APP_HOME=`pwd`
CATALINA_HOME="${APP_HOME}/tomcat"

echo "catalina home: ${CATALINA_HOME}"
cd ${CATALINA_HOME}
./bin/catalina.sh run