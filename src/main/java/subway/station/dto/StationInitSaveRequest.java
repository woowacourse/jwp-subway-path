package subway.station.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import subway.section.adapter.output.persistence.SectionEntity;
import subway.section.domain.Section;
import subway.station.domain.Station;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class StationInitSaveRequest {
    private final String firstStation;
    private final String secondStation;
    private final Long distance;
    private final Long lineId;
    
    public List<Station> toEntities() {
        return List.of(new Station(firstStation), new Station(secondStation));
    }
    
    public Section toSectionEntity() {
        return new Section(firstStation, secondStation, distance);
    }
}
