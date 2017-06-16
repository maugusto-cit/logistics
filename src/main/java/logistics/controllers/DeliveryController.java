package logistics.controllers;

import javassist.NotFoundException;
import logistics.domain.Delivery;
import logistics.domain.Package;
import logistics.domain.Step;
import logistics.domain.exceptions.DeliveryAlredyExistsException;
import logistics.domain.exceptions.DeliveryNotFoundException;
import logistics.repositories.DeliveryRepository;
import org.hibernate.annotations.NotFound;
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
    public  @ApiResponseObject List<Step> steps(@ApiPathParam(name = "deliveryId") @PathVariable("deliveryId") Long deliveryId) {

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

        private Package[] desirablePackageOrder;

        public List<Step> steps = null;

        public Supply(Delivery delivery) {

            if (delivery.getPackages() != null && !delivery.getPackages().isEmpty()) {

                this.delivery = delivery;
                this.steps = new ArrayList<>();
                this.desirablePackageOrder = new Package[delivery.getPackages().size()];

                delivery.getPackages().toArray(desirablePackageOrder);

                this.getDesirablePackageOrder();
                this.processSupply();
            }
        }

        /**
         * Strategy: order by weight to get desirable order, then search for heaviest package on A or T stacks,
         * move those above it and move the searched package to C stack, continue to do it until all the packages are in C stack.
         * <p>
         * // TODO improve comparisons and moves
         */
        private void processSupply() {

            A = new Stack<>();
            T = new Stack<>();
            C = new Stack<>();

            A.addAll(delivery.getPackages());
            int stepCounter = 1;

            while (C.size() != this.desirablePackageOrder.length) {

                for (Package aPackage : this.desirablePackageOrder) {

                    int index = A.search(aPackage);
                    Package p;

                    if (index != -1) {

                        for (int i = 0; i < index - 1; i++) {
                            p = A.pop();
                            T.push(p);
                            steps.add(new Step(stepCounter++, p.getId(), "A", "T"));
                        }

                        p = A.pop();
                        C.push(p);
                        steps.add(new Step(stepCounter++, p.getId(), "A", "C"));

                    } else {

                        index = T.search(aPackage);

                        if (index != -1) {

                            for (int i = 0; i < index - 1; i++) {
                                p = T.pop();
                                A.push(p);
                                steps.add(new Step(stepCounter++, p.getId(), "T", "A"));
                            }

                            p = T.pop();
                            C.push(p);
                            steps.add(new Step(stepCounter++, p.getId(), "T", "C"));

                        }
                    }

                }

            }
        }

        private void getDesirablePackageOrder() {

            Arrays.sort(desirablePackageOrder,
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

        }

    }

}