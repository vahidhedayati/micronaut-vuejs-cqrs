package gateway.query.clients;


import gateway.query.models.SortingAndOrderArguments;
import gateway.query.models.User;
import gateway.query.models.UserModel;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Fallback;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Client(id = "userbase-read", path = "/")
@Fallback
public class UserReadFallBack implements UserReadClient {

    @Get("/status")
    public HttpResponse status() {
        return HttpResponse.serverError();
    }

    @Get("/{id}")
    public Optional<User> findById(@NotNull Long id) {
        return Optional.ofNullable(new User());
    }

    @Get(uri="/list{?args*}" , consumes = MediaType.APPLICATION_JSON)
    public Optional<UserModel> findAll(SortingAndOrderArguments args) {
        return null;
    }

    @Get("/find/{username}")
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(new User());
    }

}