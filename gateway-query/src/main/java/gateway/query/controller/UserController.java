package gateway.query.controller;

import gateway.query.clients.UserReadClient;
import gateway.query.models.SortingAndOrderArguments;
import gateway.query.models.User;
import gateway.query.models.UserModel;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Controller("/user")
public class UserController {

    private final UserReadClient userReadClient;

    public UserController(UserReadClient userReadClient) {
        this.userReadClient = userReadClient;
    }

    @Get(uri="/list{?args*}" , consumes = MediaType.APPLICATION_JSON)
    public Optional<UserModel> findAll(SortingAndOrderArguments args) {
        return userReadClient.findAll(args);
    }

    @Get("/{id}")
    public Optional<User> findById(@NotNull Long id) {
        return userReadClient.findById(id);
    }



}
