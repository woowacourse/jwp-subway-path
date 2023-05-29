package subway.acceptance;

import static subway.acceptance.step.CommonStep.반환값_없는_정상_요청;
import static subway.acceptance.step.CommonStep.사용자의_잘못된_요청;
import static subway.acceptance.step.CommonStep.요청_결과의_상태를_확인한다;
import static subway.acceptance.step.CommonStep.정상_요청;
import static subway.acceptance.step.LineStep.노선_생성_요청;
import static subway.acceptance.step.LineStep.노선_전체_조회_결과를_확인한다;
import static subway.acceptance.step.LineStep.노선_전체_조회_요청;
import static subway.acceptance.step.LineStep.노선_정보;
import static subway.acceptance.step.StationStep.노선에_역이_없는_경우_역_등록_요청;
import static subway.acceptance.step.StationStep.역_등록_요청;
import static subway.acceptance.step.StationStep.역_삭제_요청;
import static subway.domain.Direction.RIGHT;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class StationAcceptanceTest extends AcceptanceTest {

    @Nested
    class 노선에_역이_없는_경우 {

        @Test
        void 초기_역을_추가한다() {
            // given
            노선_생성_요청("1호선", "RED");

            // when
            final ExtractableResponse<Response> 요청_결과 = 노선에_역이_없는_경우_역_등록_요청("1호선", "A", "B", 3);

            // then
            요청_결과의_상태를_확인한다(요청_결과, 정상_요청);
            final ExtractableResponse<Response> 노선_전체 = 노선_전체_조회_요청();
            노선_전체_조회_결과를_확인한다(노선_전체, 노선_정보("1호선", "RED", "A", "B"));
        }

        @Test
        void 노선에_역이_이미_존재하는_경우_예외가_발생한다() {
            // given
            노선_생성_요청("1호선", "RED");
            노선에_역이_없는_경우_역_등록_요청("1호선", "A", "B", 3);

            // when
            final ExtractableResponse<Response> 요청_결과 = 노선에_역이_없는_경우_역_등록_요청("1호선", "B", "C", 3);

            // then
            요청_결과의_상태를_확인한다(요청_결과, 사용자의_잘못된_요청);
        }
    }

    @Nested
    class 노선에_역이_있는_경우 {

        @Test
        void 역을_추가한다() {
            // given
            노선_생성_요청("1호선", "RED");
            노선에_역이_없는_경우_역_등록_요청("1호선", "A", "B", 3);

            // when
            final ExtractableResponse<Response> 요청_결과 = 역_등록_요청("1호선", "B", "C", RIGHT, 3);

            // then
            요청_결과의_상태를_확인한다(요청_결과, 정상_요청);
            final ExtractableResponse<Response> 노선_전체 = 노선_전체_조회_요청();
            노선_전체_조회_결과를_확인한다(노선_전체, 노선_정보("1호선", "RED", "A", "B", "C"));
        }

        @Test
        void 기존에_존재하는_역을_추가하면_예외가_발생한다() {
            // given
            노선_생성_요청("1호선", "RED");
            노선에_역이_없는_경우_역_등록_요청("1호선", "A", "B", 3);

            // when
            final ExtractableResponse<Response> 요청_결과 = 역_등록_요청("1호선", "B", "A", RIGHT, 3);

            // then
            요청_결과의_상태를_확인한다(요청_결과, 사용자의_잘못된_요청);
        }

        @Test
        void 전체_지하철_노선에_존재하는_구간을_추가하면_예외가_발생한다() {
            // given
            노선_생성_요청("1호선", "RED");
            노선에_역이_없는_경우_역_등록_요청("1호선", "A", "B", 3);
            노선_생성_요청("2호선", "BLUE");
            노선에_역이_없는_경우_역_등록_요청("1호선", "C", "B", 3);

            // when
            final ExtractableResponse<Response> 요청_결과 = 역_등록_요청("2호선", "B", "A", RIGHT, 3);

            // then
            요청_결과의_상태를_확인한다(요청_결과, 사용자의_잘못된_요청);
        }
    }

    @Nested
    class 역을_삭제한다 {

        @Test
        void 노선에_역이_두개인_경우_모든_역이_삭제된다() {
            // given
            노선_생성_요청("1호선", "RED");
            노선에_역이_없는_경우_역_등록_요청("1호선", "A", "B", 3);

            // when
            final ExtractableResponse<Response> 요청_결과 = 역_삭제_요청("1호선", "A");

            // then
            요청_결과의_상태를_확인한다(요청_결과, 반환값_없는_정상_요청);
            final ExtractableResponse<Response> 노선_전체 = 노선_전체_조회_요청();
            노선_전체_조회_결과를_확인한다(노선_전체, 노선_정보("1호선", "RED"));
        }

        @Test
        void 노선에_역이_두개이상인_경우_해당_역이_삭제된다() {
            // given
            노선_생성_요청("1호선", "RED");
            노선에_역이_없는_경우_역_등록_요청("1호선", "A", "B", 3);
            역_등록_요청("1호선", "A", "C", RIGHT, 2);

            // when
            final ExtractableResponse<Response> 요청_결과 = 역_삭제_요청("1호선", "C");

            // then
            요청_결과의_상태를_확인한다(요청_결과, 반환값_없는_정상_요청);
            final ExtractableResponse<Response> 노선_전체 = 노선_전체_조회_요청();
            노선_전체_조회_결과를_확인한다(노선_전체, 노선_정보("1호선", "RED", "A", "B"));
        }

        @Test
        void 존재하지_않는_역을_삭제할_경우_예외가_발생한다() {
            // given
            노선_생성_요청("1호선", "RED");
            노선에_역이_없는_경우_역_등록_요청("1호선", "A", "B", 3);

            // when
            final ExtractableResponse<Response> 요청_결과 = 역_삭제_요청("1호선", "C");

            // then
            요청_결과의_상태를_확인한다(요청_결과, 사용자의_잘못된_요청);
        }
    }
}
