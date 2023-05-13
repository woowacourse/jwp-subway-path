package subway.ui.v2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.config.ControllerTestConfig;
import subway.integration.builder.StationAssured;

import static subway.integration.builder.StationAssured.request;
import static subway.integration.builder.StationAssured.역_요청;

class StationControllerV2Test extends ControllerTestConfig {

    @DisplayName("새로운 역을 등록하고 조회한다.")
    @Test
    void createStation() {
        final Long saveStationId = StationAssured
                .request()
                        .역을_등록한다(역_요청("잠실"))
                .response()
                        .toBody(Long.class);

        request()
                .역을_조회한다(saveStationId)
        .response()
                .등록된_역이_조회된다(saveStationId, "잠실");
    }
}
