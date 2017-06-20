package logistics.controllers;

import logistics.domain.Delivery;
import logistics.domain.Package;
import logistics.domain.Step;
import logistics.domain.exceptions.DeliveryAlredyExistsException;
import logistics.domain.exceptions.DeliveryNotFoundException;
import logistics.repositories.DeliveryRepository;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by alien on 6/14/17.
 */

@RestController
@Api(description = "Responsible for control and persistence of deliveries", name = "Delivery API")
public class DeliveryController {


    @Autowired
    private DeliveryRepository repository;

    public DeliveryController() {
    }

    @ApiMethod
    @RequestMapping(value = "/delivery", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity create(@RequestBody() Delivery delivery) {

        this.checkDelivery((delivery.getDeliveryId()));

        repository.save(delivery);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ApiMethod
    @RequestMapping(value = "/delivery/{deliveryId}/step", method = RequestMethod.GET)
    public @ApiResponseObject
    List<Step> steps(@ApiPathParam(name = "deliveryId") @PathVariable("deliveryId") Long deliveryId) {

        this.findDelivery(deliveryId);

        Delivery delivery = this.repository.findOne(deliveryId);

        return new Supply(delivery).steps;
    }

    private void findDelivery(Long deliveryId) {
        this.repository.findByDeliveryId(deliveryId).orElseThrow(
                () -> (new DeliveryNotFoundException(deliveryId)));
    }

    private void checkDelivery(Long deliveryId) {
        this.repository.findByDeliveryId(deliveryId).ifPresent(
                delivery -> {
                    throw new DeliveryAlredyExistsException(delivery.getDeliveryId());
                });
    }


    private class Supply {

        private Delivery delivery;

        private Stack<Package> A, T, C;

        private Package[] orderedPackages;

        public List<Step> steps = null;

        private int stepCounter = 1;

        public Supply(Delivery delivery) {

            if (delivery.getPackages() != null && !delivery.getPackages().isEmpty()) {

                this.delivery = delivery;
                this.steps = new ArrayList<>();
                this.orderedPackages = new Package[delivery.getPackages().size()];

                delivery.getPackages().toArray(orderedPackages);

                this.orderPackages();
                this.processSupply();
            }
        }

        /**
         * Strategy: hanoi solution recursively
         * <p>
         */
        private void processSupply() {

            A = new Stack<>();
            T = new Stack<>();
            C = new Stack<>();

            A.addAll(Arrays.asList(orderedPackages));

            recursiveLoad(orderedPackages[0], A, C, T);

        }

        private void recursiveLoad(Package pack, Stack<Package> source, Stack<Package> destination, Stack<Package> aux) {

            if (pack.index == orderedPackages.length - 1) {
                destination.push(source.pop());
                steps.add(new Step(stepCounter++, pack.getId(), zone(source), zone(destination)));
            } else {
                recursiveLoad(orderedPackages[pack.index + 1], source, aux, destination);
                destination.push(source.pop());
                steps.add(new Step(stepCounter++, pack.getId(), zone(source), zone(destination)));
                recursiveLoad(orderedPackages[pack.index + 1], aux, destination, source);
            }

        }

        private void orderPackages() {

            // order packages
            Arrays.sort(orderedPackages,
                    (Package a, Package b) -> {
                        if (a.getWeight() < b.getWeight()) {
                            return 1;
                        }
                        if (a.getWeight() > b.getWeight()) {
                            return -1;
                        }
                        return 0;
                    }
            );

            // insert aux index order
            int i = 0;
            for (Package orderedPackage : orderedPackages) {
                orderedPackage.index = i;
                i++;
            }

        }

        public String zone(Stack<Package> stack) {

            if (stack.hashCode() == A.hashCode())
                return "A";
            else if (stack.hashCode() == T.hashCode())
                return "T";
            else if (stack.hashCode() == C.hashCode())
                return "C";
            else
                return "K";

        }

    }

}