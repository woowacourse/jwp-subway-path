package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.exception.section.DisconnectedSectionException;
import subway.exception.station.DuplicateStationNameException;
import subway.exception.station.NotFoundStationException;

class JgraphtForDijkstraTest {

    JgraphtForShortestPathAlgorithm jgraphtForDijkstra;

    @BeforeEach
    void init() {
        jgraphtForDijkstra = new JgraphtForShortestPathAlgorithm();
    }

    @DisplayName("최단 경로의 역 조회 테스트")
    @Test
    void getShortestPath() {
        int distanceValue = 3;
        Distance distance = new Distance(distanceValue);
        Station station1 = new Station("일역");
        Station station2 = new Station("이역");
        Section section = Section.builder()
                .startStation(station1)
                .endStation(station2)
                .distance(distance).build();
        Sections sections = new Sections(List.of(section));

        List<Station> actual = jgraphtForDijkstra.getShortestPath(sections, station1, station2);

        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactly(station1, station2);
    }

    @DisplayName("최단 경로의 거리 조회 테스트")
    @Test
    void validateNotConnectedRoute() {
        int distanceValue = 3;
        Distance distance = new Distance(distanceValue);
        Station station1 = new Station("일역");
        Station station2 = new Station("이역");
        Station station3 = new Station("삼역");
        Station station4 = new Station("사역");

        Section section1 = Section.builder()
                .startStation(station1)
                .endStation(station2)
                .distance(distance).build();

        Section section2 = Section.builder()
                .startStation(station2)
                .endStation(station3)
                .distance(distance).build();

        Section section3 = Section.builder()
                .startStation(station3)
                .endStation(station4)
                .distance(distance).build();

        Sections sections = new Sections(List.of(section1, section2, section3));

        Distance shortestPathWeight = jgraphtForDijkstra.getShortestPathWeight(sections, station1, station4);

        assertThat(shortestPathWeight).isEqualTo(new Distance(9));
    }


    @Nested
    @DisplayName("구간 요금 조회 예외 테스트")
    class ValidateSectionFee {

        @DisplayName("출발역과 도착역이 같은 경우 예외 테스트")
        @Test
        void validateFeeBySameStations() {
            int distanceValue = 3;
            Distance distance = new Distance(distanceValue);
            Station station1 = new Station("일역");
            Station station2 = new Station("이역");
            Section section = Section.builder()
                    .startStation(station1)
                    .endStation(station2)
                    .distance(distance).build();
            Sections sections = new Sections(List.of(section));

            assertThatThrownBy(() -> jgraphtForDijkstra.getShortestPath(sections, station1, station1))
                    .isInstanceOf(DuplicateStationNameException.class)
                    .hasMessage("같은 역으로 경로를 조회할 수 없습니다.");
        }

        @DisplayName("출발역과 도착역이 같은 경우 예외 테스트")
        @Test
        void validateFeeBySameStations2() {
            int distanceValue = 3;
            Distance distance = new Distance(distanceValue);
            Station station1 = new Station("일역");
            Station station2 = new Station("이역");
            Section section = Section.builder()
                    .startStation(station1)
                    .endStation(station2)
                    .distance(distance).build();
            Sections sections = new Sections(List.of(section));

            assertThatThrownBy(() -> jgraphtForDijkstra.getShortestPathWeight(sections, station1, station1))
                    .isInstanceOf(DuplicateStationNameException.class)
                    .hasMessage("같은 역으로 경로를 조회할 수 없습니다.");
        }


        @DisplayName("출발역은 존재하지만 도착역이 존재하지 않는 경우 예외 테스트")
        @Test
        void validateFeeByNotExistsStations1() {
            int distanceValue = 3;
            Distance distance = new Distance(distanceValue);
            Station station1 = new Station("일역");
            Station station2 = new Station("이역");
            Section section = Section.builder()
                    .startStation(station1)
                    .endStation(station2)
                    .distance(distance).build();
            Sections sections = new Sections(List.of(section));

            assertThatThrownBy(() -> jgraphtForDijkstra.getShortestPath(sections, station1, new Station("삼역")))
                    .isInstanceOf(NotFoundStationException.class)
                    .hasMessage("출발역 또는 도착역이 존재하지 않습니다.");
        }

        @DisplayName("도착역은 존재하지만 출발역이 존재하지 않는 경우 예외 테스트")
        @Test
        void validateFeeByNotExistsStations2() {
            int distanceValue = 3;
            Distance distance = new Distance(distanceValue);
            Station station1 = new Station("일역");
            Station station2 = new Station("이역");
            Section section = Section.builder()
                    .startStation(station1)
                    .endStation(station2)
                    .distance(distance).build();
            Sections sections = new Sections(List.of(section));

            assertThatThrownBy(() -> jgraphtForDijkstra.getShortestPath(sections, new Station("삼역"), station2))
                    .isInstanceOf(NotFoundStationException.class)
                    .hasMessage("출발역 또는 도착역이 존재하지 않습니다.");
        }

        @DisplayName("연결되지 않은 구간에서의 경로 조회 시 예외 테스트")
        @Test
        void validateNotConnectedRoute() {
            int distanceValue = 3;
            Distance distance = new Distance(distanceValue);
            Station station1 = new Station("일역");
            Station station2 = new Station("이역");
            Station station3 = new Station("삼역");
            Station station4 = new Station("사역");

            Section section1 = Section.builder()
                    .startStation(station1)
                    .endStation(station2)
                    .distance(distance).build();

            Section section2 = Section.builder()
                    .startStation(station3)
                    .endStation(station4)
                    .distance(distance).build();

            Sections sections = new Sections(List.of(section1, section2));

            assertThatThrownBy(() -> jgraphtForDijkstra.getShortestPath(sections, station1, station4))
                    .isInstanceOf(DisconnectedSectionException.class)
                    .hasMessage("연결되어 있지 않은 구간은 조회할 수 없습니다.");
        }

    }

}