package hotel.write.clients;


import hotel.write.models.User;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Fallback;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Client(id = "userbase-read", path = "/")
@Fallback
public class UserReadFallBack implements UserReadClient {


    @Get("/{id}")
    public Optional<User> findById(@NotNull Long id) {
        System.out.println("Backend app is down using fallback findById");
        return Optional.ofNullable(new User());
    }

}