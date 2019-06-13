package command.handler.controller;


import command.handler.clients.UserWriteClient;
import command.handler.commands.UserSaveCommand;
import command.handler.commands.UserUpdateCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("/user")
public class UserController {

    private final UserWriteClient userWriteClient;
    public UserController(UserWriteClient userWriteClient) {
        this.userWriteClient = userWriteClient;
    }


    @Delete("/{id}")
    public HttpResponse delete(Long id) {
        return userWriteClient.delete(id);
    }

    @Put(uri = "/update/{id}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse update(Long id, @Body UserUpdateCommand args) {
        return userWriteClient.update(id,args);
    }

    @Post(uri = "/", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse save(@Body UserSaveCommand args) {
        return userWriteClient.save(args);
    }

}
