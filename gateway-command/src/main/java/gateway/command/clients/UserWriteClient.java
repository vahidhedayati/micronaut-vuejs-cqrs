package gateway.command.clients;

import gateway.adaptors.models.implementation.UserSaveCommand;
import gateway.adaptors.models.implementation.UserUpdateCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

import javax.validation.constraints.NotNull;

@Client(id = "userbase-write", path = "/")
public interface UserWriteClient {

    @Get("/delete/{id}")
    HttpResponse deleteById(@NotNull Long id);

    @Post(uri = "/", consumes = MediaType.APPLICATION_JSON)
    HttpResponse save(@Body UserSaveCommand args);


    @Put(uri = "/update/{id}", consumes = MediaType.APPLICATION_JSON)
    HttpResponse update(Long id, @Body UserUpdateCommand args);


    @Delete("/{id}")
    HttpResponse delete(Long id);

}
