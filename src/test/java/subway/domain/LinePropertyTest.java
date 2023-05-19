package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.LineProperty;

import static org.assertj.core.api.Assertions.assertThat;

class LinePropertyTest {

    @Test
    @DisplayName("LineProperty을 생성한다.")
    void createLineProperty() {
        LineProperty lineProperty = new LineProperty(1L, "1호선", "rg-red-600");

        assertThat(lineProperty.getId()).isEqualTo(1L);
        assertThat(lineProperty.getName()).isEqualTo("1호선");
        assertThat(lineProperty.getColor()).isEqualTo("rg-red-600");
    }

}
