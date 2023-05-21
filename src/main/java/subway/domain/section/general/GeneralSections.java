package subway.domain.section.general;

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.*;
import java.util.stream.Collectors;

import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.SectionNotFoundException;

public class GeneralSections {

    private static final int UP_END_STATION_FIND_SIZE = 1;
    private static final int UP_END_STATION_FIND_INDEX = 0;

    private final List<GeneralSection> generalSections;

    public GeneralSections(List<GeneralSection> generalSections) {
        this.generalSections = new ArrayList<>(generalSections);
    }

    public GeneralSection findSectionByDownStation(Station station) {
        Long stationId = station.getId();
        return generalSections.stream()
                .filter(section -> section.getDownStation().getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new SectionNotFoundException("해당 역을 하행역으로 가지는 구간이 없습니다."));
    }

    public GeneralSection findSectionByUpStation(Station station) {
        Long stationId = station.getId();
        return generalSections.stream()
                .filter(section -> section.getUpStation().getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new SectionNotFoundException("해당 역을 상행역으로 가지는 구간이 없습니다."));
    }

    public GeneralSectionCase determineSectionCaseByStationId(Long stationId) {
        Optional<GeneralSection> upSection = generalSections.stream()
                .filter(section -> section.getDownStation().getId().equals(stationId))
                .findFirst();

        Optional<GeneralSection> downSection = generalSections.stream()
                .filter(section -> section.getUpStation().getId().equals(stationId))
                .findFirst();

        boolean isUpEndStation = upSection.isEmpty() && downSection.isPresent();
        boolean isDownEndStation = upSection.isPresent() && downSection.isEmpty();

        if (isUpEndStation || isDownEndStation) {
            return GeneralSectionCase.END_SECTION;
        }
        return GeneralSectionCase.MIDDLE_SECTION;
    }

    public Optional<GeneralSection> findSectionHasDownStationNameAsDownStationByLine(String downStationName, Line line) {
        return generalSections.stream()
                .filter(section -> section.isSameDownStationName(downStationName) &&
                        section.isSameLineId(line.getId()))
                .findFirst();
    }

    public Optional<GeneralSection> findSectionHasUpStationNameAsUpStationByLine(String upStationName, Line line) {
        return generalSections.stream()
                .filter(section -> section.isSameUpStationName(upStationName) &&
                        section.isSameLineId(line.getId()))
                .findFirst();
    }

    public boolean hasSectionOnlyOne() {
        return generalSections.size() == 1;
    }

    public List<Station> getSortedStations() {
        Map<Station, Station> stationConnections = generateStationConnections();

        List<Station> sortedStations = new ArrayList<>();
        Station upEndStation = findUpEndStation(stationConnections);
        sortedStations.add(upEndStation);
        Station tempUpStation = upEndStation;
        for (int repeatCount = 0; repeatCount < stationConnections.size(); repeatCount++) {
            Station downStation = stationConnections.get(tempUpStation);
            sortedStations.add(downStation);
            tempUpStation = downStation;
        }
        return sortedStations;
    }

    public Map<Station, Station> generateStationConnections() {
        return generalSections.stream()
                .collect(toUnmodifiableMap(
                        Section::getUpStation,
                        Section::getDownStation));
    }

    public List<String> getSortedStationNames() {
        List<Station> sortedStations = getSortedStations();
        return sortedStations.stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
    }

    private Station findUpEndStation(Map<Station, Station> stationConnections) {
        List<Station> upStations = new ArrayList<>(stationConnections.keySet());
        List<Station> downStations = new ArrayList<>(stationConnections.values());
        upStations.removeAll(downStations);
        if (upStations.size() != UP_END_STATION_FIND_SIZE) {
            throw new IllegalStateException("상행 종점을 찾을 수 없습니다.");
        }
        return upStations.get(UP_END_STATION_FIND_INDEX);
    }

    public List<Section> getSections() {
        return List.copyOf(generalSections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeneralSections generalSections1 = (GeneralSections) o;
        return Objects.equals(generalSections, generalSections1.generalSections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generalSections);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + generalSections +
                '}';
    }
}
