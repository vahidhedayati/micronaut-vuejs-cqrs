package gateway.query.clients;

import gateway.adaptors.models.Hotel;
import gateway.adaptors.models.HotelModel;
import gateway.query.models.SortingAndOrderArguments;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Fallback;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Client(id = "hotel-read", path = "/")
@Fallback
public class HotelReadFallBack implements HotelReadClient {

    @Get("/status")
    public HttpResponse status() {
        System.out.println("Backend app is down using fallback status");
        return HttpResponse.serverError();
    }

    @Get("/{id}")
    public Optional<Hotel> findById(@NotNull Long id) {
        System.out.println("Backend app is down using fallback findById");
        return Optional.ofNullable(new Hotel());
    }

    @Get(uri="/list{?args*}" , consumes = MediaType.APPLICATION_JSON)
    public Optional<HotelModel> findAll(SortingAndOrderArguments args) {
        System.out.println("Backend app is down using fallback  findAll");
        return null;
    }

    @Get("/find/{code}")
    public Optional<Hotel> findByCode(String code) {
        System.out.println("Backend app is down using fallback  findByCode");
        return Optional.ofNullable(new Hotel());
    }

}