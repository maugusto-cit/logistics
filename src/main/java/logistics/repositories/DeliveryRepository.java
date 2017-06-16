package logistics.repositories;

import logistics.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by alien on 6/15/17.
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByDeliveryId(Long deliveryId);

}