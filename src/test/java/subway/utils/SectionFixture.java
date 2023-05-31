package subway.utils;

import subway.domain.Section;
import subway.domain.Sections;

import java.util.ArrayList;
import java.util.List;

import static subway.utils.StationFixture.*;

public class SectionFixture {

    public static final int DISTANCE = 5;
    public static final Section SULLEUNG_TO_JAMSIL = new Section(SULLEUNG_STATION, JAMSIL_STATION, DISTANCE);
    public static final Section JAMSIL_TO_JAMSILNARU = new Section(JAMSIL_STATION, JAMSILNARU_STATION, DISTANCE);
    public static final Sections SULLEUNG_JAMSIL_JAMSILNARU_SECTIONS = new Sections(new ArrayList<>(List.of(SULLEUNG_TO_JAMSIL, JAMSIL_TO_JAMSILNARU)));
}
