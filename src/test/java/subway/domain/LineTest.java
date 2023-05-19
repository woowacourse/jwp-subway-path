package subway.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    void ID와_이름과_색깔이_같으면_같은_호선이다() {
        Line line1 = new Line(1L, "2호선", "초록");
        Line line2 = new Line(1L, "2호선", "초록");

        assertThat(line1).isEqualTo(line2);
    }
}
