package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.dto.SectionAddRequest;
import subway.dto.StationAddRequest;
import subway.dto.StationDeleteRequest;
import subway.dto.StationResponse;

@SuppressWarnings("NonAsciiCharacters")
public class IntegrationTestFixture {

    public static void 비정상_요청이라는_응답인지_검증한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 정상_응답인지_검증한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 정상_생성이라는_응답인지_검증한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotEmpty();
    }

    public static void 반환값이_없는지_검증한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 리소스를_찾을_수_없다는_응답인지_검증한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static ExtractableResponse<Response> 노선_생성_요청(String lineName, String source, String target, int distance) {
        LineRequest request = new LineRequest(lineName, source, target, distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 단일_노선_조회_요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }

    public static void 노선_조회_결과(ExtractableResponse<Response> response, LineResponse lineResponse) {
        assertThat(response.as(LineResponse.class)).usingRecursiveComparison()
                .isEqualTo(lineResponse);
    }

    public static LineResponse 노선_정보(String lineName, String... stationName) {
        List<StationResponse> stations = Arrays.stream(stationName)
                .map(StationResponse::new)
                .collect(Collectors.toList());
        return new LineResponse(lineName, stations);
    }

    public static ExtractableResponse<Response> 전체_노선_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static void 노선_전체_조회_결과를_확인한다(ExtractableResponse<Response> response, LineResponse... lineResponses) {
        List<LineResponse> 전체_노선_정보 = Arrays.stream(lineResponses)
                .collect(Collectors.toList());
        assertThat(response.jsonPath().getList(".", LineResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(전체_노선_정보);
    }

    public static ExtractableResponse<Response> 구간_추가_요청(Long lineId, String source, String target, int distance) {
        SectionAddRequest request = new SectionAddRequest(source, target, distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 역_삭제_요청(Long lineId, String stationName) {
        StationDeleteRequest request = new StationDeleteRequest(stationName);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().delete("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 최단_거리_조회_요청(String source, String target) {
        PathRequest request = new PathRequest(source, target);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().get("/lines/paths")
                .then().log().all()
                .extract();
    }


    public static void 최단_거리_정보를_확인한다(ExtractableResponse<Response> response, int fee, int distance,
                                      String... station) {
        List<StationResponse> stations = Arrays.stream(station).map(StationResponse::new)
                .collect(Collectors.toList());
        assertThat(response.as(PathResponse.class)).usingRecursiveComparison()
                .isEqualTo(new PathResponse(stations, distance, fee));
    }

    public static ExtractableResponse<Response> 역_생성_요청(String stationName) {
        StationAddRequest request = new StationAddRequest(stationName);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 역_삭제_요청(Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{stationId}", id)
                .then().log().all()
                .extract();
    }
}
