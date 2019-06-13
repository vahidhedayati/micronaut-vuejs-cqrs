package gateway.query.clients;


import gateway.query.models.Hotel;
import gateway.query.models.HotelModel;
import gateway.query.models.SortingAndOrderArguments;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Client(id = "hotel-read", path = "/")
public interface HotelReadClient {

    @Get("/status")
    HttpResponse status();

    @Get("/{id}")
    Optional<Hotel> findById(@NotNull Long id);



    //@Get("/list/{?max,offset,order}")
    //List<Hotel> findAll(@Nullable Integer max, @Nullable Integer offset, @Nullable String order);

    @Get(uri="/list{?args*}" , consumes = MediaType.APPLICATION_JSON)
    Optional<HotelModel> findAll(SortingAndOrderArguments args);

    @Get("/find/{code}")
    Optional<Hotel> findByCode(String code);


}
