package subway.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    void 모든_필드가_같으면_같은_호선이다() {
        // given
        Line line1 = new Line(1L, "2호선", "초록", 0);
        Line line2 = new Line(1L, "2호선", "초록", 0);

        // expect
        assertThat(line1).isEqualTo(line2);
    }
}
