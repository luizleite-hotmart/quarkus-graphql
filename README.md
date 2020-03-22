# graphql-example project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

### Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `graphql-example-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/graphql-example-1.0.0-SNAPSHOT-runner.jar`.

### Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/graphql-example-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide.

## The project 

This project will get the API that exist in  [randomuser.me](https://randomuser.me) and put all in a GraphQL.

### The call from randomuser.me

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

## GRAPHQL On Quarkus

### Dependencies 

To use GraphQL on quarkus you will need the following dependency provided by  [Vert.x](https://vertx.io/), full doc from the 
GraphQL part you can find [here](https://vertx.io/docs/vertx-web-graphql/java/)

```xml
    <dependency>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-web-graphql</artifactId>
    </dependency>
```

### Code part
First you will need to create a entity User to receive the results, for my example will be like this:

```java
public class User {

    private String email;
    private String gender;
    private Names name;
    private String phone;
    private String cell;
    private String nat;

    public User() {}

//    Getters and setters

}
```

And the class names will be an  implementation for the names part will be something like you can see:

```java
public class Names {

    private String title;
    private String first;
    private String last;
    
//    Getters and Setters

}
```

For the model you need Fetcher that will be a model to use our entities, our UserFetcher will be something like this:


```java
public class UserFetcher implements DataFetcher<List<User>> {

    private final UserClient userClient;

    public UserFetcher(UserClient userClient) {
        this.userClient = userClient;
    }


    private Results getResult() {
        return userClient.getUsers();
    }

    @Override
    public List<User> get(DataFetchingEnvironment environment) throws Exception {
        return getResult().getResults();
    }
}
``` 

To get our result will need to get a GraphQL producer is a little boilerplate and for your projects will be a class
that produces :

```java
public class GraphQLProducer {

    private Logger LOGGER = LoggerFactory.getLogger(GraphQLProducer.class);

    @RestClient
    @Inject
    private UserClient userClient;

    @Produces
    public GraphQL setup() {

        LOGGER.info("Setting up GraphQL..");

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry registry = schemaParser.parse(
            new InputStreamReader(
                Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("META-INF/resources/graphql.schema"))));

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(registry, wirings());
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private RuntimeWiring wirings() {

        LOGGER.info("Wiring queries..");

        return RuntimeWiring.newRuntimeWiring()
            .type("Query",
                builder -> builder.dataFetcher("allUsers", new UserFetcher(userClient)))
            .build();
    }


}
```

On the `getResourceAsStream()` will receive what is your graphql schema.
To all calls on `allUsers` he will make a `get` from `UserFetcher`

The last part will be the ROUTES:

```java
@ApplicationScoped
public class Routes {

    @Inject
    GraphQL graphQL;

    public void init(@Observes Router router) {

        boolean enableGraphiQL = true;

        router.route("/graphql")
            .handler(new GraphQLHandlerImpl(graphQL, new GraphQLHandlerOptions()));
        router.route("/graphiql/*").handler(
            GraphiQLHandler.create(new GraphiQLHandlerOptions().setEnabled(enableGraphiQL)));
    }

}
```

There are two routes one is the `/graphql` that will be our GraphQL endpoint, and another one will be `/graphiql` 
that will open a URL `http://localhost:8080/graphiql/`

And our schema will be like this:

```
type Names {
  first: String
  last: String
}


type User {
  email: String
  name: Names
  gender: String
  phone: String
  cell: String
  nat: String
}

type Query {
  allUsers : [User]
}
```

## Rest client on Quarkus
To test deeply the Quarkus and this GraphQL we will use the rest client.

### The dependencies we need will be 
One of them is just to help with the json part, the other part will help us with the rest part.
```xml
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy-jsonb</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-rest-client</artifactId>
    </dependency>
```
Inside our application properties you the URI from that we want to call, in our case will be like this:
```properties
org.luizleiteoliveira.entity.rest.UserClient/mp-rest/uri = https://randomuser.me
```
on the key part you will call the class that will be used as client , `UserClient`, as value the URI that we will use `https://randomuser.me`

After this we just need to declare our client class:

```java
@Path("/api")
@RegisterRestClient
public interface UserClient {

    @GET
    @Produces("application/json")
    @Path("/")
    Results getUsers();
}

```

The results class will be a Helper for by pass the result from the randomuser API

```java
public class Results {

    private List<User> results;
    public Results() {}

    public List<User> getResults() {
        return results;
    }

    public void setResults(List<User> results) {
        this.results = results;
    }
}
```

To test without GraphQL I create and resource with just a get:

```java
@Path("/users")
public class UserResource {


    @Inject
    @RestClient
    UserClient userClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Results getAllCountries() {
        return userClient.getUsers();
    }
}
```
