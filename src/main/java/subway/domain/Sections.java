package subway.domain;

import subway.dto.AddResultDto;

import java.util.ArrayList;
import java.util.List;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public AddResultDto add(Station upStation, Station downStation, Distance distance) {
        validateIsEqualStations(upStation, downStation);
        return new AddResultDto(new ArrayList<>(), new ArrayList<>(), new Station("aa"));
    }

    private void validateIsEqualStations(Station upStation, Station downStation) {
        if (upStation.isSame(downStation)) {
            throw new IllegalArgumentException("동일한 역 2개가 입력으로 들어왔습니다. 이름을 다르게 설정해주세요.");
        }
    }
}
