package subway.acceptance.line;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.common.JsonMapper.toJson;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.UUID;
import org.springframework.core.ParameterizedTypeReference;
import subway.line.application.dto.LineQueryResponse;
import subway.line.application.dto.LineQueryResponse.SectionQueryResponse;
import subway.line.presentation.request.LineCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class LineSteps {

    public static ExtractableResponse<Response> 노선_생성_요청(
            final String lineName
    ) {
        final LineCreateRequest request = new LineCreateRequest(lineName);
        return 노선_생성_요청(request);
    }

    public static ExtractableResponse<Response> 노선_생성_요청(final LineCreateRequest request) {
        final String body = toJson(request);
        return given().log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/lines")
                .then()
                .log().all()
                .extract();
    }

    public static UUID 노선_생성하고_아이디_반환(
            final String lineName
    ) {
        final LineCreateRequest request = new LineCreateRequest(lineName);
        final ExtractableResponse<Response> response = 노선_생성_요청(request);
        return 응답_헤더에_담긴_노선_아이디(response);
    }

    public static UUID 응답_헤더에_담긴_노선_아이디(final ExtractableResponse<Response> response) {
        final String location = response.header("location");
        final String id = location.substring(location.lastIndexOf("/") + 1);
        return UUID.fromString(id);
    }

    public static ExtractableResponse<Response> 노선_조회_요청(final UUID id) {
        return given().log().all()
                .when()
                .get("/lines/{id}", id)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_전체_조회_요청() {
        return given().log().all()
                .when()
                .get("/lines")
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

    public static List<LineQueryResponse> 노선_전체_조회_결과(final ExtractableResponse<Response> response) {
        return response.as(
                new ParameterizedTypeReference<List<LineQueryResponse>>() {
                }.getType()
        );
    }
}
