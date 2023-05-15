package subway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionWithStationNameEntity {
    private final Long id;
    private final Long lineId;
    private final String preStationName;
    private final String stationName;
    private final Long distance;

    public SectionWithStationNameEntity(Long lineId, String preStationName, String stationName, Long distance) {
        this(null, lineId, preStationName, stationName, distance);
    }
}
