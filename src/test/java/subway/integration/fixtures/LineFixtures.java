package subway.integration.fixtures;

import subway.dto.request.LineRequest;
import subway.dto.response.StationResponse;

import java.util.Collections;
import java.util.List;

public class LineFixtures {

    public static final LineRequest 일_호선_남색_추가요금_100원 = new LineRequest("1호선", "남색", 100);
    public static final LineRequest 일_호선_초록색_추가요금_100원 = new LineRequest("1호선", "초록색", 100);
    public static final LineRequest 이_호선_남색_추가요금_100원 = new LineRequest("2호선", "남색", 100);
    public static final LineRequest 이_호선_초록색_추가요금_100원 = new LineRequest("2호선", "초록색", 100);
    public static final List<StationResponse> 빈_역_목록 = Collections.emptyList();
    public static final int 존재하지_않는_노선_아이디 = 5959;
}
