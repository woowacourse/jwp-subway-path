package subway.utils;

import subway.domain.DownTerminalSection;
import subway.domain.MiddleSection;
import subway.domain.UpTerminalSection;

import static subway.utils.StationFixture.*;

public class SectionFixture {

    public static final int DISTANCE = 5;
    public static final UpTerminalSection TERMINAL_TO_JAMSIL = new UpTerminalSection(JAMSIL_STATION);
    public static final UpTerminalSection TERMINAL_TO_JAMSIL_NARU = new UpTerminalSection(JAMSIL_NARU_STATION);
    public static final MiddleSection SULLEUNG_TO_JAMSIL = new MiddleSection(SULLEUNG_STATION, JAMSIL_STATION, DISTANCE);
    public static final MiddleSection JAMSIL_TO_JAMSILNARU = new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, DISTANCE);
    public static final MiddleSection JAMSIL_NARU_TO_JAMSIL = new MiddleSection(JAMSIL_NARU_STATION, JAMSIL_STATION, DISTANCE);
    public static final DownTerminalSection JAMSIL_TO_TERMINAL = new DownTerminalSection(JAMSIL_STATION);
    public static final DownTerminalSection JAMSILNARU_TO_TERMINAL = new DownTerminalSection(JAMSIL_NARU_STATION);
}
