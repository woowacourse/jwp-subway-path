package subway.domain;

import subway.dto.AddResultDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public AddResultDto add(Station upStation, Station downStation, Distance distance, Line line) {
        validateIsEqualStations(upStation, downStation);
        if (sections.isEmpty()) {
            return addInitStations(upStation, downStation, distance, line);
        }
        validateIsNonExistStations(upStation, downStation);
        Station upEndStation = findUpEndStation();
        Station downEndStation = findDownEndStation();

        // 상행 종점 추가
        if (downStation.equals(upEndStation)) {
            return addUpEndStation(upStation, downStation, distance, line);
        }
        // 하행 종점 추가
        if (upStation.equals(downEndStation)) {
            return addDownEndStation(upStation, downStation, distance, line);
        }
        return addStationInMiddle(upStation, downStation, distance, line);
    }

    private void validateIsEqualStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("동일한 역 2개가 입력으로 들어왔습니다. 이름을 다르게 설정해주세요.");
        }
    }

    private void validateIsNonExistStations(Station upStation, Station downStation) {
        if (isNonExistInSection(upStation) && isNonExistInSection(downStation)) {
            throw new IllegalArgumentException("이미 노선에 역들이 존재하기 때문에 한 번에 새로운 역 2개를 추가할 수 없습니다.");
        }
    }

    private static AddResultDto addDownEndStation(Station upStation, Station downStation, Distance distance, Line line) {
        Section newDownEndStation = new Section(upStation, downStation, distance, line);
        return new AddResultDto(List.of(newDownEndStation), List.of(), List.of(downStation));
    }

    private AddResultDto addUpEndStation(Station upStation, Station downStation, Distance distance, Line line) {
        Section newUpEndStation = new Section(upStation, downStation, distance, line);
        return new AddResultDto(List.of(newUpEndStation), List.of(), List.of(upStation));
    }

    private AddResultDto addInitStations(Station upStation, Station downStation, Distance distance, Line line) {
        Section section = new Section(upStation, downStation, distance, line);
        return new AddResultDto(
                List.of(section),
                List.of(),
                List.of(upStation, downStation)
        );
    }

    private AddResultDto addStationInMiddle(Station upStation, Station downStation, Distance distance, Line line) {
        Optional<Section> isExistUpStation = sections.stream()
                .filter(section -> section.contains(upStation))
                .findAny();
        // upstation이 기존 노선에 존재할 경우 하행 방향에 역 추가
        if (isExistUpStation.isPresent()) {
            Section originalSection = isExistUpStation.get();
            Distance newSectionDistance = originalSection.calculateNewSectionDistance(distance);
            Section newSectionUpward = new Section(upStation, downStation, distance, line);
            Section newSectionDownward = new Section(downStation, originalSection.getDownStation(), newSectionDistance, line);
            return new AddResultDto(List.of(newSectionUpward, newSectionDownward), List.of(originalSection), List.of(downStation));
        }
        // downstation이 기존 노선에 존재할 경우 상행 방향에 역 추가
        return addStationDownwardInMiddle(upStation, downStation, distance, line);
    }

    private AddResultDto addStationDownwardInMiddle(Station upStation, Station downStation, Distance distance, Line line) {
        Section originalDownSection = sections.stream()
                .filter(section -> section.contains(downStation))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이미 노선에 역들이 존재하기 때문에 한 번에 새로운 역 2개를 추가할 수 없습니다."));

        Distance newSectionDistance = originalDownSection.calculateNewSectionDistance(distance);
        Section newSectionUpward = new Section(upStation, downStation, distance, line);
        Section newSectionDownward = new Section(originalDownSection.getUpStation(), upStation, newSectionDistance, line);

        return new AddResultDto(List.of(newSectionDownward, newSectionUpward), List.of(originalDownSection), List.of(upStation));
    }

    private boolean isNonExistInSection(Station station) {
        return sections.stream()
                .noneMatch(section -> section.contains(station));
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

}
