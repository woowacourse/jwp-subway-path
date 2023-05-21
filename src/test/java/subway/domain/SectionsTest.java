package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.domain.fixture.SectionFixtures.createSection;
import static subway.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;
import static subway.domain.fixture.StationFixture.사당역;
import static subway.domain.fixture.StationFixture.잠실새내역;
import static subway.domain.fixture.StationFixture.잠실역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.exception.ArgumentNotValidException;
import subway.exception.LineStationAddException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Sections 은(는)")
class SectionsTest {

    @Nested
    class 구간_생성시 {

        @Test
        void 구간이_연결되어있으면_생성에_성공한다() {
            // given
            final List<Section> sections = List.of(
                    createSection("말랑역", "오리역", 10),
                    createSection("오리역", "잠실역", 10),
                    createSection("잠실역", "종착역", 10)
            );

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> new Sections(sections));
        }

        @Test
        void 구간이_연결되어있지_않으면_예외() {
            // given
            final List<Section> sections = List.of(
                    createSection("말랑역", "오리역", 10),
                    createSection("오리역", "잠실역", 10),
                    createSection("잠실새내역", "종착역", 10)
            );

            // when & then
            assertThrows(ArgumentNotValidException.class, () ->
                    new Sections(sections)
            );
        }
    }

    @Nested
    class 구간_추가시 {

        @Test
        void 중간에_추가할_수_있다() {
            // given
            // 출발역 -[10km]- 종착역
            final Sections sections = new Sections(createSection("출발역", "종착역", 10));
            // 출발역 -[6km]- 경유역 2 -[4km]- 종착역
            final Section middle1 = createSection("경유역 2", "종착역", 4);
            // 출발역 -[1km]- 경유역 1 -[5km]- 경유역 2 -[4km]- 종착역
            final Section middle2 = createSection("출발역", "경유역 1", 1);

            // when
            sections.addSection(middle1);
            sections.addSection(middle2);

            // then
            포함된_구간들을_검증한다(sections.getSections(),
                    "출발역-[1km]-경유역 1",
                    "경유역 1-[5km]-경유역 2",
                    "경유역 2-[4km]-종착역"
            );
        }

        @Test
        void 상행_종점에_추가할_수_있다() {
            // given
            // 잠실역 -[10km]- 종착역
            final Sections sections = new Sections(createSection("잠실역", "종착역", 10));
            // 낙성대역 - [7km] - 잠실역 -[10km]- 종착역
            final Section middle = createSection("낙성대역", "잠실역", 7);
            // 출발역 - [1km] - 낙성대역 - [7km] - 잠실역 -[10km]- 종착역
            final Section top = createSection("출발역", "낙성대역", 1);

            // when
            sections.addSection(middle);
            sections.addSection(top);

            // then
            포함된_구간들을_검증한다(sections.getSections(),
                    "출발역-[1km]-낙성대역",
                    "낙성대역-[7km]-잠실역",
                    "잠실역-[10km]-종착역"
            );
        }

        @Test
        void 하행_종점에_추가할_수_있다() {
            // given
            // 출발역 -[10km]- 잠실역
            final Sections sections = new Sections(createSection("출발역", "잠실역", 10));
            // 출발역 -[10km]- 잠실역 -[7km]- 건대역
            final Section middle = createSection("잠실역", "건대역", 7);
            // 출발역 -[10km]- 잠실역 -[7km]- 건대역 -[1km]- 종착역
            final Section down = createSection("건대역", "종착역", 1);

            // when
            sections.addSection(middle);
            sections.addSection(down);

            // then
            포함된_구간들을_검증한다(sections.getSections(),
                    "출발역-[10km]-잠실역",
                    "잠실역-[7km]-건대역",
                    "건대역-[1km]-종착역"
            );
        }

        @Test
        void 추가하려는_구간의_두_역이_이미_구간들에_포함되어있으면_예외() {
            // given
            final Sections sections = new Sections(createSection("출발역", "종착역", 10));
            final Section middle1 = createSection("경유역 2", "종착역", 4);
            final Section middle2 = createSection("출발역", "경유역 1", 3);
            sections.addSection(middle1);
            sections.addSection(middle2);

            // when & then
            final String message = assertThrows(LineStationAddException.class, () ->
                    sections.addSection(
                            createSection("출발역", "종착역", 1)
                    )).getMessage();
            assertThat(message).isEqualTo("추가하려는 두 역이 이미 포함되어 있습니다.");
        }

        @Test
        void 추가하려는_구간의_두_역_모두_구간들에_존재하지_않으면_예외() {
            // given
            final Sections sections = new Sections(createSection("출발역", "종착역", 10));
            final Section middle1 = createSection("경유역 2", "종착역", 4);
            final Section middle2 = createSection("출발역", "경유역 1", 1);
            sections.addSection(middle1);
            sections.addSection(middle2);

            // when & then
            final String message = assertThrows(LineStationAddException.class, () ->
                    sections.addSection(
                            createSection("없는역", "없는역2", 15)
                    )).getMessage();
            assertThat(message).isEqualTo("노선에 존재하지 않는 역과 연결할 수 없습니다.");
        }

        @Test
        void 중간에_추가시_추가하려는_구간의_길이가_기존_구간의_길이와_같거나_크다면_예외() {
            // given
            // 출발역 -[10km]- 종착역
            final Sections sections = new Sections(createSection("출발역", "종착역", 10));

            // 출발역 -[6km]- 경유역 2 -[4km]- 종착역
            final Section middle1 = createSection("경유역 2", "종착역", 4);
            sections.addSection(middle1);

            // when & then
            final String message = assertThrows(LineStationAddException.class, () ->
                    sections.addSection(
                            createSection("경유역 1", "경유역 2", 6)
                    )).getMessage();
            assertThat(message).isEqualTo("현재 구간이 더 작아 차이를 구할 수 없습니다.");
        }
    }

    @Nested
    class 구간들에서_역_제거시 {

        @Test
        void 역을_제거하고_구간들을_재조정한다() {
            // given
            final Sections sections = new Sections(List.of(
                    createSection("출발역", "잠실역", 10),
                    createSection("잠실역", "잠실나루역", 5),
                    createSection("잠실나루역", "종착역", 7)
            ));

            // when
            sections.removeStation(new Station("잠실역"));

            // then
            포함된_구간들을_검증한다(sections.getSections(),
                    "출발역-[15km]-잠실나루역",
                    "잠실나루역-[7km]-종착역"
            );
        }

        @Test
        void 상행_종점_제거_가능() {
            // given
            final Sections sections = new Sections(List.of(
                    createSection("출발역", "잠실역", 10),
                    createSection("잠실역", "잠실나루역", 5),
                    createSection("잠실나루역", "종착역", 7)
            ));

            // when
            sections.removeStation(new Station("출발역"));

            // then
            포함된_구간들을_검증한다(sections.getSections(),
                    "잠실역-[5km]-잠실나루역",
                    "잠실나루역-[7km]-종착역"
            );
        }

        @Test
        void 하행_종점_제거_가능() {
            // given
            final Sections sections = new Sections(List.of(
                    createSection("출발역", "잠실역", 10),
                    createSection("잠실역", "잠실나루역", 5),
                    createSection("잠실나루역", "종착역", 7)
            ));

            // when
            sections.removeStation(new Station("종착역"));

            // then
            포함된_구간들을_검증한다(sections.getSections(),
                    "출발역-[10km]-잠실역",
                    "잠실역-[5km]-잠실나루역"
            );
        }

        @Test
        void 없는_역은_제거할_수_없다() {
            // given
            final Sections sections = new Sections(createSection("출발역", "종착역", 10));

            // when & then
            final String message = assertThrows(IllegalArgumentException.class, () ->
                    sections.removeStation(new Station("없는역"))
            ).getMessage();
            assertThat(message).isEqualTo("없는 역은 제거할 수 없습니다.");
        }

        @Test
        void 구간이_단_하나일_경우_하나의_역을_제거하면_구간_자체가_제거된다() {
            // given
            final Sections sections = new Sections(createSection("출발역", "종착역", 10));

            // when
            sections.removeStation(new Station("출발역"));

            // then
            assertThat(sections.getSections()).isEmpty();
        }
    }

    @Test
    void 섹션을_포함하고_있으면_true() {
        // given
        Sections sections = new Sections(List.of(new Section(잠실역, 사당역, 10)));
        Section sameSection = new Section(잠실역, 사당역, 10);

        // when & then
        assertThat(sections.contains(sameSection)).isTrue();
    }

    @Test
    void 섹션을_포함하고_있지않으면_false() {
        // given
        Sections sections = new Sections(List.of(new Section(잠실역, 사당역, 10)));
        Section sameSection = new Section(잠실새내역, 사당역, 10);

        // when & then
        assertThat(sections.contains(sameSection)).isFalse();
    }
}
