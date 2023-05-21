package subway.dto;

import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class SectionRequest {
    
    @Min(1)
    long lineId;
    @Min(1)
    long newStationId;
    @Min(1)
    long baseStationId;
    @Pattern(regexp = "UP|DOWN")
    String direction;
    @Min(1)
    int distance;
    
    public SectionRequest(final long lineId, final long newStationId, final long baseStationId, final String direction,
            final int distance) {
        this.lineId = lineId;
        this.newStationId = newStationId;
        this.baseStationId = baseStationId;
        this.direction = direction;
        this.distance = distance;
    }
    
    public long getLineId() {
        return this.lineId;
    }
    
    public long getNewStationId() {
        return this.newStationId;
    }
    
    public long getBaseStationId() {
        return this.baseStationId;
    }
    
    public String getDirection() {
        return this.direction;
    }
    
    public int getDistance() {
        return this.distance;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.lineId, this.newStationId, this.baseStationId, this.direction, this.distance);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SectionRequest that = (SectionRequest) o;
        return this.lineId == that.lineId && this.newStationId == that.newStationId
                && this.baseStationId == that.baseStationId
                && this.distance == that.distance && Objects.equals(this.direction, that.direction);
    }
}