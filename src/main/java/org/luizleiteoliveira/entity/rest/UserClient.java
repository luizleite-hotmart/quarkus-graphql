package org.luizleiteoliveira.entity.rest;

import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.luizleiteoliveira.entity.Results;

@Path("/api")
@RegisterRestClient
public interface UserClient {

    @GET
    @Produces("application/json")
    @Path("/")
    Results getUsers();
}
