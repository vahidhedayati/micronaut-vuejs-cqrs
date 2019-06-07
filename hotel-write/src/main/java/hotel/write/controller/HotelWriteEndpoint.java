package hotel.write.controller;

import hotel.write.domain.Hotel;
import hotel.write.model.HotelSaveCommand;
import hotel.write.services.write.HotelService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import javax.validation.Valid;


@Controller("/")
public class HotelWriteEndpoint {

	private HotelService writeService;
	
	public HotelWriteEndpoint(HotelService writeService) {
		super();
		this.writeService = writeService;
	}

	@Post("/")
	public HttpResponse<HotelSaveCommand> save(@Body @Valid HotelSaveCommand cmd) {
		System.out.println("HotelWriteEndpoint save");

		writeService.addHotel(new Hotel(cmd.getCode(),cmd.getName(),cmd.getPhone(),cmd.getEmail()));
		return HttpResponse.ok(cmd);

	}
    /*
	@Post()
	public HttpResponse saveCodeName(String code,String name) {
		System.out.println("HotelWriteEndpoint saveCodeName");
		Hotel hotel = new Hotel(code,name);
		writeService.addHotel(hotel);
		//writeService.addCodeName(code,name);
		return HttpResponse.ok(hotel);
	}

*/

}
