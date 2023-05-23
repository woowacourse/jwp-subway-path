package subway.entity;

public class FarePolicyEntity {

    private Long id;
    private final Long lineId;
    private final Integer additionalFare;

    public FarePolicyEntity(Long lineId, Integer additionalFare) {
        this.lineId = lineId;
        this.additionalFare = additionalFare;
    }

    public FarePolicyEntity(Long id, Long lineId, Integer additionalFare) {
        this.id = id;
        this.lineId = lineId;
        this.additionalFare = additionalFare;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getAdditionalFare() {
        return additionalFare;
    }

}
