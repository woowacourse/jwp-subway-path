package subway.common.fixture;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import subway.dto.LineRequest;
import subway.dto.StationRequest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class WebFixture {

    public static final LineRequest 일호선_남색_요청 = new LineRequest("일호선", "남색");
    public static final LineRequest 이호선_초록색_요청 = new LineRequest("이호선", "초록색");

    public static final StationRequest 후추_요청 = new StationRequest("후추");
    public static final StationRequest 디노_요청 = new StationRequest("디노");
    public static final StationRequest 조앤_요청 = new StationRequest("조앤");
    public static final StationRequest 로운_요청 = new StationRequest("로운");

}
