package subway.domain.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.station.Station;

import java.util.Queue;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SectionTest {

    @ParameterizedTest
    @MethodSource("createStation")
    @DisplayName("간선에서 정점이 null일 수는 없다.")
    void validate_both_stations_is_null(Station leftStation, Station rightStation) {
        //given
        Long id = 1L;
        int distance = 10;

        // when + then
        assertThatThrownBy(() -> new Section(id, leftStation, rightStation, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("비어 있는 역이 존재하면 안됩니다.");
    }

    private static Stream<Arguments> createStation() {
        return Stream.of(
                Arguments.arguments(new Station(1L, "잠실역"), null),
                Arguments.arguments(null, new Station(1L, "잠실역")),
                Arguments.arguments(null, null)
        );
    }

    @Test
    @DisplayName("두 역과 거리가 주어지면 생성된다.")
    void generate_section_success() {
        //given
        Station leftStation = new Station(1L, "잠실역");
        Station rightStation = new Station(2L, "강남역");
        int distance = 10;

        // when + then
        Section section = new Section(leftStation, rightStation, distance);
        assertAll(
                () -> assertEquals(leftStation, section.getLeftStation()),
                () -> assertEquals(rightStation, section.getRightStation()),
                () -> assertEquals(distance, section.getDistance().getDistance())
        );
    }

    @Test
    @DisplayName("영역을 두 개로 나누면 나눈 영역 두개가 반환된다.")
    void section_split() {
        //given
        Station leftStation = new Station(1L, "잠실역");
        Station rightStation = new Station(2L, "강남역");
        int distance = 30;

        Station innerStation = new Station(3L, "선릉역");
        int leftDistance = 20;
        int rightDistance = 10;

        // when + then
        Section section = new Section(leftStation, rightStation, distance);
        Queue<Section> splitSections = section.split(innerStation, leftDistance, rightDistance);

        assertAll(
                () -> assertEquals(2, splitSections.size()),
                () -> assertEquals(splitSections.peek().getLeftStation(), leftStation),
                () -> assertEquals(splitSections.peek().getRightStation(), innerStation),
                () -> assertEquals(splitSections.remove().getDistance().getDistance(), leftDistance),
                () -> assertEquals(splitSections.peek().getLeftStation(), innerStation),
                () -> assertEquals(splitSections.peek().getRightStation(), rightStation),
                () -> assertEquals(splitSections.remove().getDistance().getDistance(), rightDistance)
        );
    }
}
