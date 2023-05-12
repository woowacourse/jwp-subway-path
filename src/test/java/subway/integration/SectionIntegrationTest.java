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
import subway.dao.entity.SectionEntity;
import subway.domain.Line;
import subway.domain.Station;
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
    private StationDao stationDao;
    @Autowired
    private SectionDao sectionDao;
    private Long 이호선;
    private Long 잠실역;
    private Long 잠실새내역;

    @BeforeEach
    void init() {
        이호선 = lineDao.insert(new Line("2호선", "초록색"));
        잠실역 = stationDao.insert(new Station("잠실역")).getId();
        잠실새내역 = stationDao.insert(new Station("잠실새내역")).getId();
        sectionDao.insert(new SectionEntity(10, 잠실역, 잠실새내역, 이호선));
    }

    @Test
    void 구간을_추가한다() {
        // given
        final Long 삼성역 = stationDao.insert(new Station("삼성역")).getId();
        final SectionRequest request = new SectionRequest(10, 잠실새내역, 삼성역, 이호선);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).contains("/sections/")
        );
    }

    @Test
    void 구간을_삭제한다() {
        // given
        final SectionDeleteRequest request = new SectionDeleteRequest(1L);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .delete("/sections/stations/{stationId}", 2L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
