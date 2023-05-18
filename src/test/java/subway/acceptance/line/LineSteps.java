package subway.acceptance.line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.CommonSteps.doDelete;
import static subway.acceptance.CommonSteps.doGet;
import static subway.acceptance.CommonSteps.doPost;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.ui.line.dto.in.LineAddStationRequest;
import subway.ui.line.dto.in.LineCreateRequest;
import subway.ui.line.dto.in.LineResponse;
import subway.ui.line.dto.in.LinesResponse;

@SuppressWarnings("NonAsciiCharacters")
public class LineSteps {

    public static ExtractableResponse<Response> 노선_생성_요청(final String 노선명,
            final String 색상,
            final Long 상행역_ID,
            final Long 하행역_ID,
            final Long 거리) {
        return doPost("/lines", new LineCreateRequest(노선명, 색상, 상행역_ID, 하행역_ID, 거리));
    }

    public static ExtractableResponse<Response> 노선_목록_조회_요청() {
        return doGet("/lines");
    }

    public static ExtractableResponse<Response> 노선에서_id_조회_요청(final Long 노선_ID) {
        return doGet("/lines/" + 노선_ID);
    }

    public static void 노선_목록_크기_비교(final ExtractableResponse<Response> 노선_목록_조회_요청_결과, final int 크기) {
        assertThat(노선_목록_조회_요청_결과.as(LinesResponse.class).getLines()).hasSize(크기);
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(final Long 노선_ID) {
        return doDelete("/lines/" + 노선_ID);
    }

    public static long 노선_생성_결과에서_id_가져오기(final ExtractableResponse<Response> 노선_생성_요청_결과) {
        return 노선_생성_요청_결과.as(LineResponse.class).getId();
    }

    public static ExtractableResponse<Response> 노선에서_역_추가_요청(final Long 노선_ID, final Long 상행선_역_ID, final Long 하행선_역_ID,
            final Long 새_역_ID, final Long 거리) {
        return doPost("/lines/" + 노선_ID, new LineAddStationRequest(상행선_역_ID, 하행선_역_ID, 새_역_ID, 거리));
    }
}
