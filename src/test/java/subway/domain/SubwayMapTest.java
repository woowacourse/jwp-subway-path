package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubwayMapTest {

    private SubwayMap subwayMap;

    @BeforeEach
    void setUp() {
        this.subwayMap = new SubwayMap(new Lines(new HashMap<>()));
    }

    @DisplayName("역을 추가한다")
    @Test
    void addStation_success() {
        //given
        Station station1 = new Station(1L, "잠실");
        Station station2 = new Station(2L, "선릉");

        //when
        subwayMap.addStation(station1, station2, 11);

        //then
        assertAll(
            () -> assertThat(subwayMap.containsStation(station1)).isTrue(),
            () -> assertThat(subwayMap.containsStation(station2)).isTrue(),
            () -> assertThat(subwayMap.isNextStation(station1, station2)).isTrue()
        );
    }

    @DisplayName("역을 삭제한다")
    @Test
    void deleteStation_success() {
        //given
        Station station1 = new Station(1L, "잠실");
        Station station2 = new Station(2L, "선릉");
        subwayMap.addStation(station1, station2, 11);

        //when
        subwayMap.deleteStation(station1);

        //then
        assertAll(
            () -> assertThat(subwayMap.containsStation(station1)).isFalse(),
            () -> assertThat(subwayMap.containsStation(station2)).isTrue(),
            () -> assertThat(subwayMap.isNextStation(station1, station2)).isFalse()
        );
    }
}