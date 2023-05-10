package subway.utils;

import subway.domain.Section;

import static subway.utils.StationFixture.JAMSIL_NARU_STATION;
import static subway.utils.StationFixture.JAMSIL_STATION;

public class SectionFixture {

    public static final int DISTANCE = 5;
    public static final Section JAMSIL_TO_JAMSILNARU = new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, DISTANCE);
}
