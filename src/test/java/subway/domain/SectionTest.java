package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    void 두_역과_하나의_호선과_거리를_가진다() {
        Station upStation = new Station(1L, "신림");
        Station downStation = new Station(2L, "봉천");
        Line line = new Line(1L, "2호선", "초록색");
        Distance distance = new Distance(10);

        assertThatNoException().isThrownBy(() -> new Section(line, upStation, downStation, distance));
    }

    @Test
    void 모든_필드가_같으면_같은_객체이다() {
        Station upStation = new Station(1L, "신림");
        Station downStation = new Station(2L, "봉천");
        Line line = new Line(1L, "2호선", "초록색");
        Distance distance = new Distance(10);

        Section section1 = new Section(1L, line, upStation, downStation, distance);
        Section section2 = new Section(1L, line, upStation, downStation, distance);

        assertThat(section1).isEqualTo(section2);
    }
}
