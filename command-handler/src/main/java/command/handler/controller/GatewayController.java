package command.handler.controller;

import command.handler.clients.HotelWriteClient;
import command.handler.clients.UserWriteClient;
import command.handler.commands.HotelDeleteCommand;
import command.handler.commands.HotelSaveCommand;
import command.handler.commands.HotelUpdateCommand;
import command.handler.models.Hotel;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Controller("/")
public class GatewayController {

    private final HotelWriteClient hotelWriteClient;
    private final UserWriteClient userWriteClient;

    public GatewayController(UserWriteClient userWriteClient,HotelWriteClient hotelWriteClient) {
        this.userWriteClient=userWriteClient;
        this.hotelWriteClient=hotelWriteClient;
    }



    @Delete("/delete/{id}")
    public HttpResponse delete(Long id) {
        HotelDeleteCommand cmd=new HotelDeleteCommand(id);
        return hotelWriteClient.delete(id,cmd);
    }


    @Put(uri = "/update/{id}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse update(Long id, @Body HotelUpdateCommand args) {
        return hotelWriteClient.update(id,args);
    }





    /**
     * Validated backend class -    @Valid  does this
     * @Valid
     * throws ValidationException
     * //@Error(exception = ConstraintViolationException.class)
     */
    @Post(uri = "/", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse save(@Body @Valid HotelSaveCommand args)  {
       // hotelWriteClient.save(args.getHotel());
        /**
         * Below captures and returns a http response with all errors  -
         * this is backend validation against HotelSaveCommand in the Gateway application - gateway will decide on if it
         * needs to pass traffic on if item validated here or not - if ok - will save - if not will fail back
         * vuejs will return error on screen upon save
         */
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<HotelSaveCommand>> constraintViolations = validator.validate(args);
        if (constraintViolations.size() > 0) {
            Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getMessage());
                //violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
            }
            //System.out.println(" 01 ---->"+violationMessages);
//            throw new ValidationException("Hotel is not valid:\n" + violationMessages);
            return HttpResponse.badRequest(violationMessages);
        }

        System.out.println("Default save of hotel in gateway");
        Hotel hotel = args.getHotel();
        hotelWriteClient.save(hotel);

        //Hotel hotel = backendClient.save(args);
        //Hotel hotel = hotelWriteClient.save(args.getHotel());
        return HttpResponse
               .created(hotel)
                .headers(headers -> headers.location(location(hotel.getId())));



    }

    protected URI location(Long id) {
        return URI.create("/" + id);
    }


}
