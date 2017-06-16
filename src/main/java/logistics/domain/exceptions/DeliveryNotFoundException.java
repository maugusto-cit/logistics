package logistics.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by alien on 6/15/17.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeliveryNotFoundException extends RuntimeException {

    public DeliveryNotFoundException(Long deliveryId) {
        super("delivery: " + deliveryId + " not found.");
    }
}
