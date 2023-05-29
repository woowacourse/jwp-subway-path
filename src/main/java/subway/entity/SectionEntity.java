package subway.entity;

public class SectionEntity {

    private final long id;
    private final long upstreamId;
    private final long downstreamId;
    private final long lineId;
    private final int distance;

    public SectionEntity(long id, long upstreamId, long downstreamId, long lineId, int distance) {
        this.id = id;
        this.upstreamId = upstreamId;
        this.downstreamId = downstreamId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public long getUpstreamId() {
        return upstreamId;
    }

    public long getDownstreamId() {
        return downstreamId;
    }

    public long getLineId() {
        return lineId;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", upstreamId=" + upstreamId +
                ", downstreamId=" + downstreamId +
                ", lineId=" + lineId +
                ", distance=" + distance +
                '}';
    }

    public static class Builder {

        private long id;
        private long upstreamId;
        private long downstreamId;
        private long lineId;
        private int distance;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder upstreamId(long upstreamId) {
            this.upstreamId = upstreamId;
            return this;
        }

        public Builder downstreamId(long downstreamId) {
            this.downstreamId = downstreamId;
            return this;
        }

        public Builder lineId(long lineId) {
            this.lineId = lineId;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public SectionEntity build() {
            return new SectionEntity(id, upstreamId, downstreamId, lineId, distance);
        }
    }
}
