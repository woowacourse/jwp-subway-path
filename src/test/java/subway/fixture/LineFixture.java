package subway.fixture;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

import java.util.List;

public abstract class LineFixture {

    public static Line 노선(final String 노선명, final String 노선_색상) {
        return new Line(노선명, 노선_색상, new Sections());
    }

    public static Line 노선(final String 노선명, final String 노선_색상, final List<Section> 구간_목록) {
        return new Line(노선명, 노선_색상, Sections.from(구간_목록));
    }

    public static Line 노선(final String 노선명, final String 노선_색상, final Sections 구간_목록) {
        return new Line(노선명, 노선_색상, 구간_목록);
    }
}
