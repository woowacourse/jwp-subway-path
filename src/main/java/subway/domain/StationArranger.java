package subway.domain;

import subway.exception.SectionNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StationArranger {

    private static final String KEY_LAST_STATION_OF_UP = "up";
    private static final String KEY_LAST_STATION_OF_DOWN = "down";

    public List<Station> arrange(final List<Section> sections) {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Station> endStations = findAllFinalStations(sections);
        Station lastStationOfUp = endStations.get(KEY_LAST_STATION_OF_UP);
        Station lastStationOfDown = endStations.get(KEY_LAST_STATION_OF_DOWN);

        return arrangeStations(sections, lastStationOfUp, lastStationOfDown);
    }

    private Map<String, Station> findAllFinalStations(final List<Section> sections) {
        Set<Station> upStations = allUpStations(sections);
        Set<Station> downStations = allDownStations(sections);

        removeAllDuplicate(upStations, downStations);

        Station lastStationOfUp = List.copyOf(upStations).get(0);
        Station lastStationOfDown = List.copyOf(downStations).get(0);

        return new HashMap<>() {{
            put(KEY_LAST_STATION_OF_UP, lastStationOfUp);
            put(KEY_LAST_STATION_OF_DOWN, lastStationOfDown);
        }};
    }

    private Set<Station> allUpStations(final List<Section> sections) {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toSet());
    }

    private Set<Station> allDownStations(final List<Section> sections) {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toSet());
    }

    private void removeAllDuplicate(final Set<Station> upStations, final Set<Station> downStations) {
        Set<Station> duplicateStations = new HashSet<>(upStations);
        duplicateStations.retainAll(downStations);

        upStations.removeAll(duplicateStations);
        downStations.removeAll(duplicateStations);
    }

    private List<Station> arrangeStations(final List<Section> sections, final Station lastUpStation, final Station lastDownStation) {
        List<Station> stations = new ArrayList<>();

        Station currentStation = lastUpStation;
        while (!currentStation.equals(lastDownStation)) {
            stations.add(currentStation);
            currentStation = nextStationOf(sections, currentStation);
        }
        stations.add(lastDownStation);

        return stations;
    }

    private Station nextStationOf(final List<Section> sections, final Station upStation) {
        Section foundSection = sections.stream()
                .filter(section -> section.isSameUpStation(upStation))
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);

        return foundSection.getDownStation();
    }
}
