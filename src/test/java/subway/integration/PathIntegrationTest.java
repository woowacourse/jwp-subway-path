package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.*;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.LineIntegrationTest.*;


@DisplayName("지하철 역 사이 최단 거리 조회 관련 기능")
public class PathIntegrationTest extends IntegrationTest{

    Long 판교역ID;
    Long 선릉역ID;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> 신분당선 = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600"));
        ExtractableResponse<Response> 호선2 = 지하철_노선_생성_요청(new LineRequest("2호선", "bg-green-600"));
        Long 신분당선ID = Long.parseLong(parseUri(신분당선.header("Location")));
        Long 호선2ID = Long.parseLong(parseUri(호선2.header("Location")));

        ExtractableResponse<Response> 판교역 = StationIntegrationTest.지하철_역_생성_요청(new StationRequest("판교역"));
        ExtractableResponse<Response> 양재역 = StationIntegrationTest.지하철_역_생성_요청(new StationRequest("양재역"));
        ExtractableResponse<Response> 도곡역 = StationIntegrationTest.지하철_역_생성_요청(new StationRequest("도곡역"));
        ExtractableResponse<Response> 강남역 = StationIntegrationTest.지하철_역_생성_요청(new StationRequest("강남역"));
        ExtractableResponse<Response> 선릉역 = StationIntegrationTest.지하철_역_생성_요청(new StationRequest("선릉역"));
        판교역ID = Long.parseLong(parseUri(판교역.header("Location")));
        Long 양재역ID = Long.parseLong(parseUri(양재역.header("Location")));
        Long 도곡역ID = Long.parseLong(parseUri(도곡역.header("Location")));
        Long 강남역ID = Long.parseLong(parseUri(강남역.header("Location")));
        선릉역ID = Long.parseLong(parseUri(선릉역.header("Location")));


        /*
         * 판교 -10- 양재 -10- 도곡 -10- 선릉 = 30
         * 판교 -10- 양재 -5- 강남 -5- 선릉 = 20
         * */

        지하철_노선에_역_등록_요청(신분당선ID, new LineStationRequest(판교역ID, 양재역ID, 10));
        지하철_노선에_역_등록_요청(신분당선ID, new LineStationRequest(양재역ID, 도곡역ID, 10));
        지하철_노선에_역_등록_요청(신분당선ID, new LineStationRequest(도곡역ID, 선릉역ID, 10));
        지하철_노선에_역_등록_요청(신분당선ID, new LineStationRequest(양재역ID, 강남역ID, 5));
        지하철_노선에_역_등록_요청(호선2ID, new LineStationRequest(강남역ID, 선릉역ID, 5));
    }

    @DisplayName("역 간의 최단 거리와 요금, 거리 정보를 조회한다.")
    @Test
    void findShortestPath() {
        // given
        ShortestPathRequest shortestPathRequest = new ShortestPathRequest(판교역ID, 선릉역ID);

        // when
        ExtractableResponse<Response> pathResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("srcStationId", shortestPathRequest.getSrcStationId())
                .param("dstStationId", shortestPathRequest.getDstStationId())
                .when().get("/path")
                .then().log().all()
                .extract();

        // then
        ShortestPathResponse shortestPathResponse = pathResponse.as(ShortestPathResponse.class);
        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(shortestPathResponse.getPath())
                .extracting(StationResponse::getName)
                .containsExactly("판교역", "양재역", "강남역", "선릉역");
        assertThat(shortestPathResponse.getDistance()).isEqualTo(20);
        assertThat(shortestPathResponse.getFare()).isEqualTo(1450);
    }
}
