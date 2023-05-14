package subway.domain.calculator;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RemoveCalculator {

    public static final int MAX_SECTION_SIZE_TO_REMOVE_WHOLE_LINE = 1;
    private final Sections sections;

    public RemoveCalculator(Sections sections) {
        this.sections = sections;
    }

    public Changes calculate(Station stationToDelete) {
        if (sections.getSectionsSize() <= MAX_SECTION_SIZE_TO_REMOVE_WHOLE_LINE) {
            return getChangesWhenRemoveLine(stationToDelete);
        }

        Optional<Section> nullableUpSection = sections.findUpSectionByStation(stationToDelete);
        Optional<Section> nullableDownSection = sections.findDownSectionByStation(stationToDelete);
        if (isEndStation(nullableUpSection, nullableDownSection)) {
            return getChangesWhenRemoveEndStation(stationToDelete);
        }

        Section upSection = nullableUpSection.get();
        Section downSection = nullableDownSection.get();
        return getChangesWhenRemoveMiddleStation(stationToDelete, upSection, downSection);
    }

    private Changes getChangesWhenRemoveMiddleStation(Station stationToDelete, Section upSection, Section downSection) {
        int upDistance = upSection.getDistanceValue();
        int downDistance = downSection.getDistanceValue();
        int newDistance = upDistance + downDistance;

        Station newUpStation = upSection.getUpStation();
        Station newDownStation = downSection.getDownStation();

        Section newSection = new Section(null, newUpStation, newDownStation, newDistance);
        return new Changes(stationToDelete.getLineId(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), List.of(stationToDelete),
                new ArrayList<>(), List.of(newSection));
    }

    private boolean isEndStation(Optional<Section> nullableUpSection, Optional<Section> nullableDownSection) {
        return (nullableUpSection.isEmpty() && nullableDownSection.isPresent()) ||
                (nullableUpSection.isPresent() && nullableDownSection.isEmpty());
    }

    private Changes getChangesWhenRemoveEndStation(Station stationToDelete) {
        return new Changes(stationToDelete.getLineId(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), List.of(stationToDelete),
                new ArrayList<>(), new ArrayList<>());
    }

    private Changes getChangesWhenRemoveLine(Station stationToDelete) {
        return new Changes(stationToDelete.getLineId(),
                new ArrayList<>(), List.of(stationToDelete.getLine()),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
    }
}
