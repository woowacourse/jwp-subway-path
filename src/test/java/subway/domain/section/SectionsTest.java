package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.section.Sections.emptySections;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.fixture.SectionsFixture.AB;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class SectionsTest {

    @Test
    void 구간들은_null_일_시_예외가_발생한다() {
        assertThatThrownBy(() -> new Sections(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선에 구간들은 없을 수 없습니다.");
    }

    @Test
    void 구간들은_비어있는_상태로_생성될_수_있다() {
        assertThat(emptySections().isEmpty()).isTrue();
    }

    @Test
    void 구간들이_비어있을_때_새로운_구간을_추가할_수_있다() {
        // given
        final StationName stationNameA = new StationName("종합운동장");
        final StationName stationNameB = new StationName("잠실새내");

        final Station stationA = new Station(stationNameA);
        final Station stationB = new Station(stationNameB);

        final Distance distance = new Distance(5);

        final Section section = new Section(stationA, stationB, distance);

        // when
        final Sections updatedSections = emptySections().addSection(section);

        // then
        final List<Section> sections = updatedSections.getSections();
        assertAll(
                () -> assertThat(sections).hasSize(1),
                () -> assertThat(sections.get(0).getUpStation().getName().name()).isEqualTo("종합운동장"),
                () -> assertThat(sections.get(0).getDownStation().getName().name()).isEqualTo("잠실새내"),
                () -> assertThat(sections.get(0).getDistance().distance()).isEqualTo(5)
        );
    }

    /*
       기존 단일 구간: A-B
       새로운 구간 연결: C-A-B
    */
    @Test
    void 새로운_구간을_제일_앞에_추가한다() {
        // given
        final Station stationC = new Station(new StationName("C"));
        final Distance otherDistance = new Distance(5);
        final Section other = new Section(stationC, AB.stationA, otherDistance);

        // when
        final Sections updatedSections = AB.sections.addSection(other);

        // then
        final List<Section> linkedSections = updatedSections.getSections();
        assertAll(
                () -> assertThat(linkedSections.get(0).getUpStation().isSameStation(stationC)).isTrue(),
                () -> assertThat(linkedSections.get(0).getDownStation().isSameStation(AB.stationA)).isTrue(),
                () -> assertThat(linkedSections.get(1).getDownStation().isSameStation(AB.stationB)).isTrue()
        );
    }

    /*
       기존 단일 구간: A-B
       새로운 구간 연결: A-B-C
    */
    @Test
    void 새로운_구간을_제일_뒤에_추가한다() {
        // given
        final Station stationC = new Station(new StationName("C"));
        final Distance otherDistance = new Distance(5);
        final Section other = new Section(AB.stationB, stationC, otherDistance);

        // when
        final Sections updatedSections = AB.sections.addSection(other);

        // then
        final List<Section> linkedSections = updatedSections.getSections();
        assertAll(
                () -> assertThat(linkedSections.get(0).getUpStation().isSameStation(AB.stationA)).isTrue(),
                () -> assertThat(linkedSections.get(0).getDownStation().isSameStation(AB.stationB)).isTrue(),
                () -> assertThat(linkedSections.get(1).getDownStation().isSameStation(stationC)).isTrue()
        );
    }
}
