package subway.line.acceptance;

import static subway.common.acceptance.CommonSteps.비정상_요청;
import static subway.common.acceptance.CommonSteps.비정상_요청_요청한_리소스_찾을_수_없음;
import static subway.common.acceptance.CommonSteps.요청_결과가_반환하는_리소스의_위차가_존재하는지_확인한다;
import static subway.common.acceptance.CommonSteps.요청_결과의_상태를_검증한다;
import static subway.common.acceptance.CommonSteps.정상_생성;
import static subway.common.acceptance.CommonSteps.정상_요청;
import static subway.common.acceptance.CommonSteps.정상_요청이지만_반환값_없음;
import static subway.line.acceptance.LineSteps.노선_목록_조회_요청;
import static subway.line.acceptance.LineSteps.노선_목록_크기_비교;
import static subway.line.acceptance.LineSteps.노선_삭제_요청;
import static subway.line.acceptance.LineSteps.노선_생성_결과에서_id_가져오기;
import static subway.line.acceptance.LineSteps.노선_생성_요청;
import static subway.line.acceptance.LineSteps.노선에서_id_조회_요청;
import static subway.line.acceptance.LineSteps.노선에서_역_추가_요청;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.common.acceptance.IntegrationTest;

@SuppressWarnings("NonAsciiCharacters")
public class LineAcceptanceTest extends IntegrationTest {

    @Nested
    class 노선을_추가할_때 {

        @Test
        void 정상_요청의_경우_노선이_정상적으로_추가된다() {
            var 요청_결과 = 노선_생성_요청("name", "color", 1L, 2L, 10L);

            요청_결과가_반환하는_리소스의_위차가_존재하는지_확인한다(요청_결과);
            요청_결과의_상태를_검증한다(요청_결과, 정상_생성);
        }

        @Test
        void 역이_존재하지_않는_경우_예외가_발생한다() {
            var 요청_결과 = 노선_생성_요청("name", "color", null, null, 10L);

            요청_결과의_상태를_검증한다(요청_결과, 비정상_요청);
        }

        @Test
        void 거리가_음수인_경우_예외가_발생한다() {
            var 요청_결과 = 노선_생성_요청("name", "color", 1L, 2L, -1L);

            요청_결과의_상태를_검증한다(요청_결과, 비정상_요청);
        }
    }

    @Nested
    class 노선_목록을_조회할_때 {

        @Test
        void 정상_요청의_경우_노선_목록이_반환된다() {
            var 요청_결과 = 노선_목록_조회_요청();

            요청_결과의_상태를_검증한다(요청_결과, 정상_요청);
            노선_목록_크기_비교(요청_결과, 0);

            노선_생성_요청("name", "color", 1L, 2L, 10L);

            var 요청_결과_2 = 노선_목록_조회_요청();

            요청_결과의_상태를_검증한다(요청_결과_2, 정상_요청);
            노선_목록_크기_비교(요청_결과_2, 1);
        }
    }

    @Nested
    class 노선을_삭제할_때 {

        @Test
        void 정상_요청의_경우_노선이_정상적으로_삭제된다() {
            var 노선_생성_결과 = 노선_생성_요청("name", "color", 1L, 2L, 10L);
            var 노선_id = 노선_생성_결과에서_id_가져오기(노선_생성_결과);

            var 노선_목록_조회_결과 = 노선_목록_조회_요청();
            노선_목록_크기_비교(노선_목록_조회_결과, 1);

            var 요청_결과 = 노선_삭제_요청(노선_id);

            요청_결과의_상태를_검증한다(요청_결과, 정상_요청이지만_반환값_없음);

            var 노선_목록_조회_결과_2 = 노선_목록_조회_요청();
            노선_목록_크기_비교(노선_목록_조회_결과_2, 0);
        }
    }

    @Nested
    class 노선을_id를_통해서_조회할_때 {

        @Test
        void 정상_요청의_경우_노선이_정상적으로_조회된다() {
            var 노선_생성_결과 = 노선_생성_요청("name", "color", 1L, 2L, 10L);
            var 노선_id = 노선_생성_결과에서_id_가져오기(노선_생성_결과);

            var 요청_결과 = 노선에서_id_조회_요청(노선_id);

            요청_결과의_상태를_검증한다(요청_결과, 정상_요청);
        }

        @Test
        void 없는_역을_조회하면_예외가_발생한다() {
            var 요청_결과 = 노선에서_id_조회_요청(1L);

            요청_결과의_상태를_검증한다(요청_결과, 비정상_요청_요청한_리소스_찾을_수_없음);
        }
    }

    @Nested
    class 노선에서_역을_추가할때 {

        @Test
        void 정상_요청의_경우_역이_추가된다() {
            var 노선_생성_결과 = 노선_생성_요청("name", "color", 1L, 2L, 10L);
            var 노선_id = 노선_생성_결과에서_id_가져오기(노선_생성_결과);

            var 요청_결과 = 노선에서_역_추가_요청(노선_id, 1L, 2L, 3L, 4L);

            요청_결과의_상태를_검증한다(요청_결과, 정상_요청);
        }

        @Test
        void 존재하지_않는_역을_기준으로_생성하면_예외가_발생한다() {
            var 노선_생성_결과 = 노선_생성_요청("name", "color", 1L, 2L, 10L);
            var 노선_id = 노선_생성_결과에서_id_가져오기(노선_생성_결과);

            var 요청_결과 = 노선에서_역_추가_요청(노선_id, 2L, 1000L, 3L, 4L);

            요청_결과의_상태를_검증한다(요청_결과, 비정상_요청);
        }

        @Test
        void 가장_앞에_정상_요청의_경우_역이_추가된다() {
            var 노선_생성_결과 = 노선_생성_요청("name", "color", 1L, 2L, 10L);
            var 노선_id = 노선_생성_결과에서_id_가져오기(노선_생성_결과);

            var 요청_결과 = 노선에서_역_추가_요청(노선_id, null, 1L, 3L, 4L);

            요청_결과의_상태를_검증한다(요청_결과, 정상_요청);
        }

        @Test
        void 가장_뒤에_정상_요청의_경우_역이_추가된다() {
            var 노선_생성_결과 = 노선_생성_요청("name", "color", 1L, 2L, 10L);
            var 노선_id = 노선_생성_결과에서_id_가져오기(노선_생성_결과);

            var 요청_결과 = 노선에서_역_추가_요청(노선_id, 2L, null, 3L, 4L);

            요청_결과의_상태를_검증한다(요청_결과, 정상_요청);
        }
    }
}
