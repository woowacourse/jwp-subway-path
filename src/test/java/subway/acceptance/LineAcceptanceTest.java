package subway.acceptance;

import static subway.common.steps.CommonSteps.비정상_요청;
import static subway.common.steps.CommonSteps.비정상_요청_요청한_리소스_찾을_수_없음;
import static subway.common.steps.CommonSteps.요청_결과가_반환하는_리소스의_위차가_존재하는지_확인한다;
import static subway.common.steps.CommonSteps.요청_결과의_상태를_검증한다;
import static subway.common.steps.CommonSteps.정상_생성;
import static subway.common.steps.CommonSteps.정상_요청;
import static subway.common.steps.CommonSteps.정상_요청이지만_반환값_없음;
import static subway.common.steps.LineSteps.노선_번호를_구한다;
import static subway.common.steps.LineSteps.노선_삭제_요청;
import static subway.common.steps.LineSteps.노선_생성_요청;
import static subway.common.steps.LineSteps.노선_수정_요청;
import static subway.common.steps.LineSteps.노선_전체_조회_결과를_확인한다;
import static subway.common.steps.LineSteps.노선_전체_조회_요청;
import static subway.common.steps.LineSteps.노선_정보;
import static subway.common.steps.LineSteps.노선_조회_결과를_확인한다;
import static subway.common.steps.LineSteps.노선_조회_요청;
import static subway.common.steps.SectionSteps.구간_생성_요청;
import static subway.common.steps.SectionSteps.노선에_구간이_존재하지_않을_때_초기_구간_생성_요청;
import static subway.common.steps.SectionSteps.오른쪽;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.common.AcceptanceTest;

@SuppressWarnings("NonAsciiCharacters")
public class LineAcceptanceTest extends AcceptanceTest {

    @Nested
    public class 노선을_추가할_때 {

        @Test
        void 정상_요청의_경우_노선이_정상적으로_추가된다() {
            // given
            final var 요청_결과 = 노선_생성_요청("1호선", "파랑", 0);

            // expect
            요청_결과의_상태를_검증한다(요청_결과, 정상_생성);
            요청_결과가_반환하는_리소스의_위차가_존재하는지_확인한다(요청_결과);
        }

        @Test
        void 이미_같은_이름으로_생성된_노선이_존재하는_경우_예외가_발생한다() {
            // given
            노선_생성_요청("1호선", "파랑", 0);

            // when
            final var 요청_결과 = 노선_생성_요청("1호선", "파랑", 0);

            // then
            요청_결과의_상태를_검증한다(요청_결과, 비정상_요청);
        }
    }

    @Nested
    public class 노선을_조회할_때 {

        @Test
        void 노선_번호를_입력받아_상행종점역_부터_하행종점역으로_정렬된_결과를_반환한다() {
            // given
            final var 초록색_2호선 = 노선_생성_요청("2호선", "초록", 0);
            노선에_구간이_존재하지_않을_때_초기_구간_생성_요청("2호선", "잠실", "잠실새내", 5);
            final var 노선_번호 = 노선_번호를_구한다(초록색_2호선);

            // when
            final var 조회_결과 = 노선_조회_요청(노선_번호);

            // then
            요청_결과의_상태를_검증한다(조회_결과, 정상_요청);
            노선_조회_결과를_확인한다(조회_결과, "2호선", "초록", 0, "잠실", "잠실새내");
        }

        @Test
        void 존재하지_않는_노선_번호를_입력받는_경우_예외가_발생한다() {
            // given
            final var 존재_하지_않는_노선_번호 = String.valueOf(Long.MAX_VALUE);

            // when
            final var 조회_결과 = 노선_조회_요청(존재_하지_않는_노선_번호);

            // then
            요청_결과의_상태를_검증한다(조회_결과, 비정상_요청_요청한_리소스_찾을_수_없음);
        }
    }

    @Nested
    public class 노선을_전체_조회할_때 {

        @Test
        void 상행종점역_부터_하행종점역으로_정렬된_결과를_반환한다() {
            // given
            노선_생성_요청("2호선", "초록", 0);
            노선에_구간이_존재하지_않을_때_초기_구간_생성_요청("2호선", "잠실", "잠실새내", 5);
            구간_생성_요청("2호선", "잠실새내", "종합운동장", 오른쪽, 5);

            노선_생성_요청("9호선", "고동", 0);
            노선에_구간이_존재하지_않을_때_초기_구간_생성_요청("9호선", "봉은사", "종합운동장", 3);
            구간_생성_요청("9호선", "종합운동장", "삼전", 오른쪽, 7);

            // when
            final var 조회_결과 = 노선_전체_조회_요청();

            // then
            요청_결과의_상태를_검증한다(조회_결과, 정상_요청);
            노선_전체_조회_결과를_확인한다(
                    조회_결과,
                    노선_정보("2호선", "초록", 0, "잠실", "잠실새내", "종합운동장"),
                    노선_정보("9호선", "고동", 0, "봉은사", "종합운동장", "삼전")
            );
        }
    }

    @Nested
    public class 노선을_수정할_때 {

        @Test
        void 노선_번호와_수정할_내용을_입력받아_노선을_수정한다() {
            // given
            final var 초록색_2호선 = 노선_생성_요청("2호선", "초록", 0);
            final String 노선_번호 = 노선_번호를_구한다(초록색_2호선);

            // when
            final var 수정_결과 = 노선_수정_요청(노선_번호, "1호선", "파랑");

            // then
            요청_결과의_상태를_검증한다(수정_결과, 정상_요청이지만_반환값_없음);
            final var 조회_결과 = 노선_조회_요청(노선_번호);
            노선_조회_결과를_확인한다(조회_결과, "1호선", "파랑", 0);
        }

        @Test
        void 존재하지_않는_노선_번호를_입력받는_경우_예외가_발생한다() {
            // given
            final var 존재_하지_않는_노선_번호 = String.valueOf(Long.MAX_VALUE);

            // when
            final var 수정_결과 = 노선_수정_요청(존재_하지_않는_노선_번호, "1호선", "파랑");

            // then
            요청_결과의_상태를_검증한다(수정_결과, 비정상_요청_요청한_리소스_찾을_수_없음);
        }
    }

    @Nested
    public class 노선을_삭제할_때 {

        @Test
        void 노선_번호를_입력받아_노선을_삭제한다() {
            // given
            final var 초록색_2호선 = 노선_생성_요청("2호선", "초록", 0);
            final var 노선_번호 = 노선_번호를_구한다(초록색_2호선);

            // when
            final var 삭제_결과 = 노선_삭제_요청(노선_번호);

            // then
            요청_결과의_상태를_검증한다(삭제_결과, 정상_요청이지만_반환값_없음);
            final var 조회_결과 = 노선_조회_요청(노선_번호);
            요청_결과의_상태를_검증한다(조회_결과, 비정상_요청_요청한_리소스_찾을_수_없음);
        }

        @Test
        void 존재하지_않는_노선_번호를_입력받는_경우_예외가_발생한다() {
            // given
            final var 존재_하지_않는_노선_번호 = String.valueOf(Long.MAX_VALUE);

            // when
            final var 삭제_결과 = 노선_삭제_요청(존재_하지_않는_노선_번호);

            // then
            요청_결과의_상태를_검증한다(삭제_결과, 비정상_요청_요청한_리소스_찾을_수_없음);
        }
    }
}
