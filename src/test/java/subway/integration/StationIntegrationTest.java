package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.fixtures.StationFixtures.강남역;
import static subway.integration.fixtures.StationFixtures.교대역;
import static subway.integration.fixtures.StationFixtures.바뀐역;
import static subway.integration.fixtures.StationFixtures.역삼역;
import static subway.integration.fixtures.StationFixtures.존재하지_않는_역_아이디;
import static subway.integration.steps.StationSteps.단일_역_조회_요청;
import static subway.integration.steps.StationSteps.역_삭제_요청;
import static subway.integration.steps.StationSteps.역_생성_요청;
import static subway.integration.steps.StationSteps.역_생성하고_아이디_반환;
import static subway.integration.steps.StationSteps.역_수정_요청;
import static subway.integration.steps.StationSteps.전체_역_조회_요청;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    @Nested
    class 역을_생성할_때 {

        @Test
        void 정상_요청이면_성공적으로_생성한다() {
            // when
            ExtractableResponse<Response> 역_생성_결과 = 역_생성_요청(강남역);

            // then
            assertThat(역_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(역_생성_결과.header("Location")).isNotBlank();
        }

        @Test
        void 기존에_존재하는_역_이름이면_생성에_실패한다() {
            //given
            역_생성_요청(강남역);

            //when
            ExtractableResponse<Response> 역_생성_결과 = 역_생성_요청(강남역);

            //then
            assertThat(역_생성_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(역_생성_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 중복된 역 이름을 등록할 수 없습니다.");

        }
    }

    @Test
    void 전체_역_목록을_조회한다() {
        // given
        long 역삼역_아이디 = 역_생성하고_아이디_반환(역삼역);
        long 강남역_아이디 = 역_생성하고_아이디_반환(강남역);
        long 교대역_아이디 = 역_생성하고_아이디_반환(교대역);

        // when
        ExtractableResponse<Response> 전체_역_조회_결과 = 전체_역_조회_요청();
        List<Long> 역_아이디_목록 = 전체_역_조회_결과.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        // then
        assertThat(전체_역_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(역_아이디_목록).containsAll(List.of(역삼역_아이디, 강남역_아이디, 교대역_아이디));
    }

    @Nested
    class 단일_역을_조회할_때 {

        @Test
        void 정상_요청이면_성공적으로_조회한다() {
            // given
            long 역삼역_아이디 = 역_생성하고_아이디_반환(역삼역);

            // when
            ExtractableResponse<Response> 단일_역_조회_결과 = 단일_역_조회_요청(역삼역_아이디);

            // then
            assertThat(단일_역_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(단일_역_조회_결과.jsonPath().getObject(".", StationResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(StationResponse.of(
                            역삼역_아이디,
                            역삼역.getName()
                    ));
        }

        @Test
        void 존재하지_않는_역_아이디면_조회에_실패한다() {
            // when
            ExtractableResponse<Response> 단일_역_조회_결과 = 단일_역_조회_요청(존재하지_않는_역_아이디);

            // then
            assertThat(단일_역_조회_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(단일_역_조회_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 존재하지 않는 역입니다.");
        }
    }

    @Nested
    class 역을_수정할_때 {

        @Test
        void 정상_요청이면_성공적으로_수정한다() {
            // given
            long 역삼역_아이디 = 역_생성하고_아이디_반환(역삼역);

            // when
            ExtractableResponse<Response> 역_수정_결과 = 역_수정_요청(역삼역_아이디, 바뀐역);

            // then
            assertThat(역_수정_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 다른_역의_이름과_중복되면_수정에_실패한다() {
            // given
            역_생성_요청(강남역);
            long 역삼역_아이디 = 역_생성하고_아이디_반환(역삼역);

            // when
            ExtractableResponse<Response> 역_수정_결과 = 역_수정_요청(역삼역_아이디, 강남역);

            // then
            assertThat(역_수정_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(역_수정_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 중복된 역 이름을 등록할 수 없습니다.");
        }
    }

    @Nested
    class 역을_삭제할_때 {

        @Test
        void 정상_요청이면_성공적으로_삭제한다() {
            // given
            long 역삼역_아이디 = 역_생성하고_아이디_반환(역삼역);

            // when
            ExtractableResponse<Response> 역_삭제_결과 = 역_삭제_요청(역삼역_아이디);

            // then
            assertThat(역_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 존재하지_않는_역_아이디면_삭제에_실패한다() {
            // when
            ExtractableResponse<Response> 역_삭제_결과 = 역_삭제_요청(존재하지_않는_역_아이디);

            // then
            assertThat(역_삭제_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(역_삭제_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 존재하지 않는 역을 삭제할 수 없습니다.");
        }
    }
}
