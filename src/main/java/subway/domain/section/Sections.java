package subway.domain.section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import subway.domain.station.Station;

public class Sections {

    private final List<Section> sections;
    private final List<Station> stations;

    public Sections(final List<Section> sections) {
        this.sections = sections;
        this.stations = sort(sections);
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public void validateSections(final Section requestSection) {
        final Station sourceRequest = requestSection.getSource();
        final Station targetRequest = requestSection.getTarget();

        if (!stations.contains(sourceRequest) && !stations.contains(targetRequest)) {
            throw new IllegalArgumentException("존재하지 않는 역을 추가할 수 없습니다.");
        }

        if (stations.contains(sourceRequest) && stations.contains(targetRequest)) {
            throw new IllegalArgumentException("이미 추가된 구간입니다.");
        }
    }

    public boolean isTargetUpward(final Station targetStation) {
        return stations.get(0).equals(targetStation);
    }

    public boolean isSourceDownward(final Station sourceStation) {
        return stations.get(stations.size() - 1).equals(sourceStation);
    }

    public Optional<Section> getExistsSectionOfSource(final Section requestSection) {
        for (Section section : sections) {
            if (section.getSource().equals(requestSection.getSource())) {
                return validateDistance(requestSection, section);
            }
        }
        return Optional.empty();
    }

    public Optional<Section> getExistsSectionOfTarget(final Section requestSection) {
        for (Section section : sections) {
            if (section.getTarget().equals(requestSection.getTarget())) {
                return validateDistance(requestSection, section);
            }
        }
        return Optional.empty();
    }

    public Optional<Section> combineSection(final Station station) {
        if (isTargetUpward(station) || isSourceDownward(station)) {
            return Optional.empty();
        }

        Station newSourceStation = null, newTargetStation = null;
        int distance = 0;
        for (Section section : sections) {
            if (section.getSource().equals(station)) {
                distance += section.getDistance();
                newTargetStation = section.getTarget();
            }
            if (section.getTarget().equals(station)) {
                distance += section.getDistance();
                newSourceStation = section.getSource();
            }
        }
        return Optional.of(new Section(newSourceStation, newTargetStation, distance));
    }

    private Optional<Section> validateDistance(final Section requestSection, final Section section) {
        final int distance = section.getDistance();
        final int requestDistance = requestSection.getDistance();
        if (requestDistance >= distance) {
            throw new IllegalArgumentException("거리가 너무 커서 역을 추가할 수 없습니다.");
        }
        return Optional.of(section);
    }

    private List<Station> sort(final List<Section> sections) {
        final Map<Station, Station> stationRelationShip = getStationRelationShip(sections);
        final Optional<Station> startStation = getStartStation(stationRelationShip);
        return startStation.map(station -> getSortedStations(stationRelationShip, station))
            .orElse(Collections.emptyList());
    }

    private List<Station> getSortedStations(final Map<Station, Station> stationRelationShip,
                                            final Station startStation) {
        final List<Station> sortedStations = new ArrayList<>();
        Optional<Station> nextStation = Optional.ofNullable(startStation);
        while (nextStation.isPresent()) {
            sortedStations.add(nextStation.get());
            nextStation = Optional.ofNullable(stationRelationShip.get(nextStation.get()));
        }
        return sortedStations;
    }

    private Map<Station, Station> getStationRelationShip(final List<Section> sections) {
        return sections.stream()
            .collect(Collectors.toMap(Section::getSource, Section::getTarget));
    }

    private Optional<Station> getStartStation(final Map<Station, Station> stationRelationShip) {
        return stationRelationShip.keySet().stream()
            .filter(station -> !stationRelationShip.containsValue(station))
            .findFirst();
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return stations;
    }
}
