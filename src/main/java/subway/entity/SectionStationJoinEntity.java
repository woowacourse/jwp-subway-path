package subway.entity;

import java.util.Objects;

public class SectionStationJoinEntity {

    private final Long sectionId;
    private final Long upStationId;
    private final String upStationName;
    private final Long downStationId;
    private final String downStationName;
    private final Long lineId;
    private final int distance;

    private SectionStationJoinEntity(Long sectionId, Long upStationId,
                                     String upStationName, Long downStationId,
                                     String downStationName, Long lineId,
                                     int distance) {
        this.sectionId = sectionId;
        this.upStationId = upStationId;
        this.upStationName = upStationName;
        this.downStationId = downStationId;
        this.downStationName = downStationName;
        this.lineId = lineId;
        this.distance = distance;
    }

    public static class Builder {

        private Long sectionId;
        private Long upStationId;
        private String upStationName;
        private Long downStationId;
        private String downStationName;
        private Long lineId;
        private int distance;

        public Builder sectionId(Long sectionId) {
            this.sectionId = sectionId;
            return this;
        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder upStationName(String upStationName) {
            this.upStationName = upStationName;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder downStationName(String downStationName) {
            this.downStationName = downStationName;
            return this;
        }

        public Builder lineId(Long lineId) {
            this.lineId = lineId;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public SectionStationJoinEntity build() {
            return new SectionStationJoinEntity(sectionId, upStationId,
                    upStationName, downStationId,
                    downStationName, lineId, distance);
        }
    }

    public Long getSectionId() {
        return sectionId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Long getLineId() {
        return lineId;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionStationJoinEntity that = (SectionStationJoinEntity) o;
        return distance == that.distance && Objects.equals(sectionId, that.sectionId) && Objects.equals(upStationId, that.upStationId) && Objects.equals(upStationName, that.upStationName) && Objects.equals(downStationId, that.downStationId) && Objects.equals(downStationName, that.downStationName) && Objects.equals(lineId, that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionId, upStationId, upStationName, downStationId, downStationName, lineId, distance);
    }

    @Override
    public String toString() {
        return "SectionStationJoinEntity{" +
                "sectionId=" + sectionId +
                ", upStationId=" + upStationId +
                ", upStationName='" + upStationName + '\'' +
                ", downStationId=" + downStationId +
                ", downStationName='" + downStationName + '\'' +
                ", lineId=" + lineId +
                ", distance=" + distance +
                '}';
    }
}
