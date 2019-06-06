package gateway.adaptors.clients;

import gateway.adaptors.models.Hotel;
import gateway.adaptors.models.implementation.HotelSaveCommand;
import gateway.adaptors.models.implementation.HotelUpdateCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Fallback;

@Client(id = "hotel-write", path = "/")
@Fallback
public class HotelWriteFallBack implements HotelWriteClient {

    @Post(uri = "/", consumes = MediaType.APPLICATION_JSON)
    public  HttpResponse<Hotel> save(@Body Hotel args) {
        System.out.println("Backend app is down using fallback save");
        return  HttpResponse.serverError();
    }

    @Put(uri = "/update/{id}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse update(Long id, @Body HotelUpdateCommand args) {
        System.out.println("Backend app is down using fallback update");
        return HttpResponse.serverError();
    }


    @Delete("/{id}")
    public HttpResponse delete(Long id) {
        return HttpResponse.serverError();
    }
}