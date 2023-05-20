package subway.domain.line.entity;


import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionEntity(final Long id, final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        validate(distance);
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionEntity(final Long id, final SectionEntity sectionEntity) {
        this(id, sectionEntity.lineId, sectionEntity.upStationId, sectionEntity.downStationId, sectionEntity.distance);
    }

    public SectionEntity(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    private void validate(final int distance) {
        if (distance > 0) {
            return;
        }
        throw new IllegalArgumentException("거리는 양의 정수만 가능합니다.");
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {

        private Long id;
        private Long lineId;
        private Long upStationId;
        private Long downStationId;
        private int distance;

        public Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setLineId(Long lineId) {
            this.lineId = lineId;
            return this;
        }

        public Builder setUpStationId(final Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder setDownStationId(final Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder setDistance(final int distance) {
            this.distance = distance;
            return this;
        }

        public SectionEntity build() {
            if (id == null) {
                return new SectionEntity(null, lineId, upStationId, downStationId, distance);
            }
            return new SectionEntity(id, lineId, upStationId, downStationId, distance);
        }
    }
}
