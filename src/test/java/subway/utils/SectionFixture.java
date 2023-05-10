package subway.utils;

import subway.domain.Section;

import static subway.utils.StationFixture.*;

public class SectionFixture {

    public static final int DISTANCE = 5;
    public static final Section SULLEUNG_TO_JAMSIL = new Section(SULLEUNG_STATION, JAMSIL_STATION, DISTANCE);
    public static final Section JAMSIL_TO_JAMSILNARU = new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, DISTANCE);
}
