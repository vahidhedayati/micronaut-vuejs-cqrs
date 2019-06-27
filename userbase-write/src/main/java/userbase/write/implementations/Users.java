package userbase.write.implementations;

import userbase.write.domain.User;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface Users {
    Optional<User> findById(@NotNull Long id);
    Optional<User>  findByUsername(String username);
}