package userbase.read.service;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;
import userbase.read.commands.UserDeleteCommand;
import userbase.read.commands.UserSaveCommand;
import userbase.read.commands.UserUpdateCommand;
import userbase.read.domain.User;
import userbase.read.implementations.MyApplicationConfiguration;
import userbase.read.interfaces.Users;
import userbase.read.models.SortingAndOrderArguments;

import userbase.read.models.UserModel;


import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserService implements Users {

    @PersistenceContext
    private EntityManager entityManager;
    private final MyApplicationConfiguration myApplicationConfiguration;


    public UserService(@CurrentSession EntityManager entityManager,
                       MyApplicationConfiguration myApplicationConfiguration) {
        this.entityManager = entityManager;
        this.myApplicationConfiguration = myApplicationConfiguration;
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "username", "firstname", "surname");

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Transactional(readOnly = true)
    public Optional<UserModel> findAll(@NotNull SortingAndOrderArguments args) {

        String countQueryString = "select count(h) FROM User as  h ";
        String qlString = "FROM User as  h ";
        if (args.getName().isPresent()) {
            qlString += " where  ( lower(h.username) like (:name)  or  lower(h.firstname) like (:name)   lower(h.surname) like (:name) )  ";
            countQueryString += " where ( lower(h.username) like (:name)  or  lower(h.firstname) like (:name)   lower(h.surname) like (:name) ) ";
        }
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY h." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<User> query;
        TypedQuery<Long> countQuery;
        if (args.getName().isPresent()) {
            query = entityManager.createQuery(qlString, User.class).setParameter("name", '%' + args.getName().get().toLowerCase() + '%');
            countQuery = entityManager.createQuery(countQueryString, Long.class).setParameter("name", '%' + args.getName().get().toLowerCase() + '%');
        } else {
            query = entityManager.createQuery(qlString, User.class);
            countQuery = entityManager.createQuery(countQueryString, Long.class);
        }

        query.setMaxResults(args.getMax().orElseGet(myApplicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);
        UserModel model = new UserModel();
        model.setInstanceList(query.getResultList());
        model.setInstanceTotal(countQuery.getSingleResult());

        model.setNumberOfPages(model.getInstanceTotal() / args.getMax().get());
        return Optional.of(model);
    }

    @Transactional
    @Override
    public Optional<User> findByUsername(String username) {
        return entityManager
                .createQuery("from User h where h.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    @Override
    public void save(UserSaveCommand cmd) {
        User user = new User(cmd.getUsername(), cmd.getPassword(), cmd.getFirstname(), cmd.getSurname(), cmd.getLastUpdated());
        entityManager.persist(user);
    }

    @Transactional
    @Override
    public void delete(UserDeleteCommand cmd) {
        findById(cmd.getId()).ifPresent(hotel -> entityManager.remove(hotel));
    }

    @Transactional
    @Override
    public void update(UserUpdateCommand cmd) {
        findById(cmd.getId()).ifPresent(user -> entityManager.createQuery("UPDATE User h  SET username = :username, password = :password, firstname=:firstname, surname=:surname where id = :id")
                .setParameter("username", cmd.getUsername())
                .setParameter("id", cmd.getId())
                .setParameter("password", cmd.getPassword())
                .setParameter("firstname", cmd.getFirstname())
                .setParameter("surname", cmd.getSurname())
                .executeUpdate()
        );
    }
}

