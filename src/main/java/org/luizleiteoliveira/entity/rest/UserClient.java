package org.luizleiteoliveira.entity.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.luizleiteoliveira.entity.Results;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/api")
@RegisterRestClient
public interface UserClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    Results getUsers(@QueryParam("results") int results);
}
