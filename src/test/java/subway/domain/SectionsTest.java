package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    Sections sections;

    @BeforeEach
    void setUp() {
        sections = new Sections(new LinkedList<>());
        sections.addSection(new Section(new Station("서울역"), new Station("용산역"), 10));
    }

    @Test
    void 서울_용산에_서울_기준_하행에_잠실_추가() {
        // when
        sections.addSection(new Section(new Station("서울역"), new Station("잠실역"), 1));

        // expect
        List<Section> sectionsList = sections.getSections();
        sectionsList.forEach(System.out::println);
        assertThat(sectionsList.get(0).getStartStation())
                .isEqualTo(new Station("서울역"));
        assertThat(sectionsList.get(0).getEndStation())
                .isEqualTo(new Station("잠실역"));
        assertThat(sectionsList.get(1).getStartStation())
                .isEqualTo(new Station("잠실역"));
        assertThat(sectionsList.get(1).getEndStation())
                .isEqualTo(new Station("용산역"));
    }

    @Test
    void 서울_용산에_용산_기준_상행에_잠실_추가() {
        // given
        sections.addSection(new Section(new Station("잠실역"), new Station("용산역"), 1));

        // expect
        List<Section> sectionsList = sections.getSections();
        sectionsList.forEach(System.out::println);
        assertThat(sectionsList.get(1).getStartStation())
                .isEqualTo(new Station("서울역"));
        assertThat(sectionsList.get(1).getEndStation())
                .isEqualTo(new Station("잠실역"));
        assertThat(sectionsList.get(0).getStartStation())
                .isEqualTo(new Station("잠실역"));
        assertThat(sectionsList.get(0).getEndStation())
                .isEqualTo(new Station("용산역"));
    }

    @Test
    void 서울_용산에_서울_기준_상행에_잠실_추가() {
        // when
        sections.addSection(new Section(new Station("잠실역"), new Station("서울역"), 1));

        // expect
        List<Section> sectionsList = sections.getSections();
        sectionsList.forEach(System.out::println);
        assertThat(sectionsList.get(1).getStartStation())
                .isEqualTo(new Station("잠실역"));
        assertThat(sectionsList.get(1).getEndStation())
                .isEqualTo(new Station("서울역"));
        assertThat(sectionsList.get(0).getStartStation())
                .isEqualTo(new Station("서울역"));
        assertThat(sectionsList.get(0).getEndStation())
                .isEqualTo(new Station("용산역"));
    }

    @Test
    void 서울_용산에_용산_기준_하행에_잠실_추가() {
        // when
        sections.addSection(new Section(new Station("용산역"), new Station("잠실역"), 1));

        // expect
        List<Section> sectionsList = sections.getSections();
        sectionsList.forEach(System.out::println);
        assertThat(sectionsList.get(0).getStartStation())
                .isEqualTo(new Station("서울역"));
        assertThat(sectionsList.get(0).getEndStation())
                .isEqualTo(new Station("용산역"));
        assertThat(sectionsList.get(1).getStartStation())
                .isEqualTo(new Station("용산역"));
        assertThat(sectionsList.get(1).getEndStation())
                .isEqualTo(new Station("잠실역"));
    }
}
