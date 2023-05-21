package subway.integration.path;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.line.LineSteps.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import subway.dto.line.LineCreateRequest;
import subway.dto.path.ShortestPathSelectResponse;
import subway.dto.station.StationSaveRequest;
import subway.dto.station.StationSelectResponse;
import subway.integration.IntegrationTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
public class PathIntegrationTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void 최단경로를_조회한다() {
        // given
        final ExtractableResponse<Response> createResponse1 = 노선_생성_요청(new LineCreateRequest("2호선", "서초역", "교대역", 7));
        Long lineId1 = Long.parseLong(createResponse1.header("Location").split("/")[2]);
        노선에_역_등록_요청(lineId1, new StationSaveRequest("교대역", "강남역", 12));
        final StationSelectResponse 역삼역 = 노선에_역_등록_요청(lineId1, new StationSaveRequest("강남역", "역삼역", 8)).as(
                StationSelectResponse.class);
        노선에_역_등록_요청(lineId1, new StationSaveRequest("역삼역", "선릉역", 12));

        final ExtractableResponse<Response> createResponse2 = 노선_생성_요청(new LineCreateRequest("3호선", "신사역", "잠원역", 15));
        Long lineId2 = Long.parseLong(createResponse2.header("Location").split("/")[2]);
        노선에_역_등록_요청(lineId2, new StationSaveRequest("잠원역", "고속터미널역", 9));
        노선에_역_등록_요청(lineId2, new StationSaveRequest("고속터미널역", "교대역", 12));

        final ExtractableResponse<Response> createResponse3 = 노선_생성_요청(new LineCreateRequest("9호선", "고속터미널역", "사평역", 8));
        Long lineId3 = Long.parseLong(createResponse3.header("Location").split("/")[2]);
        final StationSelectResponse 신논현역 = 노선에_역_등록_요청(lineId3, new StationSaveRequest("사평역", "신논현역", 11)).as(
                StationSelectResponse.class);

        final ExtractableResponse<Response> createResponse4 = 노선_생성_요청(new LineCreateRequest("신분당선", "신사역", "논현역", 7));
        Long lineId4 = Long.parseLong(createResponse4.header("Location").split("/")[2]);
        노선에_역_등록_요청(lineId4, new StationSaveRequest("논현역", "신논현역", 8));
        노선에_역_등록_요청(lineId4, new StationSaveRequest("신논현역", "강남역", 9));
        
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("sourceStationId", 역삼역.getId())
                .queryParam("targetStationId", 신논현역.getId())
                .when().get("/path")
                .then().log().all()
                .extract();
        final ShortestPathSelectResponse pathSelectResponse = response.as(ShortestPathSelectResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(pathSelectResponse.getPath()).map(StationSelectResponse::getName)
                .containsExactly("역삼역", "강남역", "신논현역");
        assertThat(pathSelectResponse.getDistance()).isEqualTo(17);
        assertThat(pathSelectResponse.getFare()).isEqualTo(1_450);
    }
}
