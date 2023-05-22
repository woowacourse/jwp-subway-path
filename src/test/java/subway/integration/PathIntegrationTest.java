package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.request.SectionCreationRequest;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.fixtures.LineFixtures.이_호선_초록색_추가요금_100원;
import static subway.integration.fixtures.LineFixtures.일_호선_남색_추가요금_100원;
import static subway.integration.fixtures.StationFixtures.강남역;
import static subway.integration.fixtures.StationFixtures.노량진역;
import static subway.integration.fixtures.StationFixtures.서울역;
import static subway.integration.fixtures.StationFixtures.역삼역;
import static subway.integration.fixtures.StationFixtures.용산역;
import static subway.integration.steps.LineSteps.노선_생성하고_아이디_반환;
import static subway.integration.steps.PathSteps.경로_조회_요청;
import static subway.integration.steps.SectionSteps.노선에_구간_생성_요청;
import static subway.integration.steps.StationSteps.역_생성하고_아이디_반환;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("출발역과 도착역 사이의 경로 관련 기능")
public class PathIntegrationTest extends IntegrationTest {

    private final int 성인 = 25;

    @Nested
    class 출발역과_도착역_사이의_경로를_조회할_때 {

        @Test
        void 정상_요청이면_성공적으로_조회한다() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(일_호선_아이디, new SectionCreationRequest(서울역_아이디, 용산역_아이디, 10));
            노선에_구간_생성_요청(일_호선_아이디, new SectionCreationRequest(용산역_아이디, 노량진역_아이디, 3));

            // when
            ExtractableResponse<Response> 경로_조회_결과 = 경로_조회_요청(노량진역_아이디, 서울역_아이디, 성인);
            List<Long> 경로에_있는_역_아이디_목록 = 경로_조회_결과.jsonPath().getList("stations", StationResponse.class).stream()
                    .map(StationResponse::getId)
                    .collect(Collectors.toList());

            // then
            assertThat(경로_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(경로에_있는_역_아이디_목록).containsExactly(노량진역_아이디, 용산역_아이디, 서울역_아이디);
        }

        @Test
        void 출발역과_도착역이_연결되어있지_않으면_조회에_실패한다() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            노선에_구간_생성_요청(일_호선_아이디, new SectionCreationRequest(서울역_아이디, 용산역_아이디, 10));

            long 이_호선_아이디 = 노선_생성하고_아이디_반환(이_호선_초록색_추가요금_100원);
            long 역삼역_아이디 = 역_생성하고_아이디_반환(역삼역);
            long 강남역_아이디 = 역_생성하고_아이디_반환(강남역);
            노선에_구간_생성_요청(이_호선_아이디, new SectionCreationRequest(역삼역_아이디, 강남역_아이디, 3));

            // when
            ExtractableResponse<Response> 경로_조회_결과 = 경로_조회_요청(
                    역삼역_아이디,
                    용산역_아이디,
                    성인
            );

            // then
            assertThat(경로_조회_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(경로_조회_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 출발역과 도착역이 연결되어 있지 않습니다.");
        }


        @Test
        void 출발역과_도착역이_같으면_조회에_실패한다() {
            // given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
            long 서울역_아이디 = 역_생성하고_아이디_반환(서울역);
            long 용산역_아이디 = 역_생성하고_아이디_반환(용산역);
            long 노량진역_아이디 = 역_생성하고_아이디_반환(노량진역);
            노선에_구간_생성_요청(일_호선_아이디, new SectionCreationRequest(서울역_아이디, 용산역_아이디, 10));
            노선에_구간_생성_요청(일_호선_아이디, new SectionCreationRequest(용산역_아이디, 노량진역_아이디, 3));

            // when
            ExtractableResponse<Response> 경로_조회_결과 = 경로_조회_요청(
                    서울역_아이디,
                    서울역_아이디,
                    성인
            );

            // then
            assertThat(경로_조회_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(경로_조회_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 출발역과 도착역이 같아 조회할 경로가 없습니다.");
        }
    }
}
