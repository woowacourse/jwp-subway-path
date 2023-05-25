package subway.line.domain;

import static subway.utils.StationFixture.*;

public class SectionFixture {

    public static final int DISTANCE = 5;
    public static final UpstreamTerminalSection TERMINAL_TO_JAMSIL = new UpstreamTerminalSection(JAMSIL_STATION);
    public static final UpstreamTerminalSection TERMINAL_TO_JAMSIL_NARU = new UpstreamTerminalSection(JAMSIL_NARU_STATION);

    public static final MiddleSection SULLEUNG_TO_JAMSIL = new MiddleSection(SULLEUNG_STATION, JAMSIL_STATION, DISTANCE);
    public static final MiddleSection JAMSIL_TO_JAMSILNARU = new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, DISTANCE);
    public static final MiddleSection JAMSIL_NARU_TO_JAMSIL = new MiddleSection(JAMSIL_NARU_STATION, JAMSIL_STATION, DISTANCE);

    public static final DownstreamTerminalSection JAMSIL_TO_TERMINAL = new DownstreamTerminalSection(JAMSIL_STATION);
    public static final DownstreamTerminalSection JAMSILNARU_TO_TERMINAL = new DownstreamTerminalSection(JAMSIL_NARU_STATION);
}
