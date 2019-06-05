package commandservice.services.write;

public interface Dao<T> {

	void save(T object);

}
