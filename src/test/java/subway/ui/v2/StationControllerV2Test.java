package subway.ui.v2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.config.ControllerTestConfig;

import static subway.integration.builder.StationAssured.request;
import static subway.integration.builder.StationAssured.역_요청;

class StationControllerV2Test extends ControllerTestConfig {

    @DisplayName("새로운 역을 등록하고 조회한다.")
    @Test
    void createStation() {
        request()
                .역을_등록한다(역_요청("잠실"));

        request()
                .역을_조회한다(1L)
        .response()
                .등록된_역이_조회된다(1L, "잠실");
    }
}
