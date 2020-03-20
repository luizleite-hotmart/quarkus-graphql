package org.luizleiteoliveira.entity;

import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.luizleiteoliveira.entity.rest.UserClient;

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
