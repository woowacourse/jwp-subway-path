package subway.route.acceptance;

import static subway.common.acceptance.CommonSteps.비정상_요청_요청한_리소스_찾을_수_없음;
import static subway.common.acceptance.CommonSteps.요청_결과의_상태를_검증한다;
import static subway.common.acceptance.CommonSteps.정상_요청;
import static subway.line.acceptance.LineSteps.노선_생성_결과에서_id_가져오기;
import static subway.line.acceptance.LineSteps.노선_생성_요청;
import static subway.line.acceptance.LineSteps.노선에서_역_추가_요청;
import static subway.route.acceptance.RouteSteps.경로_거리_계산_결과_확인;
import static subway.route.acceptance.RouteSteps.경로_금액_계산_결과_확인;
import static subway.route.acceptance.RouteSteps.경로_하나_만들기;
import static subway.route.acceptance.RouteSteps.경로_확인;
import static subway.route.acceptance.RouteSteps.역_거리_계산_요청;
import static subway.station.acceptance.StationSteps.역_생성_결과에서_id_가져오기;
import static subway.station.acceptance.StationSteps.역_생성_요청;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.common.acceptance.IntegrationTest;

@SuppressWarnings("NonAsciiCharacters")
class RouteAcceptanceTest extends IntegrationTest {

    @Nested
    class 거리를_계산할_때 {

        @Test
        void 정상적인_요청이_들어오면_거리를_계산한다() {
            var 첫번째_역_요청_결과 = 역_생성_요청("강남역");
            var 첫번째_역_ID = 역_생성_결과에서_id_가져오기(첫번째_역_요청_결과);

            var 두번째_역_요청_결과 = 역_생성_요청("역삼역");
            var 두번째_역_ID = 역_생성_결과에서_id_가져오기(두번째_역_요청_결과);

            var 세번째_역_요청_결과 = 역_생성_요청("삼성역");
            var 세번째_역_ID = 역_생성_결과에서_id_가져오기(세번째_역_요청_결과);

            var 노선_생성_결과 = 노선_생성_요청("2호선", "초록색", 첫번째_역_ID, 두번째_역_ID, 20L);
            var 노선_ID = 노선_생성_결과에서_id_가져오기(노선_생성_결과);

            노선에서_역_추가_요청(노선_ID, 첫번째_역_ID, 두번째_역_ID, 세번째_역_ID, 10L);

            var 역_거리_계산_요청_결과 = 역_거리_계산_요청(첫번째_역_ID, 세번째_역_ID);

            요청_결과의_상태를_검증한다(역_거리_계산_요청_결과, 정상_요청);

            경로_거리_계산_결과_확인(역_거리_계산_요청_결과, 10);
            경로_확인(역_거리_계산_요청_결과, 경로_하나_만들기(첫번째_역_ID, 세번째_역_ID, 10L, 노선_ID));
            경로_금액_계산_결과_확인(역_거리_계산_요청_결과, 1250);

            var 역_거리_계산_요청_결과_2 = 역_거리_계산_요청(첫번째_역_ID, 두번째_역_ID);

            요청_결과의_상태를_검증한다(역_거리_계산_요청_결과_2, 정상_요청);

            경로_거리_계산_결과_확인(역_거리_계산_요청_결과_2, 20);
            경로_확인(역_거리_계산_요청_결과_2, 경로_하나_만들기(첫번째_역_ID, 세번째_역_ID, 10L, 노선_ID),
                    경로_하나_만들기(세번째_역_ID, 두번째_역_ID, 10L, 노선_ID));
            경로_금액_계산_결과_확인(역_거리_계산_요청_결과_2, 1450);
        }

        @Test
        void 역이_존재하지_않으면_예외를_발생한다() {
            var 역_거리_계산_요청_결과 = 역_거리_계산_요청(1L, 2L);

            요청_결과의_상태를_검증한다(역_거리_계산_요청_결과, 비정상_요청_요청한_리소스_찾을_수_없음);
        }
    }
}
