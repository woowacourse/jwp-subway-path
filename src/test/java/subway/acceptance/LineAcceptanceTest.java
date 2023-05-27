package subway.acceptance;

import static subway.acceptance.step.CommonStep.반환값_없는_정상_요청;
import static subway.acceptance.step.CommonStep.생성_정상_요청;
import static subway.acceptance.step.CommonStep.요청_결과의_상태를_확인한다;
import static subway.acceptance.step.CommonStep.정상_요청;
import static subway.acceptance.step.LineStep.노선_id를_찾는다;
import static subway.acceptance.step.LineStep.노선_생성_요청;
import static subway.acceptance.step.LineStep.노선_수정_요청;
import static subway.acceptance.step.LineStep.노선_전체_조회_결과를_확인한다;
import static subway.acceptance.step.LineStep.노선_전체_조회_요청;
import static subway.acceptance.step.LineStep.노선_정보;
import static subway.acceptance.step.LineStep.노선_제거_요청;
import static subway.acceptance.step.LineStep.노선_조회_결과를_확인한다;
import static subway.acceptance.step.LineStep.노선_조회_요청;
import static subway.acceptance.step.StationStep.노선에_역이_없는_경우_역_등록_요청;
import static subway.acceptance.step.StationStep.역_등록_요청;
import static subway.domain.Direction.RIGHT;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class LineAcceptanceTest extends AcceptanceTest {

    @Nested
    class 노선을_추가하는_경우 {

        @Test
        void 노선을_추가한다() {
            // given
            final ExtractableResponse<Response> 요청_결과 = 노선_생성_요청("1호선", "RED");

            // expect
            요청_결과의_상태를_확인한다(요청_결과, 생성_정상_요청);
        }
    }

    @Nested
    class 노선을_조회하는_경우 {

        @Test
        void 노선의_id로_조회한다() {
            // given
            final ExtractableResponse<Response> RED_1호선 = 노선_생성_요청("1호선", "RED");
            final int 노선_id = 노선_id를_찾는다(RED_1호선);

            // when
            final ExtractableResponse<Response> 요청_결과 = 노선_조회_요청(노선_id);

            // then
            요청_결과의_상태를_확인한다(요청_결과, 정상_요청);
            노선_조회_결과를_확인한다(요청_결과, 노선_정보("1호선", "RED"));
        }

        @Test
        void 노선_전체를_조회한다() {
            // given
            final ExtractableResponse<Response> RED_1호선 = 노선_생성_요청("1호선", "RED");
            노선에_역이_없는_경우_역_등록_요청("1호선", "A", "B", 3);
            역_등록_요청("1호선", "B", "C", RIGHT, 3);

            final ExtractableResponse<Response> BLUE_2호선 = 노선_생성_요청("2호선", "BLUE");
            노선에_역이_없는_경우_역_등록_요청("2호선", "Z", "B", 3);
            역_등록_요청("2호선", "B", "Y", RIGHT, 3);

            // when
            final ExtractableResponse<Response> 요청_결과 = 노선_전체_조회_요청();

            // then
            요청_결과의_상태를_확인한다(요청_결과, 정상_요청);
            노선_전체_조회_결과를_확인한다(요청_결과,
                    노선_정보("1호선", "RED", "A", "B", "C"),
                    노선_정보("2호선", "BLUE", "Z", "B", "Y"));
        }
    }

    @Nested
    class 노선을_수정하는_경우 {

        @Test
        void 노선_id와_수정할_내용을_입력받아_노선을_수정한다() {
            // given
            final ExtractableResponse<Response> RED_1호선 = 노선_생성_요청("1호선", "RED");
            final int 노선_id = 노선_id를_찾는다(RED_1호선);

            // when
            final ExtractableResponse<Response> 요청_결과 = 노선_수정_요청(노선_id, "1호선", "BLUE");

            // then
            요청_결과의_상태를_확인한다(요청_결과, 반환값_없는_정상_요청);
            final ExtractableResponse<Response> 조회_요청_결과 = 노선_조회_요청(노선_id);
            노선_조회_결과를_확인한다(조회_요청_결과, 노선_정보("1호선", "BLUE"));
        }
    }

    @Nested
    class 노선을_삭제하는_경우 {

        @Test
        void 노선_id를_입력받아_노선을_수정한다() {
            // given
            final ExtractableResponse<Response> RED_1호선 = 노선_생성_요청("1호선", "RED");
            final int 노선_id = 노선_id를_찾는다(RED_1호선);

            // when
            final ExtractableResponse<Response> 요청_결과 = 노선_제거_요청(노선_id);

            // then
            요청_결과의_상태를_확인한다(요청_결과, 반환값_없는_정상_요청);
        }
    }
}
