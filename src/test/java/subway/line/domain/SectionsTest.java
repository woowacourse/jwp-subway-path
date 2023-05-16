package subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.line.domain.fixture.StationFixture.건대입구;
import static subway.line.domain.fixture.StationFixture.경유역1;
import static subway.line.domain.fixture.StationFixture.경유역2;
import static subway.line.domain.fixture.StationFixture.낙성대;
import static subway.line.domain.fixture.StationFixture.없는역;
import static subway.line.domain.fixture.StationFixture.없는역2;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.역4;
import static subway.line.domain.fixture.StationFixture.잠실;
import static subway.line.domain.fixture.StationFixture.잠실나루;
import static subway.line.domain.fixture.StationFixture.잠실새내;
import static subway.line.domain.fixture.StationFixture.종착역;
import static subway.line.domain.fixture.StationFixture.출발역;
import static subway.line.domain.fixture.StationFixture.홍대입구;
import static subway.line.exception.line.LineExceptionType.ADDED_SECTION_NOT_SMALLER_THAN_ORIGIN;
import static subway.line.exception.line.LineExceptionType.ALREADY_EXIST_STATIONS;
import static subway.line.exception.line.LineExceptionType.DELETED_STATION_NOT_EXIST;
import static subway.line.exception.line.LineExceptionType.NO_RELATION_WITH_ADDED_SECTION;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.common.exception.BaseExceptionType;
import subway.line.domain.fixture.SectionFixtures;
import subway.line.exception.line.LineException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Sections 은(는)")
class SectionsTest {

    @Test
    void 특정_구간의_포함_여부를_반환한다() {
        // given
        final Sections sections = new Sections(
                new Section(역1, 역2, 4),
                new Section(역2, 역3, 5)
        );
        final Section section1 = new Section(역1, 역2, 4);
        final Section section2 = new Section(역2, 역3, 5);
        final Section section4 = new Section(역2, 역1, 4);
        final Section section3 = new Section(역1, 역2, 6);
        final Section section5 = new Section(역1, 역3, 9);

        // when & then
        assertThat(sections.contains(section1)).isTrue();
        assertThat(sections.contains(section2)).isTrue();
        assertThat(sections.contains(section3)).isFalse();
        assertThat(sections.contains(section4)).isFalse();
        assertThat(sections.contains(section5)).isFalse();
    }

    @Test
    void 구간들의_총_거리를_구한다() {
        // given
        final Sections sections = new Sections(
                new Section(역1, 역2, 4),
                new Section(역2, 역3, 5),
                new Section(역3, 역4, 200)
        );

        // when & then
        assertThat(sections.totalDistance()).isEqualTo(209);
    }

    @Test
    void 상행_종점을_구한다() {
        // given
        final Sections sections = new Sections(
                new Section(역1, 역2, 4),
                new Section(역2, 역3, 5),
                new Section(역3, 역4, 200)
        );

        // when & then
        assertThat(sections.upTerminal()).isEqualTo(역1);
    }

    @Test
    void 하행_종점을_구한다() {
        // given
        final Sections sections = new Sections(
                new Section(역1, 역2, 4),
                new Section(역2, 역3, 5),
                new Section(역3, 역4, 200)
        );

        // when & then
        assertThat(sections.downTerminal()).isEqualTo(역4);
    }

    @Test
    void 구간들을_뒤집는다() {
        // given
        final Sections sections = new Sections(
                new Section(역1, 역2, 4),
                new Section(역2, 역3, 5),
                new Section(역3, 역4, 200)
        );

        // when
        final Sections reverse = sections.reverse();

        // then
        assertThat(reverse.sections()).containsExactly(
                new Section(역4, 역3, 200),
                new Section(역3, 역2, 5),
                new Section(역2, 역1, 4)
        );
    }

    @Nested
    class 구간_생성시 {

        @Test
        void 구간이_연결되어있으면_생성에_성공한다() {
            // given
            final List<Section> sections = List.of(
                    new Section(건대입구, 홍대입구, 10),
                    new Section(홍대입구, 잠실, 10),
                    new Section(잠실, 종착역, 10)
            );

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> new Sections(sections));
        }

        @Test
        void 구간이_연결되어있지_않으면_예외() {
            // given
            final List<Section> sections = List.of(
                    new Section(건대입구, 홍대입구, 10),
                    new Section(홍대입구, 잠실, 10),
                    new Section(잠실새내, 종착역, 10)
            );

            // when & then
            final String message = assertThrows(LineException.class, () ->
                    new Sections(sections)
            ).getMessage();
            assertThat(message).contains("각 구간의 연결 상태가 올바르지 않습니다.");
        }
    }

    @Nested
    class 구간_추가시 {

        @Test
        void 중간에_추가할_수_있다() {
            // given
            // 출발역 -[10km]- 종착역
            final Sections sections = new Sections(new Section(출발역, 종착역, 10));
            // 출발역 -[6km]- 경유역2 -[4km]- 종착역
            final Section middle1 = new Section(경유역2, 종착역, 4);
            // 출발역 -[1km]- 경유역1 -[5km]- 경유역2 -[4km]- 종착역
            final Section middle2 = new Section(출발역, 경유역1, 1);

            // when
            sections.addSection(middle1);
            sections.addSection(middle2);

            // then
            SectionFixtures.포함된_구간들을_검증한다(sections.sections(),
                    "출발역-[1km]-경유역1",
                    "경유역1-[5km]-경유역2",
                    "경유역2-[4km]-종착역"
            );
        }

        @Test
        void 상행_종점에_추가할_수_있다() {
            // given
            // 잠실 -[10km]- 종착역
            final Sections sections = new Sections(new Section(잠실, 종착역, 10));
            // 낙성대역 - [7km] - 잠실 -[10km]- 종착역
            final Section middle = new Section(낙성대, 잠실, 7);
            // 출발역 - [1km] - 낙성대역 - [7km] - 잠실 -[10km]- 종착역
            final Section top = new Section(출발역, 낙성대, 1);

            // when
            sections.addSection(middle);
            sections.addSection(top);

            // then
            SectionFixtures.포함된_구간들을_검증한다(sections.sections(),
                    "출발역-[1km]-낙성대",
                    "낙성대-[7km]-잠실",
                    "잠실-[10km]-종착역"
            );
        }

        @Test
        void 하행_종점에_추가할_수_있다() {
            // given
            // 출발역 -[10km]- 잠실
            final Sections sections = new Sections(new Section(출발역, 잠실, 10));
            // 출발역 -[10km]- 잠실 -[7km]- 건대역
            final Section middle = new Section(잠실, 건대입구, 7);
            // 출발역 -[10km]- 잠실 -[7km]- 건대역 -[1km]- 종착역
            final Section down = new Section(건대입구, 종착역, 1);

            // when
            sections.addSection(middle);
            sections.addSection(down);

            // then
            SectionFixtures.포함된_구간들을_검증한다(sections.sections(),
                    "출발역-[10km]-잠실",
                    "잠실-[7km]-건대입구",
                    "건대입구-[1km]-종착역"
            );
        }

        @Test
        void 추가하려는_구간의_두_역이_이미_구간들에_포함되어있으면_예외() {
            // given
            final Sections sections = new Sections(new Section(출발역, 종착역, 10));
            final Section middle1 = new Section(경유역2, 종착역, 4);
            final Section middle2 = new Section(출발역, 경유역1, 3);
            sections.addSection(middle1);
            sections.addSection(middle2);

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    sections.addSection(
                            new Section(출발역, 종착역, 1)
                    )).exceptionType();
            assertThat(exceptionType).isEqualTo(ALREADY_EXIST_STATIONS);
        }

        @Test
        void 추가하려는_구간의_두_역_모두_구간들에_존재하지_않으면_예외() {
            // given
            final Sections sections = new Sections(new Section(출발역, 종착역, 10));
            final Section middle1 = new Section(경유역2, 종착역, 4);
            final Section middle2 = new Section(출발역, 경유역1, 1);
            sections.addSection(middle1);
            sections.addSection(middle2);

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    sections.addSection(
                            new Section(없는역, 없는역2, 15)
                    )).exceptionType();
            assertThat(exceptionType).isEqualTo(NO_RELATION_WITH_ADDED_SECTION);
        }

        @Test
        void 중간에_추가시_추가하려는_구간의_길이가_기존_구간의_길이와_같거나_크다면_예외() {
            // given
            // 출발역 -[10km]- 종착역
            final Sections sections = new Sections(new Section(출발역, 종착역, 10));

            // 출발역 -[6km]- 경유역2 -[4km]- 종착역
            final Section middle1 = new Section(경유역2, 종착역, 4);
            sections.addSection(middle1);

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    sections.addSection(
                            new Section(경유역1, 경유역2, 6)
                    )).exceptionType();
            assertThat(exceptionType).isEqualTo(ADDED_SECTION_NOT_SMALLER_THAN_ORIGIN);
        }
    }

    @Nested
    class 구간들에서_역_제거시 {

        @Test
        void 역을_제거하고_구간들을_재조정한다() {
            // given
            final Sections sections = new Sections(
                    new Section(출발역, 잠실, 10),
                    new Section(잠실, 잠실나루, 5),
                    new Section(잠실나루, 종착역, 7)
            );

            // when
            sections.removeStation(잠실);

            // then
            SectionFixtures.포함된_구간들을_검증한다(sections.sections(),
                    "출발역-[15km]-잠실나루",
                    "잠실나루-[7km]-종착역"
            );
        }

        @Test
        void 상행_종점_제거_가능() {
            // given
            final Sections sections = new Sections(
                    new Section(출발역, 잠실, 10),
                    new Section(잠실, 잠실나루, 5),
                    new Section(잠실나루, 종착역, 7)
            );

            // when
            sections.removeStation(출발역);

            // then
            SectionFixtures.포함된_구간들을_검증한다(sections.sections(),
                    "잠실-[5km]-잠실나루",
                    "잠실나루-[7km]-종착역"
            );
        }

        @Test
        void 하행_종점_제거_가능() {
            // given
            final Sections sections = new Sections(
                    new Section(출발역, 잠실, 10),
                    new Section(잠실, 잠실나루, 5),
                    new Section(잠실나루, 종착역, 7)
            );

            // when
            sections.removeStation(종착역);

            // then
            SectionFixtures.포함된_구간들을_검증한다(sections.sections(),
                    "출발역-[10km]-잠실",
                    "잠실-[5km]-잠실나루"
            );
        }

        @Test
        void 없는_역은_제거할_수_없다() {
            // given
            final Sections sections = new Sections(new Section(출발역, 종착역, 10));

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    sections.removeStation(없는역)
            ).exceptionType();
            assertThat(exceptionType).isEqualTo(DELETED_STATION_NOT_EXIST);
        }

        @Test
        void 구간이_단_하나일_경우_하나의_역을_제거하면_구간_자체가_제거된다() {
            // given
            final Sections sections = new Sections(new Section(출발역, 종착역, 10));

            // when
            sections.removeStation(출발역);

            // then
            assertThat(sections.sections()).isEmpty();
        }
    }
}
