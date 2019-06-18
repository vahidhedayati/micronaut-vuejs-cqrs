package hotel.write.services.write;

import hotel.write.commands.HotelCreatedCommand;
import hotel.write.commands.HotelSaveCommand;
import hotel.write.commands.commandActions.CreateHotelCommand;
import hotel.write.commands.commandActions.DeleteHotelCommand;
import hotel.write.commands.commandActions.UpdateHotelCommand;
import hotel.write.cqrs.bus.Bus;
import hotel.write.domain.Hotel;
import hotel.write.commands.HotelDeleteCommand;
import hotel.write.commands.HotelUpdateCommand;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

public class HotelService {


    @Inject
    private Bus bus;

    public void save(HotelSaveCommand cmd) {
        bus.handleCommand(new CreateHotelCommand(new HotelCreatedCommand(cmd.getCode(), cmd.getName(), cmd.getPhone(), cmd.getEmail())));
    }
    public void addHotel(Hotel m) {
        System.out.println("bus.handleCommand new CreateHotelCommand --------------------------------------------  ");
        bus.handleCommand(new CreateHotelCommand(new HotelCreatedCommand(m)));
    }
    public void addHotel(HotelCreatedCommand m) {
        System.out.println("bus.handleCommand new CreateHotelCommand --------------------------------------------  ");
        bus.handleCommand(new CreateHotelCommand(m));
    }
    public void addCodeName(String code,String name) {
        System.out.println("bus.handleCommand new CreateHotelCommand");
        bus.handleCommand(new CreateHotelCommand(new HotelCreatedCommand(code,name)));
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
