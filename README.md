# api-collap-fs-uploader
[![Build Status](https://travis-ci.org/api-collab/api-collab-fs-uploader.png)](https://travis-ci.org/api-collab/api-collab-fs-uploader)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=io.apicollab%3Afs%3Auploader&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.apicollab%3Afs%3Auploader)

This module makes it easy to upload new or updated api swagger spec to api portal.

# Prerequisites
- Java 8
- Maven
- Git

# Running tests locally
`
mvn clean package
` 
# Deploy
`
java -jar target\fs-uploader.jar
`
# Configuration
## File upload directory
`${java.io.tmpdir}/api_input` folder is scanned for changes by default. This can be changed with `upload.baseDir` command line argument.
## Api portal base url
Default portal base url is `http://localhost:8080`. This can be changed with `upload.portal.protocol`, `upload.portal.host`, `upload.portal.port` command line arguments.

```
java -jar target\fs-uploader \
    --upload.baseDir=F:\\dev\\api_upload_dir \
    --upload.portal.protocol=https \
    --upload.portal.host=apiportal.com \
    --upload.portal.port=8443
```