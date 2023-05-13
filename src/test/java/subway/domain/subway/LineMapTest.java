package subway.domain.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.SectionsFixture.createSections;

class LineMapTest {

    @Test
    @DisplayName("연결된 선로를 순서대로 출력한다.")
    void find_ordered_line_map_success() {
        // given
        Sections sections = createSections();
        LineMap lineMap = new LineMap(sections);

        // when
        List<Station> orderedStations = lineMap.getOrderedStations(sections);

        // then
        assertAll(
                () -> assertThat(orderedStations.size()).isEqualTo(3),
                () -> assertThat(orderedStations.get(0).getName()).isEqualTo("잠실역"),
                () -> assertThat(orderedStations.get(1).getName()).isEqualTo("잠실새내역"),
                () -> assertThat(orderedStations.get(2).getName()).isEqualTo("종합운동장역")
        );
    }
}
