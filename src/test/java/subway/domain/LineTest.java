package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LineTest {

    // TODO: 2023/05/10 역 추가할 때 노선에 같은 이름의 역이 존재하는지 확인하는 로직
    @Test
    @DisplayName("유효한 Section 하나를 입력받으면 노선이 정상적으로 생성된다.")
    void constructor_success() {
        //given
        String name = "상행역";
        Station nextStation = new Station("하행역");
        Distance distance = new Distance(10);
        Station station = new Station(name, nextStation, distance);

        //when, then
        assertThatCode(() -> new Line("2호선", "초록색", station)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("입력받은 역이 다음 역을 가지고 있지 않으면 예외발생")
    void constructor_fail_station_null() {
        //given
        String name = "상행역";
        Station nextStation = new Station("");
        Distance distance = new Distance(10);
        Station station = new Station(name, nextStation, distance);

        //when, then
        assertThatThrownBy(() -> new Line("2호선", "초록색", station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선을 생성할 때 최소 2개 이상의 역이 존재해야 합니다.");
    }

    @Test
    @DisplayName("findStation()를 통해 특정 이름의 Station을 찾을 수 있다")
    void findStation_success() {
        //given
        String name = "강남역";
        Station nextStation = new Station("역삼역");
        Distance distance = new Distance(10);
        Station UpEndstation = new Station(name, nextStation, distance);

        Line line = new Line("2호선", "초록색", UpEndstation);

        //when
        Station actualStation = line.findStation("강남역");
        boolean actual = actualStation.isSameName("강남역");


        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("findStation()를 통해 특정 이름의 Station을 찾을 수 있다")
    void findStation_fail() {
        //given
        String name = "강남역";
        Station nextStation = new Station("역삼역");
        Distance distance = new Distance(10);
        Station UpEndstation = new Station(name, nextStation, distance);

        Line line = new Line("2호선", "초록색", UpEndstation);

        //when
        Station actualStation = line.findStation("강남역");
        boolean actual = actualStation.isSameName("성수역");


        //then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("hasStation()를 호출할 때 특정 이름의 Station을 가지고 있다면 true를 반환한다.")
    void hasStation_true() {
        //given
        String name = "강남역";
        Station nextStation = new Station("역삼역");
        Distance distance = new Distance(10);
        Station UpEndstation = new Station(name, nextStation, distance);

        Line line = new Line("2호선", "초록색", UpEndstation);

        //when
        boolean actual = line.hasStation("강남역");

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("hasStation()를 호출할 때 특정 이름의 Station을 가지고 있다면 true를 반환한다.")
    void hasStation_false() {
        //given
        String name = "강남역";
        Station nextStation = new Station("역삼역");
        Distance distance = new Distance(10);
        Station UpEndstation = new Station(name, nextStation, distance);

        Line line = new Line("2호선", "초록색", UpEndstation);

        //when
        boolean actual = line.hasStation("잠실역");

        //then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("getStations()를 통해 모든 Station들을 가져올 수 있다")
    void getStations_success() {
        //given
        String name = "강남역";
        Station nextStation = new Station("역삼역");
        Distance distance = new Distance(10);
        Station UpEndstation = new Station(name, nextStation, distance);

        Line line = new Line("2호선", "초록색", UpEndstation);

        //when
        int actual = line.getStations().size();

        //then
        Assertions.assertThat(actual).isEqualTo(2);
    }

    @Nested
    @DisplayName("addStation()을 호출할 때")
    class addSection {
        private Line line;
        private Station upEndStation;
        private Station downEndStation;

        @BeforeEach
        void init() {
            String name = "강남역";
            downEndStation = new Station("역삼역");
            Distance distance = new Distance(10);
            upEndStation = new Station(name, downEndStation, distance);

            line = new Line("2호선", "초록색", upEndStation);
        }

        @Test
        @DisplayName("상행 종점에 역을 추가할 수 있다")
        void upStation_success() {
            //given
            String newStationName = "삼성역";
            Distance distance = new Distance(10);
            Station newStation = new Station(newStationName, upEndStation, distance);

            //when
            line.addStation(newStation);
            int expectedSize = line.getStations().size();
            int actualDistance = line.findStation("삼성역").getDistance().getValue();

            //then
            assertThat(expectedSize).isEqualTo(3);
            assertThat(line.getStations().get(0)).isEqualTo(newStation);
            assertThat(actualDistance).isEqualTo(10);
        }

        @Test
        @DisplayName("하행 종점에 역을 추가할 수 있다")
        void downStation_success() {
            //given
            String newStationName = "삼성역";
            Station newStation = new Station(newStationName);
            Distance distance = new Distance(10);
            Station station = new Station("역삼역", newStation, distance);

            //when
            line.addStation(station);
            int expectedSize = line.getStations().size();
            int actualDistance = line.findStation("역삼역").getDistance().getValue();

            //then
            assertThat(expectedSize).isEqualTo(3);
            assertThat(line.getStations().get(2)).isEqualTo(newStation);
            assertThat(actualDistance).isEqualTo(10);
        }

        @Test
        @DisplayName("두 역 사이에 역을 추가할 때 기존 역의 상행 위치에 역을 추가할 수 있다")
        void between_upStation_success() {
            //given
            String newStationName = "삼성역";
            Distance distance = new Distance(6);
            Station newStation = new Station(newStationName, downEndStation, distance);

            //when
            line.addStation(newStation);
            int expectedSize = line.getStations().size();

            int actualFirst = line.findStation("강남역").getDistance().getValue();
            int actualSecond = line.findStation("삼성역").getDistance().getValue();

            //then
            assertThat(expectedSize).isEqualTo(3);
            assertThat(line.getStations().get(1)).isEqualTo(newStation);

            assertThat(actualFirst).isEqualTo(4);
            assertThat(actualSecond).isEqualTo(6);
        }

        @Test
        @DisplayName("두 역 사이에 역을 추가할 때 기존 역의 하행 위치에 역을 추가할 수 있다")
        void between_downStation_success() {
            //given
            String newStationName = "삼성역";
            Station newStation = new Station(newStationName);
            Distance distance = new Distance(6);
            Station station = new Station("강남역", newStation, distance);

            //when
            line.addStation(station);
            int expectedSize = line.getStations().size();

            int actualFirst = line.findStation("강남역").getDistance().getValue();
            int actualSecond = line.findStation("삼성역").getDistance().getValue();

            //then
            assertThat(expectedSize).isEqualTo(3);
            assertThat(line.getStations().get(1)).isEqualTo(newStation);

            assertThat(actualFirst).isEqualTo(6);
            assertThat(actualSecond).isEqualTo(4);
        }

        @Test
        @DisplayName("두 역 사이에 역을 추가할 때 새로운 역과 기존 역 간 거리가 기존의 두 역 간 거리보다 크거나 같으면 예외 발생")
        void between_upStation_fail() {
            //given
            String newStationName = "삼성역";
            Distance distance = new Distance(10);
            Station station = new Station(newStationName, downEndStation, distance);

            //when, then
            assertThatThrownBy(() -> line.addStation(station))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("거리는 양의 정수여야 합니다");
        }

        @Test
        @DisplayName("두 역 사이에 역을 추가할 때 기존 역의 하행 위치에 역을 추가할 수 있다")
        void between_downStation_fail() {
            //given
            String newStationName = "삼성역";
            Station newStation = new Station(newStationName);
            Distance distance = new Distance(10);
            Station station = new Station("강남역", newStation, distance);

            //when, then
            assertThatThrownBy(() -> line.addStation(station))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("거리는 양의 정수여야 합니다");
        }

        @Test
        @DisplayName("상행역과 하행역 모두 노선에 존재할 경우 예외 발생")
        void exist_downStation_fail() {
            //given
            String newStationName = "역삼역";
            Station newStation = new Station(newStationName);
            Distance distance = new Distance(6);
            Station station = new Station("강남역", newStation, distance);

            //when, then
            assertThatThrownBy(() -> line.addStation(station))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 존재하는 역입니다.");
        }
    }
}
