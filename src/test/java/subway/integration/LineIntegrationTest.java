package subway.integration;

import org.junit.jupiter.api.Test;
import subway.config.ControllerTestConfig;
import subway.dto.StationResponse;
import subway.integration.builder.LineAssured;
import subway.integration.builder.SectionAssured;
import subway.integration.builder.StationAssured;

import java.util.List;

import static subway.integration.builder.LineAssured.노선_요청;
import static subway.integration.builder.SectionAssured.구간_요청;
import static subway.integration.builder.StationAssured.역_요청;

class LineIntegrationTest extends ControllerTestConfig {

    @Test
    void 노선을_등록한다() {
        final Long 잠실역_식별자값 = StationAssured
                .request().역을_등록한다(역_요청("잠실"))
                .response().toBody(Long.class);

        final Long 잠실나루역_식별자값 = StationAssured
                .request().역을_등록한다(역_요청("잠실나루"))
                .response().toBody(Long.class);

        final Long 노선_식별자값 = LineAssured
                .request().노선을_등록한다(노선_요청("2", "초록"))
                .response().toBody(Long.class);

        SectionAssured
                .request().구간을_등록한다(구간_요청("잠실", "잠실나루", 노선_식별자값, 10))
                .response().toBody(Long.class);

        final StationResponse 잠실역 = StationAssured
                .request().역을_조회한다(잠실역_식별자값)
                .response().toBody(StationResponse.class);

        final StationResponse 잠실나루역 = StationAssured
                .request().역을_조회한다(잠실나루역_식별자값)
                .response().toBody(StationResponse.class);

        LineAssured
                .request().노선을_조회한다(노선_식별자값)
                .response().노선이_조회된다(노선_식별자값, "2", "초록", List.of(잠실역, 잠실나루역));
    }
}
