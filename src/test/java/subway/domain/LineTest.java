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
    @DisplayName("findStation를 호출하면 노선에 존재하는 역을 찾아 반환한다")
    void findStations(){
        //given


        //when

        //then
    }

    @Nested
    @DisplayName("addStation()을 호출할 때")
    class addSection {
        private Line line;
        private Station UpEndstation;

        @BeforeEach
        void init() {
            String name = "강남역";
            Station nextStation = new Station("역삼역");
            Distance distance = new Distance(10);
            Station UpEndstation = new Station(name, nextStation, distance);

            line = new Line("2호선", "초록색", UpEndstation);
        }

//        @Test
//        @DisplayName("상행 종점에 구간을 추가할 수 있다")
//        void upStation_success() {
//            //given
//            String newStationName ="삼성역";
//            Distance distance = new Distance(10);
//            Station newStation = new Station(newStationName, UpEndstation, distance);
//
//            //when
//            line.addStation(newStation);
//            int expectedSize=line.findStations().size();
//
//            //then
//            assertThat(expectedSize).isEqualTo(2);
//            assertThat(line.findStations().get(0)).isEqualTo(newStation);
//        }
    }

    @Test
    @DisplayName("getStations()를 통해 모든 Station들을 가져올 수 있다")
    void getStations_success(){
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
}
