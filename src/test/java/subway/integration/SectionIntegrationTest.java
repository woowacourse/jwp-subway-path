package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.dao.LineDao;
import subway.line.domain.Line;
import subway.section.dao.SectionDao;
import subway.section.domain.Section;
import subway.section.dto.SectionCreateRequest;
import subway.section.dto.SectionDeleteRequest;
import subway.section.dto.SectionResponse;
import subway.station.dao.StationDao;
import subway.station.domain.Station;

public class SectionIntegrationTest extends IntegrationTest {

    private SectionCreateRequest sectionCreateRequest;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;

    private Long lineId;
    private Long stationId1;
    private Long stationId2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        final Station station1 = stationDao.insert(new Station("잠실역"));
        stationId1 = station1.getId();
        final Station station2 = stationDao.insert(new Station("선릉역"));
        stationId2 = station2.getId();

        final Line line = lineDao.insert(new Line("2호선", "초록색"));
        lineId = line.getId();


        sectionDao.insert(new Section(lineId, stationId1, stationId2, 4));

        sectionCreateRequest = new SectionCreateRequest(lineId, stationId1, stationId2, true, 3);
    }

    @DisplayName("노선에 역을 등록한다.")
    @Test
    void createSection() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)
                .when().post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);
        Assertions.assertAll(
                () -> assertThat(result.get(0).getId()).isPositive(),
                () -> assertThat(result.get(0).getLineId()).isEqualTo(lineId),
                () -> assertThat(result.get(0).getUpStationId()).isEqualTo(stationId2),
                () -> assertThat(result.get(0).getDownStationId()).isEqualTo(stationId1),
                () -> assertThat(result.get(0).getDistance()).isEqualTo(3)
        );
    }

    @DisplayName("노선에서 역을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        final SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest(lineId, stationId1);

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionDeleteRequest)
                .when().delete("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
