package subway.service.section.dto;

import subway.service.section.domain.Section;
import subway.service.station.domain.Station;

import java.util.List;

public class AddResult {
    private final List<Section> addedResults;
    private final List<Section> deletedResults;
    private final List<Station> addedStation;

    public AddResult(List<Section> addedResults, List<Section> deletedResults, List<Station> addedStation) {
        this.addedResults = addedResults;
        this.deletedResults = deletedResults;
        this.addedStation = addedStation;
    }

    public List<Section> getAddedResults() {
        return addedResults;
    }

    public List<Section> getDeletedResults() {
        return deletedResults;
    }

    public List<Station> getAddedStation() {
        return addedStation;
    }
}
