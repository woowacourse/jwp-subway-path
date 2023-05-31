package subway.utils;

import subway.domain.Line;
import subway.domain.LineName;

import java.util.LinkedList;
import java.util.List;

import static subway.utils.SectionFixture.JAMSIL_TO_JAMSILNARU;
import static subway.utils.SectionFixture.SULLEUNG_TO_JAMSIL;

public class LineFixture {

    public static final Line LINE_NUMBER_TWO = new Line(1L, new LineName("2호선"), new LinkedList(List.of(SULLEUNG_TO_JAMSIL, JAMSIL_TO_JAMSILNARU)));
}
