package subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.section.domain.Section;
import subway.station.domain.Station;
import subway.vo.Name;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathTest {

    Path path;

    @BeforeEach
    void setUp() {
        List<Station> stations = List.of(
                Station.of(1L, Name.from("잠실역")),
                Station.of(2L, Name.from("잠실새내역")),
                Station.of(3L, Name.from("선릉역")),
                Station.of(4L, Name.from("종합운동장"))
        );

        List<Section> sections = List.of(
                Section.of(1L, stations.get(0), stations.get(1), 5),
                Section.of(1L, stations.get(1), stations.get(2), 9),
                Section.of(2L, stations.get(0), stations.get(3), 6),
                Section.of(2L, stations.get(3), stations.get(2), 7)
        );
        path = new Path(stations, sections);
    }

    @DisplayName("Graph에 저장된 경로들을 조회해서 최단 경로를 찾아서 반환한다.")
    @Test
    void findPath() {
        // when
        List<String> paths = path.findPath("잠실역", "선릉역");

        // then
        assertAll(
                () -> assertThat(paths.size()).isEqualTo(3),
                () -> assertThat(paths.get(0)).isEqualTo("잠실역"),
                () -> assertThat(paths.get(1)).isEqualTo("종합운동장"),
                () -> assertThat(paths.get(2)).isEqualTo("선릉역")
        );
    }

    @DisplayName("Graph에 저장된 경로들을 조회해서 최단 경로를 찾아서 반환한다.")
    @Test
    void findPathDistance() {
        // when
        double distance = path.findPathDistance("잠실역", "선릉역");

        // then
        assertThat(distance).isEqualTo(13);
    }


}
