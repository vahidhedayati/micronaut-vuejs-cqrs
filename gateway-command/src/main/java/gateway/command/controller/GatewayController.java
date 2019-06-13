package gateway.command.controller;

import gateway.adaptors.models.implementation.HotelDeleteCommand;
import gateway.adaptors.models.implementation.HotelUpdateCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
@Controller("/")
public class GatewayController {

    /*
    private final HotelWriteClient hotelWriteClient;
    private final UserWriteClient userWriteClient;

    public GatewayController(UserWriteClient userWriteClient,HotelWriteClient hotelWriteClient) {
        this.userWriteClient=userWriteClient;
        this.hotelWriteClient=hotelWriteClient;
    }
    */



    @Delete("/delete/{id}")
    public HttpResponse delete(Long id) {
        HotelDeleteCommand cmd=new HotelDeleteCommand(id);
     //   return hotelWriteClient.delete(id,cmd);
        return null;
    }


    @Put(uri = "/update/{id}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse update(Long id, @Body HotelUpdateCommand args) {
        //return hotelWriteClient.update(id,args);
        return null;
    }





    /**
     * Validated backend class -    @Valid  does this
     * @Valid
     * throws ValidationException
     * //@Error(exception = ConstraintViolationException.class)
     */
    @Post(uri = "/", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse save(@Body String args)  {
       // hotelWriteClient.save(args.getHotel());

        return HttpResponse.accepted();




    }

    protected URI location(Long id) {
        return URI.create("/" + id);
    }


}
