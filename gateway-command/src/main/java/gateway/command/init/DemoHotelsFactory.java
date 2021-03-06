package gateway.command.init;

import gateway.command.event.commands.HotelCreateCommand;
import gateway.command.event.commands.HotelRoomsCreateCommand;
import io.micronaut.runtime.server.EmbeddedServer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoHotelsFactory {

	
	static List<HotelCreateCommand> defaultHotels(EmbeddedServer embeddedServer) {
		List<HotelCreateCommand> hotels = new ArrayList<HotelCreateCommand>();
		hotels.add(addHotel(embeddedServer,"HILL","Hilton - London Bridge","aa@aa.com","+44-111111111111"));


		hotels.add(addHotel(embeddedServer,"MARL","Mariott International - London Bridge","bb@aa.com","+44-22222222222222"));
		hotels.add(addHotel(embeddedServer,"STWL","Starwood Hotels - London Bridge","cc@aa.com","+44-3333333333333"));
		hotels.add(addHotel(embeddedServer,"ACGL","Accor Group - London Bridge","dd@aa.com","+44-44444444444444444"));
		hotels.add(addHotel(embeddedServer,"CHIL","Choice Hotels - London Bridge","ee@aa.com","+44-5555555555555"));
		hotels.add(addHotel(embeddedServer,"BEWL","Best Western - London Bridge","ff@aa.com","+44-6666666666"));
		hotels.add(addHotel(embeddedServer,"CARL","Carlson - London Bridge","hh@aa.com","+44-777777777777777"));
		
		
		hotels.add(addHotel(embeddedServer,"HILI","Hilton - Islington","ii@aa.com","+44-88888888888888888"));
		hotels.add(addHotel(embeddedServer,"MARI","Mariott International - Islington","jj@aa.com","+44-99999999999999"));
		hotels.add(addHotel(embeddedServer,"STWI","Starwood Hotels - Islington","kk@aa.com","+45-1111111111"));
		hotels.add(addHotel(embeddedServer,"ACGI","Accor Group - Islington","ll@aa.com","+45-22222222"));
		hotels.add(addHotel(embeddedServer,"CHII","Choice Hotels - Islington","mm@aa.com","+45-33333333333333"));
		hotels.add(addHotel(embeddedServer,"BEWI","Best Western - Islington","nn@aa.com","+45-44444444444444444"));
		hotels.add(addHotel(embeddedServer,"CARI","Carlson - Islington","oo@aa.com","+45-5555555555555555"));
		
		hotels.add(addHotel(embeddedServer,"BEWK","Best Western - Kensington","pp@aa.com","+45-666666666666"));
		hotels.add(addHotel(embeddedServer,"STWK","Starwood Hotels - Kensington","qq@aa.com","+45-77777777777777777"));
		hotels.add(addHotel(embeddedServer,"ACGK","Accor Group - Kensington","rr@aa.com","+45-88888888888888888888"));
		hotels.add(addHotel(embeddedServer,"CHIK","Choice Hotels - Kensington","ss@aa.com","+45-99999999999"));
		hotels.add(addHotel(embeddedServer,"BEWK","Best Western - Kensington","tt@aa.com","+46-11111111111111"));
		hotels.add(addHotel(embeddedServer,"CARK","Carlson - Kensington","uu@aa.com","+46-2222222222222222"));
		
		
		hotels.add(addHotel(embeddedServer,"BEWW","Best Western - Waterloo","vv@aa.com","+46-333333333"));
		hotels.add(addHotel(embeddedServer,"STWW","Starwood Hotels - Waterloo","ww@aa.com","+46-4444444444444"));
		hotels.add(addHotel(embeddedServer,"ACGW","Accor Group - Waterloo","xx@aa.com","+46-55555555555555555"));
		hotels.add(addHotel(embeddedServer,"CHIW","Choice Hotels - Waterloo","yy@aa.com","+46-66666666666666666"));
		hotels.add(addHotel(embeddedServer,"BEWW","Best Western - Waterloo","zz@aa.com","+46-7777777777777777777"));
		hotels.add(addHotel(embeddedServer,"CARW","Carlson - Waterloo","ab@aa.com","+46-999999999999999999"));

		
		return hotels;
    }
	
	
	static HotelCreateCommand addHotel(EmbeddedServer embeddedServer, String code,String name, String email, String phone) {
		HotelCreateCommand hotel = new HotelCreateCommand(code,name ,phone,email,1L,
				Date.from(LocalDate.parse( "2019-01-10" ).plusDays( 10 ).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),addRooms());
		hotel.initiate(embeddedServer,"HotelCreateCommand");
		return hotel;
	}

	static List<HotelRoomsCreateCommand>  addRooms() {
		List<HotelRoomsCreateCommand> hotelRooms=new ArrayList<>();
		//Some generic values of room types for each hotel added
		hotelRooms.add(new HotelRoomsCreateCommand("SING",new BigDecimal("45.00"), 200L));
		hotelRooms.add(new HotelRoomsCreateCommand("DOUB",new BigDecimal("65.00"), 200L));
		hotelRooms.add(new HotelRoomsCreateCommand("TWIN",new BigDecimal("65.00"), 200L));
		hotelRooms.add(new HotelRoomsCreateCommand("TRIP",new BigDecimal("85.00"), 200L));
		hotelRooms.add(new HotelRoomsCreateCommand( "FAM",new BigDecimal("95.00"), 200L));
		return hotelRooms;
	}

	
}
