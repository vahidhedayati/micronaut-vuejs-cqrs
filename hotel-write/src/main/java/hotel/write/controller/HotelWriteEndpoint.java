package hotel.write.controller;

import hotel.write.domain.Hotel;
import hotel.write.commands.HotelDeleteCommand;
import hotel.write.commands.HotelSaveCommand;
import hotel.write.commands.HotelUpdateCommand;
import hotel.write.services.write.HotelService;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.validation.Valid;
import java.net.URI;


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

    @Put("/update/{id}")
    public HttpResponse update(Long id,@Body @Valid HotelUpdateCommand command) {
        System.out.println(" In controller updateHotel");
         writeService.update(id, command);

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath());
    }
    @Delete("/{id}")
    public HttpResponse delete(Long id, @Body @Valid HotelDeleteCommand cmd) {
        writeService.deleteById(cmd);
        return HttpResponse.ok();
    }

    protected URI location(Long id) {
        return URI.create("/" + id);
    }

    protected URI location(Hotel hotel) {
        return location(hotel.getId());
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
