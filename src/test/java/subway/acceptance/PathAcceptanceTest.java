package subway.acceptance;

import static subway.acceptance.step.CommonStep.요청_결과의_상태를_확인한다;
import static subway.acceptance.step.CommonStep.정상_요청;
import static subway.acceptance.step.LineStep.노선_생성_요청;
import static subway.acceptance.step.PathStep.최단_경로_조회_결과를_확인한다;
import static subway.acceptance.step.SectionStep.구간_생성_요청;
import static subway.acceptance.step.SectionStep.초기_구간_생성_요청;
import static subway.domain.Direction.RIGHT;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import subway.acceptance.step.PathStep;

@SuppressWarnings("NonAsciiCharacters")
public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    void 최단_경로를_조회한다() {
        // given
        // 1호선 생성
        노선_생성_요청("1호선", "RED");
        // 1호선 초기구간 생성 A-B 2
        초기_구간_생성_요청("1호선", "A", "B", 2);
        // 1호선 구간 생성 B-C RIGHT 3
        구간_생성_요청("1호선", "B", "C", RIGHT, 3);
        // 1호선 구간 생성 C-D RIGHT 4
        구간_생성_요청("1호선", "C", "D", RIGHT, 4);

        // 2호선 생성
        노선_생성_요청("2호선", "BLUE");
        // 2호선 초기구간 생성 D-B 2
        초기_구간_생성_요청("2호선", "D", "B", 2);
        // 2호선 구간 생성 B-E RIGHT 2
        구간_생성_요청("2호선", "B", "E", RIGHT, 2);

        // when
        // 최단 경로 요청 A-D
        ExtractableResponse<Response> A역부터_D역까지_최단거리 = PathStep.최단_경로_조회_요청("A", "D");

        // then
        요청_결과의_상태를_확인한다(A역부터_D역까지_최단거리, 정상_요청);
        최단_경로_조회_결과를_확인한다(A역부터_D역까지_최단거리, 4,1250, "A", "B", "D");
    }
}
