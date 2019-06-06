package hotel.read.services.read;

import hotel.read.adaptors.models.HotelModel;
import hotel.read.domain.Hotel;
import hotel.read.implementation.SortingAndOrderArguments;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class QueryHotelViewList implements QueryHotelViewDao {

	//List<Hotel> hotels = new ArrayList<>();

	//public List<Hotel> getHotels() {
	//	return hotels;
	//}

	@PersistenceContext
	private EntityManager entityManager;

	public QueryHotelViewList(@CurrentSession EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Optional<HotelModel> findAll(@NotNull SortingAndOrderArguments args) {
		return Optional.empty();
	}

	@Override
	public Optional<Hotel> findById(@NotNull Long id) {
		return Optional.empty();
	}

	@Override
	public Optional<Hotel> findByCode(String code) {
		return Optional.empty();
	}

	@Transactional
	public void save(Hotel hotel) {
		System.out.println(" Hotel being added  to READ ------------------------------------------------------------------------");
		entityManager.persist(hotel);
	}

}
