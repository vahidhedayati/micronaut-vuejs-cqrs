package userbase.read.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.validation.Validated;
import io.reactivex.annotations.Nullable;
import userbase.read.domain.User;
import userbase.read.interfaces.Users;
import userbase.read.models.SortingAndOrderArguments;
import userbase.read.models.UserModel;

import java.util.Optional;

@Validated
@Controller("/")
public class UserController {

    protected final Users users;

    public UserController(Users users) {
        this.users = users;
    }


    @Get("/status")
    public HttpResponse status() {
        return HttpResponse.ok();
    }


    @Get("/{id}")
    public User show(Long id) {
        return users
                .findById(id)
                .orElse(null);
    }

    @Get("/find/{username}")
    Optional<User> findByUsername(String username) {
        return users
                .findByUsername(username);

    }

    @Get("/list{?args*}")
    public Optional<UserModel> findAll(@Nullable SortingAndOrderArguments args) {
        return users.findAll(args);
    }


}
