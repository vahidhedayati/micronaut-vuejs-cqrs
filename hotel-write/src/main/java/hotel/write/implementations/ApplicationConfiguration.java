package hotel.write.implementations;

import javax.validation.constraints.NotNull;

public interface ApplicationConfiguration {

    @NotNull Integer getMax();
}
