package gateway.command.clients;


import gateway.command.commands.HotelDeleteCommand;
import gateway.command.commands.HotelUpdateCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

import javax.validation.Valid;

@Client(id = "hotel-write", path = "/")
public interface HotelWriteClient {

    @Post("/")
    HttpResponse save(@Body String args);

    @Delete("/{id}")
    HttpResponse delete(Long id, @Body @Valid HotelDeleteCommand cmd);

    @Put("/update/{id}")
    HttpResponse update(Long id,@Body @Valid HotelUpdateCommand command);

}
