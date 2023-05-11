package subway.persistence.entity;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    private SectionEntity(final Builder builder) {
        this.id = builder.id;
        this.lineId = builder.lineId;
        this.upStationId = builder.upStationId;
        this.downStationId = builder.downStationId;
        this.distance = builder.distance;
    }

    public static class Builder {

        private Long id;
        private Long lineId;
        private Long upStationId;
        private Long downStationId;
        private int distance;

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder lineId(Long lineId) {
            this.lineId = lineId;
            return this;
        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public SectionEntity build() {
            return new SectionEntity(this);
        }
    }

    public boolean containsStationId(Long stationId) {
        return upStationId.equals(stationId) || downStationId.equals(stationId);
    }

    public boolean containsDownStationId(Long stationId) {
        return downStationId.equals(stationId);
    }

    public boolean containsUpStationId(Long stationId) {
        return upStationId.equals(stationId);
    }

    public boolean matchesByUpAndDownStationId(Long upStationId, Long downStationId) {
        return upStationId.equals(upStationId) && downStationId.equals(downStationId);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
