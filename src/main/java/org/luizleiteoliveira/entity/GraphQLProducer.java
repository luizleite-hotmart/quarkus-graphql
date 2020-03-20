package org.luizleiteoliveira.entity;


import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.io.InputStreamReader;
import java.util.Objects;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.luizleiteoliveira.entity.fetchers.UserFetcher;
import org.luizleiteoliveira.entity.rest.UserClient;

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
