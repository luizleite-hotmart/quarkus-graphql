package org.luizleiteoliveira.entity.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.luizleiteoliveira.entity.Results;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/api")
@RegisterRestClient
public interface UserClient {

    @GET
    @Produces("application/json")
    @Path("/")
    Results getUsers();
}
