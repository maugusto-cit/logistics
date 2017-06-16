package logistics.domain;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by alien on 6/14/17.
 */

@Entity
@ApiObject
public class Delivery {

    @Id
    @Column(name = "delivery_id")
    @ApiObjectField(description = "Delivery identifier")
    private Long deliveryId;

    @NotNull
    @Column(name = "vehicle_id")
    @ApiObjectField(description = "Vehicle reference")
    private Long vehicleId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "deliveries_packages",
            joinColumns = {@JoinColumn(name = "delivery_id", referencedColumnName = "delivery_id")},
            inverseJoinColumns = {@JoinColumn(name = "package_id", referencedColumnName = "id")})
    private List<Package> packages;

    public Delivery() {

    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }
}
