package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.step.LineStep.노선_생성_요청;
import static subway.integration.step.LineStep.노선_생성_요청을_생성한다;
import static subway.integration.step.PathStep.경로_조회_요청;
import static subway.integration.step.PathStep.경로_조회_요청을_생성한다;
import static subway.integration.step.SectionStep.구간_생성_요청;
import static subway.integration.step.SectionStep.구간_생성_요청을_생성한다;
import static subway.integration.step.StationStep.역_생성_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("경로 관련 기능 테스트")
class PathIntegrationTest extends IntegrationTest {
    private static void 이호선을_생성한다() {
        노선_생성_요청(노선_생성_요청을_생성한다("2호선"));
        역_생성_요청("낙성대역");
        역_생성_요청("사당역");
        역_생성_요청("방배역");
        역_생성_요청("서초역");
        역_생성_요청("교대역");
        구간_생성_요청(구간_생성_요청을_생성한다(1L, 2L, 2), 1L);
        구간_생성_요청(구간_생성_요청을_생성한다(2L, 3L, 6), 1L);
        구간_생성_요청(구간_생성_요청을_생성한다(3L, 4L, 4), 1L);
        구간_생성_요청(구간_생성_요청을_생성한다(4L, 5L, 7), 1L);
    }

    private static void 사호선을_생성한다() {
        노선_생성_요청(노선_생성_요청을_생성한다("4호선"));
        역_생성_요청("동작역");
        역_생성_요청("총신대입구역");
        역_생성_요청("남태령역");
        구간_생성_요청(구간_생성_요청을_생성한다(6L, 2L, 20), 2L);
        구간_생성_요청(구간_생성_요청을_생성한다(2L, 7L, 30), 2L);
        구간_생성_요청(구간_생성_요청을_생성한다(7L, 6L, 20), 2L);
        구간_생성_요청(구간_생성_요청을_생성한다(7L, 8L, 35), 2L);
    }

    private static void 칠호선을_생성한다() {
        노선_생성_요청(노선_생성_요청을_생성한다("7호선"));
        역_생성_요청("고속터미널역");
        역_생성_요청("남부터미널역");
        구간_생성_요청(구간_생성_요청을_생성한다(9L, 5L, 5), 3L);
        구간_생성_요청(구간_생성_요청을_생성한다(5L, 10L, 8), 3L);
    }

    @Test
    void 올바른_요청으로_환승하지_않는_경로를_조회한다() {
        // given
        이호선을_생성한다();

        final Long 낙성대_ID = 1L;
        final Long 서초_ID = 4L;

        // when
        ExtractableResponse<Response> response = 경로_조회_요청(경로_조회_요청을_생성한다(낙성대_ID, 서초_ID));

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("path")).hasSize(4),
                () -> assertThat(response.jsonPath().getString("path[0].stationName")).isEqualTo("낙성대역"),
                () -> assertThat(response.jsonPath().getString("path[1].stationName")).isEqualTo("사당역"),
                () -> assertThat(response.jsonPath().getString("path[2].stationName")).isEqualTo("방배역"),
                () -> assertThat(response.jsonPath().getString("path[3].stationName")).isEqualTo("서초역"),
                () -> assertThat(response.jsonPath().getInt("totalDistance")).isEqualTo(12),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350)
        );
    }

    @Test
    void 올바른_요청으로_한_번_환승하는_경로를_조회한다() {
        // given
        이호선을_생성한다();
        사호선을_생성한다();
        칠호선을_생성한다();

        final Long 낙성대_ID = 1L;
        final Long 고속터미널_ID = 9L;

        // when
        ExtractableResponse<Response> response = 경로_조회_요청(경로_조회_요청을_생성한다(낙성대_ID, 고속터미널_ID));

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("path")).hasSize(6),
                () -> assertThat(response.jsonPath().getString("path[0].stationName")).isEqualTo("낙성대역"),
                () -> assertThat(response.jsonPath().getString("path[1].stationName")).isEqualTo("사당역"),
                () -> assertThat(response.jsonPath().getString("path[2].stationName")).isEqualTo("방배역"),
                () -> assertThat(response.jsonPath().getString("path[3].stationName")).isEqualTo("서초역"),
                () -> assertThat(response.jsonPath().getString("path[4].stationName")).isEqualTo("교대역"),
                () -> assertThat(response.jsonPath().getString("path[5].stationName")).isEqualTo("고속터미널역"),
                () -> assertThat(response.jsonPath().getInt("totalDistance")).isEqualTo(24),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1550)
        );
    }

    @Test
    void 올바른_요청으로_두_번_환승하는_경로를_조회한다() {
        // given
        이호선을_생성한다();
        사호선을_생성한다();
        칠호선을_생성한다();

        final Long 총신대입구_ID = 7L;
        final Long 고속터미널_ID = 9L;

        // when
        ExtractableResponse<Response> response = 경로_조회_요청(경로_조회_요청을_생성한다(총신대입구_ID, 고속터미널_ID));

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("path")).hasSize(6),
                () -> assertThat(response.jsonPath().getString("path[0].stationName")).isEqualTo("총신대입구역"),
                () -> assertThat(response.jsonPath().getString("path[1].stationName")).isEqualTo("사당역"),
                () -> assertThat(response.jsonPath().getString("path[2].stationName")).isEqualTo("방배역"),
                () -> assertThat(response.jsonPath().getString("path[3].stationName")).isEqualTo("서초역"),
                () -> assertThat(response.jsonPath().getString("path[4].stationName")).isEqualTo("교대역"),
                () -> assertThat(response.jsonPath().getString("path[5].stationName")).isEqualTo("고속터미널역"),
                () -> assertThat(response.jsonPath().getInt("totalDistance")).isEqualTo(52),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150)
        );
    }

    @Test
    void 올바르지_않은_경로_조회_요청이_들어오면_예외가_발생한다() {
        // given
        final Long 올바르지_않은_역_ID = -2L;
        final Long 올바른_역_ID = 1L;

        // when
        ExtractableResponse<Response> response = 경로_조회_요청(경로_조회_요청을_생성한다(올바르지_않은_역_ID, 올바른_역_ID));

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message"))
                        .isEqualTo("역 ID는 양수여야 합니다.")
        );
    }
}
