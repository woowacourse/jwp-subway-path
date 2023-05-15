package subway.entity;

public class SectionEntity {

    private final Long id;
    private final Long upstreamId;
    private final Long downstreamId;
    private final Long lineId;
    private final Integer distance;

    public SectionEntity(Long id, Long upstreamId, Long downstreamId, Long lineId, Integer distance) {
        this.id = id;
        this.upstreamId = upstreamId;
        this.downstreamId = downstreamId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpstreamId() {
        return upstreamId;
    }

    public Long getDownstreamId() {
        return downstreamId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    public static class Builder {

        private Long id;
        private Long upstreamId;
        private Long downstreamId;
        private Long lineId;
        private Integer distance;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder upstreamId(Long upstreamId) {
            this.upstreamId = upstreamId;
            return this;
        }

        public Builder downstreamId(Long downstreamId) {
            this.downstreamId = downstreamId;
            return this;
        }

        public Builder lineId(Long lineId) {
            this.lineId = lineId;
            return this;
        }

        public Builder distance(Integer distance) {
            this.distance = distance;
            return this;
        }

        public SectionEntity build() {
            return new SectionEntity(id, upstreamId, downstreamId, lineId, distance);
        }
    }
}
