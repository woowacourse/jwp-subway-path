package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.section.Sections.emptySections;
import static subway.fixture.SectionsFixture.AB.sections;
import static subway.fixture.StationFixture.A;
import static subway.fixture.StationFixture.B;
import static subway.fixture.StationFixture.C;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.fixture.SectionsFixture.AB;
import subway.fixture.SectionsFixture.ABC;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class SectionsTest {

    @Test
    void 구간들은_null_일_시_예외가_발생한다() {
        assertThatThrownBy(() -> new Sections(null)).isInstanceOf(IllegalArgumentException.class)
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
        final List<Section> sections = updatedSections.sections();
        assertAll(() -> assertThat(sections).hasSize(1),
                () -> assertThat(sections.get(0).getUpStation().getName().name()).isEqualTo("종합운동장"),
                () -> assertThat(sections.get(0).getDownStation().getName().name()).isEqualTo("잠실새내"),
                () -> assertThat(sections.get(0).getDistance().distance()).isEqualTo(5));
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
        final Section other = new Section(stationC, A.stationA, otherDistance);

        // when
        final Sections updatedSections = AB.sections.addSection(other);

        // then
        final List<Section> linkedSections = updatedSections.sections();
        assertAll(() -> assertThat(linkedSections.get(0).getUpStation().isSameStation(C.stationC)).isTrue(),
                () -> assertThat(linkedSections.get(0).getDownStation().isSameStation(A.stationA)).isTrue(),
                () -> assertThat(linkedSections.get(1).getDownStation().isSameStation(B.stationB)).isTrue());
    }

    /*
       기존 단일 구간: A-B
       새로운 구간 연결: A-B-C
    */
    @Test
    void 새로운_구간을_제일_뒤에_추가한다() {
        // given
        final Distance otherDistance = new Distance(5);
        final Section other = new Section(B.stationB, C.stationC, otherDistance);

        // when
        final Sections updatedSections = AB.sections.addSection(other);

        // then
        final List<Section> linkedSections = updatedSections.sections();
        assertAll(() -> assertThat(linkedSections.get(0).getUpStation().isSameStation(A.stationA)).isTrue(),
                () -> assertThat(linkedSections.get(0).getDownStation().isSameStation(B.stationB)).isTrue(),
                () -> assertThat(linkedSections.get(1).getDownStation().isSameStation(C.stationC)).isTrue());
    }


    /*
       기존 구간: A-B-C
       요청된 구간: B-D
       새로운 구간 연결: A-B-D-C
    */
    @Test
    void 새로운_구간의_상행선이_기준역일_때_중간에_구간을_추가할_수_있다() {
        // given
        final StationName stationNameD = new StationName("D");
        final Station stationD = new Station(stationNameD);
        final Distance otherDistance = new Distance(5);
        final Section other = new Section(B.stationB, stationD, otherDistance);

        // when
        final Sections updatedSections = ABC.sections.addSection(other);

        // then
        final List<Section> linkedSections = updatedSections.sections();
        assertAll(() -> assertThat(linkedSections.get(0).getUpStation().isSameStation(A.stationA)).isTrue(),
                () -> assertThat(linkedSections.get(0).getDownStation().isSameStation(B.stationB)).isTrue(),
                () -> assertThat(linkedSections.get(1).getDownStation().isSameStation(stationD)).isTrue(),
                () -> assertThat(linkedSections.get(2).getDownStation().isSameStation(C.stationC)).isTrue(),
                () -> assertThat(linkedSections.get(1).getDistance().distance()).isEqualTo(5),
                () -> assertThat(linkedSections.get(2).getDistance().distance()).isEqualTo(1));
    }

    /*
       기존 구간: A-B-C
       요청된 구간: D-B
       새로운 구간 연결: A-D-B-C
    */
    @Test
    void 새로운_구간의_하행선이_기준역일_때_중간에_구간을_추가할_수_있다() {
        // given
        final StationName stationNameD = new StationName("D");
        final Station stationD = new Station(stationNameD);
        final Distance otherDistance = new Distance(5);
        final Section other = new Section(stationD, B.stationB, otherDistance);

        // when
        final Sections updatedSections = ABC.sections.addSection(other);

        // then
        final List<Section> linkedSections = updatedSections.sections();
        assertAll(() -> assertThat(linkedSections.get(0).getUpStation().isSameStation(A.stationA)).isTrue(),
                () -> assertThat(linkedSections.get(0).getDownStation().isSameStation(stationD)).isTrue(),
                () -> assertThat(linkedSections.get(1).getDownStation().isSameStation(B.stationB)).isTrue(),
                () -> assertThat(linkedSections.get(2).getDownStation().isSameStation(C.stationC)).isTrue(),
                () -> assertThat(linkedSections.get(0).getDistance().distance()).isEqualTo(1),
                () -> assertThat(linkedSections.get(1).getDistance().distance()).isEqualTo(5));
    }

    @Test
    void 요청된_구간의_거리가_기존보다_길다면_예외가_발생한다() {
        // given
        final StationName stationNameD = new StationName("D");
        final Station stationD = new Station(stationNameD);
        final Distance otherDistance = new Distance(10);
        final Section other = new Section(stationD, B.stationB, otherDistance);

        // expect
        assertThatThrownBy(() -> ABC.sections.addSection(other)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("길이는 1 이상이어야합니다.");
    }

    /*
        기존 구간: A-B-C
    */
    @Test
    void 새로운_구간의_모든_역이_이미_해당_구간들에_존재하면_새로운_구간을_추가할_수_없다() {
        // given
        final Distance otherDistance = new Distance(5);
        final Section other = new Section(A.stationA, C.stationC, otherDistance);

        // when
        assertThatThrownBy(() -> ABC.sections.addSection(other)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("두 역이 이미 존재합니다.");
    }


    /*
        기존 구간: A-B-C
        삭제 요청: A
    */
    @Test
    void 구간들에서_상행종점을_제거한다() {
        // expect
        assertThat(ABC.sections.removeStation(A.stationA).sections().get(0).getUpStation()).isEqualTo(B.stationB);
    }

    /*
        기존 구간: A-B-C
        삭제 요청: C
    */
    @Test
    void 구간들에서_하행종점을_제거한다() {
        // expect
        assertThat(ABC.sections.removeStation(C.stationC).sections().get(0).getDownStation()).isEqualTo(B.stationB);
    }

    /*
        기존 구간: A-B-C
        삭제 요청: B
    */
    @Test
    void 구간들에서_중간역을_제거한다() {
        // expect
        final Sections removedSections = ABC.sections.removeStation(B.stationB);

        assertAll(
                () -> assertThat(removedSections.sections()).hasSize(1),
                () -> assertThat(removedSections.sections().get(0).getDownStation()).isEqualTo(C.stationC),
                () -> assertThat(removedSections.sections().get(0).getUpStation()).isEqualTo(A.stationA));
    }

    /*
        기존 구간: A-B
        삭제 요청: B
    */
    @Test
    void 구간들에_역이_한개_남았다면_남은_구간_자체를_삭제한다() {
        // expect
        assertThat(sections.removeStation(A.stationA).sections()).hasSize(0);
    }
}
