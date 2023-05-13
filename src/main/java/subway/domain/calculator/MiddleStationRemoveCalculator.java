package subway.domain.calculator;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class MiddleStationRemoveCalculator {

    private final Sections sections;

    public MiddleStationRemoveCalculator(Sections sections) {
        this.sections = sections;
    }

    // 중간역 제거 시에만 호출
    public Section calculateSectionToAdd(Station station) {
        return null;
    }
}
