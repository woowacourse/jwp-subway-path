package subway.fixture;

import subway.domain.vo.Distance;
import subway.domain.Section;
import subway.domain.Station;

public abstract class SectionFixture {

    public static Section 구간(final Distance distance, final Station upStation, final Station downStation) {
        return new Section(distance, false, upStation, downStation);
    }

    public static Section 상행_종점_구간(final Distance distance, final Station upStation, final Station downStation) {
        return new Section(distance, true, upStation, downStation);
    }
}
