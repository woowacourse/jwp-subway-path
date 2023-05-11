package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationsTest {

    private Station station1 = new Station(1L, "1");
    private Station station2 = new Station(2L, "2");

    @DisplayName("특정 역 앞에 역을 추가한다")
    @Test
    void addBeforeAt_success() {
        //given
        Stations stations = new Stations(new LinkedList<>(List.of(station1)));

        //when
        stations.addBeforeAt(station1, station2);

        //then
        assertThat(stations.getStations()).containsExactly(station2, station1);
    }

    @DisplayName("3개 이상의 역이 있을 때, 특정 역 앞에 역을 추가한다")
    @Test
    void addBeforeAt_success_many() {
        //given
        Station station3 = new Station(3L, "3");
        Stations stations = new Stations(new LinkedList<>(List.of(station1, station2, station3)));
        Station station4 = new Station(4L, "4");

        //when
        stations.addBeforeAt(station2, station4);

        //then
        assertThat(stations.getStations()).containsExactly(station1, station4, station2, station3);
    }

    @DisplayName("이미 존재하는 역을 넣을 경우 예외가 발생한다.")
    @Test
    void addBeforeAt_fail() {
        //given
        Stations stations = new Stations(new LinkedList<>(List.of(station1, station2)));

        //then
        Assertions.assertThatThrownBy(() -> stations.addBeforeAt(station1, station2))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("마지막에 역을 추가한다")
    @Test
    void addLast_success() {
        //given
        Stations stations = new Stations(new LinkedList<>(List.of(station1)));

        //when
        stations.addLast(station2);

        //then
        assertThat(stations.getStations()).containsExactly(station1, station2);
    }

    @DisplayName("이미 존재하는 역을 넣을 경우 예외가 발생한다.")
    @Test
    void addLast_fail() {
        //given
        Stations stations = new Stations(new LinkedList<>(List.of(station1, station2)));

        //then
        Assertions.assertThatThrownBy(() -> stations.addLast(station1))
            .isInstanceOf(IllegalStateException.class);
    }
}