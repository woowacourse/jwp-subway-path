package subway.application.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;


class PathServiceTest extends ServiceTest {

    @Test
    void 모든_노선에서_역을_삭제한다() {
        assertThatCode(() -> pathService.removeStationFromLines(1L))
                .doesNotThrowAnyException();
    }
}
