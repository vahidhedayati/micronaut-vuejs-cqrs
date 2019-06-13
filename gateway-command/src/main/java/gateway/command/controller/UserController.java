package gateway.command.controller;

import gateway.adaptors.models.implementation.UserUpdateCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("/user")
public class UserController {

   // private final UserClient userClient;
    //public UserController(UserClient userClient) {
     //   this.userClient = userClient;
    //}

    @Delete("/{id}")
    public HttpResponse delete(Long id) {

    ///    return userClient.delete(id);
        return null;
    }

    @Put(uri = "/update/{id}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse update(Long id, @Body UserUpdateCommand args) {

        //return userClient.update(id,args);
        return null;
    }

    @Post(uri = "/", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse save(@Body String args) {

        //return userClient.save(args);
        return null;
    }

}
