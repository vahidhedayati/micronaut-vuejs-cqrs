package userbase.write.implementations;

import userbase.write.commands.UserDeleteCommand;
import userbase.write.commands.UserSaveCommand;
import userbase.write.commands.UserUpdateCommand;
import userbase.write.domain.User;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface Users {
    Optional<User> findById(@NotNull Long id);
    Optional<User>  findByUsername(String username);
    void save(UserSaveCommand hotelSaveCommand);
    void delete(UserDeleteCommand hotel);
    void update(UserUpdateCommand hotel);
}