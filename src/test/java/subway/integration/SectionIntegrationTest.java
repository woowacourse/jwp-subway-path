package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dao.dto.LineDto;
import subway.domain.Station;
import subway.ui.dto.SectionRequest;

@Sql("/truncate.sql")
public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    private Long lineId;

    @BeforeEach
    void setUpLineAndStation() {
        super.setUp();

        lineId = lineDao.insert(new LineDto(null, "1호선"));
        stationDao.insert(new Station("강남역"));
        stationDao.insert(new Station("잠실역"));
        stationDao.insert(new Station("역삼역"));
        stationDao.insert(new Station("선릉역"));
        stationDao.insert(new Station("사당역"));
        stationDao.insert(new Station("서초역"));
    }

    @DisplayName("노선에 역을 추가한다.")
    @Test
    void createSectionSuccess() {
        SectionRequest sectionRequest = new SectionRequest(lineId, "강남역", "사당역", 10);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 가장 왼쪽에 역을 추가한다.")
    @Test
    void createSectionInLeft() {
        SectionRequest sectionRequest1 = new SectionRequest(lineId, "강남역", "사당역", 10);

        createSectionRequest(sectionRequest1);

        SectionRequest sectionRequest2 = new SectionRequest(lineId, "잠실역", "강남역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 가장 오른쪽에 역을 추가한다.")
    @Test
    void createSectionInRight() {
        SectionRequest sectionRequest1 = new SectionRequest(lineId, "강남역", "사당역", 10);

        createSectionRequest(sectionRequest1);

        SectionRequest sectionRequest2 = new SectionRequest(lineId, "사당역", "잠실역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 역과 역 사이에 왼쪽 역을 기준으로 역을 추가한다.")
    @Test
    void createSectionBetweenLeft() {
        SectionRequest sectionRequest1 = new SectionRequest(lineId, "강남역", "사당역", 10);

        createSectionRequest(sectionRequest1);

        SectionRequest sectionRequest2 = new SectionRequest(lineId, "강남역", "서초역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 역과 역 사이에 오른쪽 역을 기준으로 역을 추가한다.")
    @Test
    void createSectionBetweenRight() {
        SectionRequest sectionRequest1 = new SectionRequest(lineId, "강남역", "사당역", 10);

        createSectionRequest(sectionRequest1);

        SectionRequest sectionRequest2 = new SectionRequest(lineId, "서초역", "사당역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> createSectionRequest(SectionRequest sectionRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().log().all()
                .extract();
    }

//
//    @DisplayName("노선에 역을 제거한다.")
//    @Test
//    void deleteSectionSuccess() {
//        // given
//        Long lineId = 1L;
//        Long stationId = 2L;
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .queryParam("lineId", lineId)
//                .queryParam("stationId", stationId)
//                .when().delete("/sections")
//                .then().log().all()
//                .extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }
}
