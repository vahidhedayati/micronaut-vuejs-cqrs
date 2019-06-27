package userbase.read.interfaces;

import userbase.read.domain.User;
import userbase.read.event.events.EventRoot;
import userbase.read.event.events.UserDeleted;
import userbase.read.event.events.UserSaved;
import userbase.read.event.events.UserUpdated;
import userbase.read.models.SortingAndOrderArguments;
import userbase.read.models.UserModel;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface Users {

    Optional<User> findById(@NotNull Long id);
    Optional<UserModel> findAll(@NotNull SortingAndOrderArguments args);
    Optional<User>  findByUsername(String username);
    <T extends EventRoot> void handleEvent(T  cmd);
    void handleEvent(UserSaved hotelSaveCommand);
    void handleEvent(UserDeleted hotel);
    void handleEvent(UserUpdated hotel);
}