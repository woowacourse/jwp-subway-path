package subway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionWithStationNameEntity {
    private Long id;
    private Long lineId;
    private String preStationName;
    private String stationName;
    private Long distance;

    public SectionWithStationNameEntity(Long lineId, String preStationName, String stationName, Long distance) {
        this(null, lineId, preStationName, stationName, distance);
    }
}
