package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import subway.exception.DomainException;

class SectionTest {
    @Test
    @DisplayName("Section을 생성한다.")
    void createSectionTest() {
        //given
        final Section result = new Section(1L, 2L, 1L, 10);

        //then
        assertAll(
                () -> assertThat(result.getSourceStationId()).isEqualTo(1L),
                () -> assertThat(result.getTargetStationId()).isEqualTo(2L),
                () -> assertThat(result.getDistance()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("distance가 1보다 작은 경우 예외가 발생한다.")
    void createSectionFailTest() {
        assertThatThrownBy(() -> new Section(1L, 2L, 1L, 0))
                .isInstanceOf(DomainException.class)
                .hasMessage("INVALID_DISTANCE");
    }

    @ParameterizedTest(name = "두개의 stationId를 모두 포함하고 있는지 검사한다.")
    @MethodSource("provideSectionIdsAndResult")
    void containsTheseStations(Long sourceStationId, Long targetStationId, Boolean expected) {
        // given
        final Section section = new Section(1L, 2L, 1L, 10);

        //when
        final boolean result = section.containsTheseStations(sourceStationId, targetStationId);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest(name = "인자로 받은 distance가 section의 distacne 이하인지 Boolean을 반환한다.")
    @MethodSource("provideDistanceAndExpect")
    void hasShorterOrSameDistanceThan(Integer distance, Boolean expected) {
        //given
        final Section section = new Section(1L, 2L, 1L, 10);

        //when
        final boolean result = section.hasShorterOrSameDistanceThan(distance);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest(name = "section이 인자로 받은 stationId를 포함하고 있는지 boolean값을 반환한다.")
    @MethodSource("provideStationIdAndExpect")
    void includeStation(Long stationId, Boolean expected) {
        //given
        final Section section = new Section(1L, 2L, 1L, 10);

        //when
        final boolean result = section.includeStation(stationId);

        //then
        assertThat(result).isEqualTo(expected);
    }

    public static Stream<Arguments> provideSectionIdsAndResult() {
        return Stream.of(
                Arguments.of(1L, 2L, true),
                Arguments.of(1L, 3L, false)
        );
    }

    public static Stream<Arguments> provideDistanceAndExpect() {
        return Stream.of(
                Arguments.of(9, false),
                Arguments.of(10, true),
                Arguments.of(11, true)
        );
    }

    public static Stream<Arguments> provideStationIdAndExpect() {
        return Stream.of(
                Arguments.of(1L, true),
                Arguments.of(3L, false)
        );
    }
}
