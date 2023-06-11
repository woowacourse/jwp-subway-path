package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.StationFixture.*;

class SubwayTest {
    

    @Test
    @DisplayName("환승구간이 있을때 최단거리를 구하는지 확인한다.")
    void findShortestPath() {
        Section section1 = new Section(강남1, 잠실2, new Distance(7), 1);
        Section section2 = new Section(잠실2, 선릉3, new Distance(10), 1);
        Section section3 = new Section(강남1, 석촌4, new Distance(6), 2);
        Section section4 = new Section(석촌4, 선릉3, new Distance(9), 2);

        Line line1 = new Line(1L, "2호선", "초록", List.of(section1, section2));
        Line line2 = new Line(2L, "2호선", "초록", List.of(section3, section4));

        Subway subway = Subway.from(List.of(line1, line2));
        int distance = subway.findShortestDistance(1, 3);

        assertThat(distance).isEqualTo(15);
    }

    @Test
    @DisplayName("환승구간이 있을때 한 역을 거치는 짧은 거리와, 도착지로 바로 가는 긴 거리 중 전자의 경로로 가는지 확인한다.")
    void findPathTreeStationButShortestPath() {
        Section section1 = new Section(강남1, 잠실2, new Distance(7), 1);
        Section section2 = new Section(잠실2, 선릉3, new Distance(10), 1);
        Section section3 = new Section(강남1, 선릉3, new Distance(20), 2);


        Line line1 = new Line(1L, "2호선", "초록", List.of(section1, section2));
        Line line2 = new Line(2L, "2호선", "초록", List.of(section3));

        Subway subway = Subway.from(List.of(line1, line2));
        int distance = subway.findShortestDistance(1, 3);
        List<Station> path = subway.findShortestPath(1, 3);
        List<Station> expectedPath = List.of(강남1, 잠실2, 선릉3);

        assertThat(distance).isEqualTo(17);
        assertThat(path.containsAll(expectedPath)).isTrue();
    }

    @Test
    @DisplayName("10km 이내일시 기본 요금이 부과되는지 확인한다.")
    void getFareIn10km() {
        Section section1 = new Section(강남1, 잠실2, new Distance(6), 1);
        Section section2 = new Section(잠실2, 선릉3, new Distance(4), 1);

        Line line1 = new Line(1L, "2호선", "초록", List.of(section1, section2));

        Subway subway = Subway.from(List.of(line1));
        int fare = subway.findFare(1, 3);

        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("50km 이내일 시 추가 요금이 제대로 부과되는지 확인한다.")
    void getFareIn0km() {
        Section section1 = new Section(강남1, 잠실2, new Distance(10), 1);
        Section section2 = new Section(잠실2, 선릉3, new Distance(6), 1);

        Line line1 = new Line(1L, "2호선", "초록", List.of(section1, section2));

        Subway subway = Subway.from(List.of(line1));
        int fare = subway.findFare(1, 3);

        assertThat(fare).isEqualTo(1450);
    }

    @Test
    @DisplayName("50km 일 시 추가 요금이 제대로 부과되는지 확인한다.")
    void getFareAt50km() {
        Section section1 = new Section(강남1, 잠실2, new Distance(40), 1);
        Section section2 = new Section(잠실2, 선릉3, new Distance(10), 1);

        Line line1 = new Line(1L, "2호선", "초록", List.of(section1, section2));

        Subway subway = Subway.from(List.of(line1));
        int fare = subway.findFare(1, 3);

        assertThat(fare).isEqualTo(2050);
    }

    @Test
    @DisplayName("50km 를 넘을시 추가 요금이 제대로 부과되는지 확인한다.")
    void getFareOver50km() {
        Section section1 = new Section(강남1, 잠실2, new Distance(40), 1);
        Section section2 = new Section(잠실2, 선릉3, new Distance(10), 1);
        Section section3 = new Section(선릉3, 석촌4, new Distance(8), 1);

        Line line1 = new Line(1L, "2호선", "초록", List.of(section1, section2, section3));

        Subway subway = Subway.from(List.of(line1));
        int fare = subway.findFare(1, 4);
        assertThat(subway.findShortestDistance(1, 4)).isEqualTo(58);
        assertThat(fare).isEqualTo(2150);
    }

}