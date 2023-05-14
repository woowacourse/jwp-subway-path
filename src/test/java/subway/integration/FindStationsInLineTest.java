package subway.integration;

import org.junit.jupiter.api.BeforeEach;

public class FindStationsInLineTest  extends IntegrationTestSetUp {

    @BeforeEach
    void init() {
        super.setUp();
    }

    /*@Test
    @DisplayName("기존 노선에 존재하는 모든 역을 조회한다")
    void findLineById() {
        // given
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
        String location = response.header("Location");
        Long lineId = Long.parseLong(location.split("/")[2]);

        Station newStation = stationDao.insert(new Station("충무로"));
        AddStationToLineRequest addRequest = new AddStationToLineRequest(initialUpStation.getId(),
                newStation.getId(), 5);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addRequest)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // when
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addRequest)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", is(1))
                .body("lineName", is("3호선"))
                .body("stations[0].stationName", is("경복궁"))
                .body("stations[1].stationName", is("충무로"))
                .body("stations[2].stationName", is("안국"));
    }

    @Test
    @DisplayName("모든 노선의 모든 역을 조회한다")
    void findAllLines() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all();

        Station upStation = stationDao.insert(new Station("잠실"));
        Station downStation = stationDao.insert(new Station("잠실새내"));
        LineCreateRequest createRequest2 = new LineCreateRequest("2호선", "잠실", "잠실새내", 5);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest2)
                .when().post("/lines")
                .then().log().all();

        // when
        RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

    }*/
}
