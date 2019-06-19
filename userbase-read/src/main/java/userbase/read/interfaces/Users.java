package userbase.read.interfaces;

import userbase.read.commands.UserDeleteCommand;
import userbase.read.commands.UserSaveCommand;
import userbase.read.commands.UserUpdateCommand;

import userbase.read.domain.User;
import userbase.read.models.SortingAndOrderArguments;

import userbase.read.models.UserModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface Users {

    Optional<User> findById(@NotNull Long id);


    Optional<UserModel> findAll(@NotNull SortingAndOrderArguments args);

    Optional<User>  findByUsername(String username);

    User getByUsername(String code);


    void deleteById(@NotNull Long id);

    int update(@NotNull Long id, @NotBlank String username, @NotBlank String password, @NotBlank String firstname, @NotBlank String surname);

    void add(User user);

    User save(@NotBlank String username, @NotBlank String password, @NotBlank String firstname, @NotBlank String surname);

    void save(UserSaveCommand hotelSaveCommand);
    void delete(UserDeleteCommand hotel);
    void update(UserUpdateCommand hotel);
    void add(List<User> hotel);
}