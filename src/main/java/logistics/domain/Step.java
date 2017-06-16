package logistics.domain;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Created by alien on 6/15/17.
 */

@ApiObject
public class Step {

    @ApiObjectField(description = "Step sequence")
    private int step;

    @ApiObjectField(description = "Package reference")
    private Long packageId;

    @ApiObjectField(description = "Human readable action to move package from zone")
    private String from;

    @ApiObjectField(description = "Human readable action to move package to zone")
    private String to;

    public Step(int step, Long packageId, String from, String to) {
        this.step = step;
        this.packageId = packageId;
        this.from = this.zone(from);
        this.to = this.zone(to);
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String zone(String ref) {

        switch (ref) {
            case "A":
                return "zona de abastecimento";
            case "T":
                return "zona de transferência";
            case "C":
                return "zona do caminhão";
            default:
                return "";
        }
    }
}
