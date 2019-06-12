package hotel.read.controller;

import hotel.read.adaptors.models.HotelModel;
import hotel.read.domain.Hotel;
import hotel.read.domain.interfaces.Hotels;
import hotel.read.implementation.SortingAndOrderArguments;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.validation.Validated;
import io.reactivex.annotations.Nullable;

import java.net.URI;
import java.util.Optional;

@Validated
@Controller("/")
public class HotelController {

    protected final Hotels hotels;

    public HotelController(Hotels hotels) {
        this.hotels = hotels;
    }


    @Get("/status")
    public HttpResponse status() {
        return HttpResponse.ok();
    }


    @Get("/{id}")
    public Hotel show(Long id) {
        return hotels
                .findById(id)
                .orElse(null);
    }



    @Get("/list{?args*}") //, consumes = MediaType.APPLICATION_JSON
    public Optional<HotelModel> findAll(@Nullable SortingAndOrderArguments args) {
       // System.out.println(" In findAll hotels");
        return hotels.findAll(args);

    }


    protected URI location(Long id) {
        return URI.create("/" + id);
    }

    protected URI location(Hotel hotel) {
        return location(hotel.getId());
    }

}
