package subway.domain.calculator;

import subway.domain.section.ContainingSections;
import subway.domain.section.Section;
import subway.domain.station.Station;

public class RemoveCalculator {

    private final ContainingSections containingSections;

    public RemoveCalculator(ContainingSections containingSections) {
        this.containingSections = containingSections;
    }

    // 중간역 제거 시에만 호출
    public Section calculateSectionToAdd(Station station) {
        return null;
    }
}
