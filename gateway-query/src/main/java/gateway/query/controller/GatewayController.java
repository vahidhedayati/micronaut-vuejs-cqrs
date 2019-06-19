package gateway.query.controller;

import gateway.query.clients.HotelReadClient;
import gateway.query.clients.UserReadClient;
import gateway.query.models.Hotel;
import gateway.query.models.HotelModel;
import gateway.query.models.SortingAndOrderArguments;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller("/")
public class GatewayController {

    private final HotelReadClient hotelReadClient;

    public GatewayController(HotelReadClient hotelReadClient) {
      //  this.userReadClient=userReadClient;
        this.hotelReadClient = hotelReadClient;
    }

    @Get(uri="/list{?args*}" , consumes = MediaType.APPLICATION_JSON)
    public Optional<HotelModel> findAll(SortingAndOrderArguments args) {
        //System.out.println("Trying to find"+args.getValues());
        /**
         * We bind in userClient and have a slightly different modelled hotel on gateway application which has a User updateUser
         * defined - this binds in via flatMap to bind in actual user for given user -
         *
         *
         * None of this happens any morel hotel read models bits it needs as it receives from hotel-write
         */
        /*
        Optional<HotelModel> hotelModel =  hotelReadClient.findAll(args);
        if (hotelModel.isPresent() ) {
            hotelModel.flatMap(hotelModel1 -> {
                Optional<List<Hotel>> instanceList = hotelModel1.getInstanceList();
                if (instanceList.isPresent()) {
                    instanceList.flatMap(hotel-> {
                        hotel.forEach(hotel1 -> {
                            hotel1.setUpdateUser(userReadClient.findById(hotel1.getUpdateUserId()).get());
                            //hotel1.setUpdateUser(userClient.findByUsername("admin").get());
                        });

                        return Optional.of(hotel);
                    });
                }
                return Optional.of(hotelModel1);
            });
        }
        return hotelModel;
        */

        return hotelReadClient.findAll(args);
    }

    @Get("/{id}")
    public Optional<Hotel> findById(@NotNull Long id) {
        return hotelReadClient.findById(id);
    }

}
