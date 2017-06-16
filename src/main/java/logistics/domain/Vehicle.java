package logistics.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by alien on 6/15/17.
 */

@Entity
public class Vehicle {

    @Id
    private Long id;

    public Vehicle() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
