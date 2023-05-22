package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.fare.Fare;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Sections;
import subway.domain.line.Station;

class LineTest {

    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, "잠실역");
        station2 = new Station(2L, "강남역");
        station3 = new Station(3L, "선릉역");

        Section section1 = new Section(null, station1, station2, new Distance(5));
        Section section2 = new Section(null, station2, station3, new Distance(7));

        line = new Line(1L, "1호선", new Fare(0), new Sections(new LinkedList<>(List.of(section1, section2))));
    }

    @DisplayName("노선이 역을 가지고 있으면 true를 반환한다.")
    @Test
    void hasStationTrue() {
        // when, then
        assertSoftly(softly -> {
            softly.assertThat(line.hasStation(station1)).isTrue();
            softly.assertThat(line.hasStation(station2)).isTrue();
            softly.assertThat(line.hasStation(station3)).isTrue();
        });
    }

    @DisplayName("노선이 역을 가지고 있지 않으면 false를 반환한다.")
    @Test
    void hasStationFalse() {
        // given
        Station station = new Station(5L, "신림역");

        // when, then
        assertThat(line.hasStation(station)).isFalse();
    }

    @DisplayName("구간의 왼쪽 역과 같은 역이 주어지면 true를 반환한다.")
    @Test
    void hasLeftStationInSectionTrue() {
        // given
        Station station = new Station(1L, "잠실역");

        // when, then
        assertThat(line.hasLeftStationInSection(station)).isTrue();
    }

    @DisplayName("구간의 오른쪽 역과 같은 역이 주어지면 true를 반환한다.")
    @Test
    void hasRightStationInSectionTrue() {
        // given
        Station station = new Station(3L, "선릉역");

        // when, then
        assertThat(line.hasRightStationInSection(station)).isTrue();
    }

    @DisplayName("찾는 역이 구간의 왼쪽 역에 해당하는 구간을 반환한다.")
    @Test
    void findSectionByLeftStation() {
        // given
        Station station = new Station(1L, "잠실역");

        // when
        Section section = line.findSectionByLeftStation(station);

        // then
        assertThat(section.getLeft()).isEqualTo(station);
    }

    @DisplayName("찾는 역이 구간의 오른쪽 역에 해당하는 구간을 반환한다.")
    @Test
    void findSectionByRightStation() {
        // given
        Station station = new Station(3L, "선릉역");

        // when
        Section section = line.findSectionByRightStation(station);

        // then
        assertThat(section.getRight()).isEqualTo(station);
    }

    @DisplayName("노선에 구간이 한 개 존재하면 true를 반환한다.")
    @Test
    void hasOneSectionTrue() {
        // given
        Station newStation1 = new Station(1L, "잠실역");
        Station newStation2 = new Station(2L, "강남역");
        Section section = new Section(null, newStation1, newStation2, new Distance(3));
        Line newLine = new Line(2L, "2호선", new Fare(100), new Sections(new LinkedList<>(List.of(section))));

        // when, then
        assertThat(newLine.hasOneSection()).isTrue();
    }

    @DisplayName("노선에 구간이 두 개 이상 존재하면 false를 반환한다.")
    @Test
    void hasOneSectionFalse() {
        // when, then
        assertThat(line.hasOneSection()).isFalse();
    }

    @DisplayName("상행 종점에 해당하는 역이면 true를 반환한다.")
    @Test
    void isLastStationAtLeftTrue() {
        // given
        Station station = new Station(1L, "잠실역");

        // when, then
        assertThat(line.isLastStationAtLeft(station)).isTrue();
    }

    @DisplayName("상행 종점에 해당하지 않는 역이면 false를 반환한다.")
    @Test
    void isLastStationAtLeftFalse() {
        // given
        Station station = new Station(2L, "강남역");

        // when, then
        assertThat(line.isLastStationAtLeft(station)).isFalse();
    }

    @DisplayName("하행 종점에 해당하는 역이면 true를 반환한다.")
    @Test
    void isLastStationAtRightTrue() {
        // given
        Station station = new Station(3L, "선릉역");

        // when, then
        assertThat(line.isLastStationAtRight(station)).isTrue();
    }

    @DisplayName("하행 종점에 해당하지 않는 역이면 false를 반환한다.")
    @Test
    void isLastStationAtRightFalse() {
        // given
        Station station = new Station(2L, "강남역");

        // when, then
        assertThat(line.isLastStationAtRight(station)).isFalse();
    }

    @DisplayName("노선의 전체 역을 조회한다.")
    @Test
    void findLeftToRightRoute() {
        // when
        List<Station> stations = line.findLeftToRightRoute();

        // then
        assertThat(stations).containsExactly(station1, station2, station3);
    }
}
