package subway.section.domain;

import subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Sections {

    private List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> getStationsAsc(final Station finalUpStation) {
        final List<Station> stations = new ArrayList<>();
        Long beforeStationId = finalUpStation.getId();

        while (true) {
            Section nextSection = findNextSectionOrNull(beforeStationId);
            if (nextSection == null) {
                Station finalStation = getFinalStationOrNull(beforeStationId);
                stations.add(finalStation);
                break;
            }
            stations.add(nextSection.getUpStation());
            beforeStationId = nextSection.getDownStation().getId();
        }

        return stations;
    }

    private Station getFinalStationOrNull(final Long beforeStationId) {
        for (Section section : sections) {
            if (section.downStationIdIsSameId(beforeStationId)) {
                return section.getDownStation();
            }
        }
        return null;
    }

    private Section findNextSectionOrNull(final Long beforeStationId) {
        for (Section section : sections) {
            if (section.upStationIdIsSameId(beforeStationId)) {
                return section;
            }
        }
        return null;
    }


    public List<Section> getSections() {
        return sections;
    }

}
