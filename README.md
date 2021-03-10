# Build docker image

depends on a yet to be released spring-boot extension for rdf4j



```
mvn spring-boot:build-iamge Dspring-boot.build-image.imageName=jerven/sapfhir-server
docker push jerve/sapfhir-server
```



```
docker pull jerven/sapfhir-server
docker run -p 8086:8080 -v $LOCAL_DIR/dna/:/data jerven/sapfhir-server --byteBuffer=/data/1kg_hs37d5.h4js --base=http://example.org/vg/
```

Test queries e.g.

```
curl -H 'accept: application/sparql-results+json'  \
  "http://localhost:8086/sparql/"  \
  --data-urlencode "query=SELECT (COUNT(?s) AS ?c) WHERE {?s ?p ?o}"

```

