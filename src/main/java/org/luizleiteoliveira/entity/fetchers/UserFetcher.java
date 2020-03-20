package org.luizleiteoliveira.entity.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.luizleiteoliveira.entity.Results;
import org.luizleiteoliveira.entity.User;
import org.luizleiteoliveira.entity.rest.UserClient;

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
