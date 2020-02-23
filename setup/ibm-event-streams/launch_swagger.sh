#!/bin/bash
set -x

# Get Swagger definition
rm -rf admin-api/
wget https://raw.githubusercontent.com/ibm-messaging/event-streams-docs/master/admin-rest-api/admin-rest-api.yaml
mkdir admin-api
mv admin-rest-api.yaml admin-api/

# Launch Swagger-UI w/ Docker container
docker run -d --rm \
  -p 9080:8080 \
  -v ${PWD}/admin-api:/usr/share/nginx/html/admin-api \
  -e API_URL=admin-api/admin-rest-api.yaml \
  --name ic-event-streams-admin-api \
  swaggerapi/swagger-ui

# Open Swagger-UI w/ browser
open http://localhost:9080
