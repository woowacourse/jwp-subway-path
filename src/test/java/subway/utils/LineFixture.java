package subway.utils;

import subway.line.domain.Line;
import subway.line.domain.MiddleSection;

import java.util.LinkedList;
import java.util.List;

import static subway.line.domain.SectionFixture.JAMSIL_TO_JAMSILNARU;
import static subway.line.domain.SectionFixture.SULLEUNG_TO_JAMSIL;
import static subway.utils.StationFixture.*;

public class LineFixture {

    public static final Line LINE_NUMBER_TWO = new Line(1L, "2호선", 0, new LinkedList<>(List.of(SULLEUNG_TO_JAMSIL, JAMSIL_TO_JAMSILNARU)));
    public static final Line LINE_NUMBER_THREE = new Line(2L, "3호선", 0, new LinkedList<>(List.of(
            new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, 10),
            new MiddleSection(JAMSIL_NARU_STATION, SULLEUNG_STATION, 97)
    )));
    public static final Line LINE_NUMBER_FOUR = new Line(3L, "4호선", 0, new LinkedList<>(List.of(
            new MiddleSection(JAMSIL_NARU_STATION, GANGNAM_STATION, 40),
            new MiddleSection(GANGNAM_STATION, SULLEUNG_STATION, 24)
    )));
}
