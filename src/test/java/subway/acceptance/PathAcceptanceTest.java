package subway.acceptance;

import static subway.common.steps.CommonSteps.요청_결과의_상태를_검증한다;
import static subway.common.steps.CommonSteps.정상_요청;
import static subway.common.steps.LineSteps.노선_생성_요청;
import static subway.common.steps.PathSteps.최단거리_경로;
import static subway.common.steps.PathSteps.최단거리_경로_조회_결과를_확인한다;
import static subway.common.steps.PathSteps.최단거리_경로_조회_요청;
import static subway.common.steps.PathSteps.최단거리_구간;
import static subway.common.steps.SectionSteps.구간_생성_요청;
import static subway.common.steps.SectionSteps.노선에_구간이_존재하지_않을_때_초기_구간_생성_요청;
import static subway.common.steps.SectionSteps.오른쪽;

import org.junit.jupiter.api.Test;
import subway.common.AcceptanceTest;

@SuppressWarnings("NonAsciiCharacters")
public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    void 최단_경로를_조회한다() {
        // given
        final var 초록색_2호선 = 노선_생성_요청("2호선", "초록", 0);
        노선에_구간이_존재하지_않을_때_초기_구간_생성_요청("2호선", "잠실", "잠실새내", 2);
        구간_생성_요청("2호선", "잠실새내", "종합운동장", 오른쪽, 100);

        final var 주황색_3호선 = 노선_생성_요청("9호선", "갈색", 500);
        노선에_구간이_존재하지_않을_때_초기_구간_생성_요청("9호선", "봉은사", "종합운동장", 2);
        구간_생성_요청("9호선", "종합운동장", "삼전", 오른쪽, 2);
        구간_생성_요청("9호선", "삼전", "석촌고분", 오른쪽, 2);
        구간_생성_요청("9호선", "석촌고분", "석촌", 오른쪽, 2);

        final var 핑크색_8호선 = 노선_생성_요청("8호선", "핑크색", 1500);
        노선에_구간이_존재하지_않을_때_초기_구간_생성_요청("8호선", "잠실", "석촌", 1);

        // when
        final var 조회_요청_결과 = 최단거리_경로_조회_요청("잠실", "종합운동장", 15);

        // then
        요청_결과의_상태를_검증한다(조회_요청_결과, 정상_요청);
        최단거리_경로_조회_결과를_확인한다(
                조회_요청_결과,
                7,
                1920,
                최단거리_경로(
                        "8호선",
                        "핑크색",
                        최단거리_구간("잠실", "석촌", 1)
                ),
                최단거리_경로(
                        "9호선",
                        "갈색",
                        최단거리_구간("석촌고분", "석촌", 2),
                        최단거리_구간("삼전", "석촌고분", 2),
                        최단거리_구간("종합운동장", "삼전", 2)
                )
        );
    }
}
