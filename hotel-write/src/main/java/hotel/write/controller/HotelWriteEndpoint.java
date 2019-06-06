package hotel.write.controller;

import hotel.write.domain.Hotel;
import hotel.write.model.HotelSaveCommand;
import hotel.write.services.write.HotelService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;


@Controller("/")
public class HotelWriteEndpoint {

	private HotelService writeService;
	
	public HotelWriteEndpoint(HotelService writeService) {
		super();
		this.writeService = writeService;
	}


    @Post()
    public HttpResponse<Hotel> save(@Body Hotel hotel) {
		System.out.println("HotelWriteEndpoint save");
    		writeService.addHotel(hotel);
    	    return HttpResponse.ok(hotel);
    }

	@Post()
	public HttpResponse saveCodeName(String code,String name) {
		System.out.println("HotelWriteEndpoint saveCodeName");
		Hotel hotel = new Hotel(code,name);
		writeService.addHotel(hotel);
		//writeService.addCodeName(code,name);
		return HttpResponse.ok(hotel);
	}



}
