package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.exception.IllegalAddStationException;
import subway.exception.StationAlreadyExistException;
import java.util.LinkedList;
import java.util.List;

class StationsTest {

    private Stations stations;

    @BeforeEach
    void setUp() {
        this.stations = Stations.emptyStations();
    }

    private final Station station1 = new Station(1L, "1");
    private final Station station2 = new Station(2L, "2");
    private final Station station3 = new Station(3L, "3");

    @Test
    void 두_개의_역을_함께_추가한다() {
        stations.addTwoStation(station1, station2);

        assertThat(stations.getStations()).containsExactly(station1, station2);
    }

    @Test
    void 특정_역_앞에_역을_추가한다() {
        stations.addTwoStation(station1, station2);

        stations.addBeforeAt(station2, station3);

        assertThat(stations.getStations()).containsExactly(station1, station3, station2);
    }

    @Test
    void 맨_앞에_역을_추가한다() {
        stations.addTwoStation(station1, station2);

        stations.addFirst(station3);

        assertThat(stations.getStations()).containsExactly(station3, station1, station2);
    }

    @Test
    void 둘_이상의_역이_존재할_때_특정_역_앞에_새로운_역을_추가한다() {
        stations.addTwoStation(station1, station2);

        stations.addBeforeAt(station2, station3);

        assertThat(stations.getStations()).containsExactly(station1, station3, station2);
    }

    @Test
    void 특정_역_앞에_추가할_때_이미_존재하는_역을_넣을_경우_예외가_발생한다() {
        Stations stations = new Stations(new LinkedList<>(List.of(station1, station2)));

        assertThatThrownBy(() -> stations.addBeforeAt(station1, station2))
                .isInstanceOf(StationAlreadyExistException.class);
    }

    @Test
    void 역이_2개_이하_존재할_때_역을_중간에_하나_추가하면_예외가_발생한다() {
        assertThatThrownBy(() -> stations.addBeforeAt(station1, station2))
                .isInstanceOf(IllegalAddStationException.class);
    }

    @Test
    void 마지막에_역을_추가한다() {
        stations.addTwoStation(station1, station2);

        stations.addLast(station3);

        assertThat(stations.getStations()).containsExactly(station1, station2, station3);
    }

    @Test
    void 역이_2개_이하_존재할_때_역을_마지막에_추가하면_예외가_발생한다() {
        assertThatThrownBy(() -> stations.addLast(station1))
                .isInstanceOf(IllegalAddStationException.class);
    }

    @Test
    void 마지막에_역을_추가할_때_이미_존재하는_역을_넣을_경우_예외가_발생한다() {
        Stations stations = new Stations(new LinkedList<>(List.of(station1, station2)));

        assertThatThrownBy(() -> stations.addLast(station1))
            .isInstanceOf(StationAlreadyExistException.class);
    }
}
