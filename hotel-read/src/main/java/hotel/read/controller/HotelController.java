package hotel.read.controller;

import hotel.read.adaptors.models.HotelModel;
import hotel.read.domain.Hotel;
import hotel.read.domain.interfaces.HotelsInterface;
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

    protected final HotelsInterface hotels;

    public HotelController(HotelsInterface hotels) {
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

    @Get("/list{?args*}")
    public Optional<HotelModel> findAll(@Nullable SortingAndOrderArguments args) {
        return hotels.findAll(args);

    }

    protected URI location(Long id) {
        return URI.create("/" + id);
    }

    protected URI location(Hotel hotel) {
        return location(hotel.getId());
    }

}
