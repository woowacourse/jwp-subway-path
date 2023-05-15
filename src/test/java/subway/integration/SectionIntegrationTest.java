package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import subway.domain.Station;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@DisplayName("구간 관련 API 테스트")
@SuppressWarnings("NonAsciiCharacters")
class SectionIntegrationTest extends SubwayFixture {

    @Nested
    class 종점_기준으로_구간을_추가한다 {

        @Test
        void 상행_종점을_기준으로_구간을_추가한다() {
            // given
            final Long 역삼역 = stationDao.insert(new Station("역삼역")).getId();
            final SectionRequest request = new SectionRequest(10, 선릉역, 역삼역, 이호선);

            // when
            final ExtractableResponse<Response> response = 구간을_추가한다(request);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(response.header(HttpHeaders.LOCATION)).contains("/sections/")
            );
        }

        @Test
        void 하행_종점을_기준으로_구간을_추가한다() {
            // given
            final Long 건대입구 = stationDao.insert(new Station("건대입구")).getId();
            final SectionRequest request = new SectionRequest(10, 건대입구, 잠실역, 이호선);

            // when
            final ExtractableResponse<Response> response = 구간을_추가한다(request);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(response.header(HttpHeaders.LOCATION)).contains("/sections/")
            );
        }
    }

    @Nested
    class 노선_중간에_상행역_기준으로 {

        @Test
        void 구간을_추가을_추가할_수_있다() {
            // given
            final Long 건대입구 = stationDao.insert(new Station("건대입구")).getId();
            final SectionRequest request = new SectionRequest(7, 잠실새내역, 건대입구, 이호선);

            // when
            final ExtractableResponse<Response> response = 구간을_추가한다(request);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(response.header(HttpHeaders.LOCATION)).contains("/sections/")
            );
        }

        @ParameterizedTest(name = "거리 : {0}")
        @ValueSource(ints = {20, 21})
        void 구간을_추가할_때_기존_거리보다_크거나_같으면_추가할_수_없다(final int distance) {
            // given
            final Long 건대입구 = stationDao.insert(new Station("건대입구")).getId();
            final SectionRequest request = new SectionRequest(distance, 잠실새내역, 건대입구, 이호선);

            // when
            final ExtractableResponse<Response> response = 구간을_추가한다(request);

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }
    }

    @Nested
    class 노선_중간에_하행역_기준으로 {

        @Test
        void 구간을_추가할_수_있다() {
            // given
            final Long 건대입구 = stationDao.insert(new Station("건대입구")).getId();
            final SectionRequest request = new SectionRequest(7, 건대입구, 잠실새내역, 이호선);

            // when
            final ExtractableResponse<Response> response = 구간을_추가한다(request);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(response.header(HttpHeaders.LOCATION)).contains("/sections/")
            );
        }

        @ParameterizedTest(name = "거리 : {0}")
        @ValueSource(ints = {10, 11})
        void 구간을_추가할_때_기존_거리보다_크거나_같으면_추가할_수_없다(final int distance) {
            // given
            final Long 건대입구 = stationDao.insert(new Station("건대입구")).getId();
            final SectionRequest request = new SectionRequest(distance, 건대입구, 잠실새내역, 이호선);

            // when
            final ExtractableResponse<Response> response = 구간을_추가한다(request);

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }
    }

    private ExtractableResponse<Response> 구간을_추가한다(SectionRequest request) {
        return given()
                .body(request)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();
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
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }
}
