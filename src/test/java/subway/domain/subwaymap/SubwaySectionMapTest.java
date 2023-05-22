package subway.domain.subwaymap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.section.SectionFixture.LINE1;
import static subway.domain.section.SectionFixture.LINE2;
import static subway.domain.section.SectionFixture.SECTIONS1;
import static subway.domain.section.SectionFixture.SECTIONS2;
import static subway.domain.section.SectionFixture.SECTIONS4;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.section.Section;

class SubwaySectionMapTest {

    static Stream<Arguments> getSections() {
        return Stream.of(
                Arguments.of(SECTIONS1),
                Arguments.of(SECTIONS2)
        );
    }

    @DisplayName("id에 해당하는 노선의 모든 구간 조회")
    @ParameterizedTest
    @MethodSource("getSections")
    void getSections(final List<Section> sections) {
        final SubwaySectionMap subwaySectionMap = SubwaySectionMap.of(List.of(LINE1), sections);
        assertThat(subwaySectionMap.getSections(1L).getSections()).containsExactly(
                SECTIONS1.get(0),
                SECTIONS1.get(1),
                SECTIONS1.get(2),
                SECTIONS1.get(3),
                SECTIONS1.get(4),
                SECTIONS1.get(5)
        );
    }

    @DisplayName("모든 id에 대해 노선의 모든 구간 조회")
    @Test
    void getAllSections() {
        final SubwaySectionMap subwaySectionMap = SubwaySectionMap.of(List.of(LINE1, LINE2), SECTIONS4);
        assertAll(
                () -> assertThat(subwaySectionMap.getSections(LINE1.getId()).getSections()).containsExactly(
                        SECTIONS4.get(0),
                        SECTIONS4.get(1),
                        SECTIONS4.get(2),
                        SECTIONS4.get(3),
                        SECTIONS4.get(4),
                        SECTIONS4.get(5)
                ),
                () -> assertThat(subwaySectionMap.getSections(LINE2.getId()).getSections()).containsExactly(
                        SECTIONS4.get(6),
                        SECTIONS4.get(7),
                        SECTIONS4.get(8),
                        SECTIONS4.get(9),
                        SECTIONS4.get(10)
                )
        );
    }
}
