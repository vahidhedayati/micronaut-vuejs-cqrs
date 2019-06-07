package gateway.adaptors.clients;

import gateway.adaptors.models.Hotel;
import gateway.adaptors.models.implementation.HotelDeleteCommand;
import gateway.adaptors.models.implementation.HotelSaveCommand;
import gateway.adaptors.models.implementation.HotelUpdateCommand;
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
    HttpResponse<Hotel> save(@Body Hotel args);

    @Delete("/{id}")
    HttpResponse delete(Long id, @Body @Valid HotelDeleteCommand cmd);

    @Put("/update/{id}")
    HttpResponse update(Long id,@Body @Valid HotelUpdateCommand command);

}
