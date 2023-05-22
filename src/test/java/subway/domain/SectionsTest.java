package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("Sections에서 정렬된 구간들을 조회할 수 있다.")
    void getSortedSections() {
        // given
        Sections sections = new Sections(new ArrayList<>(List.of(
            new Section(new Station("강남역"), new Station("역삼역"), Distance.from(1)),
            new Section(new Station("선릉역"), new Station("삼성역"), Distance.from(1)),
            new Section(new Station("역삼역"), new Station("선릉역"), Distance.from(1)),
            new Section(new Station("교대역"), new Station("강남역"), Distance.from(1)))));

        // expect
        assertThat(sections.getSortedSections()).usingRecursiveComparison()
            .isEqualTo(List.of(
                new Section(new Station("교대역"), new Station("강남역"), Distance.from(1)),
                new Section(new Station("강남역"), new Station("역삼역"), Distance.from(1)),
                new Section(new Station("역삼역"), new Station("선릉역"), Distance.from(1)),
                new Section(new Station("선릉역"), new Station("삼성역"), Distance.from(1))));
    }
}
