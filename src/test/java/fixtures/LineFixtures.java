package fixtures;

import subway.dto.LineFindResponse;

import java.util.List;

public class LineFixtures {

    public static final String LINE2_NAME = "2호선";
    public static final String LINE7_NAME = "7호선";
    public static final LineFindResponse LINE2_노선도 = new LineFindResponse(LINE2_NAME, List.of("잠실역", "강변역", "건대역"));
    public static final LineFindResponse LINE7_노선도 = new LineFindResponse(LINE7_NAME, List.of("온수역", "대림역", "논현역", "장암역"));

    public static final List<LineFindResponse> ALL_노선도 = List.of(LINE2_노선도, LINE7_노선도);
}
