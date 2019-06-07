package hotel.write.services.write;

import hotel.write.commands.CreateHotelCommand;
import hotel.write.commands.DeleteHotelCommand;
import hotel.write.commands.UpdateHotelCommand;
import hotel.write.cqrs.bus.Bus;
import hotel.write.domain.Hotel;
import hotel.write.model.HotelDeleteCommand;
import hotel.write.model.HotelUpdateCommand;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Singleton
public class HotelService {


    @Inject
    private Bus bus;

    public void addHotel(Hotel m) {
        System.out.println("bus.handleCommand new CreateHotelCommand --------------------------------------------  ");
        bus.handleCommand(new CreateHotelCommand(m));
    }

    public void addCodeName(String code,String name) {
        System.out.println("bus.handleCommand new CreateHotelCommand");
        bus.handleCommand(new CreateHotelCommand(new Hotel(code,name)));
    }


    public void deleteById(HotelDeleteCommand cmd) {
        bus.handleCommand(new DeleteHotelCommand(cmd));

        //findById(id).ifPresent(hotel -> entityManager.remove(hotel));
    }

    //@Override
    //@Transactional
    public void update(@NotNull Long id, HotelUpdateCommand command) {
        bus.handleCommand(new UpdateHotelCommand(command));
    }

}
