package userbase.write.implementations;

import userbase.write.event.commands.CommandRoot;
import userbase.write.event.commands.UserDeleteCommand;
import userbase.write.event.commands.UserSaveCommand;
import userbase.write.event.commands.UserUpdateCommand;
import userbase.write.domain.User;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface Users {
    Optional<User> findById(@NotNull Long id);
    Optional<User>  findByUsername(String username);
    <T extends CommandRoot> void  handleCommand(T  cmd);
    void handleCommand(UserSaveCommand hotelSaveCommand);
    void handleCommand(UserDeleteCommand hotel);
    void handleCommand(UserUpdateCommand hotel);
}