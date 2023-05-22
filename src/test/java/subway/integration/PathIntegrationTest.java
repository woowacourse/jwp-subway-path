package subway.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.AddStationLocation;
import subway.controller.dto.request.AddInitStationToLineRequest;
import subway.controller.dto.request.AddStationToLineRequest;
import subway.controller.dto.response.FindShortestPathResponse;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.StationInformationResponse;
import subway.controller.dto.response.StationResponse;

class PathIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("같은 경로를 공유하는 두 호선에서 도착점과 끝점이 그 경로 위에 있을 때, 최단 경로를 찾는다.")
    void testFindShortestPath() {
        //given
        final LineResponse lineResponse1 = saveLine1().as(LineResponse.class);
        final LineResponse lineResponse2 = saveLine2().as(LineResponse.class);

        final StationResponse stationResponse1 = saveStation1().as(StationResponse.class);
        final StationResponse stationResponse2 = saveStation2().as(StationResponse.class);
        final StationResponse stationResponse3 = saveStation3().as(StationResponse.class);

        final int age = 10;

        saveInitStationToLine(new AddInitStationToLineRequest(lineResponse1.getName(), stationResponse1.getName(),
            stationResponse2.getName(), 10L));
        saveAdditionalStationToLine(
            new AddStationToLineRequest(AddStationLocation.BOTTOM, lineResponse1.getName(), stationResponse3.getName(),
                stationResponse1.getName(), stationResponse2.getName(), 10L));

        saveInitStationToLine(new AddInitStationToLineRequest(lineResponse2.getName(), stationResponse1.getName(),
            stationResponse2.getName(), 1L));
        saveAdditionalStationToLine(
            new AddStationToLineRequest(AddStationLocation.BOTTOM, lineResponse2.getName(), stationResponse3.getName(),
                stationResponse1.getName(), stationResponse2.getName(), 1L));

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/path/shortest?startStationName=" + stationResponse1.getName() + "&endStationName="
                + stationResponse3.getName() + "&age=" + age)
            .then().log().all()
            .extract();

        //then
        final FindShortestPathResponse findShortestPathResponse = response.as(FindShortestPathResponse.class);

        final StationInformationResponse stationInformationResponse1 = new StationInformationResponse(
            stationResponse1.getId(), stationResponse1.getName(), 6L, lineResponse2.getName(),
            lineResponse2.getColor());
        final StationInformationResponse stationInformationResponse2 = new StationInformationResponse(
            stationResponse2.getId(), stationResponse2.getName(), 6L, lineResponse2.getName(),
            lineResponse2.getColor());
        final StationInformationResponse stationInformationResponse3 = new StationInformationResponse(
            stationResponse3.getId(), stationResponse3.getName(), 6L, lineResponse2.getName(),
            lineResponse2.getColor());

        final List<StationInformationResponse> expectedStationInformationResponses = List.of(
            stationInformationResponse1,
            stationInformationResponse2,
            stationInformationResponse3
        );
        final FindShortestPathResponse expectedResponse = new FindShortestPathResponse(
            expectedStationInformationResponses, 2L, 720L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findShortestPathResponse.getTotalCost()).isEqualTo(expectedResponse.getTotalCost());
        assertThat(findShortestPathResponse.getTotalDistance()).isEqualTo(expectedResponse.getTotalDistance());

        final List<StationInformationResponse> stationInformations = findShortestPathResponse.getStationInformations();
        for (int index = 0; index < stationInformations.size(); index++) {
            final StationInformationResponse stationInformationResponse = stationInformations.get(index);
            final StationInformationResponse expectedStationInformationResponse = expectedStationInformationResponses.get(
                index);
            assertThat(stationInformationResponse).extracting("stationId", "stationName", "lineName", "lineColor")
                .containsExactly(
                    expectedStationInformationResponse.getStationId(),
                    expectedStationInformationResponse.getStationName(),
                    expectedStationInformationResponse.getLineName(),
                    expectedStationInformationResponse.getLineColor()
                );
        }
    }
}
