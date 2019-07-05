package gateway.command.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.command.domain.Events;
import gateway.command.event.commands.CommandRoot;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.annotation.Primary;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Singleton
public class GatewayService {

    private final ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager entityManager;



    public GatewayService(@CurrentSession EntityManager entityManager, ObjectMapper objectMapper) {
        this.entityManager = entityManager;
        this.objectMapper=objectMapper;

    }

    @Transactional
    public void save(Events event) {
        if (event!=null && !findByTransactionId(event.getTransactionId()).isPresent()) {
                entityManager.persist(event);
        }
    }
    @Transactional
    public void markCompleted(Events event) {
        if (event!=null && findByTransactionId(event.getTransactionId()).isPresent()) {
            entityManager.createQuery("UPDATE Events h  SET completed = :completed  where id = :id")
                    .setParameter("completed", new Date())
                    .executeUpdate();
        }
    }

    @Transactional(readOnly = true)
    public List<Events> processMissing() {
        String qlString = "FROM Events as  h where processed is null ";
        TypedQuery<Events> query=  query=entityManager.createQuery(qlString, Events.class);
        return query.getResultList();
    }

    @Transactional
    public Optional<Events> findByCode(String code) {
        return entityManager
                .createQuery("from Events h where h.eventType = :code", Events.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }
    @Transactional
    public Optional<Events> findByTransactionId(String code) {
        return entityManager
                .createQuery("from Events h where h.transactionId = :code", Events.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }

    public <T extends CommandRoot> String serializeMessage(T command) {
        String json;
        try {
            json = objectMapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

}
