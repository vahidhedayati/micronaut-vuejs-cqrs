package hotel.write.clients;


import hotel.write.models.User;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Client(id = "userbase-read", path = "/")
public interface UserReadClient {


    @Get("/{id}")
    Optional<User> findById(@NotNull Long id);



}
