# SapFhir server

This makes the handlegraph4j-simple available as a [W3C SPARQL API](https://www.w3.org/TR/sparql11-protocol/).

You first need to make a handlegraph4j-simple disk/based buffer image use[sapfhir-cli](https://github.com/JervenBolleman/sapfhir-cli)


```
java -Xmx16G \ # set ram to load GFA into memory as needed
    -jar target/sapfhir-cli-1.0-SNAPSHOT-jar-with-dependencies.jar \
    --gfa ${PATH_TO_GFA_FILE} \
    --byte-buffer ${PATH_TO_STORE_DISK_BASKED_FILE} \
    --convert-to-byte \
    --time \
    "PREFIX vg:<http://biohackathon.org/resource/vg#> SELECT ?path WHERE {?path a vg:Path} LIMIT 1"
```


# Usage

```
docker pull jerven/sapfhir-server
docker run -p 8086:8080 -v $LOCAL_DIR/dna/:/data jerven/sapfhir-server --byteBuffer=/data/1kg_hs37d5.h4js --base=http://example.org/vg/
```
At the moment both the base IRI and byteBuffer location needs to be given.
There is no support yet for loading a GFA into memory directly.

Test queries e.g.

```
curl -H 'accept: application/sparql-results+json'  \
  "http://localhost:8086/sparql/"  \
  --data-urlencode "query=SELECT (COUNT(?s) AS ?c) WHERE {?s ?p ?o}"
```

# Build docker image

Depends on a yet to be released spring-boot extension for rdf4j (schedulled for [rdf4j 3.7.0](https://github.com/eclipse/rdf4j/pull/2905)


```
mvn spring-boot:build-iamge Dspring-boot.build-image.imageName=jerven/sapfhir-server
docker push jerve/sapfhir-server
```


