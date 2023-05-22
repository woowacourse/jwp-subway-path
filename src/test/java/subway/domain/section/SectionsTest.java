package subway.domain.section;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.domain.Position.DOWN;
import static subway.domain.Position.UP;
import static subway.fixtures.domain.DistanceFixture.TEN_DISTANCE;
import static subway.fixtures.domain.SectionFixture.JAMSIL_SADANG_5;
import static subway.fixtures.domain.SectionFixture.JAMSIL_SEOLLEUNG_10;
import static subway.fixtures.domain.SectionFixture.JAMSIL_UP_SEOLLEUNG_DOWN_10;
import static subway.fixtures.domain.SectionFixture.JAMSIL_UP_SEOLLEUNG_MID_10;
import static subway.fixtures.domain.SectionFixture.SEOLLEUNG_MID_SADANG_DOWN_10;
import static subway.fixtures.domain.SectionFixture.SEOLLEUNG_SADANG_10;
import static subway.fixtures.domain.StationFixture.HONGDAE;
import static subway.fixtures.domain.StationFixture.JAMSIL;
import static subway.fixtures.domain.StationFixture.JAMSIL_UP;
import static subway.fixtures.domain.StationFixture.SADANG;
import static subway.fixtures.domain.StationFixture.SEOLLEUNG;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    @Nested
    class 구간_추가_테스트 {

        @Test
        void 구간을_추가하다() {
            final Sections sections = Sections.create();

            assertDoesNotThrow(() -> sections.addSection(JAMSIL_SEOLLEUNG_10));
        }

        @Test
        void 중복된_구간을_추가할_경우_예외를_반환한다() {
            final Sections sections = Sections.from(JAMSIL_SEOLLEUNG_10);

            assertThatThrownBy(() -> sections.addSection(JAMSIL_SEOLLEUNG_10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 등록된 경로입니다.");
        }
    }

    @Nested
    class 구간의_위치_반환_테스트 {

        @Test
        void 구간의_위치를_반환하다() {
            final Sections sections = Sections.from(JAMSIL_SEOLLEUNG_10);

            assertThat(sections.findIndex(JAMSIL_SEOLLEUNG_10)).isEqualTo(0);
        }

        @Test
        void 존재하지_않는_구간의_위치를_찾으려고_하면_에러를_반환한다() {
            final Sections sections = Sections.create();

            assertThatThrownBy(() -> sections.findIndex(JAMSIL_SEOLLEUNG_10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("입력한 구간이 존재하지 않습니다");
        }
    }

    @Test
    void 구역이_비어있는지_여부를_확인하다() {
        final Sections emptySections = Sections.create();
        final Section ignore = JAMSIL_SEOLLEUNG_10;
        final Sections notEmptySections = Sections.from(ignore);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(emptySections.isInitial(ignore)).isTrue();
            softAssertions.assertThat(notEmptySections.isInitial(ignore)).isFalse();
        });
    }

    @Test
    void 추가하려는_구역이_상행_종점으로_들어가는지_확인한다() {
        final Sections sections = Sections.from(JAMSIL_UP_SEOLLEUNG_DOWN_10);
        final Section insertSection = Section.of(SADANG, JAMSIL, TEN_DISTANCE);

        assertDoesNotThrow(() -> sections.isUpEndSection(insertSection));
    }

    @Test
    void 추가하려는_구역이_하행_종점으로_들어가는지_확인한다() {
        final Sections sections = Sections.from(JAMSIL_UP_SEOLLEUNG_DOWN_10);

        assertDoesNotThrow(() -> sections.isDownEndSection(SEOLLEUNG_SADANG_10));
    }

    @Test
    void 추가하려는_구역이_중간에_들어가는지_확인한다() {
        final Sections sections = Sections.from(JAMSIL_UP_SEOLLEUNG_DOWN_10);

        assertDoesNotThrow(() -> sections.isMidSection(JAMSIL_SADANG_5));
    }

    @Nested
    class 특정_구간_가져오기_테스트 {

        @Test
        void 구간들에_저장된_특정_구간을_가져오는지_확인한다() {
            final Sections sections = Sections.from(JAMSIL_SEOLLEUNG_10);
            final Section findSection = JAMSIL_SEOLLEUNG_10;

            assertThat(sections.findOriginSection(findSection)).isEqualTo(JAMSIL_SEOLLEUNG_10);
        }

        @Test
        void 구간들에_존재하지_않는_특정_구간을_가져오려고하면_예외를_반환한다() {
            final Sections sections = Sections.from(JAMSIL_SEOLLEUNG_10);
            final Section findSection = SEOLLEUNG_SADANG_10;

            assertThatThrownBy(() -> sections.findOriginSection(findSection))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("노선에 저장된 구간이 아닙니다");
        }
    }

    @Nested
    class 구간_삭제_테스트 {

        @Test
        void 구간들에서_특정_구간을_제대로_삭제하는지_확인하다() {
            final Sections sections = Sections.from(JAMSIL_SEOLLEUNG_10);

            assertDoesNotThrow(() -> sections.delete(JAMSIL_SEOLLEUNG_10));
        }

        @Test
        void 구간들에서_존재하지_않는_구간을_삭제하려고_하면_예외를_반환한다() {
            final Sections sections = Sections.from(JAMSIL_SEOLLEUNG_10);
            final Section fakeSection = SEOLLEUNG_SADANG_10;

            assertThatThrownBy(() -> sections.delete(fakeSection))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("저장되어 있지 않는 구간을 삭제할 수 없습니다.");
        }
    }

    @Nested
    class 특정_역이_종점_구간에_속하면_구간을_가져오는지_테스트 {

        Sections sections;

        @BeforeEach
        void setUp() {
            final Station jamsil = Station.of(1L, "잠실역", UP);
            final Station seolleung = Station.of(2L, "선릉역", DOWN);
            final Section section = Section.of(jamsil, seolleung, TEN_DISTANCE);

            sections = Sections.create();
            sections.addSection(section);
        }

        @Test
        void 해당_역이_가지고_있는_종점_구간을_가져온다() {
            assertThat(sections.getEndSectionBy(JAMSIL)).isEqualTo(JAMSIL_SEOLLEUNG_10);
        }

        @Test
        void 해당_역이_종점_구간을_가지고_있지_않은_경우_예외가_발생한다() {
            assertThatThrownBy(() -> sections.getEndSectionBy(SADANG))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 역은 종점 구간에 속하지 않습니다");
        }
    }

    @Nested
    class 특정_구간을_가져오는지_테스트 {

        Sections sections;

        @BeforeEach
        void setUp() {
            sections = Sections.create();
            sections.addSection(JAMSIL_SEOLLEUNG_10);
            sections.addSection(SEOLLEUNG_SADANG_10);
        }

        @Test
        void 각_구간의_아랫_역이_특정_역에_해당하는_구간을_가져온다() {
            final Section actual = sections.getSectionBy(SEOLLEUNG, Section::getDownStation);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getUpStation()).isEqualTo(JAMSIL);
                softAssertions.assertThat(actual.getDownStation()).isEqualTo(SEOLLEUNG);
            });
        }

        @Test
        void 각_구간의_아랫_역이_존재하지_않는_역을_찾으려고하면_예외를_발생한다() {
            assertThatThrownBy(() -> sections.getSectionBy(JAMSIL, Section::getDownStation))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("검색하려는 역이 존재하지 않습니다.");
        }

        @Test
        void 각_구간의_윗_역이_특정_역에_해당하는_구간을_가져온다() {
            final Section actual = sections.getSectionBy(JAMSIL, Section::getUpStation);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getUpStation()).isEqualTo(JAMSIL);
                softAssertions.assertThat(actual.getDownStation()).isEqualTo(SEOLLEUNG);
            });
        }

        @Test
        void 각_구간의_윗_역이_존재하지_않는_역을_찾으려고하면_예외를_발생한다() {
            assertThatThrownBy(() -> sections.getSectionBy(SADANG, Section::getUpStation))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("검색하려는 역이 존재하지 않습니다.");
        }
    }

    @Test
    void 구역들에_저장된_모든_역을_확인한다() {
        // given
        final Sections sections = Sections.create();
        sections.addSection(JAMSIL_UP_SEOLLEUNG_MID_10);
        sections.addSection(SEOLLEUNG_MID_SADANG_DOWN_10);

        // when
        final List<Station> actual = sections.getStations();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).isEqualTo(JAMSIL);
            softAssertions.assertThat(actual.get(1)).isEqualTo(SEOLLEUNG);
            softAssertions.assertThat(actual.get(2)).isEqualTo(SADANG);
        });
    }

    @Nested
    class 구역들에_저장된_지하철_역_가져오기_테스트 {

        Sections sections;

        @BeforeEach
        void setUp() {
            sections = Sections.create();
            sections.addSection(JAMSIL_UP_SEOLLEUNG_MID_10);
            sections.addSection(SEOLLEUNG_MID_SADANG_DOWN_10);
        }

        @Test
        void 구역들에_저장된_지하철_역을_가져온다() {
            final Station actual = sections.findStation(JAMSIL_UP);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getId()).isEqualTo(JAMSIL_UP.getId());
                softAssertions.assertThat(actual.getName()).isEqualTo(JAMSIL_UP.getName());
                softAssertions.assertThat(actual.getPosition()).isEqualTo(JAMSIL_UP.getPosition());
            });
        }

        @Test
        void 구역들에_저장되지_않은_지하철_역을_가져오려고하면_예외를_반환한다() {
            assertThatThrownBy(() -> sections.findStation(HONGDAE))
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
            sections.addSection(JAMSIL_UP_SEOLLEUNG_MID_10);
            sections.addSection(SEOLLEUNG_MID_SADANG_DOWN_10);

            // when
            final Section actual = sections.findUpSection();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getUpStation()).isEqualTo(JAMSIL);
                softAssertions.assertThat(actual.getDownStation()).isEqualTo(SEOLLEUNG);
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
            sections.addSection(JAMSIL_UP_SEOLLEUNG_MID_10);
            sections.addSection(SEOLLEUNG_MID_SADANG_DOWN_10);

            // when
            final Section actual = sections.findDownSection();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getUpStation()).isEqualTo(SEOLLEUNG);
                softAssertions.assertThat(actual.getDownStation()).isEqualTo(SADANG);
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
