package gateway.command.clients;

import gateway.adaptors.models.implementation.UserSaveCommand;
import gateway.adaptors.models.implementation.UserUpdateCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Fallback;

import javax.validation.constraints.NotNull;

@Client(id = "userbase", path = "/")
@Fallback
public class UserClientFallBack implements UserWriteClient {

    @Get("/status")
    public HttpResponse status() {
        System.out.println("Backend app is down using fallback status");
        return HttpResponse.serverError();
    }



    @Get("/delete/{id}")
    public HttpResponse deleteById(@NotNull Long id) {
        System.out.println("Backend app is down using fallback deleteById");
            //
        return null;
    }

    @Post(uri = "/", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse save(@Body UserSaveCommand args) {
        System.out.println("Backend app is down using fallback save");
        return null;
    }

    @Put(uri = "/update/{id}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse update(Long id, @Body UserUpdateCommand args) {
        System.out.println("Backend app is down using fallback update");
        return HttpResponse.serverError();
    }


    @Delete("/{id}")
    public HttpResponse delete(Long id) {
        return HttpResponse.serverError();
    }
}