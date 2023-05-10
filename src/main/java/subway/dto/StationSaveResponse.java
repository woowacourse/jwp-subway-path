package subway.dto;

import java.util.List;
import java.util.Objects;

public class StationSaveResponse {

    private final Long lineId;
    private final List<Long> savedStationIds;
    private final List<Long> savedSectionIds;

    public StationSaveResponse(Long lineId, List<Long> savedStationIds,
                               List<Long> savedSectionIds) {
        this.lineId = lineId;
        this.savedStationIds = savedStationIds;
        this.savedSectionIds = savedSectionIds;
    }

    public Long getLineId() {
        return lineId;
    }

    public List<Long> getSavedStationIds() {
        return savedStationIds;
    }

    public List<Long> getSavedSectionIds() {
        return savedSectionIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationSaveResponse that = (StationSaveResponse) o;
        return Objects.equals(lineId, that.lineId) && Objects.equals(savedStationIds, that.savedStationIds) && Objects.equals(savedSectionIds, that.savedSectionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, savedStationIds, savedSectionIds);
    }

    @Override
    public String toString() {
        return "StationSaveResponse{" +
                "lineId=" + lineId +
                ", savedStationIds=" + savedStationIds +
                ", savedSectionIds=" + savedSectionIds +
                '}';
    }
}
