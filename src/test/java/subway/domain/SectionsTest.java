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
                new Section(new Station("samsung"), new Station("busan"), 1),
                new Section(new Station("gangnam"), new Station("eulji"), 1),
                new Section(new Station("busan"), new Station("gangnam"), 1),
                new Section(new Station("jamsil"), new Station("samsung"), 1))));

        // expect
        assertThat(sections.getSections()).usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Section(new Station("jamsil"), new Station("samsung"), 1),
                        new Section(new Station("samsung"), new Station("busan"), 1),
                        new Section(new Station("busan"), new Station("gangnam"), 1),
                        new Section(new Station("gangnam"), new Station("eulji"), 1)));
    }
}
