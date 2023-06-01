package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subway.Distance;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("구간 관련 API 테스트")
@SuppressWarnings("NonAsciiCharacters")
class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private StationDao stationDao;

    private Long 이호선;

    @BeforeEach
    void init() {
        Station station1 = new Station(1L, "잠실역1");
        Station station2 = new Station(2L, "잠실역2");
        Station station3 = new Station(3L, "잠실역3");

        stationDao.insert(station1);
        stationDao.insert(station2);
        stationDao.insert(station3);

        이호선 = lineDao.insert(new Line("2호선", "초록색"));
        sectionDao.insert(new Section(new Distance(10), station1, station2, 이호선));
    }

    @Test
    void 구간을_추가한다() {
        // given
        final SectionRequest request = new SectionRequest(10, 2L, 3L, 이호선);

        // when
        Response response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/sections")
                .then().log().all()
                .extract().response();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).contains("/sections/")
        );
    }

    @Test
    void 구간을_삭제한다() {
        // given
        final SectionDeleteRequest request = new SectionDeleteRequest(1L,2L);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .delete("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
