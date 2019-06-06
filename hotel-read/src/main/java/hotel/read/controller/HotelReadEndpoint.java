package hotel.read.controller;

import hotel.read.domain.Hotel;
import hotel.read.services.read.QueryHotelService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;

import java.util.List;


//@Controller("/hotel-read")

public class HotelReadEndpoint {

	private QueryHotelService queryService;

	public HotelReadEndpoint(QueryHotelService queryService) {
		super();
		this.queryService = queryService;
	}
    
    //@Get("/all")
    //public HttpResponse<List<Hotel>> get() {
    //	    return HttpResponse.ok(queryService.getHotels());
    //}
}
