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
            final String 노선,
            final int 추가비용
    ) {
        final LineCreateRequest request = new LineCreateRequest(노선, 추가비용);
        return 노선_생성_요청(request);
    }

    public static ExtractableResponse<Response> 노선_생성_요청(final LineCreateRequest 노선_생성_요청) {
        final String body = toJson(노선_생성_요청);
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
            final String 노선,
            final int 추가비용
    ) {
        final LineCreateRequest request = new LineCreateRequest(노선, 추가비용);
        final ExtractableResponse<Response> response = 노선_생성_요청(request);
        return 응답_헤더에_담긴_노선_아이디(response);
    }

    public static UUID 응답_헤더에_담긴_노선_아이디(final ExtractableResponse<Response> 노선_생성_응답) {
        final String location = 노선_생성_응답.header("location");
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

    public static void 단일_노선의_이름을_검증한다(final ExtractableResponse<Response> 노선_조회_응답, final String 노선) {
        final LineQueryResponse result = 노선_조회_응답.as(LineQueryResponse.class);
        단일_노선의_이름을_검증한다(result, 노선);
    }

    public static void 단일_노선의_이름을_검증한다(final LineQueryResponse 노선_조회_응답, final String 노선) {
        assertThat(노선_조회_응답.getLineName()).isEqualTo(노선);
    }

    public static void 노선에_포함된_N번째_구간을_검증한다(
            final ExtractableResponse<Response> 노선_조회_응답,
            final int 순서,
            final String 상행역,
            final String 하행역,
            final int 거리
    ) {
        final LineQueryResponse result = 노선_조회_응답.as(LineQueryResponse.class);
        노선에_포함된_N번째_구간을_검증한다(result, 순서, 상행역, 하행역, 거리);
    }

    public static void 노선에_포함된_N번째_구간을_검증한다(
            final LineQueryResponse 노선_조회_응답,
            final int 순서,
            final String 상행역,
            final String 하행역,
            final int 거리
    ) {
        final List<SectionQueryResponse> responses = 노선_조회_응답.getStationQueryResponseList();
        assertThat(responses.get(순서).getUpStationName()).isEqualTo(상행역);
        assertThat(responses.get(순서).getDownStationName()).isEqualTo(하행역);
        assertThat(responses.get(순서).getDistance()).isEqualTo(거리);
    }

    public static List<LineQueryResponse> 노선_전체_조회_결과(final ExtractableResponse<Response> 노선_전체_조회_결과) {
        return 노선_전체_조회_결과.as(
                new ParameterizedTypeReference<List<LineQueryResponse>>() {
                }.getType()
        );
    }

    public static void 단일_노선의_가격을_검증한다(final ExtractableResponse<Response> 노선_조회_응답, final int 추가_비용) {
        final LineQueryResponse result = 노선_조회_응답.as(LineQueryResponse.class);
        단일_노선의_가격을_검증한다(result, 추가_비용);
    }

    public static void 단일_노선의_가격을_검증한다(final LineQueryResponse 노선_조회_결과, final int 추가_비용) {
        assertThat(노선_조회_결과.getSurcharge()).isEqualTo(추가_비용);
    }
}
