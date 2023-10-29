# SapFhir server

This makes the handlegraph4j-simple available as a [W3C SPARQL API](https://www.w3.org/TR/sparql11-protocol/).

You first need to make a handlegraph4j-simple disk/based buffer image use [sapfhir-cli](https://github.com/JervenBolleman/sapfhir-cli).


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
docker run -p 8086:8080 -v $LOCAL_DIR/:/data jerven/sapfhir-server --byteBuffer=/data/1kg_hs37d5.h4js --base=http://example.org/vg/
```
At the moment both the base IRI and byteBuffer location needs to be given.
There is no support yet for loading a GFA into memory directly.
Notice the byteBuffer argument takes a file name in the folder mounted by docker.
The default port is 8080, here remapped to localhost:8086 in the docker host.

Test queries e.g.

```
curl -H 'accept: application/sparql-results+json'  \
  "http://localhost:8086/sparql/"  \
  --data-urlencode "query=SELECT (COUNT(?s) AS ?c) WHERE {?s ?p ?o}"
```

# Build docker image


```
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=jerven/sapfhir-server
docker push jerven/sapfhir-server
```


