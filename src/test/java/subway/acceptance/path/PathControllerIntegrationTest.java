package subway.acceptance.path;

import static subway.acceptance.line.LineStationSteps.노선에_역_추가_요청;
import static subway.acceptance.line.LineSteps.노선_생성_요청;
import static subway.acceptance.path.PathSteps.경로가_없다;
import static subway.acceptance.path.PathSteps.최단경로_조회_요청;
import static subway.acceptance.path.PathSteps.최단경로의_각_구간은;
import static subway.acceptance.path.PathSteps.최단경로의_요금은;
import static subway.acceptance.path.PathSteps.최단경로의_총_길이는;
import static subway.acceptance.path.PathSteps.최단경로의_환승역은;
import static subway.acceptance.station.StationSteps.역들을_생성한다;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import subway.common.exception.ExceptionResponse;
import subway.path.application.dto.ShortestRouteResponse;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("PathController 통합테스트")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PathControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class 최단경로_조회_시 {

        @Test
        void 최단경로를_조회한다() {
            // given
            역들을_생성한다("장암", "도봉산", "수락산", "건대입구", "강남구청", "잠실", "구의", "왕십리");
            노선_생성_요청("7호선", 700);
            노선에_역_추가_요청("7호선", "장암", "도봉산", 5);
            노선에_역_추가_요청("7호선", "도봉산", "수락산", 10);
            노선에_역_추가_요청("7호선", "수락산", "건대입구", 5);
            노선에_역_추가_요청("7호선", "건대입구", "강남구청", 15);

            노선_생성_요청("2호선", 300);
            노선에_역_추가_요청("2호선", "잠실", "구의", 5);
            노선에_역_추가_요청("2호선", "구의", "건대입구", 20);
            노선에_역_추가_요청("2호선", "건대입구", "왕십리", 10);

            노선_생성_요청("100호선", 10000);
            노선에_역_추가_요청("100호선", "장암", "잠실", 500);

            // when
            final var 장암역에서_잠실역_최단경로_응답 = 최단경로_조회_요청("장암", "잠실");
            final var 잠실역에서_장암역_최단경로_응답 = 최단경로_조회_요청("잠실", "장암");

            // then
            final var 장암역에서_잠실역_최단경로_정보 = 장암역에서_잠실역_최단경로_응답.as(ShortestRouteResponse.class);
            최단경로의_총_길이는(장암역에서_잠실역_최단경로_정보, 45);
            최단경로의_환승역은(장암역에서_잠실역_최단경로_정보, "건대입구");
            최단경로의_요금은(장암역에서_잠실역_최단경로_정보,
                    "영유아: 0",
                    "어린이: 1150",
                    "청소년: 1840",
                    "성인: 2650",
                    "노인: 0"
            );
            최단경로의_각_구간은(장암역에서_잠실역_최단경로_정보,
                    "[7호선: (장암) -> (도봉산), 5km]",
                    "[7호선: (도봉산) -> (수락산), 10km]",
                    "[7호선: (수락산) -> (건대입구), 5km]",
                    "[2호선: (건대입구) -> (구의), 20km]",
                    "[2호선: (구의) -> (잠실), 5km]"
            );

            final var 잠실역에서_장암역_최단경로_정보 = 잠실역에서_장암역_최단경로_응답.as(ShortestRouteResponse.class);
            최단경로의_총_길이는(잠실역에서_장암역_최단경로_정보, 45);
            최단경로의_환승역은(잠실역에서_장암역_최단경로_정보, "건대입구");
            최단경로의_요금은(잠실역에서_장암역_최단경로_정보,
                    "영유아: 0",
                    "어린이: 1150",
                    "청소년: 1840",
                    "성인: 2650",
                    "노인: 0"
            );
            최단경로의_각_구간은(잠실역에서_장암역_최단경로_정보,
                    "[2호선: (잠실) -> (구의), 5km]",
                    "[2호선: (구의) -> (건대입구), 20km]",
                    "[7호선: (건대입구) -> (수락산), 5km]",
                    "[7호선: (수락산) -> (도봉산), 10km]",
                    "[7호선: (도봉산) -> (장암), 5km]"
            );
        }

        @Test
        void 경로가_없는_경우_예외이다() {
            역들을_생성한다("장암", "도봉산", "수락산", "건대입구", "강남구청", "잠실", "구의", "왕십리");
            노선_생성_요청("7호선", 0);
            노선에_역_추가_요청("7호선", "장암", "도봉산", 5);
            노선에_역_추가_요청("7호선", "도봉산", "수락산", 10);
            노선에_역_추가_요청("7호선", "수락산", "건대입구", 5);

            노선_생성_요청("2호선", 0);
            노선에_역_추가_요청("2호선", "잠실", "구의", 5);

            // when
            final var 장암역에서_잠실역_최단경로_응답 = 최단경로_조회_요청("장암", "잠실");

            // then
            final var 장암역에서_잠실역_최단경로_정보 = 장암역에서_잠실역_최단경로_응답.as(ExceptionResponse.class);
            경로가_없다(장암역에서_잠실역_최단경로_정보);
        }
    }
}
