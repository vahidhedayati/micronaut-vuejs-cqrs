package hotel.write.controller;

import hotel.write.event.commands.CommandRoot;
import hotel.write.websocket.WebsocketMessage;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Maybe;

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

//@Validated
@Controller("/")
public class HttpListener {
    @Inject
    ApplicationEventPublisher publisher;

    /**
     * Please note @Valid tag had to be removed since this stopped call from getting into actual block
     * and doing our own validation as such.
     * @param command
     * @param <T>
     * @return
     */
    @Post("/")
    public <T extends CommandRoot> HttpResponse publish(T command) {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<T>> constraintViolations = validator.validate(command);
        if (constraintViolations.size() > 0) {
            Set<String> violationMessages = new HashSet<String>();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getMessage());
            }
            /**
             * TODO - there appears to be some of form of a bug within micronauts httpClient interface implementation
             * at the moment if you return HttpResponse.serverError or any form of error, the client interface
             * throws exception which doesn't appear to be capturable - for now relying on .ok and making it simulate
             * an ok transaction with specific status
             */
            HashMap<String,Set<String>> errors = new HashMap<>();
            errors.put("error", violationMessages);
            return HttpResponse.ok(errors);
        }
        publisher.publishEvent(command);
        return HttpResponse.ok();
    }
}
