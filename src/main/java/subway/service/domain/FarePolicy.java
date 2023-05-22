package subway.service.domain;

public class FarePolicy {

    private Long id;
    private final LineProperty lineProperty;
    private final Integer additionalFare;

    public FarePolicy(LineProperty lineProperty, Integer additionalFare) {
        this.lineProperty = lineProperty;
        this.additionalFare = additionalFare;
    }

    public FarePolicy(Long id, LineProperty lineProperty, Integer additionalFare) {
        this.id = id;
        this.lineProperty = lineProperty;
        this.additionalFare = additionalFare;
    }

    public Long getId() {
        return id;
    }

    public LineProperty getLineProperty() {
        return lineProperty;
    }

    public Integer getAdditionalFare() {
        return additionalFare;
    }

}
