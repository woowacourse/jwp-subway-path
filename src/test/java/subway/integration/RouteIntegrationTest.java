package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineStationAddRequest;
import subway.dto.LineStationInitRequest;
import subway.dto.RouteRequest;
import subway.dto.RouteResponse;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@SuppressWarnings("NonAsciiCharacters")
public class RouteIntegrationTest extends IntegrationTest {

    private LineRequest line2Request = new LineRequest("2호선", "초록"); //1
    private LineRequest line3Request = new LineRequest("3호선", "주황"); //2
    private LineRequest line4Request = new LineRequest("4호선", "하늘"); //3
    private LineRequest line7Request = new LineRequest("7호선", "올리브"); //4

    private List<LineRequest> lineRequests = List.of(line2Request, line3Request, line4Request, line7Request);
    private List<StationRequest> stationRequests = List.of("낙성대", "사당", "이수", "내방", "고속터미널", "방배", "잠원", "교대").stream()
            .map(name -> new StationRequest(name))
            .collect(Collectors.toList());

    @Test
    @DisplayName("최단 경로를 조회한다")
    void create() {
        //given
        lineRequests.forEach(this::노선_생성);
        stationRequests.forEach(this::역_생성);
        //2호선
        노선에_초기_구간_추가(1,
                new LineStationInitRequest("낙성대", "사당", 5));
        노선에_구간_추가(1,
                new LineStationAddRequest("사당", "교대", "UP", 5));
        노선에_구간_추가(1,
                new LineStationAddRequest("교대", "방배", "UP", 5));
        //3호선
        노선에_초기_구간_추가(2,
                new LineStationInitRequest("잠원", "고속터미널", 5));
        노선에_구간_추가(2,
                new LineStationAddRequest("고속터미널", "교대", "UP", 500));
        //4호선
        노선에_초기_구간_추가(3,
                new LineStationInitRequest("사당", "이수", 5));
        //7호선
        노선에_초기_구간_추가(4,
                new LineStationInitRequest("이수", "내방", 5));
        노선에_구간_추가(4,
                new LineStationAddRequest("내방", "고속터미널", "UP", 5));

        RouteRequest routeRequest = new RouteRequest("낙성대", "고속터미널");

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(routeRequest)
                .when().get("subway/route")
                .then().log().all().
                extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        RouteResponse resultResponse = response.as(RouteResponse.class);
        assertThat(resultResponse.getRoute().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList()))
                .isEqualTo(List.of("낙성대", "사당", "이수", "내방", "고속터미널"));
        assertThat(resultResponse.getDistance()).isEqualTo(20);
    }


    private void 노선_생성(LineRequest lineRequest) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
    }

    private void 역_생성(StationRequest stationRequest) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().log().all().
                extract();
    }

    private void 노선에_초기_구간_추가(int lineId, LineStationInitRequest lineStationInitRequest) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineStationInitRequest)
                .when().post("/lines/" + lineId + "/stations/init")
                .then().log().all().
                extract();
    }

    private void 노선에_구간_추가(int lineId, LineStationAddRequest lineStationAddRequest) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineStationAddRequest)
                .when().post("/lines/" + lineId + "/stations")
                .then().log().all().
                extract();
    }

}
