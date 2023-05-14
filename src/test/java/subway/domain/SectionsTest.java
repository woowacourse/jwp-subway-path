package subway.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Sections는")
class SectionsTest {

/*    @Test
    @DisplayName("역마다 연결된 구간 정보를 관리한다.")
    void sectionsTest() {
        // given
        Station station1 = Station.of(1L, "잠실나루");
        Station station2 = Station.of(2L, "잠실");
        Station station3 = Station.of(3L, "잠실새내");
        Line line = Line.of("2호선", "초록색");

        Section section1 = Section.of(station1, station2, 10, line);
        Section section2 = Section.of(station2, station3, 5, line);

        List<Section> sections = new ArrayList<>(List.of(section1, section2));

        // then
        assertDoesNotThrow(() -> SubwayMap.from(sections));
    }

    @Test
    @DisplayName("노선에 처음 추가되는 구간을 생성할 수 있다.")
    void addFirstSectionTest() {
        // given
        SubwayMap sections = SubwayMap.from(new ArrayList<>());
        Station station1 = Station.of(1L, "잠실나루");
        Station station2 = Station.of(2L, "잠실");
        Line line = Line.of(1L, "2호선", "초록색");

        // then
        assertDoesNotThrow(() -> sections.addInitSection(station1, station2, 10, line));
    }*/
}
