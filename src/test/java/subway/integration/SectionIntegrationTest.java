package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import subway.domain.Station;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("구간 관련 API 테스트")
@SuppressWarnings("NonAsciiCharacters")
class SectionIntegrationTest extends SubwayFixture {

    @Test
    void 하행_종점을_기준으로_구간을_추가한다() {
        // given
        final Long 역삼역 = stationDao.insert(new Station("역삼역")).getId();
        final SectionRequest request = new SectionRequest(10, 선릉역, 역삼역, 이호선);

        // when
        final ExtractableResponse<Response> response = given()
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
        final ExtractableResponse<Response> response = given()
                .body(request)
                .when()
                .delete("/sections/stations/{stationId}", 2L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
