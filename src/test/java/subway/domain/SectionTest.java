package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SectionTest {

    public static final Station UP_STATION = new Station("잠실나루");
    public static final Station DOWN_STATION = new Station("잠실새내");
    public static final Distance DISTANCE = new Distance(10);


    @DisplayName("생성한다")
    @Test
    void create() {
        assertDoesNotThrow(() -> new Section(UP_STATION, DOWN_STATION, DISTANCE));
    }

    @DisplayName("upStation이 null이면 예외를 발생한다.")
    @Test
    void throwExceptionWhenStationIsNull() {

        assertThatThrownBy(() -> new Section(null, DOWN_STATION, DISTANCE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("downStation이 nu정l이면 예외를 발생한다.")
    @Test
    void throwExceptionWhenLineIsNull() {

        assertThatThrownBy(() -> new Section(UP_STATION, null, DISTANCE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("distance가 null이면 예외를 발생한다.")
    @Test
    void throwExceptionWhenDistanceIsNull() {

        assertThatThrownBy(() -> new Section(UP_STATION, DOWN_STATION, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("두 구간을 입력 받아 중간역의 거리가 적합하면 true를 반환한다.")
    @Test
    void isPossibleDivideTo() {
        //given
        Section targetSection = new Section(UP_STATION, DOWN_STATION, DISTANCE);

        Station station = new Station("잠실");
        Section upSection = new Section(UP_STATION, station, new Distance(7));
        Section downSection = new Section(station, DOWN_STATION, new Distance(3));
        //when
        boolean possibleDivide = targetSection.isPossibleDivideTo(upSection, downSection);
        //then
        assertThat(possibleDivide).isTrue();
    }

    @DisplayName("두 구간을 입력 받아 중간역의 거리가 부적합하면 false를 반환한다.")
    @Test
    void whenUnavailableDistanceIsNotPossibleDivideTo() {
        //given
        Section targetSection = new Section(UP_STATION, DOWN_STATION, DISTANCE);

        Station station = new Station("잠실");
        Section upSection = new Section(UP_STATION, station, new Distance(7));
        Section downSection = new Section(station, DOWN_STATION, new Distance(2));
        //when
        boolean possibleDivide = targetSection.isPossibleDivideTo(upSection, downSection);
        //then
        assertThat(possibleDivide).isFalse();
    }

    @DisplayName("나누려는 구간들이이 부적합하면 false를 반환한다.")
    @Test
    void whenWrongDownStationIsNotPossibleDivideTo() {
        //given
        Section targetSection = new Section(UP_STATION, DOWN_STATION, DISTANCE);

        Station station = new Station("잠실");
        Station downStation = new Station("잠실새내아님");
        Section upSection = new Section(UP_STATION, station, new Distance(7));
        Section downSection = new Section(station, downStation, new Distance(3));
        //when
        boolean possibleDivide = targetSection.isPossibleDivideTo(upSection, downSection);
        //then
        assertThat(possibleDivide).isFalse();
    }

    @DisplayName("두 구간을 병합한다.")
    @Test
    void mergeWith() {
        //given
        Section upSection = new Section(UP_STATION, DOWN_STATION, DISTANCE);
        Section downSection = new Section(DOWN_STATION, new Station("구의"), new Distance(10));
        //when
        Section mergedSection = upSection.mergeWith(downSection);
        //then
        assertThat(mergedSection)
                .isEqualTo(new Section(new Station("잠실나루"), new Station("구의"), new Distance(20)));
    }
}
