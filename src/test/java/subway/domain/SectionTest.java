package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.Fixture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionTest {

    @Test
    @DisplayName("역A를 구간BC의 상행 역인 B에 연결한다")
    void connectToUp() {
        // given & when
        final Section section = Fixture.sectionBC.connectToUp(Fixture.stationA, Fixture.distance1);

        // then
        assertAll(
                () -> assertThat(section.getUp()).isEqualTo(Fixture.stationA),
                () -> assertThat(section.getDown()).isEqualTo(Fixture.stationB),
                () -> assertThat(section.getDistance()).isEqualTo(Fixture.distance1)
        );
    }

    @Test
    @DisplayName("역C를 구간AB의 하행 역인 C에 연결한다")
    void connectToDown() {
        // given & when
        final Section section = Fixture.sectionAB.connectToDown(Fixture.stationC, Fixture.distance1);

        // then
        assertAll(
                () -> assertThat(section.getUp()).isEqualTo(Fixture.stationB),
                () -> assertThat(section.getDown()).isEqualTo(Fixture.stationC),
                () -> assertThat(section.getDistance()).isEqualTo(Fixture.distance1)
        );
    }

    @Test
    @DisplayName("역C를 구간AB의 사이에 연결한다")
    void connectIntermediate() {
        // given && when
        final Section section = Fixture.sectionAB.connectIntermediate(Fixture.stationC, Fixture.distance1);

        // then
        assertAll(
                () -> assertThat(section.getUp()).isEqualTo(Fixture.stationA),
                () -> assertThat(section.getDown()).isEqualTo(Fixture.stationC),
                () -> assertThat(section.getDistance()).isEqualTo(Fixture.distance1)
        );
    }
}