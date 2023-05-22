package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase
class SubwayRouteMapTest {
    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_FARE_DISTANCE = 10;
    private static final int TEN_TO_FIFTY = 40;
    private final Station station1 = new Station(1L, "station1");
    private final Station station2 = new Station(2L, "station2");
    private final Station station3 = new Station(3L, "station3");
    private final Station station4 = new Station(4L, "station4");
    private final Station station5 = new Station(5L, "station5");
    private final Station station6 = new Station(6L, "station6");

    @DisplayName("두 역의 최단 거리를 반환한다.")
    @Test
    void shortestDistanceBetween() {
        //given
        final Section section1 = new Section(station1, station2, new Distance(1));
        final Section section2 = new Section(station2, station3, new Distance(2));
        final Section section3 = new Section(station2, station4, new Distance(3));
        final SubwayRouteMap subway = new SubwayRouteMap(List.of(section1, section2, section3));

        //when
        final long distanceBetween1 = subway.shortestDistanceBetween(station1, station3);
        final long distanceBetween2 = subway.shortestDistanceBetween(station1, station4);

        //then
        assertAll(
                () -> assertThat(distanceBetween1).isEqualTo(3L),
                () -> assertThat(distanceBetween2).isEqualTo(4L)
        );
    }

    @DisplayName("두 역의 최단 경로를 반환한다.")
    @Test
    void shortestPathBetween() {
        //given
        final Section section1 = new Section(station1, station2, new Distance(1));
        final Section section2 = new Section(station2, station3, new Distance(2));
        final Section section3 = new Section(station2, station4, new Distance(3));
        final SubwayRouteMap subway = new SubwayRouteMap(List.of(section1, section2, section3));

        //when
        final List<Station> path1 = subway.shortestPathBetween(station1, station3);
        final List<Station> path2 = subway.shortestPathBetween(station1, station4);

        //then
        assertAll(() -> assertThat(path1).containsExactly(station1, station2, station3), () -> assertThat(path2).containsExactly(station1, station2, station4));
    }

    @DisplayName("10km 이내는 기본 요금이 부과된다.")
    @ValueSource(ints = {9, 10})
    @ParameterizedTest
    void fareBetween_defaultFareDistance(final int distance) {
        //given
        final Section section1 = new Section(station1, station2, new Distance(1));
        final Section section2 = new Section(station2, station3, new Distance(distance - 1));
        final SubwayRouteMap subway = new SubwayRouteMap(List.of(section1, section2));

        //when
        final int fare = subway.fareBetween(station1, station3);

        //then
        assertThat(fare).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("10km~50km: 5km 까지 마다 100원 추가")
    @ValueSource(ints = {11, 50})
    @ParameterizedTest
    void fareBetween_10kmTo50km(final int distance) {
        //given
        final Section section1 = new Section(station1, station2, new Distance(10));
        final Section section2 = new Section(station2, station3, new Distance(distance - 10));
        final SubwayRouteMap subway = new SubwayRouteMap(List.of(section1, section2));

        final int overDistance = distance - DEFAULT_FARE_DISTANCE;
        final int overFare = (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);

        //when
        final int fare = subway.fareBetween(station1, station3);

        //then
        assertThat(fare).isEqualTo(DEFAULT_FARE + overFare);
    }

    @DisplayName("50km 초과: 10 ~ 50 -> 5km 까지 마다 100원 추가, 50 ~ -> 8km 까지 마다 100원 추가")
    @ValueSource(ints = {51, 1000})
    @ParameterizedTest
    void fareBetween_over50km(final int distance) {
        //given
        final Section section1 = new Section(station1, station2, new Distance(10));
        final Section section2 = new Section(station2, station3, new Distance(distance - 10));
        final SubwayRouteMap subway = new SubwayRouteMap(List.of(section1, section2));

        final int additionalDistance = distance - DEFAULT_FARE_DISTANCE;
        final int tenToFifty = (int) ((Math.ceil((TEN_TO_FIFTY - 1) / 5) + 1) * 100);
        final int overFifty = (int) ((Math.ceil((additionalDistance - TEN_TO_FIFTY) / 8) + 1) * 100);


        //when
        final int fare = subway.fareBetween(station1, station3);

        //then
        assertThat(fare).isEqualTo(DEFAULT_FARE + tenToFifty + overFifty);
    }
}
