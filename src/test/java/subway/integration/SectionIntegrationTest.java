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
import subway.controller.dto.SectionDeleteRequest;
import subway.controller.dto.SectionRequest;
import subway.domain.Line;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Sql("/truncate.sql")
public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    private Line line;

    @BeforeEach
    void setUpLineAndStation() {
        super.setUp();

        line = lineRepository.save(new Line(null, "1호선", null));
        stationRepository.save(new Station("강남역"));
        stationRepository.save(new Station("잠실역"));
        stationRepository.save(new Station("역삼역"));
        stationRepository.save(new Station("선릉역"));
        stationRepository.save(new Station("사당역"));
        stationRepository.save(new Station("서초역"));
    }

    @DisplayName("노선에 역을 추가한다.")
    @Test
    void createSectionSuccess() {
        SectionRequest sectionRequest = new SectionRequest(line.getId(), "강남역", "사당역", 10);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 가장 왼쪽에 역을 추가한다.")
    @Test
    void createSectionInLeft() {
        SectionRequest sectionRequest1 = new SectionRequest(line.getId(), "강남역", "사당역", 10);

        createSectionRequest(sectionRequest1);

        SectionRequest sectionRequest2 = new SectionRequest(line.getId(), "잠실역", "강남역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 가장 오른쪽에 역을 추가한다.")
    @Test
    void createSectionInRight() {
        SectionRequest sectionRequest1 = new SectionRequest(line.getId(), "강남역", "사당역", 10);

        createSectionRequest(sectionRequest1);

        SectionRequest sectionRequest2 = new SectionRequest(line.getId(), "사당역", "잠실역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 역과 역 사이에 왼쪽 역을 기준으로 역을 추가한다.")
    @Test
    void createSectionBetweenLeft() {
        SectionRequest sectionRequest1 = new SectionRequest(line.getId(), "강남역", "사당역", 10);

        createSectionRequest(sectionRequest1);

        SectionRequest sectionRequest2 = new SectionRequest(line.getId(), "강남역", "서초역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 역과 역 사이에 오른쪽 역을 기준으로 역을 추가한다.")
    @Test
    void createSectionBetweenRight() {
        SectionRequest sectionRequest1 = new SectionRequest(line.getId(), "강남역", "사당역", 10);

        createSectionRequest(sectionRequest1);

        SectionRequest sectionRequest2 = new SectionRequest(line.getId(), "서초역", "사당역", 5);

        ExtractableResponse<Response> response = createSectionRequest(sectionRequest2);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에 간선이 하나 있으면 모든 역을 제거한다.")
    @Test
    void deleteSectionSuccess() {
        // given
        SectionRequest createRequest = new SectionRequest(line.getId(), "강남역", "잠실역", 5);
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(line.getId(), "강남역");
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
        SectionRequest createRequest1 = new SectionRequest(line.getId(), "강남역", "잠실역", 5);
        SectionRequest createRequest2 = new SectionRequest(line.getId(), "잠실역", "선릉역", 5);
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(line.getId(), "강남역");
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
        SectionRequest createRequest1 = new SectionRequest(line.getId(), "강남역", "잠실역", 5);
        SectionRequest createRequest2 = new SectionRequest(line.getId(), "잠실역", "선릉역", 5);
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(line.getId(), "선릉역");
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
        SectionRequest createRequest1 = new SectionRequest(line.getId(), "강남역", "잠실역", 5);
        SectionRequest createRequest2 = new SectionRequest(line.getId(), "잠실역", "선릉역", 5);
        SectionDeleteRequest deleteRequest = new SectionDeleteRequest(line.getId(), "잠실역");
        createSectionRequest(createRequest1);
        createSectionRequest(createRequest2);

        // when
        ExtractableResponse<Response> response = createDeleteSectionRequest(deleteRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    // TODO 이름 바꾸기
    private ExtractableResponse<Response> createSectionRequest(SectionRequest sectionRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
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
