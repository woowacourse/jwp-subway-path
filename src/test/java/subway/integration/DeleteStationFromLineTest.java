package subway.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import subway.dao.StationDao;

public class DeleteStationFromLineTest extends IntegrationTestSetUp {

    @BeforeEach
    void init() {
        super.setUp();
    }

    @Autowired
    public StationDao stationDao;

    /*@DisplayName("기존 노선에 존재하는 역을 제거한다.")
    @Test
    void deleteStationFromLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
        String location = createResponse.header("Location");
        Long lineId = Long.parseLong(location.split("/")[2]);

        Station newStation = stationDao.insert(new Station("충무로"));
        AddStationToLineRequest addRequest = new AddStationToLineRequest(initialUpStation.getId(), newStation.getId(),
                5);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addRequest)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", lineId, newStation.getId())
                .then().log().all().
                extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }*/
}
