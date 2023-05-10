package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;

class LineTest {

    @Test
    @DisplayName("역에 개수에 따른 간선의 개수가 올바르지 않으면 에러를 발생합니다.")
    void validate_stations_sections_size() {
        // given
        Station lestStation = new Station("잠실역");
        Station rightStation = new Station("삼성역");
        Station anotherStation = new Station("강남역");
        Stations stations = new Stations(List.of(lestStation, rightStation, anotherStation));
        Sections sections = new Sections(List.of(new Section(1L, lestStation, rightStation, 100)));

        // when + then
        assertThatThrownBy(() -> new Line(1L, "2호선", "#111111", stations, sections))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("역에 개수에 따른 간선의 개수가 올바르면 노선이 생성됩니다.")
    void generate_line_success() {
        // given
        Station lestStation = new Station("잠실역");
        Station rightStation = new Station("삼성역");
        Stations stations = new Stations(List.of(lestStation, rightStation));
        Sections sections = new Sections(List.of(new Section(1L, lestStation, rightStation, 100)));

        // when + then
        assertDoesNotThrow(() -> new Line(1L, "2호선", "#111111", stations, sections));
    }
}
