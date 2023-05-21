package subway.integration.line;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.common.JsonMapper.toJson;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import subway.dto.request.LineCreateRequest;
import subway.dto.response.LineQueryResponse;
import subway.dto.response.SectionQueryResponse;
import subway.dto.response.ShortestPathResponse;

@SuppressWarnings("NonAsciiCharacters")
public class LineSteps {

    private static final String API_URL = "/lines";

    public static ExtractableResponse<Response> 노선_생성_요청(
            final String lineName,
            final String upTerminalName,
            final String downTerminalName,
            final Integer distance,
            final Integer additionalFee
    ) {
        final LineCreateRequest request = new LineCreateRequest(lineName, upTerminalName, downTerminalName, distance,additionalFee);
        return 노선_생성_요청(request);
    }

    public static ExtractableResponse<Response> 노선_생성_요청(final LineCreateRequest request) {
        final String body = toJson(request);
        return given().log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .extract();
    }

    public static Long 노선_생성하고_아이디_반환(
            final String lineName,
            final String upTerminalName,
            final String downTerminalName,
            final Integer distance,
            final Integer additionalFee
    ) {
        final LineCreateRequest request = new LineCreateRequest(lineName, upTerminalName, downTerminalName, distance,additionalFee);
        final ExtractableResponse<Response> response = 노선_생성_요청(request);
        return 응답_헤더에_담긴_노선_아이디(response);
    }

    public static Long 응답_헤더에_담긴_노선_아이디(final ExtractableResponse<Response> response) {
        final String location = response.header("location");
        final String id = location.substring(location.lastIndexOf("/") + 1);
        return Long.parseLong(id);
    }

    public static ExtractableResponse<Response> 노선_조회_요청(final Long id) {
        return given().log().all()
                .when()
                .get(API_URL + "/{id}", id)
                .then()
                .log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 노선_전체_조회_요청() {
        return given().log().all()
                .when()
                .get(API_URL)
                .then()
                .log().all()
                .extract();
    }

    public static void 단일_노선의_이름을_검증한다(final ExtractableResponse<Response> response, final String lineName) {
        final LineQueryResponse result = response.as(LineQueryResponse.class);
        단일_노선의_이름을_검증한다(result, lineName);
    }

    public static void 단일_노선의_이름을_검증한다(final LineQueryResponse response, final String lineName) {
        assertThat(response.getLineName()).isEqualTo(lineName);
    }

    public static void 노선에_포함된_N번째_구간을_검증한다(
            final ExtractableResponse<Response> response,
            final int index,
            final String upStationName,
            final String downStationName,
            final int distance
    ) {
        final LineQueryResponse result = response.as(LineQueryResponse.class);
        노선에_포함된_N번째_구간을_검증한다(result, index, upStationName, downStationName, distance);
    }

    public static void 노선에_포함된_N번째_구간을_검증한다(
            final LineQueryResponse response,
            final int index,
            final String upStationName,
            final String downStationName,
            final int distance
    ) {
        final List<SectionQueryResponse> responses = response.getStationQueryResponseList();
        assertThat(responses.get(index).getUpStationName()).isEqualTo(upStationName);
        assertThat(responses.get(index).getDownStationName()).isEqualTo(downStationName);
        assertThat(responses.get(index).getDistance()).isEqualTo(distance);
    }

    public static void 노선에_포함된_N번째_구간을_검증한다(
            final ShortestPathResponse response,
            final int index,
            final String upStationName,
            final String downStationName,
            final int distance
    ) {
        final List<SectionQueryResponse> responses = response.getSectionQueryResponses();
        assertThat(responses.get(index).getUpStationName()).isEqualTo(upStationName);
        assertThat(responses.get(index).getDownStationName()).isEqualTo(downStationName);
        assertThat(responses.get(index).getDistance()).isEqualTo(distance);
    }
}
