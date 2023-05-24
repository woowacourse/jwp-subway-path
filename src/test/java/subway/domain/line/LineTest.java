package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.price.Price;

class LineTest {

    @Test
    @DisplayName("노선이 정상적으로 생성되어야 한다.")
    void create_success() {
        // given
        Line line = new Line(new LineName("2호선"), new LineColor("bg-green-500"), Price.ZERO);

        // expect
        assertThat(line.getName())
                .isEqualTo("2호선");
        assertThat(line.getColor())
                .isEqualTo("bg-green-500");
    }
}
