package subway.integration;

import org.junit.jupiter.api.Test;
import subway.config.ControllerTestConfig;
import subway.integration.builder.SectionAssured;
import subway.integration.builder.StationAssured;

import static subway.integration.builder.SectionAssured.구간_요청;
import static subway.integration.builder.StationAssured.역_요청;

class SectionIntegrationTest extends ControllerTestConfig {

    @Test
    void 두_역을_등록하고_새로운_구간을_등록한다() {
        final Long 잠실역_식별자값 = StationAssured
                .request().역을_등록한다(역_요청("잠실"))
                .response().toBody(Long.class);

        final Long 잠실나루_식별자값 = StationAssured
                .request().역을_등록한다(역_요청("잠실나루"))
                .response().toBody(Long.class);

        final Long 구간_식별자값 = SectionAssured
                .request().구간을_등록한다(구간_요청(잠실역_식별자값, 잠실나루_식별자값, 1L, 10))
                .response().toBody(Long.class);

        SectionAssured
                .request().구간을_조회한다(구간_식별자값)
                .response().구간이_조회된다(구간_식별자값, 잠실역_식별자값, 잠실나루_식별자값);
    }
}
