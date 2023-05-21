package subway.service.section.domain;

import subway.service.section.dto.AddResult;
import subway.service.section.dto.DeleteResult;
import subway.service.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public AddResult add(Station upStation, Station downStation, Distance distance) {
        validateIsEqualStations(upStation, downStation);
        if (sections.isEmpty()) {
            return addInitStations(upStation, downStation, distance);
        }
        validateStationExistence(upStation, downStation);
        Station upEndStation = findUpEndStation();
        Station downEndStation = findDownEndStation();

        // 상행 종점 추가
        if (downStation.equals(upEndStation)) {
            return addUpEndStation(upStation, downStation, distance);
        }
        // 하행 종점 추가
        if (upStation.equals(downEndStation)) {
            return addDownEndStation(upStation, downStation, distance);
        }
        return addStationInMiddle(upStation, downStation, distance);
    }

    public DeleteResult deleteSection(Station station) {
        List<Section> sectionsOfContainDeleteStation = sections.stream()
                .filter(section -> section.contains(station))
                .collect(Collectors.toList());

        if (sectionsOfContainDeleteStation.size() == 0) {
            throw new IllegalArgumentException("현재 노선에 존재하지 않는 역을 삭제할 수 없습니다.");
        }
        // 종점 제거
        if (sectionsOfContainDeleteStation.size() == 1) {
            return new DeleteResult(List.of(), sectionsOfContainDeleteStation, false);
        }

        // 중간 역 제거
        Section sectionOfDeleteStationIsDown = findSectionDeleteStationIsDownStation(station, sectionsOfContainDeleteStation);
        Section sectionOfDeleteStationIsUp = findSectionDeleteStationIsUpStation(station, sectionsOfContainDeleteStation);

        Distance combinedDistance = sectionOfDeleteStationIsDown.calcuateCombineDistance(sectionOfDeleteStationIsUp);

        Section newSection = new Section(sectionOfDeleteStationIsDown.getUpStation(), sectionOfDeleteStationIsUp.getDownStation(), combinedDistance);
        return new DeleteResult(List.of(newSection), List.of(sectionOfDeleteStationIsUp, sectionOfDeleteStationIsDown), false);
    }

    public List<Station> orderStations() {
        Station upEndStation = findUpwardEndStation();
        Map<Station, Station> stationPair = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));

        List<Station> stationsInOrder = new ArrayList<>();

        Station currentStation = upEndStation;
        while (hasUnvisitedStation(stationPair, stationsInOrder)) {
            stationsInOrder.add(currentStation);
            currentStation = stationPair.get(currentStation);
        }
        stationsInOrder.add(currentStation);
        return stationsInOrder;
    }

    private boolean hasUnvisitedStation(Map<Station, Station> stationPair, List<Station> stationsInOrder) {
        return stationsInOrder.size() != stationPair.size();
    }

    private Station findUpwardEndStation() {
        List<Station> upStations = findUpStations();
        List<Station> downStations = findDownStations();
        upStations.removeAll(downStations);

        boolean isUp = sections.stream()
                .anyMatch(section -> section.isUpStation(upStations.get(0)));
        if (isUp) {
            return upStations.get(0);
        }
        return upStations.get(1);
    }

    private Section findSectionDeleteStationIsUpStation(Station station, List<Section> sectionsOfContainDeleteStation) {
        return sectionsOfContainDeleteStation.stream()
                .filter(section -> section.isUpStation(station))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException("현재 section이 존재하지 않습니다.")
                );
    }

    private Section findSectionDeleteStationIsDownStation(Station station, List<Section> sectionsOfContainDeleteStation) {
        return sectionsOfContainDeleteStation.stream()
                .filter(section -> section.isDownStation(station))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException("현재 section이 존재하지 않습니다.")
                );
    }


    private void validateIsEqualStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("동일한 역 2개가 입력으로 들어왔습니다. 이름을 다르게 설정해주세요.");
        }
    }

    private AddResult addInitStations(Station upStation, Station downStation, Distance distance) {
        Section section = new Section(upStation, downStation, distance);
        return new AddResult(
                List.of(section),
                List.of(),
                List.of(upStation, downStation)
        );
    }

    private void validateStationExistence(Station upStation, Station downStation) {
        boolean upStationExistence = isExistInSection(upStation);
        boolean downStationExistence = isExistInSection(downStation);
        if (upStationExistence && downStationExistence) {
            throw new IllegalArgumentException("추가하려는 경로의 역들은 이미 노선에 존재하는 역들입니다.");
        }

        if (!upStationExistence && !downStationExistence) {
            throw new IllegalArgumentException("이미 노선에 역들이 존재하기 때문에 한 번에 새로운 역 2개를 추가할 수 없습니다.");
        }
    }

    private static AddResult addDownEndStation(Station upStation, Station downStation, Distance distance) {
        Section newDownEndStation = new Section(upStation, downStation, distance);
        return new AddResult(List.of(newDownEndStation), List.of(), List.of(downStation));
    }

    private AddResult addUpEndStation(Station upStation, Station downStation, Distance distance) {
        Section newUpEndStation = new Section(upStation, downStation, distance);
        return new AddResult(List.of(newUpEndStation), List.of(), List.of(upStation));
    }

    private AddResult addStationInMiddle(Station upStation, Station downStation, Distance distance) {

        Optional<Section> isExistUpStation = sections.stream()
                .filter(section -> section.contains(upStation))
                .findAny();
        // upstation이 기존 노선에 존재할 경우 하행 방향에 역 추가
        if (isExistUpStation.isPresent()) {
            Section originalSection = isExistUpStation.get();
            validateDistance(distance, originalSection);
            Distance newSectionDistance = originalSection.calculateNewSectionDistance(distance);
            Section newSectionUpward = new Section(upStation, downStation, distance);
            Section newSectionDownward = new Section(downStation, originalSection.getDownStation(), newSectionDistance);
            return new AddResult(List.of(newSectionUpward, newSectionDownward), List.of(originalSection), List.of(downStation));
        }
        // downstation이 기존 노선에 존재할 경우 상행 방향에 역 추가
        return addStationDownwardInMiddle(upStation, downStation, distance);
    }

    private AddResult addStationDownwardInMiddle(Station upStation, Station downStation, Distance distance) {
        Section originalDownSection = sections.stream()
                .filter(section -> section.contains(downStation))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이미 노선에 역들이 존재하기 때문에 한 번에 새로운 역 2개를 추가할 수 없습니다."));
        validateDistance(distance, originalDownSection);

        Distance newSectionDistance = originalDownSection.calculateNewSectionDistance(distance);
        Section newSectionUpward = new Section(upStation, downStation, distance);
        Section newSectionDownward = new Section(originalDownSection.getUpStation(), upStation, newSectionDistance);

        return new AddResult(List.of(newSectionDownward, newSectionUpward), List.of(originalDownSection), List.of(upStation));
    }

    private boolean isExistInSection(Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    private void validateDistance(Distance distance, Section originalSection) {
        if (originalSection.isSmaller(distance)) {
            throw new IllegalArgumentException("새로운 경로의 거리는 기존 경로보다 클 수 없습니다.");
        }
    }

    private Station findUpEndStation() {
        List<Station> upStations = findUpStations();
        List<Station> downStations = findDownStations();

        upStations.removeAll(downStations);
        return upStations.get(0);
    }

    private Station findDownEndStation() {
        List<Station> upStations = findUpStations();
        List<Station> downStations = findDownStations();

        downStations.removeAll(upStations);
        return downStations.get(0);
    }

    private List<Station> findDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private List<Station> findUpStations() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return sections;
    }
}
