package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.LineCreateRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationRequest;
import subway.service.LineService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@Transactional
public class LineIntegrationTest extends IntegrationTest {

    @Autowired
    private LineService lineService;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private Stations stations;
    private Lines lines;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
        stations = lineService.getStations();
        lines = lineService.getLines();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        //given
        stations.addStation(new Station("역삼역", new Distance(10)));
        stations.addStation(new Station("선릉역", new Distance(0)));

        //when
        LineCreateRequest lineCreateRequest = new LineCreateRequest(lineRequest1, new StationRequest("역삼역", "선릉역", 10));

        ExtractableResponse<Response> response = RestAssured.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(lineCreateRequest).when().post("/lines").then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

//    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
//    @Test
//    void createLineWithDuplicateName() {
//        // given
//        RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest1)
//                .when().post("/lines")
//                .then().log().all().
//                extract();
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest1)
//                .when().post("/lines")
//                .then().log().all().
//                extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        Station station = new Station("역삼역", new Distance(10));
        stations.addStation(station);
        Station nextStation = new Station("선릉역", new Distance(0));
        stations.addStation(nextStation);

        //when
        LineCreateRequest lineCreateRequest = new LineCreateRequest(lineRequest1, new StationRequest("역삼역", "선릉역", 10));

        LineCreateRequest lineCreateRequest2 = new LineCreateRequest(lineRequest2, new StationRequest("역삼역", "선릉역", 10));

        lines.addLine(new Line(lineRequest1.getName(), lineRequest1.getColor(), new Stations(List.of(station, nextStation))));
        lines.addLine(new Line(lineRequest2.getName(), lineRequest2.getColor(), new Stations(List.of(station, nextStation))));


        ExtractableResponse<Response> createResponse1 = RestAssured.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(lineCreateRequest).when().post("/lines").then().log().all().extract();

        ExtractableResponse<Response> createResponse2 = RestAssured.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(lineCreateRequest2).when().post("/lines").then().log().all().extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all().accept(MediaType.APPLICATION_JSON_VALUE).when().get("/lines").then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).when().get("/lines").then().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given

        Station station = new Station("역삼역", new Distance(10));
        stations.addStation(station);
        Station nextStation = new Station("선릉역", new Distance(0));
        stations.addStation(nextStation);

        lines.addLine(new Line(lineRequest1.getName(), lineRequest1.getColor(), new Stations(List.of(station, nextStation))));

        //when
        LineCreateRequest lineCreateRequest =
                new LineCreateRequest(lineRequest1, new StationRequest("역삼역", "선릉역", 10));

        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(1);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Station station = new Station("역삼역", new Distance(10));
        stations.addStation(station);
        Station nextStation = new Station("선릉역", new Distance(0));
        stations.addStation(nextStation);
        Station thirdStation = new Station("강남역", new Distance(3));
        stations.addStation(thirdStation);

        LineCreateRequest lineCreateRequest = new LineCreateRequest(lineRequest1, new StationRequest("역삼역", "선릉역", 10));
        LineCreateRequest lineCreateRequest2 = new LineCreateRequest(lineRequest1, new StationRequest("강남역", "선릉역", 10));

        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest2)
                .when().put("/lines/{lineId}", 1)
                .then().log().all()
                .extract();


        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

//    @DisplayName("지하철 노선을 제거한다.")
//    @Test
//    void deleteLine() {
//        // given
//        ExtractableResponse<Response> createResponse = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest1)
//                .when().post("/lines")
//                .then().log().all().
//                extract();
//
//        // when
//        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .when().delete("/lines/{lineId}", lineId)
//                .then().log().all()
//                .extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }
}
