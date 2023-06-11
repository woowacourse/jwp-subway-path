package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.exception.invalid.DistanceInvalidException;
import subway.exception.invalid.SectionInvalidException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SectionsTest {

    private Section section = new Section(new Station(1L, "잠실역"), new Station(2L, "강남역"), 3L);

    @Test
    void 기존의_역_구간_사이에_새로운_역_구간을_추가할_때_길이_관계가_올바르지_않으면_예외를_던진다() {
        final Sections sections = new Sections(List.of(section));
        final Section addedSection = new Section(new Station("잠실역"), new Station("신촌역"), 4L);

        assertThatThrownBy(() -> sections.addSection(addedSection))
                .isInstanceOf(DistanceInvalidException.class);
    }

    @Test
    void 기존의_역_구간_사이에_중복된_역_구간을_추가하면_예외를_던진다() {
        final Sections sections = new Sections(List.of(section));

        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(SectionInvalidException.class);
    }

    @Test
    void 기존의_역_구간_사이에_새로운_역_구간을_추가한다() {
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section);
        final Sections sections = new Sections(sectionList);
        final Section addedSection = new Section(new Station("잠실역"), new Station("신촌역"), 1L);

        sections.addSection(addedSection);

        final Section section1 = sections.getSections().get(0);
        final Section section2 = sections.getSections().get(1);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(sections.getSections().size()).isEqualTo(2);

        softAssertions.assertThat(section1.getUpStation().getName()).isEqualTo("잠실역");
        softAssertions.assertThat(section1.getDownStation().getName()).isEqualTo("신촌역");
        softAssertions.assertThat(section1.getDistance()).isEqualTo(1L);

        softAssertions.assertThat(section2.getUpStation().getName()).isEqualTo("신촌역");
        softAssertions.assertThat(section2.getDownStation().getName()).isEqualTo("강남역");
        softAssertions.assertThat(section2.getDistance()).isEqualTo(2L);
        softAssertions.assertAll();
    }

    @Test
    void 기존의_역_구간_밖에_서로_이어지지_않는_분리된_역_구간을_추가하면_예외를_던진다() {
        final Sections sections = new Sections(List.of(section));
        final Section addedSection = new Section(new Station("아현역"), new Station("신촌역"), 4L);

        assertThatThrownBy(() -> sections.addSection(addedSection))
                .isInstanceOf(SectionInvalidException.class);
    }

    @Test
    void 기존의_역_구간_밖에_이어지는_새로운_역_구간을_추가한다() {
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section);
        final Sections sections = new Sections(sectionList);
        final Section addedSection = new Section(new Station("강남역"), new Station("신촌역"), 4L);

        sections.addSection(addedSection);

        final Section section1 = sections.getSections().get(0);
        final Section section2 = sections.getSections().get(1);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(sections.getSections().size()).isEqualTo(2);

        softAssertions.assertThat(section1.getUpStation().getName()).isEqualTo("잠실역");
        softAssertions.assertThat(section1.getDownStation().getName()).isEqualTo("강남역");
        softAssertions.assertThat(section1.getDistance()).isEqualTo(3L);

        softAssertions.assertThat(section2.getUpStation().getName()).isEqualTo("강남역");
        softAssertions.assertThat(section2.getDownStation().getName()).isEqualTo("신촌역");
        softAssertions.assertThat(section2.getDistance()).isEqualTo(4L);
        softAssertions.assertAll();
    }

    @Test
    void 기존의_역_구간들_사이에_있는_역을_삭제한다() {
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section);
        sectionList.add(new Section(new Station("강남역"), new Station("신촌역"), 4L));
        final Sections sections = new Sections(sectionList);

        sections.deleteSectionByStation(new Station("강남역"));

        final Section section1 = sections.getSections().get(0);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(sections.getSections().size()).isEqualTo(1);

        softAssertions.assertThat(section1.getUpStation().getName()).isEqualTo("잠실역");
        softAssertions.assertThat(section1.getDownStation().getName()).isEqualTo("신촌역");
        softAssertions.assertThat(section1.getDistance()).isEqualTo(7L);
        softAssertions.assertAll();
    }

    @Test
    void 기존의_역_구간들_끝에_있는_역을_삭제한다() {
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section);
        sectionList.add(new Section(new Station("강남역"), new Station("신촌역"), 4L));
        final Sections sections = new Sections(sectionList);

        sections.deleteSectionByStation(new Station("잠실역"));

        final Section section1 = sections.getSections().get(0);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(sections.getSections().size()).isEqualTo(1);

        softAssertions.assertThat(section1.getUpStation().getName()).isEqualTo("강남역");
        softAssertions.assertThat(section1.getDownStation().getName()).isEqualTo("신촌역");
        softAssertions.assertThat(section1.getDistance()).isEqualTo(4L);
        softAssertions.assertAll();
    }

    @Test
    void 특정_역을_상행으로_가지는_역_구간_정보를_찾는다() {
        final Sections sections = new Sections(List.of(section));

        final Section section = sections.findSectionWithUpStation(new Station("잠실역"));

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(section.getUpStation().getName()).isEqualTo("잠실역");
        softAssertions.assertThat(section.getDownStation().getName()).isEqualTo("강남역");
        softAssertions.assertThat(section.getDistance()).isEqualTo(3L);
        softAssertions.assertAll();
    }

    @Test
    void 특정_역을_하행으로_가지는_역_구간_정보를_찾는다() {
        final Sections sections = new Sections(List.of(section));

        final Section section = sections.findSectionWithDownStation(new Station("강남역"));

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(section.getUpStation().getName()).isEqualTo("잠실역");
        softAssertions.assertThat(section.getDownStation().getName()).isEqualTo("강남역");
        softAssertions.assertThat(section.getDistance()).isEqualTo(3L);
        softAssertions.assertAll();
    }

    @Test
    void 특정_역이_상행으로서_존재하는_역_구간이_있는지_확인한다() {
        final Sections sections = new Sections(List.of(section));

        assertThat(sections.isExistAsUpStation(new Station("잠실역"))).isTrue();
    }

    @Test
    void 중복이_제거된_모든_역을_반환한다() {
        // given
        final Sections sections = new Sections(List.of(
                section,
                new Section(new Station(1L, "잠실역"), new Station(3L, "신촌역"), 1L)));

        // when
        final List<Station> stations = sections.getStations();

        // then
        final List<Station> expected = List.of(new Station(1L, "잠실역"), new Station(2L, "강남역"), new Station(3L, "신촌역"));
        assertThat(expected).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(stations);
    }
}
