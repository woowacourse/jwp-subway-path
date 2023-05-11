package subway.domain;

import subway.dto.AddResultDto;

import java.util.ArrayList;
import java.util.List;

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

        return new AddResultDto(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private static AddResultDto addInitStations(Station upStation, Station downStation, Distance distance, Line line) {
        Section section = new Section(upStation, downStation, distance, line);
        return new AddResultDto(
                List.of(section),
                List.of(),
                List.of(upStation, downStation)
        );
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

    private boolean isNonExistInSection(Station station) {
        return sections.stream()
                .noneMatch(section -> section.contains(station));
    }


}
