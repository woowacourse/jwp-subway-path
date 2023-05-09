package subway.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationsTest {

    private Stations stations;

    @BeforeEach
    void setUp() {
        Station station1 = new Station("사당");
        Station station2 = new Station("신림");

        stations = new Stations(station1, station2);

    }

    @Test
    @DisplayName("노선에 최초로 등록할 역은 2개 등록해야 한다")
    void initTwoStations() {
        // given
        final Station station1 = new Station("잠실");
        final Station station2 = new Station("역삼");

        // then
        Assertions.assertDoesNotThrow(
                () -> new Stations(station1, station2)
        );
    }

    @Test
    @DisplayName("노선에 등록되는 역의 이름은 중복되지 않는다")
    void nameNotDuplicate() {
        // given
        final Station station1 = new Station("잠실");
        final Station station2 = new Station("잠실");

        // then
        assertThatThrownBy(
                () -> new Stations(station1, station2))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("이미 등록된 이름입니다. 다른 이름을 입력해주세요.");
    }

    @Test
    @DisplayName("노선의 역을 등록합니다")
    void addStation() {
        //given
        final Station station = new Station("잠실새내");

        //then

        Assertions.assertDoesNotThrow(
                () -> stations.addStation(station)
        );
    }
}