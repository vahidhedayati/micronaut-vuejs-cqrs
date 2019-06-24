package gateway.query.controller;

import gateway.query.clients.HotelReadClient;
import gateway.query.clients.UserReadClient;
import gateway.query.models.Hotel;
import gateway.query.models.HotelModel;
import gateway.query.models.SortingAndOrderArguments;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller("/")
public class GatewayController {

    private final HotelReadClient hotelReadClient;

    public GatewayController(HotelReadClient hotelReadClient) {
      //  this.userReadClient=userReadClient;
        this.hotelReadClient = hotelReadClient;
    }

    @Get(uri="/list{?args*}" , consumes = MediaType.APPLICATION_JSON)
    public Optional<HotelModel> findAll(SortingAndOrderArguments args) {
        return hotelReadClient.findAll(args);
    }

    @Get("/{id}")
    public Optional<Hotel> findById(@NotNull Long id) {
        return hotelReadClient.findById(id);
    }

}
