package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.response.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.fixtures.LineFixtures.빈_역_목록;
import static subway.integration.fixtures.LineFixtures.이_호선_남색_추가요금_100원;
import static subway.integration.fixtures.LineFixtures.이_호선_초록색_추가요금_100원;
import static subway.integration.fixtures.LineFixtures.일_호선_남색_추가요금_100원;
import static subway.integration.fixtures.LineFixtures.일_호선_초록색_추가요금_100원;
import static subway.integration.fixtures.LineFixtures.존재하지_않는_노선_아이디;
import static subway.integration.steps.LineSteps.노선_삭제_요청;
import static subway.integration.steps.LineSteps.노선_생성_요청;
import static subway.integration.steps.LineSteps.노선_생성하고_아이디_반환;
import static subway.integration.steps.LineSteps.노선_수정_요청;
import static subway.integration.steps.LineSteps.단일_노선_조회_요청;
import static subway.integration.steps.LineSteps.전체_노선_조회_요청;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Nested
    class 노선을_생성할_때 {

        @Test
        void 정상_요청이면_성공적으로_생성한다() {
            // when
            ExtractableResponse<Response> 노선_생성_결과 = 노선_생성_요청(일_호선_남색_추가요금_100원);

            // then
            assertThat(노선_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(노선_생성_결과.header("Location")).isNotBlank();
        }

        @Test
        void 기존에_존재하는_노선_이름이면_생성에_실패한다() {
            //given
            노선_생성_요청(일_호선_남색_추가요금_100원);

            // when
            ExtractableResponse<Response> 노선_생성_결과 = 노선_생성_요청(일_호선_초록색_추가요금_100원);

            // then
            assertThat(노선_생성_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(노선_생성_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 중복된 노선 이름을 등록할 수 없습니다.");
        }

        @Test
        void 기존에_존재하는_노선_색상이면_생성에_실패한다() {
            //given
            노선_생성_요청(일_호선_남색_추가요금_100원);

            // when
            ExtractableResponse<Response> 노선_생성_결과 = 노선_생성_요청(이_호선_남색_추가요금_100원);

            // then
            assertThat(노선_생성_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(노선_생성_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 중복된 노선 색상을 등록할 수 없습니다.");
        }
    }

    @Test
    void 전체_노선_목록을_조회한다() {
        // given
        long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);
        long 이_호선_아이디 = 노선_생성하고_아이디_반환(이_호선_초록색_추가요금_100원);

        // when
        ExtractableResponse<Response> 전체_노선_조회_결과 = 전체_노선_조회_요청();
        List<Long> 노선_아이디_목록 = 전체_노선_조회_결과.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());


        // then
        assertThat(전체_노선_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(노선_아이디_목록).containsAll(List.of(일_호선_아이디, 이_호선_아이디));
    }

    @Nested
    class 단일_노선을_조회할_때 {

        @Test
        void 정상_요청이면_성공적으로_조회한다() {
            //given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);

            //when
            ExtractableResponse<Response> 단일_노선_조회_결과 = 단일_노선_조회_요청(일_호선_아이디);

            //then
            assertThat(단일_노선_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(단일_노선_조회_결과.jsonPath().getObject(".", LineResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(LineResponse.of(
                            일_호선_아이디,
                            일_호선_남색_추가요금_100원.getName(),
                            일_호선_남색_추가요금_100원.getColor(),
                            일_호선_남색_추가요금_100원.getExtraFare(),
                            빈_역_목록
                    ));
        }

        @Test
        void 존재하지_않는_노선_아이디면_수정에_실패한다() {
            //when
            ExtractableResponse<Response> 단일_노선_조회_결과 = 단일_노선_조회_요청(존재하지_않는_노선_아이디);

            //then
            assertThat(단일_노선_조회_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(단일_노선_조회_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 존재하지 않는 노선입니다.");
        }
    }


    @Nested
    class 노선을_수정할_때 {

        @Test
        void 정상_요청이면_성공적으로_수정한다() {
            //given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);

            //when
            ExtractableResponse<Response> 노선_수정_결과 = 노선_수정_요청(일_호선_아이디, 일_호선_초록색_추가요금_100원);

            //then
            assertThat(노선_수정_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 다른_노선의_이름과_중복되면_수정에_실패한다() {
            //given
            노선_생성_요청(일_호선_남색_추가요금_100원);
            long 이_호선_아이디 = 노선_생성하고_아이디_반환(이_호선_초록색_추가요금_100원);

            //when
            ExtractableResponse<Response> 노선_수정_결과 = 노선_수정_요청(이_호선_아이디, 일_호선_초록색_추가요금_100원);

            //then
            assertThat(노선_수정_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(노선_수정_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 중복된 노선 이름을 등록할 수 없습니다.");
        }

        @Test
        void 다른_노선의_색상과_중복되면_수정에_실패한다() {
            //given
            노선_생성_요청(일_호선_남색_추가요금_100원);
            long 이_호선_아이디 = 노선_생성하고_아이디_반환(이_호선_초록색_추가요금_100원);

            //when
            ExtractableResponse<Response> 노선_수정_결과 = 노선_수정_요청(이_호선_아이디, 이_호선_남색_추가요금_100원);

            //then
            assertThat(노선_수정_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(노선_수정_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 중복된 노선 색상을 등록할 수 없습니다.");
        }
    }

    @Nested
    class 노선을_제거할_때 {

        @Test
        void 정상_요청이면_성공적으로_삭제한다() {
            //given
            long 일_호선_아이디 = 노선_생성하고_아이디_반환(일_호선_남색_추가요금_100원);

            //when
            ExtractableResponse<Response> 노선_삭제_결과 = 노선_삭제_요청(일_호선_아이디);

            //then
            assertThat(노선_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        }

        @Test
        void 존재하지_않는_노선_아이디면_삭제에_실패한다() {
            //when
            ExtractableResponse<Response> 노선_삭제_결과 = 노선_삭제_요청(존재하지_않는_노선_아이디);

            //then
            assertThat(노선_삭제_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(노선_삭제_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 존재하지 않는 노선입니다.");
        }
    }
}
