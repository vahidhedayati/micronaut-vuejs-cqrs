package hotel.write.controller;

import hotel.write.model.HotelSaveCommand;
import hotel.write.services.write.HotelService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;


@Controller("/hotel-write")
public class HotelWriteEndpoint {

	private HotelService writeService;
	
	public HotelWriteEndpoint(HotelService writeService) {
		super();
		this.writeService = writeService;
	}


    @Post()
    public HttpResponse<HotelSaveCommand> save(@Body HotelSaveCommand hotel) {
		System.out.println("HotelWriteEndpoint save");
    		writeService.addHotel(hotel);
    	    return HttpResponse.ok(hotel);
    }
}
