package gateway.query.clients;

import gateway.adaptors.models.UserModel;
import gateway.query.models.SortingAndOrderArguments;
import gateway.query.models.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Client(id = "userbase-read", path = "/")
public interface UserReadClient {

    @Get("/status")
    HttpResponse status();

    @Get("/{id}")
    Optional<User> findById(@NotNull Long id);

    @Get(uri="/list{?args*}" , consumes = MediaType.APPLICATION_JSON)
    Optional<UserModel> findAll(SortingAndOrderArguments args);

    @Get("/find/{username}")
    Optional<User> findByUsername(String username);

}
