package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

public class IntegrationTestFixture {

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
        List<StationResponse> stations = Arrays.stream(stationName).map(StationResponse::new)
                .collect(Collectors.toList());
        return new LineResponse(lineName, stations);
    }
}
