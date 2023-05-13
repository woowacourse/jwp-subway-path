package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;
import subway.dto.request.SectionRequest;
import subway.dto.response.SectionResponse;
import subway.dto.response.StationsResponse;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:/testData.sql")
@DisplayName("노선에 역 추가 / 삭제")
public class SectionIntegrationTest extends IntegrationTest {

    @DisplayName("[노선에 역 추가][정상] 이미 역들이 있는 비어있지 않은 노선에 역을 추가한다.")
    @Test
    void createStationInNotEmptyLine() {
        // given
        Long upBoundStationId = 3L;
        Long downBoundStationId = 4L;
        Long lindId = 1L;
        int distance = 4;

        SectionRequest sectionRequest = new SectionRequest(upBoundStationId, downBoundStationId, distance);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{line_id}/stations", lindId)
                .then()
                .log().all()
                .extract();

        SectionResponse bodyResponse = response.body().as(SectionResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(bodyResponse.getLineId()).isEqualTo(lindId);
        assertThat(bodyResponse.getUpBoundStationId()).isEqualTo(upBoundStationId);
        assertThat(bodyResponse.getDownBoundStationId()).isEqualTo(downBoundStationId);
    }

    @DisplayName("[노선에 역 추가][정상] 역들이 없는 비어있는 노선에 새로운 역 두개를 추가한다.")
    @Test
    void createTwoStationsInEmptyLine() {
        // given
        Long upBoundStationId = 1L;
        Long downBoundStationId = 5L;
        Long lindId = 3L;
        int distance = 30;

        SectionRequest sectionRequest = new SectionRequest(upBoundStationId, downBoundStationId, distance);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{line_id}/stations", lindId)
                .then()
                .log().all()
                .extract();

        SectionResponse bodyResponse = response.body().as(SectionResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(bodyResponse.getLineId()).isEqualTo(lindId);
        assertThat(bodyResponse.getUpBoundStationId()).isEqualTo(upBoundStationId);
        assertThat(bodyResponse.getDownBoundStationId()).isEqualTo(downBoundStationId);
    }

    @DisplayName("[노선에 역 추가][비정상] 원래의 노선길이보다 길거나 같은 역을 추가하면 상태코드가 BAD_REQUEST 이다.")
    @Test
    void createStationInNotEmptyLineWithInvalidDistance() {
        // given
        Long upBoundStationId = 1L;
        Long downBoundStationId = 4L;
        Long lindId = 1L;
        int distance = 10;

        SectionRequest sectionRequest = new SectionRequest(upBoundStationId, downBoundStationId, distance);
        ArrayList<Object> objects = new ArrayList<>();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{line_id}/stations", lindId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("[노선에 역 추가][비정상] 이미 역들이 있는 비어있지 않은 노선에 새로운 두개의 역을 추가하려하면 상태코드가 BAD REQUEST 이다.")
    @Test
    void createTwoStationsInNotEmptyLine() {
        // given
        Long upBoundStationId = 4L;
        Long downBoundStationId = 5L;
        Long lindId = 1L;
        int distance = 4;

        SectionRequest sectionRequest = new SectionRequest(upBoundStationId, downBoundStationId, distance);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{line_id}/stations", lindId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("[노선에 역 추가][비정상] 등록되지 않는 노선에 역을 추가하려하면 상태코드가 BAD_REQUEST 이다.")
    @Test
    void createStationInNotExistingLine() {
        // given
        Long upBoundStationId = 3L;
        Long downBoundStationId = 4L;
        Long lindId = 20L;
        int distance = 4;

        SectionRequest sectionRequest = new SectionRequest(upBoundStationId, downBoundStationId, distance);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{line_id}/stations", lindId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("[노선에 역 추가][비정상] 노선에 등록되지 않은 역을 추가하려하면 상태코드가 BAD_REQUEST 이다.")
    @Test
    void createNotExistingStationInLine() {
        // given
        Long upBoundStationId = 30L;
        Long downBoundStationId = 2L;
        Long lindId = 1L;
        int distance = 4;

        SectionRequest sectionRequest = new SectionRequest(upBoundStationId, downBoundStationId, distance);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{line_id}/stations", lindId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("[노선에 역 제거][정상] 상행종점역을 노선에서 삭제한다.")
    @Test
    void removeUpEndPointStationInLine() {
        // given
        Long lindId = 1L;
        Long stationId = 1L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{line_id}/stations/{station_id}", lindId, stationId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("[노선에 역 제거][정상] 하행종점역을 노선에서 삭제한다.")
    @Test
    void removeDownEndPointStationInLine() {
        // given
        Long lindId = 1L;
        Long stationId = 3L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{line_id}/stations/{station_id}", lindId, stationId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("[노선에 역 제거][정상] 중간역을 노선에서 삭제한다.")
    @Test
    void removeMiddleStationInLine() {
        // given
        Long lindId = 1L;
        Long stationId = 2L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{line_id}/stations/{station_id}", lindId, stationId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("[노선에 역 제거][비정상] 등록되어 있지 않은 역을 삭제하려하면 상태코드가 BAD_REQUEST 이다.")
    @Test
    void removeLineStationWithNotExisingId() {
        // given
        Long lindId = 1L;
        Long stationId = 30L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{line_id}/stations/{station_id}", lindId, stationId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Sql("classpath:/testData.sql")
    @DisplayName("[노선에 역 추가 후 조회][정상] 상행종점에 역을 추가한 뒤 조회하면 추가된 역이 조회된다.")
    @Test
    void createStationToUpEndPointAndGetLine() {
        // given
        Station[] expectedStations = {
                new Station(4L, "삼성"),
                new Station(1L, "잠실"),
                new Station(2L, "잠실새내"),
                new Station(3L, "종합운동장")
        };
        Long upBoundStationId = 4L;
        Long downBoundStationId = 1L;
        Long lindId = 1L;
        int distance = 30;

        SectionRequest sectionRequest = new SectionRequest(upBoundStationId, downBoundStationId, distance);

        // when
        ExtractableResponse<Response> createStation = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{line_id}/stations", lindId)
                .then()
                .log().all()
                .extract();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{line_id}", lindId)
                .then().log().all()
                .extract();

        StationsResponse bodyResponse = response.as(StationsResponse.class);

        // then
        assertThat(bodyResponse.getStations()).containsExactly(expectedStations);
    }

    @Sql("classpath:/testData.sql")
    @DisplayName("[노선에 역 추가 후 조회][정상] 하행종점에 역을 추가한 뒤 조회하면 추가된 역이 조회된다.")
    @Test
    void createStationToDownEndPointAndGetLine() {
        // given
        Station[] expectedStations = {
                new Station(1L, "잠실"),
                new Station(2L, "잠실새내"),
                new Station(3L, "종합운동장"),
                new Station(4L, "삼성")
        };
        Long upBoundStationId = 3L;
        Long downBoundStationId = 4L;
        Long lindId = 1L;
        int distance = 30;

        SectionRequest sectionRequest = new SectionRequest(upBoundStationId, downBoundStationId, distance);

        // when
        ExtractableResponse<Response> createStation = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{line_id}/stations", lindId)
                .then()
                .log().all()
                .extract();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{line_id}", lindId)
                .then().log().all()
                .extract();

        StationsResponse bodyResponse = response.as(StationsResponse.class);

        // then
        assertThat(bodyResponse.getStations()).containsExactly(expectedStations);
    }

    @Sql("classpath:/testData.sql")
    @DisplayName("[노선에 역 추가 후 조회][정상] 두 역 사이에 역을 추가한 뒤 조회하면 추가된 역이 조회된다.")
    @Test
    void createStationMiddleAndGetLine() {
        // given
        Station[] expectedStations = {
                new Station(1L, "잠실"),
                new Station(4L, "삼성"),
                new Station(2L, "잠실새내"),
                new Station(3L, "종합운동장")
        };
        Long upBoundStationId = 1L;
        Long downBoundStationId = 4L;
        Long lindId = 1L;
        int distance = 6;

        SectionRequest sectionRequest = new SectionRequest(upBoundStationId, downBoundStationId, distance);

        // when
        ExtractableResponse<Response> createStation = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{line_id}/stations", lindId)
                .then()
                .log().all()
                .extract();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{line_id}", lindId)
                .then().log().all()
                .extract();

        StationsResponse bodyResponse = response.as(StationsResponse.class);

        // then
        assertThat(bodyResponse.getStations()).containsExactly(expectedStations);
    }
}
