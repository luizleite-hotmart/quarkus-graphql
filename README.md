# graphql-example project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `graphql-example-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/graphql-example-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/graphql-example-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide.

# The project 

This project will get the API that exist in  [randomuser.me](https://randomuser.me) and put all in a GraphQL.

## The call from randomuser.me

For the following call:

```bash
curl --location --request GET 'https://randomuser.me/api'
```

you will receive as result:

```json
{
    "results": [
        {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Judas",
                "last": "Campos"
            },
            "location": {
                "street": {
                    "number": 908,
                    "name": "Rua José Bonifácio "
                },
                "city": "Divinópolis",
                "state": "Roraima",
                "country": "Brazil",
                "postcode": 48248,
                "coordinates": {
                    "latitude": "-76.1350",
                    "longitude": "-139.5806"
                },
                "timezone": {
                    "offset": "+11:00",
                    "description": "Magadan, Solomon Islands, New Caledonia"
                }
            },
            "email": "judas.campos@example.com",
            "login": {
                "uuid": "91f154d6-76a9-4964-9497-05509bd39ac8",
                "username": "organicladybug151",
                "password": "dalejr",
                "salt": "ByOLMzsf",
                "md5": "4ff310a10af0da949b7153482b6c58c7",
                "sha1": "377de5ce5d8e291693b451de462dd4407c0ce745",
                "sha256": "84f5177531505d633881a1a19212f0e31658068d7a3d19f125e79a8dbc6d1bc1"
            },
            "dob": {
                "date": "1962-07-15T02:08:02.037Z",
                "age": 58
            },
            "registered": {
                "date": "2008-05-01T01:22:04.877Z",
                "age": 12
            },
            "phone": "(67) 8256-7568",
            "cell": "(17) 4526-7409",
            "id": {
                "name": "",
                "value": null
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/2.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/2.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/2.jpg"
            },
            "nat": "BR"
        }
    ],
    "info": {
        "seed": "6c1737e1f584f423",
        "results": 1,
        "page": 1,
        "version": "1.3"
    }
}
```

you can pass to the API the query parameter `results` that will retrieve to you the number of results you want  

# GRAPHQL On Quarkus

## Dependencies 

To use GraphQL on quarkus you will need the following dependency provided by  [Vert.x](https://vertx.io/), full doc from the 
GraphQL part you can find [here](https://vertx.io/docs/vertx-web-graphql/java/)

```xml
    <dependency>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-web-graphql</artifactId>
    </dependency>
```

## Code part
First you will need to create a entity User to receive the results