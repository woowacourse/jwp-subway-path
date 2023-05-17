package subway.domain.section;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.domain.Position.DOWN;
import static subway.domain.Position.MID;
import static subway.domain.Position.UP;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    private final Station UP_END_STATION = Station.of(1L, "잠실역", UP);
    private final Station MID_STATION = Station.of(2L, "선릉역", MID);
    private final Station DUMMY_STATION = Station.of(3L, "사당역");
    private final Station MID_STATION_TWO = Station.of(4L, "삼성역");
    private final Station MID_STATION_THREE = Station.of(5L, "디지털미디어시티역");
    private final Station DOWN_END_STATION = Station.of(6L, "홍대입구역", DOWN);

    @Nested
    class 구간_추가_테스트 {

        @Test
        void 구간을_추가하다() {
            final Sections sections = Sections.create();
            final Section section = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));

            assertDoesNotThrow(() -> sections.addSection(section));
        }

        @Test
        void 중복된_구간을_추가할_경우_예외를_반환한다() {
            final Section section = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            final Sections sections = Sections.from(section);

            assertThatThrownBy(() -> sections.addSection(section))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 등록된 경로입니다.");
        }
    }

    @Nested
    class 구간의_위치_반환_테스트 {

        @Test
        void 구간의_위치를_반환하다() {
            final Section section = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            final Sections sections = Sections.from(section);

            assertThat(sections.findIndex(section)).isEqualTo(0);
        }

        @Test
        void 존재하지_않는_구간의_위치를_찾으려고_하면_에러를_반환한다() {
            final Section section = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            final Sections sections = Sections.create();

            assertThatThrownBy(() -> sections.findIndex(section))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("입력한 구간이 존재하지 않습니다");
        }
    }

    @Test
    void 구역이_비어있는지_여부를_확인하다() {
        final Sections emptySections = Sections.create();
        final Section ignore = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
        final Sections notEmptySections = Sections.from(ignore);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(emptySections.isInitial(ignore)).isTrue();
            softAssertions.assertThat(notEmptySections.isInitial(ignore)).isFalse();
        });
    }

    @Test
    void 추가하려는_구역이_상행_종점으로_들어가는지_확인한다() {
        final Section section = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
        final Sections sections = Sections.from(section);
        final Section insertSection = Section.of(DUMMY_STATION, UP_END_STATION, Distance.from(10));

        assertDoesNotThrow(() -> sections.isUpEndSection(insertSection));
    }

    @Test
    void 추가하려는_구역이_하행_종점으로_들어가는지_확인한다() {
        final Section section = Section.of(UP_END_STATION, DOWN_END_STATION, Distance.from(10));
        final Sections sections = Sections.from(section);
        final Section insertSection = Section.of(DOWN_END_STATION, DUMMY_STATION, Distance.from(10));

        assertDoesNotThrow(() -> sections.isDownEndSection(insertSection));
    }

    @Test
    void 추가하려는_구역이_중간에_들어가는지_확인한다() {
        final Section section = Section.of(UP_END_STATION, DOWN_END_STATION, Distance.from(10));
        final Sections sections = Sections.from(section);
        final Section insertSection = Section.of(UP_END_STATION, DUMMY_STATION, Distance.from(5));

        assertDoesNotThrow(() -> sections.isMidSection(insertSection));
    }

    @Nested
    class 특정_구간_가져오기_테스트 {

        @Test
        void 구간들에_저장된_특정_구간을_가져오는지_확인한다() {
            final Section section = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            final Sections sections = Sections.from(section);
            final Section findSection = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));

            assertThat(sections.findOriginSection(findSection)).isEqualTo(section);
        }

        @Test
        void 구간들에_존재하지_않는_특정_구간을_가져오려고하면_예외를_반환한다() {
            final Section section = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            final Sections sections = Sections.from(section);
            final Section findSection = Section.of(MID_STATION, DUMMY_STATION, Distance.from(10));

            assertThatThrownBy(() -> sections.findOriginSection(findSection))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("노선에 저장된 구간이 아닙니다");
        }
    }

    @Nested
    class 구간_삭제_테스트 {

        @Test
        void 구간들에서_특정_구간을_제대로_삭제하는지_확인하다() {
            final Section section = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            final Sections sections = Sections.from(section);

            assertDoesNotThrow(() -> sections.delete(section));
        }

        @Test
        void 구간들에서_존재하지_않는_구간을_삭제하려고_하면_예외를_반환한다() {
            final Section section = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            final Sections sections = Sections.from(section);
            final Section fakeSection = Section.of(MID_STATION, DUMMY_STATION, Distance.from(10));

            assertThatThrownBy(() -> sections.delete(fakeSection))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("저장되어 있지 않는 구간을 삭제할 수 없습니다.");
        }
    }

    @Nested
    class 특정_역이_종점_구간에_속하면_구간을_가져오는지_테스트 {

        Sections sections;
        Section sectionOne;
        Section sectionTwo;
        Section sectionThree;
        Section sectionFour;

        @BeforeEach
        void setUp() {
            sections = Sections.create();
            sectionOne = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            sectionTwo = Section.of(MID_STATION, MID_STATION_TWO, Distance.from(10));
            sectionThree = Section.of(MID_STATION_TWO, MID_STATION_THREE, Distance.from(10));
            sectionFour = Section.of(MID_STATION_THREE, DOWN_END_STATION, Distance.from(10));
            sections.addSection(sectionOne);
            sections.addSection(sectionTwo);
            sections.addSection(sectionThree);
            sections.addSection(sectionFour);
        }

        @Test
        void 해당_역이_가지고_있는_종점_구간을_가져온다() {
            assertThat(sections.getEndSectionBy(MID_STATION)).isEqualTo(sectionOne);
        }

        @Test
        void 해당_역이_종점_구간을_가지고_있지_않은_경우_예외가_발생한다() {
            assertThatThrownBy(() -> sections.getEndSectionBy(MID_STATION_TWO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 역은 종점 구간에 속하지 않습니다");
        }
    }

    @Nested
    class 특정_구간을_가져오는지_테스트 {

        Sections sections;
        Section sectionOne;
        Section sectionTwo;

        @BeforeEach
        void setUp() {
            sections = Sections.create();
            sectionOne = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            sectionTwo = Section.of(MID_STATION, DOWN_END_STATION, Distance.from(10));
            sections.addSection(sectionOne);
            sections.addSection(sectionTwo);
        }

        @Test
        void 각_구간의_아랫_역이_특정_역에_해당하는_구간을_가져온다() {
            final Section actual = sections.getSectionBy(MID_STATION, Section::getDownStation);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getUpStation()).isEqualTo(UP_END_STATION);
                softAssertions.assertThat(actual.getDownStation()).isEqualTo(MID_STATION);
            });
        }

        @Test
        void 각_구간의_아랫_역이_존재하지_않는_역을_찾으려고하면_예외를_발생한다() {
            assertThatThrownBy(() -> sections.getSectionBy(MID_STATION_THREE, Section::getDownStation))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("검색하려는 역이 존재하지 않습니다.");
        }

        @Test
        void 각_구간의_윗_역이_특정_역에_해당하는_구간을_가져온다() {
            final Section actual = sections.getSectionBy(MID_STATION, Section::getUpStation);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getUpStation()).isEqualTo(MID_STATION);
                softAssertions.assertThat(actual.getDownStation()).isEqualTo(DOWN_END_STATION);
            });
        }

        @Test
        void 각_구간의_윗_역이_존재하지_않는_역을_찾으려고하면_예외를_발생한다() {
            assertThatThrownBy(() -> sections.getSectionBy(MID_STATION_THREE, Section::getUpStation))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("검색하려는 역이 존재하지 않습니다.");
        }
    }

    @Test
    void 구역들에_저장된_모든_역을_확인한다() {
        // given
        final Sections sections = Sections.create();
        final Section sectionOne = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
        final Section sectionTwo = Section.of(MID_STATION, DOWN_END_STATION, Distance.from(10));
        sections.addSection(sectionOne);
        sections.addSection(sectionTwo);

        // when
        final List<Station> actual = sections.getStations();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).isEqualTo(UP_END_STATION);
            softAssertions.assertThat(actual.get(1)).isEqualTo(MID_STATION);
            softAssertions.assertThat(actual.get(2)).isEqualTo(DOWN_END_STATION);
        });
    }

    @Nested
    class 구역들에_저장된_지하철_역_가져오기_테스트 {

        Sections sections;
        Section sectionOne;
        Section sectionTwo;

        @BeforeEach
        void setUp() {
            sections = Sections.create();
            sectionOne = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            sectionTwo = Section.of(MID_STATION, DOWN_END_STATION, Distance.from(10));
            sections.addSection(sectionOne);
            sections.addSection(sectionTwo);
        }

        @Test
        void 구역들에_저장된_지하철_역을_가져온다() {
            final Station station = sections.findStation(UP_END_STATION);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(station.getId()).isEqualTo(UP_END_STATION.getId());
                softAssertions.assertThat(station.getName()).isEqualTo(UP_END_STATION.getName());
                softAssertions.assertThat(station.getPosition()).isEqualTo(UP_END_STATION.getPosition());
            });
        }

        @Test
        void 구역들에_저장되지_않은_지하철_역을_가져오려고하면_예외를_반환한다() {
            assertThatThrownBy(() -> sections.findStation(MID_STATION_THREE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 노선에 역이 존재하지 않습니다.");
        }
    }

    @Nested
    class 종점_구간을_가져오는지_테스트 {

        @Test
        void 상행_종점_구간을_가져온다() {
            // given
            final Sections sections = Sections.create();
            final Section sectionOne = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            final Section sectionTwo = Section.of(MID_STATION, DOWN_END_STATION, Distance.from(10));
            sections.addSection(sectionOne);
            sections.addSection(sectionTwo);

            // when
            final Section actual = sections.findUpSection();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getUpStation()).isEqualTo(UP_END_STATION);
                softAssertions.assertThat(actual.getDownStation()).isEqualTo(MID_STATION);
            });
        }

        @Test
        void 구간들이_비어있는데_상행_종점_구간을_가져오려고하면_예외를_반환한다() {
            // given
            final Sections sections = Sections.create();

            // when, then
            assertThatThrownBy(sections::findUpSection)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아직 구간이 저장되지 않았습니다");
        }

        @Test
        void 하행_종점_구간을_가져온다() {
            // given
            final Sections sections = Sections.create();
            final Section sectionOne = Section.of(UP_END_STATION, MID_STATION, Distance.from(10));
            final Section sectionTwo = Section.of(MID_STATION, DOWN_END_STATION, Distance.from(10));
            sections.addSection(sectionOne);
            sections.addSection(sectionTwo);

            // when
            final Section actual = sections.findDownSection();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getUpStation()).isEqualTo(MID_STATION);
                softAssertions.assertThat(actual.getDownStation()).isEqualTo(DOWN_END_STATION);
            });
        }

        @Test
        void 구간들이_비어있는데_하행_종점_구간을_가져오려고하면_예외를_반환한다() {
            // given
            final Sections sections = Sections.create();

            // when, then
            assertThatThrownBy(sections::findDownSection)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아직 구간이 저장되지 않았습니다");
        }
    }
}
