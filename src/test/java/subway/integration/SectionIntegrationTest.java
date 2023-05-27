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
import subway.entity.LineEntity;
import subway.entity.StationEntity;
import subway.ui.dto.request.SectionCreateRequest;
import subway.ui.dto.request.SectionDeleteRequest;

@Sql("/truncate.sql")
public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    private LineEntity lineEntity;

    @BeforeEach
    void setUpLineAndStation() {
        super.setUp();

        lineEntity = lineDao.save(new LineEntity(1L, "1호선"));
        stationDao.save(new StationEntity(1L, "강남역"));
        stationDao.save(new StationEntity(2L, "잠실역"));
        stationDao.save(new StationEntity(3L, "역삼역"));
        stationDao.save(new StationEntity(4L, "선릉역"));
        stationDao.save(new StationEntity(5L, "사당역"));
        stationDao.save(new StationEntity(6L, "서초역"));
    }

    @DisplayName("노선에 역을 추가한다.")
    @Test
    void createSectionSuccess() {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(lineEntity.getId(), "강남역", "사당역", 10);

        ExtractableResponse<Response> response = createSectionRequest(sectionCreateRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 가장 왼쪽에 역을 추가한다.")
    @Test
    void createSectionInLeft() {
        SectionCreateRequest sectionCreateRequest1 = new SectionCreateRequest(lineEntity.getId(), "강남역", "사당역", 10);

        createSectionRequest(sectionCreateRequest1);

        SectionCreateRequest sectionCreateRequest2 = new SectionCreateRequest(lineEntity.getId(), "잠실역", "강남역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionCreateRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 가장 오른쪽에 역을 추가한다.")
    @Test
    void createSectionInRight() {
        SectionCreateRequest sectionCreateRequest1 = new SectionCreateRequest(lineEntity.getId(), "강남역", "사당역", 10);

        createSectionRequest(sectionCreateRequest1);

        SectionCreateRequest sectionCreateRequest2 = new SectionCreateRequest(lineEntity.getId(), "사당역", "잠실역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionCreateRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 역과 역 사이에 왼쪽 역을 기준으로 역을 추가한다.")
    @Test
    void createSectionBetweenLeft() {
        SectionCreateRequest sectionCreateRequest1 = new SectionCreateRequest(lineEntity.getId(), "강남역", "사당역", 10);

        createSectionRequest(sectionCreateRequest1);

        SectionCreateRequest sectionCreateRequest2 = new SectionCreateRequest(lineEntity.getId(), "강남역", "서초역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionCreateRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 역과 역 사이에 오른쪽 역을 기준으로 역을 추가한다.")
    @Test
    void createSectionBetweenRight() {
        SectionCreateRequest sectionCreateRequest1 = new SectionCreateRequest(lineEntity.getId(), "강남역", "사당역", 10);

        createSectionRequest(sectionCreateRequest1);

        SectionCreateRequest sectionCreateRequest2 = new SectionCreateRequest(lineEntity.getId(), "서초역", "사당역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionCreateRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에 간선이 하나 있으면 모든 역을 제거한다.")
    @Test
    void deleteSectionSuccess() {
        // given
        SectionCreateRequest createRequest = new SectionCreateRequest(lineEntity.getId(), "강남역", "잠실역", 5);
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(lineEntity.getId(), "강남역");
        createSectionRequest(createRequest);

        // when
        ExtractableResponse<Response> response = createDeleteSectionRequest(deleteRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("상행 종점 역을 제거한다.")
    @Test
    void deleteSectionLastLeftStation() {
        // given
        SectionCreateRequest createRequest1 = new SectionCreateRequest(lineEntity.getId(), "강남역", "잠실역", 5);
        SectionCreateRequest createRequest2 = new SectionCreateRequest(lineEntity.getId(), "잠실역", "선릉역", 5);
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(lineEntity.getId(), "강남역");
        createSectionRequest(createRequest1);
        createSectionRequest(createRequest2);

        // when
        ExtractableResponse<Response> response = createDeleteSectionRequest(deleteRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("하행 종점 역을 제거한다.")
    @Test
    void deleteSectionLastRightStation() {
        // given
        SectionCreateRequest createRequest1 = new SectionCreateRequest(lineEntity.getId(), "강남역", "잠실역", 5);
        SectionCreateRequest createRequest2 = new SectionCreateRequest(lineEntity.getId(), "잠실역", "선릉역", 5);
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(lineEntity.getId(), "선릉역");
        createSectionRequest(createRequest1);
        createSectionRequest(createRequest2);

        // when
        ExtractableResponse<Response> response = createDeleteSectionRequest(deleteRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("역과 역 사이의 역을 제거한다.")
    @Test
    void deleteSectionBetweenStations() {
        // given
        SectionCreateRequest createRequest1 = new SectionCreateRequest(lineEntity.getId(), "강남역", "잠실역", 5);
        SectionCreateRequest createRequest2 = new SectionCreateRequest(lineEntity.getId(), "잠실역", "선릉역", 5);
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(lineEntity.getId(), "잠실역");
        createSectionRequest(createRequest1);
        createSectionRequest(createRequest2);

        // when
        ExtractableResponse<Response> response = createDeleteSectionRequest(deleteRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> createSectionRequest(SectionCreateRequest sectionCreateRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)
                .when().post("/sections")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createDeleteSectionRequest(SectionDeleteRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().delete("/sections")
                .then().log().all()
                .extract();
    }
}
