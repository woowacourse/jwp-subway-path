package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("Sections를 생성하면 정렬된 Section이 되어야 한다.")
    void create_sorted_success() {
        // given
        Sections sections = new Sections(new ArrayList<>(List.of(
                new Section(new Station("강남역"), new Station("역삼역"), 1),
                new Section(new Station("선릉역"), new Station("삼성역"), 1),
                new Section(new Station("역삼역"), new Station("선릉역"), 1),
                new Section(new Station("교대역"), new Station("강남역"), 1))));

        // expect
        assertThat(sections.getSections()).usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Section(new Station("교대역"), new Station("강남역"), 1),
                        new Section(new Station("강남역"), new Station("역삼역"), 1),
                        new Section(new Station("역삼역"), new Station("선릉역"), 1),
                        new Section(new Station("선릉역"), new Station("삼성역"), 1)));
    }
}
