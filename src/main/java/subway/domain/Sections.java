package subway.domain;

import static java.util.Collections.EMPTY_LIST;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addInitialSection(Station upStation, Station downStation, Distance newDistance) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("구간은 서로 다른 두 역으로 이뤄져 있어야 합니다.");
        }
        sections.add(new Section(upStation, downStation, newDistance));
    }

    public void addAdditionalSection(Station baseStation, Station newStation, Direction directionOfBase,
            Distance newDistance) {
        validateSectionToAdd(baseStation, newStation, directionOfBase, newDistance);

        AddStationStrategy addStationStrategy = directionOfBase.findAddStrategy();
        addStationStrategy.addNewStationToSection(sections, baseStation, newStation, newDistance);
    }

    private void validateSectionToAdd(Station baseStation, Station newStation, Direction directionOfBase,
            Distance newDistance) {
        if (!isStationInSections(baseStation)) {
            throw new IllegalArgumentException("기준역이 존재하지 않습니다");
        }
        if (isStationInSections(newStation)) {
            throw new IllegalArgumentException("추가하려는 역이 이미 존재합니다");
        }
        if (!isExistingSectionIsLongerThanNewSection(baseStation, directionOfBase, newDistance)) {
            throw new IllegalArgumentException("새로운 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
        }
    }

    private boolean isStationInSections(Station station) {
        return sections.stream()
                .anyMatch(section -> section.hasStationInSection(station));
    }

    private boolean isExistingSectionIsLongerThanNewSection(Station baseStation, Direction directionOfBase,
            Distance newDistance) {
        Optional<Section> sectionToRevise = findSectionIncludingStationOnDirection(baseStation, directionOfBase);
        return sectionToRevise
                .map(section -> section.isLongerThan(newDistance))
                .orElse(false);
    }

    public List<Station> findAllStations() {
        if (sections.isEmpty()) {
            return EMPTY_LIST;
        }
        Station startStation = findFirstStation();
        List<Station> stations = new LinkedList<>(List.of(startStation));
        while (stations.size() <= sections.size()) {
            sections.stream()
                    .filter(section -> stations.get(stations.size() - 1).equals(section.getUpStation()))
                    .findFirst()
                    .ifPresent(section -> stations.add(section.getDownStation()));
        }
        return stations;
    }

    private Station findFirstStation() {
        Map<Station, Station> upAndDownStation = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        Set<Station> stations = upAndDownStation.keySet();
        stations.removeAll(upAndDownStation.values());
        return stations.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("상행 종점을 찾을 수 없습니다"));
    }

    public List<Section> findSectionsByStation(Station station) {
        List<Section> sectionWithStation = sections.stream()
                .filter(section -> section.hasStationInSection(station))
                .collect(Collectors.toList());
        if(sectionWithStation.isEmpty()) {
            throw new IllegalArgumentException("해당 역이 존재하지 않습니다.");
        }
        return sectionWithStation;
    }

    public void removeStation(Station stationToRemove) {
        if (!isStationInSections(stationToRemove)) {
            throw new IllegalArgumentException("삭제하려는 역이 존재하지 않습니다");
        }
        Optional<Section> upSectionOpt = findSectionIncludingStationOnDirection(stationToRemove, Direction.DOWN);
        Optional<Section> downSectionOpt = findSectionIncludingStationOnDirection(stationToRemove, Direction.UP);

        if (upSectionOpt.isPresent() && downSectionOpt.isPresent()) {
            Section upSection = upSectionOpt.get();
            Section downSection = downSectionOpt.get();
            Distance newDistance = upSection.getDistance().plus(downSection.getDistance());
            sections.removeAll(List.of(upSection, downSection));
            sections.add(new Section(upSection.getUpStation(), downSection.getDownStation(), newDistance));
            return;
        }
        upSectionOpt.ifPresent(sections::remove);
        downSectionOpt.ifPresent(sections::remove);
    }

    private Optional<Section> findSectionIncludingStationOnDirection(Station baseStation, Direction direction) {
        return sections.stream()
                .filter(section -> section.isStationOnDirection(baseStation, direction))
                .findFirst();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

}
