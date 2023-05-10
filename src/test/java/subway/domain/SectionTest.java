package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {
    @DisplayName("거리를 받아 전체 거리에서 뺀 거리를 계산한다.")
    @Test
    void shouldReturnRemainingDistanceWhenInputDistance() {
        Station upwardStation = new Station("잠실역");
        Station downwardStation = new Station("몽촌토성역");

        Section section = new Section(upwardStation, downwardStation, 5);
        int remainingDistance = section.calculateRemainingDistance(3);
        assertThat(remainingDistance).isEqualTo(2);
    }
}
