package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Transactional
class LineTest {

    @Test
    @DisplayName("유효한 입력시 노선이 정상적으로 생성된다.")
    void constructor_success() {
        //given
        Distance distance = new Distance(10);

        Station nextStation = new Station("하행역", distance);
        Station station = new Station("상행역", distance);

        //when, then
        Assertions.assertThatCode(() -> new Line("2호선", "초록색", new Stations(List.of(station, nextStation))))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("노선 생성시 2개의 역을 가지고 있지 않다면 예외발생")
    void constructor_fail_station_null() {
        //given
        Distance distance = new Distance(10);
        Station station = new Station("상행역", distance);

        //when, then
        assertThatThrownBy(() -> new Line("2호선", "초록색", new Stations(List.of(station))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 생성시 역을 2개 입력해야합니다");
    }


    @Test
    @DisplayName("findStation()를 통해 특정 이름의 Station을 찾을 수 있다")
    void findStation_success() {
        //given
        Stations stations = new Stations(new ArrayList<>());
        Lines lines = new Lines(new ArrayList<>());

        Station nextStation = new Station("역삼역", new Distance(0));
        Station station = new Station("강남역", new Distance(10));
        stations.addStation(station);
        stations.addStation(nextStation);

        Line line = new Line("2호선", "초록색", new Stations(List.of(station, nextStation)));
        lines.addLine(line);

        //when
        Station actualStation = line.getStations().findByName("강남역");
        boolean actual = actualStation.getName().equals("강남역");


        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("findStation()를 통해 특정 이름의 Station을 찾을 수 있다")
    void findStation_fail() {
        //given
        Stations stations = new Stations(new ArrayList<>());
        Lines lines = new Lines(new ArrayList<>());

        Station nextStation = new Station("역삼역", new Distance(0));
        Station station = new Station("강남역", new Distance(10));
        stations.addStation(station);
        stations.addStation(nextStation);

        Line line = new Line("2호선", "초록색", new Stations(List.of(station, nextStation)));
        lines.addLine(line);

        //when
        Station actualStation = line.getStations().findByName("강남역");
        boolean actual = actualStation.getName().equals("성수역");

        //then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("hasStation()를 호출할 때 특정 이름의 Station을 가지고 있다면 true를 반환한다.")
    void hasStation_true() {
        //given
        Stations stations = new Stations(new ArrayList<>());
        Lines lines = new Lines(new ArrayList<>());

        Station nextStation = new Station("역삼역", new Distance(0));
        Station station = new Station("강남역", new Distance(10));
        stations.addStation(station);
        stations.addStation(nextStation);

        Line line = new Line("2호선", "초록색", new Stations(List.of(station, nextStation)));
        lines.addLine(line);

        //when
        boolean actual = line.getStations().isExistStation("강남역");

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("hasStation()를 호출할 때 특정 이름의 Station을 가지고 있다면 false를 반환한다.")
    void hasStation_false() {
        //given
        Stations stations = new Stations(new ArrayList<>());
        Lines lines = new Lines(new ArrayList<>());

        Station nextStation = new Station("역삼역", new Distance(0));
        Station station = new Station("강남역", new Distance(10));
        stations.addStation(station);
        stations.addStation(nextStation);

        Line line = new Line("2호선", "초록색", new Stations(List.of(station, nextStation)));
        lines.addLine(line);

        //when
        boolean actual = line.getStations().isExistStation("잠실역");

        //then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("getStations()를 통해 모든 Station들을 가져올 수 있다")
    void getStations_success() {
        //given
        Stations stations = new Stations(new ArrayList<>());
        Lines lines = new Lines(new ArrayList<>());

        Station nextStation = new Station("역삼역", new Distance(0));
        Station station = new Station("강남역", new Distance(10));
        stations.addStation(station);
        stations.addStation(nextStation);

        Line line = new Line("2호선", "초록색", new Stations(List.of(station, nextStation)));
        lines.addLine(line);

        //when
        int actual = line.getStations().getStationsSize();

        //then
        Assertions.assertThat(actual).isEqualTo(2);
    }


    @Nested
    @DisplayName("addStation()을 호출할 때")
    @Transactional
    class addSection {
        private Line line;
        private Station upEndStation;
        private Station downEndStation;
        private Lines lines;
        private Stations stations;

        @BeforeEach
        void init() {
            stations = new Stations(new ArrayList<>());
            lines = new Lines(new ArrayList<>());

            String name = "강남역";
            Distance distance = new Distance(10);
            upEndStation = new Station(name, distance);
            downEndStation = new Station("역삼역", new Distance(0));

            stations.addStation(upEndStation);
            stations.addStation(downEndStation);

            line = new Line("2호선", "초록색", new Stations(new ArrayList<>(List.of(upEndStation, downEndStation))));
            lines.addLine(line);
        }

        @Test
        @DisplayName("상행 종점에 역을 추가할 수 있다")
        void upStation_success() {
            //given
            String newStationName = "삼성역";
            Distance distance = new Distance(3);
            Station newStation = new Station(newStationName, distance);

            //when
            int index = line.getStations().findIndex(upEndStation);
            line.getStations().addStationByIndex(index, newStation);
            stations.addStation(newStation);

            int expectedSize = line.getStations().getStationsSize();
            int actualDistance = line.getStations().findByName("삼성역").getDistance().getValue();

            //then
            assertThat(expectedSize).isEqualTo(3);
            assertThat(line.getStations().getStations().get(0)).isEqualTo(newStation);
            assertThat(actualDistance).isEqualTo(3);
        }

        @Test
        @DisplayName("하행 종점에 역을 추가할 수 있다")
        void downStation_success() {
            //given
            String newStationName = "삼성역";
            Distance distance = new Distance(3);
            Station newStation = new Station(newStationName, distance);

            //when
            int index = line.getStations().findIndex(downEndStation);
            line.getStations().addStation(newStation);
            stations.addStation(newStation);


            int expectedSize = line.getStations().getStationsSize();
            int actualDistance = line.getStations().findByName("삼성역").getDistance().getValue();

            //then
            assertThat(expectedSize).isEqualTo(3);
            assertThat(line.getStations().getStations().get(2)).isEqualTo(newStation);
            assertThat(actualDistance).isEqualTo(3);
        }

        @Test
        @DisplayName("두 역 사이에 역을 추가할 때 기존 역의 상행 위치에 역을 추가할 수 있다")
        void between_upStation_success() {
            //given
            String newStationName = "삼성역";
            Distance distance = new Distance(6);
            Station newStation = new Station(newStationName, distance);

            //when
            int index = line.getStations().findIndex(downEndStation);
            line.getStations().addStationByIndex(index, newStation);
            upEndStation.setDistance(new Distance(upEndStation.getDistance().getValue() - newStation.getDistance().getValue()));

            int expectedSize = line.getStations().getStationsSize();
            int actualFirst = line.getStations().findByName("강남역").getDistance().getValue();
            int actualSecond = line.getStations().findByName("삼성역").getDistance().getValue();

            //then
            assertThat(expectedSize).isEqualTo(3);
            assertThat(line.getStations().getStations().get(1)).isEqualTo(newStation);

            assertThat(actualFirst).isEqualTo(4);
            assertThat(actualSecond).isEqualTo(6);
        }

        @Test
        @DisplayName("두 역 사이에 역을 추가할 때 기존 역의 하행 위치에 역을 추가할 수 있다")
        void between_downStation_success() {
            //given
            String newStationName = "선릉역";
            Distance distance = new Distance(6);
            Station newStation = new Station(newStationName, distance);

            //when
            int index = line.getStations().findIndex(upEndStation);
            line.getStations().addStationByIndex(index + 1, newStation);
            upEndStation.setDistance(new Distance(6));
            newStation.setDistance(new Distance(4));

            int expectedSize = line.getStations().getStationsSize();
            int actualFirst = line.getStations().findByName("강남역").getDistance().getValue();
            int actualSecond = line.getStations().findByName("선릉역").getDistance().getValue();

            //then
            assertThat(expectedSize).isEqualTo(3);
            assertThat(line.getStations().getStations().get(1)).isEqualTo(newStation);

            assertThat(actualFirst).isEqualTo(6);
            assertThat(actualSecond).isEqualTo(4);
        }
    }
}

@Nested
@DisplayName("remove를 호출할 때")
class removeStation {
    private Line line;
    private Lines lines;
    private Station upEndStation;
    private Station downEndStation;
    private Station newStation;

    @BeforeEach
    void init() {
        lines = new Lines(new ArrayList<>());

        String name = "강남역";
        downEndStation = new Station("역삼역", new Distance(0));
        Distance distance = new Distance(10);
        upEndStation = new Station(name, distance);

        line = new Line("2호선", "초록색", new Stations(List.of(upEndStation, downEndStation)));
        newStation = new Station("선릉역", new Distance(4));

        line.getStations().addStation(newStation);
        line.getStations().findByName("역삼역").setDistance(new Distance(4));

        lines.addLine(line);
    }

    @Test
    @DisplayName("상행 종점을 삭제할 수 있다")
    void upEndStation_success() {
        //given
        String removeName = "강남역";

        //when
        line.getStations().remove(upEndStation);
        int expectedSize = line.getStations().getStationsSize();
        Station newUpEndStation = line.getStations().getStations().get(0);

        //then
        assertThat(expectedSize).isEqualTo(2);
        assertThat(newUpEndStation.getName()).isEqualTo("역삼역");

        assertThat(newUpEndStation.getDistance().getValue()).isEqualTo(4);

    }

    @Test
    @DisplayName("하행 종점을 삭제할 수 있다")
    void downEndStation_success() {
        //given
        String removeName = "선릉역";

        //when
        line.getStations().remove(newStation);
        int expectedSize = line.getStations().getStationsSize();
        Station newDownEndStation = line.getStations().getStations().get(1);

        //then
        assertThat(expectedSize).isEqualTo(2);
        assertThat(newDownEndStation.getName()).isEqualTo("역삼역");
    }

    @Test
    @DisplayName("두 역 사이에 있는 역을 삭제할 수 있다")
    void middle_Station_success() {
        //given
        String removeName = "역삼역";

        //when
        line.getStations().remove(downEndStation);
        int expectedSize = line.getStations().getStationsSize();

        Station upEndStation = line.getStations().getStations().get(0);
        Station nextStation = line.getStations().getStations().get(1);

        //then
        assertThat(expectedSize).isEqualTo(2);
        assertThat(nextStation.getName()).isEqualTo("선릉역");
    }
}
