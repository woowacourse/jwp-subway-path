package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.exception.StationNotFoundException;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> findSectionsContainStation(Station containedStation) {
        return sections.stream()
                .filter(section -> section.isContainStation(containedStation))
                .collect(toList());
    }

    public Optional<Section> findSectionContainSection(Section containedSection) {
        return sections.stream()
                .filter(section -> section.isContainSection(containedSection))
                .findAny();
    }

    public boolean isOnlyTwoStationsExist() {
        return sections.size() == 1;
    }

    public Set<String> getContainingStationNames() {
        Set<String> stationNames = new HashSet<>();
        for (Section section : sections) {
            stationNames.add(section.getUpStationName());
            stationNames.add(section.getDownStationName());
        }
        return stationNames;
    }

    public List<String> getSortedStationNames() {
        Map<Station, Station> stationConnections = sections.stream()
                .collect(toMap(Section::getUpStation, Section::getDownStation));

        List<String> sortedStationNames = new ArrayList<>();
        Station upEndStation = findUpEndStation(stationConnections);
        sortedStationNames.add(upEndStation.getName());
        while (stationConnections.containsKey(upEndStation)) {
            upEndStation = stationConnections.get(upEndStation);
            sortedStationNames.add(upEndStation.getName());
        }

        return sortedStationNames;
    }

    private Station findUpEndStation(Map<Station, Station> stationConnections) {
        List<Station> upStations = new ArrayList<>(stationConnections.keySet());
        List<Station> downStations = new ArrayList<>(stationConnections.values());
        upStations.removeAll(downStations);
        if (upStations.size() != 1) {
            throw new StationNotFoundException("상행 종점을 찾을 수 없습니다.");
        }
        return upStations.get(0);
    }

    public Line getLine() {
        return sections.get(0).getLine();
    }

    public Long getLineId() {
        return getLine().getId();
    }

    public int getSectionsSize() {
        return sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}
