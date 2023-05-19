package subway.domain.subway;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.subway.Section;
import subway.domain.subway.Sections;

class SectionsTest {
    private final List<Section> sections = new ArrayList<>();

    @BeforeEach
    private void setUp() {
        sections.add(new Section(3L, 3L, 4L, 1L, 10));
        sections.add(new Section(1L, 1L, 2L, 1L, 10));
        sections.add(new Section(2L, 2L, 3L, 1L, 10));
    }

    @Test
    @DisplayName("구간을 순서대로 정렬한다.")
    void lineUpTest() {
        Sections sections1 = new Sections(sections);
        assertAll(
            () -> assertThat(sections1.findFirstStationId()).isEqualTo(1L),
            () -> assertThat(sections1.findLastStationId()).isEqualTo(4L));
    }
}
