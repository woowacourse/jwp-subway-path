package subway.fixture;

import subway.domain.vo.Distance;
import subway.domain.Section;
import subway.domain.Station;

public abstract class SectionFixture {

    public static Section 구간(final Distance 거리, final Station 상행역, final Station 하행역) {
        return new Section(거리, false, 상행역, 하행역);
    }

    public static Section 상행_종점_구간(final Distance 거리, final Station 상행역, final Station 하행역) {
        return new Section(거리, true, 상행역, 하행역);
    }
}
