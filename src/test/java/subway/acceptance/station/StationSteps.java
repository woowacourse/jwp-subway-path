package subway.acceptance.station;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.CommonSteps.doDelete;
import static subway.acceptance.CommonSteps.doGet;
import static subway.acceptance.CommonSteps.doPost;
import static subway.acceptance.CommonSteps.doPut;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.ui.station.dto.in.StationCreateRequest;
import subway.ui.station.dto.in.StationInfoResponse;
import subway.ui.station.dto.in.StationInfosResponse;
import subway.ui.station.dto.in.StationUpdateInfoRequest;

public class StationSteps {

    public static ExtractableResponse<Response> 역_생성_요청(final String 역_이름) {
        return doPost("/stations", new StationCreateRequest(역_이름));
    }

    public static ExtractableResponse<Response> 역_제거_요청(final Long 역_ID) {
        return doDelete("/stations/" + 역_ID);
    }

    public static ExtractableResponse<Response> 역_목록_조회_요청() {
        return doGet("/stations");
    }

    public static void 역_목록_크기_비교(final ExtractableResponse<Response> 역_목록_조회_요청_결과, final int 크기) {
        assertThat(역_목록_조회_요청_결과.as(StationInfosResponse.class).getStations()).hasSize(크기);
    }

    public static ExtractableResponse<Response> 역_id_조회_요청(final Long 역_ID) {
        return doGet("/stations/" + 역_ID);
    }

    public static long 역_생성_결과에서_id_가져오기(final ExtractableResponse<Response> 역_생성_요청_결과) {
        return 역_생성_요청_결과.as(StationInfoResponse.class).getId();
    }

    public static void 역_이름_비교(final ExtractableResponse<Response> 역_생성_요청_결과, final String 역_이름) {
        assertThat(역_생성_요청_결과.as(StationInfoResponse.class).getName()).isEqualTo(역_이름);
    }

    public static ExtractableResponse<Response> 역_정보_변경_요청(final Long 역_ID, final String 역_이름) {
        return doPut("/stations/" + 역_ID, new StationUpdateInfoRequest(역_이름));
    }

}