package subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.vo.Distance;

class SubwayMapTest {

    private SubwayMap subwayMap;

    @BeforeEach
    void setUp() {
        this.subwayMap = new SubwayMap();
    }

    @DisplayName("역을 추가한다")
    @Test
    void addStation_success() {
        //given
        Station station1 = new Station(1L, "잠실");
        Station station2 = new Station(2L, "선릉");
        Distance distance = new Distance(11);

        //when
        subwayMap.addStation(station1, station2, distance);

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
        Distance distance = new Distance(11);
        subwayMap.addStation(station1, station2, distance);

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