package subway.integration;

import org.junit.jupiter.api.Test;
import subway.config.ControllerTestConfig;
import subway.integration.builder.LineAssured;
import subway.integration.builder.StationAssured;

import java.util.List;

import static subway.integration.builder.LineAssured.노선_요청;
import static subway.integration.builder.StationAssured.상행역_하행역_노선_거리_요청;
import static subway.integration.builder.StationAssured.역_삭제_요청;

class SubwayIntegrationTest extends ControllerTestConfig {

    @Test
    void 새로운_노선에_새로운_구간을_등록한다() {
        final Long 노선_식별자값 = LineAssured
                .request()
                    .노선을_등록한다(노선_요청("2", "초록"))
                .response()
                    .toBody(Long.class);

        StationAssured
                .request()
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("잠실", "잠실나루", 노선_식별자값, 10))
                .response();

        LineAssured
                .request()
                    .노선을_조회한다(노선_식별자값)
                .response()
                    .노선이_조회된다(노선_식별자값, "2", "초록", List.of("잠실", "잠실나루"));
    }

    @Test
    void 기존_구간_사이에_새로운_구간을_등록한다() {
        final Long 노선_식별자값 = LineAssured
                .request()
                    .노선을_등록한다(노선_요청("2", "초록"))
                .response()
                    .toBody(Long.class);

        StationAssured
                .request()
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("기존_상행역", "기존_하행역", 노선_식별자값, 10))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("새로운_상행역", "기존_하행역", 노선_식별자값, 5));

        LineAssured
                .request()
                    .노선을_조회한다(노선_식별자값)
                .response()
                    .노선이_조회된다(노선_식별자값, "2", "초록", List.of("기존_상행역", "새로운_상행역", "기존_하행역"));
    }

    @Test
    void 기존_구간_위에_새로운_구간을_등록한다() {
        final Long 노선_식별자값 = LineAssured
                .request()
                    .노선을_등록한다(노선_요청("2", "초록"))
                .response()
                .toBody(Long.class);

        StationAssured
                .request()
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("기존_상행역", "기존_하행역", 노선_식별자값, 10))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("새로운_상행역", "기존_상행역", 노선_식별자값, 5));

        LineAssured
                .request()
                    .노선을_조회한다(노선_식별자값)
                .response()
                    .노선이_조회된다(노선_식별자값, "2", "초록", List.of("새로운_상행역", "기존_상행역", "기존_하행역"));
    }

    @Test
    void 기존_구간_아래에_새로운_구간을_등록한다() {
        final Long 노선_식별자값 = LineAssured
                .request()
                    .노선을_등록한다(노선_요청("2", "초록"))
                .response()
                    .toBody(Long.class);

        StationAssured
                .request()
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("기존_상행역", "기존_하행역", 노선_식별자값, 10))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("기존_하행역", "새로운_하행역", 노선_식별자값, 5));

        LineAssured
                .request()
                    .노선을_조회한다(노선_식별자값)
                .response()
                    .노선이_조회된다(노선_식별자값, "2", "초록", List.of("기존_상행역", "기존_하행역", "새로운_하행역"));
    }

    @Test
    void 새로운_구간을_기존_구간_사이에_오도록_등록하고_노선의_모든_역을_조회한다() {
        final Long 노선_식별자값 = LineAssured
                .request()
                    .노선을_등록한다(노선_요청("2", "초록"))
                .response()
                    .toBody(Long.class);

        StationAssured
                .request()
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("A", "B", 노선_식별자값, 10))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("B", "C", 노선_식별자값, 5))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("C", "D", 노선_식별자값, 5))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("D", "G", 노선_식별자값, 10))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("D", "E", 노선_식별자값, 3))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("F", "G", 노선_식별자값, 3));

        LineAssured
                .request()
                    .노선을_조회한다(노선_식별자값)
                .response()
                    .노선이_조회된다(노선_식별자값, "2", "초록", List.of("A", "B", "C", "D", "E", "F", "G"));
    }

    @Test
    void 구간을_등록한_상태에서_상행종점역과_중간역을_삭제하고_노선의_상태를_조회한다() {
        final Long 노선_식별자값 = LineAssured
                .request()
                    .노선을_등록한다(노선_요청("2", "초록"))
                .response()
                    .toBody(Long.class);

        StationAssured
                .request()
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("A", "B", 노선_식별자값, 10))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("B", "C", 노선_식별자값, 5))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("C", "D", 노선_식별자값, 5))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("D", "G", 노선_식별자값, 10))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("D", "E", 노선_식별자값, 3))
                    .역과_구간을_등록한다(상행역_하행역_노선_거리_요청("F", "G", 노선_식별자값, 3));

        LineAssured
                .request()
                    .노선을_조회한다(노선_식별자값)
                .response()
                    .노선이_조회된다(노선_식별자값, "2", "초록", List.of("A", "B", "C", "D", "E", "F", "G"));

        StationAssured
                .request()
                    .역과_구간을_삭제한다(역_삭제_요청("A", "2"))
                    .역과_구간을_삭제한다(역_삭제_요청("C", "2"));

        LineAssured
                .request()
                    .노선을_조회한다(노선_식별자값)
                .response()
                    .노선이_조회된다(노선_식별자값, "2", "초록", List.of("B", "D", "E", "F", "G"));
    }
}
