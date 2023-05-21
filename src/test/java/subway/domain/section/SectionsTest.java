package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

class SectionsTest {

    @Test
    @DisplayName("Sections를 생성하면 정렬된 Section이 되어야 한다.")
    void create_sorted_success() {
        // given
        Sections sections = new Sections(List.of(
                new Section(new Station("삼성역"), new Station("부산역"), new Distance(1)),
                new Section(new Station("강남역"), new Station("을지역"), new Distance(1)),
                new Section(new Station("부산역"), new Station("강남역"), new Distance(1)),
                new Section(new Station("잠실역"), new Station("삼성역"), new Distance(1))));

        // expect
        assertThat(sections.getSections()).usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Section(new Station("잠실역"), new Station("삼성역"), new Distance(1)),
                        new Section(new Station("삼성역"), new Station("부산역"), new Distance(1)),
                        new Section(new Station("부산역"), new Station("강남역"), new Distance(1)),
                        new Section(new Station("강남역"), new Station("을지역"), new Distance(1))));
    }
}
