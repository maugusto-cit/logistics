package logistics.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by alien on 6/15/17.
 */

@ResponseStatus(HttpStatus.CONFLICT)
public class DeliveryAlredyExistsException extends RuntimeException {

    public DeliveryAlredyExistsException(Long deliveryId) {
        super("delivery: " + deliveryId + " already exists.");
    }
}
