package subway.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Distance;
import subway.domain.line.Section;
import subway.domain.line.Station;

class SectionTest {

    @DisplayName("구간이 정상적으로 생성된다.")
    @Test
    void createSection() {
        // given
        Station leftStation = new Station(1L, "잠실역");
        Station rightStation = new Station(2L, "강남역");
        Distance distance = new Distance(5);

        // when
        Section section = new Section(null, leftStation, rightStation, distance);

        // then
        assertSoftly(softly -> {
            softly.assertThat(section.getLeft()).isEqualTo(leftStation);
            softly.assertThat(section.getRight()).isEqualTo(rightStation);
            softly.assertThat(section.getDistance()).isEqualTo(distance.getValue());
        });
    }
}
