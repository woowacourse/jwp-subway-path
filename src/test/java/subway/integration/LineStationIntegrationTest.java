package subway.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.AddStationLocation;
import subway.controller.dto.request.AddInitStationToLineRequest;
import subway.controller.dto.request.AddStationToLineRequest;
import subway.controller.dto.request.RemoveStationOnLineRequest;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.StationResponse;

class LineStationIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("노선에 초기 지하철을 추가한다.")
    void testAddInitStationToLine() {
        //given
        final LineResponse lineResponse = saveLine1().as(LineResponse.class);

        final StationResponse stationResponse1 = saveStation1().as(StationResponse.class);
        final StationResponse stationResponse2 = saveStation2().as(StationResponse.class);

        //when
        final AddInitStationToLineRequest request = new AddInitStationToLineRequest(
            lineResponse.getName(), stationResponse1.getName(), stationResponse2.getName(),
            10L);
        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/init")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선의 맨 위에 지하철을 추가한다.")
    void testAddStationToLineOnTop() {
        //given
        final LineResponse lineResponse = saveLine1().as(LineResponse.class);

        final StationResponse stationResponse1 = saveStation1().as(StationResponse.class);
        final StationResponse stationResponse2 = saveStation2().as(StationResponse.class);
        final StationResponse stationResponse3 = saveStation3().as(StationResponse.class);

        saveInitStationToLine(new AddInitStationToLineRequest(lineResponse.getName(), stationResponse1.getName(),
            stationResponse2.getName(), 10L));

        final AddStationToLineRequest request = new AddStationToLineRequest(AddStationLocation.TOP,
            lineResponse.getName(), stationResponse3.getName(),
            stationResponse1.getName(), stationResponse2.getName(), 10L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선의 맨 아래에 지하철을 추가한다.")
    void testAddStationToLineOnBottom() {
        //given
        final LineResponse lineResponse = saveLine1().as(LineResponse.class);

        final StationResponse stationResponse1 = saveStation1().as(StationResponse.class);
        final StationResponse stationResponse2 = saveStation2().as(StationResponse.class);
        final StationResponse stationResponse3 = saveStation3().as(StationResponse.class);

        saveInitStationToLine(new AddInitStationToLineRequest(lineResponse.getName(), stationResponse1.getName(),
            stationResponse2.getName(), 10L));

        final AddStationToLineRequest request = new AddStationToLineRequest(AddStationLocation.BOTTOM,
            lineResponse.getName(), stationResponse3.getName(),
            stationResponse1.getName(), stationResponse2.getName(), 10L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선의 사이에 지하철을 추가한다.")
    void testAddStationToLineOnBetween() {
        //given
        final LineResponse lineResponse = saveLine1().as(LineResponse.class);

        final StationResponse stationResponse1 = saveStation1().as(StationResponse.class);
        final StationResponse stationResponse2 = saveStation2().as(StationResponse.class);
        final StationResponse stationResponse3 = saveStation3().as(StationResponse.class);

        saveInitStationToLine(new AddInitStationToLineRequest(lineResponse.getName(), stationResponse1.getName(),
            stationResponse2.getName(), 10L));

        final AddStationToLineRequest request = new AddStationToLineRequest(AddStationLocation.BETWEEN,
            lineResponse.getName(), stationResponse3.getName(),
            stationResponse1.getName(), stationResponse2.getName(), 5L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선에서 지하철을 삭제한다.")
    void testRemoveStationOnLine() {
        //given
        final LineResponse lineResponse = saveLine1().as(LineResponse.class);

        final StationResponse stationResponse1 = saveStation1().as(StationResponse.class);
        final StationResponse stationResponse2 = saveStation2().as(StationResponse.class);
        final StationResponse stationResponse3 = saveStation3().as(StationResponse.class);

        saveInitStationToLine(new AddInitStationToLineRequest(lineResponse.getName(), stationResponse1.getName(),
            stationResponse2.getName(), 10L));

        saveAdditionalStationToLine(
            new AddStationToLineRequest(AddStationLocation.BETWEEN, lineResponse.getName(), stationResponse3.getName(),
                stationResponse1.getName(), stationResponse2.getName(), 10L));
        
        final RemoveStationOnLineRequest request = new RemoveStationOnLineRequest(lineResponse.getName(),
            stationResponse2.getName());

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/line/station")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
