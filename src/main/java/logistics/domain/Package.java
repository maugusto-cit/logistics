package logistics.domain;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by alien on 6/15/17.
 */

@Entity
@ApiObject
public class Package {

    @Id
    @ApiObjectField(description = "Package identifier")
    private Long id;

    private Double weight;

    @Transient
    @XmlTransient
    public int index;

    public Package() {
    }

    public Package(Long id, Double weight) {
        this.id = id;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
