package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    @DisplayName("구간 안에 역이 존재하면 참을 반환한다")
    void hasStationInSection() {
        Station toTest = new Station("강남역");
        Section section = new Section(new Station("강남역"), new Station("선릉역"), new Distance(10));

        assertThat(section.hasStationInSection(toTest)).isTrue();
    }

    @Test
    @DisplayName("구간 안에 역이 존재하지 않으면 거짓을 반환한다")
    void notHasStationInSection() {
        Station toTest = new Station("강남역");
        Section section = new Section(new Station("역삼역"), new Station("선릉역"), new Distance(10));

        assertThat(section.hasStationInSection(toTest)).isFalse();
    }

    @Test
    @DisplayName("구간의 길이가 입력된 거리보다 길면 참을 반환한다")
    void isLongerThan() {
        Distance distance = new Distance(5);
        Section section = new Section(new Station("역삼역"), new Station("선릉역"), new Distance(10));

        assertThat(section.isLongerThan(distance)).isTrue();
    }


    @Test
    @DisplayName("구간의 길이가 입력된 거리보다 길면 참을 반환한다")
    void isNotLongerThan() {
        Distance distance = new Distance(15);
        Section section = new Section(new Station("역삼역"), new Station("선릉역"), new Distance(10));

        assertThat(section.isLongerThan(distance)).isFalse();
    }


    @Test
    @DisplayName("구간의 상행에 해당 역이 존재하면 참을 반환한다")
    void isStationOnUp() {
        Station toTest = new Station("강남역");
        Section section = new Section(new Station("강남역"), new Station("선릉역"), new Distance(10));

        assertThat(section.isStationOnDirection(toTest, Direction.UP)).isTrue();
    }


    @Test
    @DisplayName("구간의 하행에 해당 역이 존재하면 참을 반환한다")
    void isStationOnDown() {
        Station toTest = new Station("강남역");
        Section section = new Section(new Station("서초역"), new Station("강남역"), new Distance(10));

        assertThat(section.isStationOnDirection(toTest, Direction.DOWN)).isTrue();
    }

    @Test
    @DisplayName("구간의 해당 방향에에 해당 역이 존재하지 않으면 거짓을 반환한다")
    void isStationNotOnDirection() {
        Station toTest = new Station("사당역");
        Section section = new Section(new Station("서초역"), new Station("강남역"), new Distance(10));

        assertThat(section.isStationOnDirection(toTest, Direction.UP)).isFalse();
        assertThat(section.isStationOnDirection(toTest, Direction.DOWN)).isFalse();
    }

}
