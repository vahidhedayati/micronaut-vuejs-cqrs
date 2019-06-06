package userbase.read.implementations;

import javax.validation.constraints.NotNull;

public interface MyApplicationConfiguration {

    @NotNull Integer getMax();
}
