package gateway.command.clients;


import gateway.command.commands.HotelDeleteCommand;
import gateway.command.commands.HotelUpdateCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Fallback;

import javax.validation.Valid;

@Client(id = "hotel-write", path = "/")
@Fallback
public class HotelWriteFallBack implements HotelWriteClient {

    @Post(uri = "/", consumes = MediaType.APPLICATION_JSON)
    public  HttpResponse save(@Body String args) {
        System.out.println("Backend app is down using fallback save");
        return  HttpResponse.serverError();
    }

    @Put(uri = "/update/{id}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse update(Long id, @Body HotelUpdateCommand args) {
        System.out.println("Backend app is down using fallback update");
        return HttpResponse.serverError();
    }

    @Delete("/{id}")
    public HttpResponse delete(Long id, @Body @Valid HotelDeleteCommand cmd) {
        return HttpResponse.serverError();
    }
}