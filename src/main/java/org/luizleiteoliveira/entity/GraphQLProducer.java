package org.luizleiteoliveira.entity;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.handler.graphql.VertxDataFetcher;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.enterprise.inject.Produces;

public class GraphQLProducer {

    private Logger LOGGER = LoggerFactory.getLogger(GraphQLProducer.class);

    @Produces
    public GraphQL setup() {

        LOGGER.info("Setting up GraphQL..");

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry registry = schemaParser.parse(
            new InputStreamReader(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("META-INF/resources/graphql.schema")));

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(registry, wirings());
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private RuntimeWiring wirings() {

        LOGGER.info("Wiring queries..");

        return RuntimeWiring.newRuntimeWiring()
            .type("Query",
                builder -> builder.dataFetcher("allUsers", getUsersFetcher()))
            .build();
    }

    private VertxDataFetcher<List<User>> getUsersFetcher() {

        return  new VertxDataFetcher<>((env, promises)->{
            User luiz = new User("luiz", "luiz leite", "DOC123", "xp.luiz@gmail.com");
            promises.complete(Arrays.asList(new User[] {luiz}));
        });
    }

}
