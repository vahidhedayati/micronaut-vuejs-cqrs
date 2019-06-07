package gateway.adaptors.web;

import gateway.adaptors.clients.HotelReadClient;
import gateway.adaptors.clients.HotelWriteClient;
import gateway.adaptors.clients.UserClient;
import gateway.adaptors.models.Hotel;
import gateway.adaptors.models.HotelModel;
import gateway.adaptors.models.implementation.HotelDeleteCommand;
import gateway.adaptors.models.implementation.HotelSaveCommand;
import gateway.adaptors.models.implementation.HotelUpdateCommand;
import gateway.adaptors.models.implementation.SortingAndOrderArguments;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller("/")
public class GatewayController {
   // @Inject
   // Validator validator;
    private final HotelWriteClient hotelWriteClient;
    private final HotelReadClient hotelReadClient;
    private final UserClient userClient;

    public GatewayController(HotelReadClient hotelReadClient,UserClient userClient,HotelWriteClient hotelWriteClient) {
        this.userClient=userClient;
        this.hotelReadClient = hotelReadClient;
        this.hotelWriteClient=hotelWriteClient;
    }

    @Get(uri="/list{?args*}" , consumes = MediaType.APPLICATION_JSON)
    public Optional<HotelModel> findAll(SortingAndOrderArguments args) {
        //System.out.println("Trying to find"+args.getValues());
        Optional<HotelModel> hotelModel =  hotelReadClient.findAll(args);
        /**
         * We bind in userClient and have a slightly different modelled hotel on gateway application which has a User updateUser
         * defined - this binds in via flatMap to bind in actual user for given user -
         */
        if (hotelModel.isPresent() ) {
            hotelModel.flatMap(hotelModel1 -> {
                hotelModel1.getInstanceList().flatMap(hotel-> {
                    hotel.forEach(hotel1 -> {
                        //hotel1.setUpdateUser(userClient.findById(hotel1.getUpdateUserId()).get());
                        hotel1.setUpdateUser(userClient.findByUsername("admin").get());
                    });

                    return Optional.of(hotel);
                });
                return Optional.of(hotelModel1);
            });
        }


        return hotelModel;
    }

    @Get("/{id}")
    public Optional<Hotel> findById(@NotNull Long id) {
        return hotelReadClient.findById(id);
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
