package subway.entity;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Integer distance;
    private final Long previousStationId;
    private final Long nextStationId;

    public SectionEntity(final Long id, final Long lineId, final Integer distance,
                         final Long previousStationId, final Long nextStationId) {
        this.id = id;
        this.lineId = lineId;
        this.distance = distance;
        this.previousStationId = previousStationId;
        this.nextStationId = nextStationId;
    }

    public static class Builder {
        private Long id;
        private Long lineId;
        private Integer distance;
        private Long previousStationId;
        private Long nextStationId;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder lineId(final Long lineId) {
            this.lineId = lineId;
            return this;
        }

        public Builder distance(final Integer distance) {
            this.distance = distance;
            return this;
        }

        public Builder previousStationId(final Long previousStationId) {
            this.previousStationId = previousStationId;
            return this;
        }

        public Builder nextStationId(final Long nextStationId) {
            this.nextStationId = nextStationId;
            return this;
        }

        public SectionEntity build() {
            return new SectionEntity(id, lineId, distance, previousStationId, nextStationId);
        }

    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getPreviousStationId() {
        return previousStationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

}
