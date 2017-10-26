#!/bin/bash

#The file should have UNIX-style EOL

if [ -z "$TEMPLATE_API_CONFIG" ]
then
  TEMPLATE_API_CONFIG="template-api.yml"
fi

echo "config file: $TEMPLATE_API_CONFIG"


if [ -f /opt/newrelic/newrelic.yml ]; then
    java -javaagent:/opt/newrelic/newrelic.jar  ${JAVA_OPTS} -jar forms-api.jar server $TEMPLATE_API_CONFIG
else
    java  ${JAVA_OPTS} -jar forms-api.jar server $TEMPLATE_API_CONFIG
fi
