package subway.dto;

import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

public class AddResultDto {
    private final List<Section> addedResults;
    private final List<Section> deletedResults;
    private final Station station;

    public AddResultDto(List<Section> addedResults, List<Section> deletedResults, Station station) {
        this.addedResults = addedResults;
        this.deletedResults = deletedResults;
        this.station = station;
    }
}
