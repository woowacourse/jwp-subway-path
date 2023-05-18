package subway.integration;

import org.junit.jupiter.api.Test;
import subway.config.ControllerTestConfig;
import subway.integration.builder.LineAssured;
import subway.integration.builder.RouteAssured;
import subway.integration.builder.StationAssured;

import java.util.List;

import static subway.integration.builder.LineAssured.노선_요청_데이터;
import static subway.integration.builder.RouteAssured.중간_경로_응답;
import static subway.integration.builder.RouteAssured.최종_경로_정보_응답;
import static subway.integration.builder.StationAssured.구간_요청_데이터;
import static subway.integration.builder.StationAssured.역_삭제_요청_데이터;

class RouteIntegrationTest extends ControllerTestConfig {

    Long 노선_저장(final String number, final String 파랑) {
        return LineAssured
                .클라이언트_요청()
                    .노선을_등록한다(노선_요청_데이터(number, 파랑))
                .서버_응답_검증()
                .서버_응답_추출(Long.class);
    }

    @Test
    void 출발역에서_도착역까지_도달한_후_다시_되돌아간다고_했을_때_최단_경로는_반대이다() {

        출발역과_도착지가_있을_때_환승을_2번하는_상황에서_최단_경로와_금액을_구한다();

        RouteAssured
                .클라이언트_요청()
                    .출발역과_도착역의_최단경로를_조회한다("G", "A")
                .서버_응답_검증()
                    .최단_경로_조회_검증(최종_경로_정보_응답("G", "A",
                            List.of("F", "D"),
                            List.of(중간_경로_응답("G", "F", "3", 10),
                                    중간_경로_응답("F", "E", "2", 2),
                                    중간_경로_응답("E", "D", "2", 1),
                                    중간_경로_응답("D", "C", "1", 3),
                                    중간_경로_응답("C", "B", "1", 2),
                                    중간_경로_응답("B", "A", "1", 1)
                            ), 19, 1350));
    }

    @Test
    void 출발역에서_도착역까지_도달한_후_다시_되돌아간다고_했을_때_구간이_추가되면_최단_경로가_바뀔_수_있다() {

        출발역과_도착지가_있을_때_환승을_2번하는_상황에서_최단_경로와_금액을_구한다();

        StationAssured
                .클라이언트_요청()
                    .역과_구간을_등록한다(구간_요청_데이터("G", "A", 1L, 1));

        RouteAssured
                .클라이언트_요청()
                    .출발역과_도착역의_최단경로를_조회한다("G", "A")
                .서버_응답_검증()
                    .최단_경로_조회_검증(
                            최종_경로_정보_응답("G", "A",
                                    List.of(),
                                    List.of(중간_경로_응답("G", "A", "1", 1)),
                                    1, 1250));
    }

    @Test
    void 출발역에서_도착역까지_도달한_후_다시_되돌아간다고_했을_때_구간이_삭제되면_최단_경로가_바뀔_수_있다() {

        출발역과_도착지가_있을_때_환승을_2번하는_상황에서_최단_경로와_금액을_구한다();

        StationAssured.클라이언트_요청()
                .역과_구간을_삭제한다(역_삭제_요청_데이터("B", "1"));

        RouteAssured
                .클라이언트_요청()
                    .출발역과_도착역의_최단경로를_조회한다("G", "A")
                .서버_응답_검증()
                    .최단_경로_조회_검증(
                            최종_경로_정보_응답("G", "A",
                                    List.of("F", "D"),
                                    List.of(중간_경로_응답("G", "F", "3", 10),
                                            중간_경로_응답("F", "E", "2", 2),
                                            중간_경로_응답("E", "D", "2", 1),
                                            중간_경로_응답("D", "C", "1", 3),
                                            중간_경로_응답("C", "A", "1", 3)
                                    ), 19, 1350));
    }

    void 출발역과_도착지가_있을_때_환승을_2번하는_상황에서_최단_경로와_금액을_구한다() {
        final Long 파랑_노선_식별자값 = 노선_저장("1", "파랑");
        final Long 초록_노선_식별자값 = 노선_저장("2", "초록");
        final Long 주황_노선_식별자값 = 노선_저장("3", "주황");

        StationAssured
                .클라이언트_요청()
                    .역과_구간을_등록한다(구간_요청_데이터("A", "B", 파랑_노선_식별자값, 1))
                    .역과_구간을_등록한다(구간_요청_데이터("B", "C", 파랑_노선_식별자값, 2))
                    .역과_구간을_등록한다(구간_요청_데이터("C", "D", 파랑_노선_식별자값, 3))
                    .역과_구간을_등록한다(구간_요청_데이터("D", "E", 초록_노선_식별자값, 1))
                    .역과_구간을_등록한다(구간_요청_데이터("E", "F", 초록_노선_식별자값, 2))
                    .역과_구간을_등록한다(구간_요청_데이터("F", "G", 주황_노선_식별자값, 10));

        LineAssured
                .클라이언트_요청()
                    .노선을_조회한다(파랑_노선_식별자값)
                .서버_응답_검증()
                    .노선이_조회된다(파랑_노선_식별자값, "1", "파랑", List.of("A", "B", "C", "D"));

        LineAssured
                .클라이언트_요청()
                    .노선을_조회한다(초록_노선_식별자값)
                .서버_응답_검증()
                    .노선이_조회된다(초록_노선_식별자값, "2", "초록", List.of("D", "E", "F"));

        LineAssured
                .클라이언트_요청()
                    .노선을_조회한다(주황_노선_식별자값)
                .서버_응답_검증()
                    .노선이_조회된다(주황_노선_식별자값, "3", "주황", List.of("F", "G"));

        RouteAssured
                .클라이언트_요청()
                    .출발역과_도착역의_최단경로를_조회한다("A", "G")
                .서버_응답_검증()
                    .최단_경로_조회_검증(
                            최종_경로_정보_응답("A", "G",
                                    List.of("D", "F"),
                                    List.of(중간_경로_응답("A", "B", "1", 1),
                                            중간_경로_응답("B", "C", "1", 2),
                                            중간_경로_응답("C", "D", "1", 3),
                                            중간_경로_응답("D", "E", "2", 1),
                                            중간_경로_응답("E", "F", "2", 2),
                                            중간_경로_응답("F", "G", "3", 10)
                                    ), 19, 1350));
    }

    @Test
    void 출발역과_도착지가_있을_때_환승을_하지않는_상황에서_최단_경로와_금액을_구한다() {
        final Long 파랑_노선_식별자값 = 노선_저장("1", "파랑");
        final Long 초록_노선_식별자값 = 노선_저장("2", "초록");
        final Long 주황_노선_식별자값 = 노선_저장("3", "주황");


        StationAssured.클라이언트_요청()
                .역과_구간을_등록한다(구간_요청_데이터("A", "B", 파랑_노선_식별자값, 1))
                .역과_구간을_등록한다(구간_요청_데이터("B", "C", 파랑_노선_식별자값, 2))
                .역과_구간을_등록한다(구간_요청_데이터("C", "D", 파랑_노선_식별자값, 3))
                .역과_구간을_등록한다(구간_요청_데이터("D", "E", 초록_노선_식별자값, 1))
                .역과_구간을_등록한다(구간_요청_데이터("E", "F", 초록_노선_식별자값, 2))
                .역과_구간을_등록한다(구간_요청_데이터("A", "F", 주황_노선_식별자값, 1));

        LineAssured
                .클라이언트_요청()
                    .노선을_조회한다(파랑_노선_식별자값)
                .서버_응답_검증()
                    .노선이_조회된다(파랑_노선_식별자값, "1", "파랑", List.of("A", "B", "C", "D"));

        LineAssured
                .클라이언트_요청()
                    .노선을_조회한다(초록_노선_식별자값)
                .서버_응답_검증()
                    .노선이_조회된다(초록_노선_식별자값, "2", "초록", List.of("D", "E", "F"));

        LineAssured
                .클라이언트_요청()
                    .노선을_조회한다(주황_노선_식별자값)
                .서버_응답_검증()
                    .노선이_조회된다(주황_노선_식별자값, "3", "주황", List.of("A", "F"));

        RouteAssured
                .클라이언트_요청()
                    .출발역과_도착역의_최단경로를_조회한다("A", "F")
                .서버_응답_검증()
                    .최단_경로_조회_검증(
                            최종_경로_정보_응답("A", "F",
                                    List.of(),
                                    List.of(중간_경로_응답("A", "F", "3", 1)
                                    ), 1, 1250));
    }
}
