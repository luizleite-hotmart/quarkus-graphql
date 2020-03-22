package org.luizleiteoliveira.entity;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.luizleiteoliveira.entity.rest.UserClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class UserResource {


    @Inject
    @RestClient
    UserClient userClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Results getAllCountries(@QueryParam("results") int results) {
        return userClient.getUsers(results);
    }
}
