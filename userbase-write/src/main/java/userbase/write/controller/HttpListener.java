package userbase.write.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Maybe;
import userbase.write.event.commands.CommandRoot;
import userbase.write.websocket.WebsocketMessage;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
/**
 * This is our pretend HttpListener - it simply listens in for all events bound for hotel-write application and
 * republishes event locally which is picked up by ApplicationEventListener and process as true command object form
 * so by each individual command handler as such
 */
@Validated
@Controller("/")
public class HttpListener {
    private final ObjectMapper objectMapper;

    public HttpListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Inject
    ApplicationEventPublisher publisher;

    @Post("/")
    public <T extends CommandRoot> HttpResponse publish(@Valid T command) {
        System.out.println(" REMOTE SERVER SERVER ---------- IN HOTEL "+command.getClass());
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<T>> constraintViolations = validator.validate(command);
        if (constraintViolations.size() > 0) {
            Set<String> violationMessages = new HashSet<String>();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getMessage());
            }
            return HttpResponse.badRequest(violationMessages);
        }
        publisher.publishEvent(command);
        return HttpResponse.ok();
    }
    public String serializeMessage(WebsocketMessage command) {
        String json;
        try {
            json = objectMapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
