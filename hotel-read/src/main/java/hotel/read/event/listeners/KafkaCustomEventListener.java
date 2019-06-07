package hotel.read.event.listeners;

import hotel.read.domain.interfaces.Hotels;
import hotel.read.event.HotelCreatedEvent;
import hotel.read.event.HotelDeletedCommandEvent;
import hotel.read.event.HotelUpdateCommandEvent;
import hotel.read.services.read.QueryHotelViewDao;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Primary;
import io.micronaut.messaging.annotation.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@KafkaListener
@Primary
public class KafkaCustomEventListener {

    protected static final Logger LOG = LoggerFactory.getLogger(KafkaCustomEventListener.class);

    @Inject
    private Hotels dao;
    //private QueryHotelViewDao dao;

    @Topic("hotelCreated")
    public void consume( @KafkaKey String hotelCode, @Body HotelCreatedEvent hotelCreatedEvent) {
        LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER");
        System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER ---"+hotelCreatedEvent.getDtoFromEvent());
        dao.save(hotelCreatedEvent.getDtoFromEvent().createHotel());
    }

    @Topic("hotelEdit")
    public void consumeEdit( @KafkaKey String hotelCode, @Body HotelDeletedCommandEvent hotelCreatedEvent) {
        LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER");
        System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER ---"+hotelCreatedEvent.getDtoFromEvent());
        dao.delete(hotelCreatedEvent.getDtoFromEvent());
    }

    @Topic("hotelDelete")
    public void consumeDelete( @KafkaKey String hotelCode, @Body HotelUpdateCommandEvent hotelCreatedEvent) {
        LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER");
        System.out.println("READ --------------- KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER ---"+hotelCreatedEvent.getDtoFromEvent());
        dao.update(hotelCreatedEvent.getDtoFromEvent());
    }
}
