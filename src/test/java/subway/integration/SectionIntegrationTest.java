package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import subway.dto.request.SectionCreationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.fixtures.LineFixtures.일_호선_남색_추가요금_100원;
import static subway.integration.fixtures.StationFixtures.노량진역;
import static subway.integration.fixtures.StationFixtures.서울역;
import static subway.integration.fixtures.StationFixtures.시청역;
import static subway.integration.fixtures.StationFixtures.용산역;
import static subway.integration.steps.LineSteps.노선_생성하고_아이디_반환;
import static subway.integration.steps.SectionSteps.노선에_구간_생성_요청;
import static subway.integration.steps.SectionSteps.노선에서_역_삭제_요청;
import static subway.integration.steps.StationSteps.역_생성하고_아이디_반환;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 노선에 역 추가/삭제 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {

    @Nested
    class 노선에_구간을_추가할_때 {

        @Test
        void 처음에는_두_역을_추가한다() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);

            // when
            ExtractableResponse<Response> 구간_생성_결과 = 노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 용산역_아이디, 10)
            );

            //then
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        void 존재하는_구간들의_제일_하행_위치에_역을_추가한다() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 용산역_아이디, 10)
            );

            // when
            ExtractableResponse<Response> 구간_생성_결과 = 노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(용산역_아이디, 노량진역_아이디, 5)
            );

            // then
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        void 존재하는_구간들의_제일_상행_위치에_역을_추가한다() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(용산역_아이디, 노량진역_아이디, 10)
            );

            // when
            ExtractableResponse<Response> 구간_생성_결과 = 노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 용산역_아이디, 5)
            );

            // then
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        void 존재하는_두_역의_중간에_역을_추가한다_1() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 노량진역_아이디, 10)
            );

            // when
            ExtractableResponse<Response> 구간_생성_결과 = 노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 용산역_아이디, 7)
            );

            // then
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        void 존재하는_두_역의_중간에_역을_추가한다_2() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 노량진역_아이디, 10)
            );

            // when
            ExtractableResponse<Response> 구간_생성_결과 = 노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(용산역_아이디, 노량진역_아이디, 3)
            );

            // then
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        void 노선에_이미_역이_하나_이상_등록된_경우_새로운_두_역을_추가하면_구간_생성에_실패한다() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 시청역_아이디 = 역_생성하고_아이디_반환(시청역);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 노량진역_아이디, 10)
            );

            // when
            ExtractableResponse<Response> 구간_생성_결과 = 노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(시청역_아이디, 용산역_아이디, 3)
            );

            // then
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(구간_생성_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 노선에 역을 1개씩 삽입해 주세요.");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void 구간_거리가_양수가_아니면_구간_생성에_실패한다(int 양수가_아닌_구간_거리) {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 노량진역_아이디, 10)
            );

            // when
            ExtractableResponse<Response> 구간_생성_결과 = 노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 용산역_아이디, 양수가_아닌_구간_거리)
            );

            // then
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(구간_생성_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 두 역 사이의 거리는 양의 정수여야 합니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {10, 11})
        void 새로_생성되는_구간들_중_거리가_양수가_아닌_수가_있다면_구간_생성에_실패한다(int 유효하지_않은_구간_거리) {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 노량진역_아이디, 10)
            );

            // when
            ExtractableResponse<Response> 구간_생성_결과 = 노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 용산역_아이디, 유효하지_않은_구간_거리)
            );

            // then
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(구간_생성_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 기존 구간에 역을 삽입한 결과로 거리가 0 이하인 구간이 발생하게 됩니다.");
        }
    }

    @Nested
    class 노선에서_역을_삭제할_때 {

        @Test
        void 정상_요청이면_성공적으로_삭제한다() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(
                    일_호선_아이디,
                    new SectionCreationRequest(서울역_아이디, 노량진역_아이디, 10)
            );

            // when
            ExtractableResponse<Response> 노선에서_역_삭제_결과 = 노선에서_역_삭제_요청(일_호선_아이디, 서울역_아이디);

            // then
            assertThat(노선에서_역_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 노선에_존재하지_않는_역_아이디면_삭제에_실패한다() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);

            // when
            ExtractableResponse<Response> 노선에서_역_삭제_결과 = 노선에서_역_삭제_요청(일_호선_아이디, 서울역_아이디);

            // then
            assertThat(노선에서_역_삭제_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(노선에서_역_삭제_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 노선에 해당 역이 존재하지 않습니다.");
        }
    }
}
