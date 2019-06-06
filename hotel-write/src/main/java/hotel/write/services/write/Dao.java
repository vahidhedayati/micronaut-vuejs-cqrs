package hotel.write.services.write;

import hotel.write.domain.Hotel;

import javax.validation.constraints.NotBlank;

public interface Dao<T> {

    void save(T object);

    //T addCodeName(String code, String name);


}