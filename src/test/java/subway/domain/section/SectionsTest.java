package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.fixture.SectionFixture.이호선_역삼_삼성_3;
import subway.fixture.StationFixture.건대역;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    @Nested
    class 노선에_역_등록시_ {

        @Test
        void 최초_등록() {
            Section section = 이호선_역삼_삼성_3.SECTION;
            Sections sections = new Sections();

            sections.add(section);

            assertThat(sections.findOrderedStation())
                    .containsExactly(역삼역.STATION, 삼성역.STATION);
        }

        @Test
        void 두개의_역이_모두_노선에_존재하면_예외() {
            Section section = new Section(삼성역.STATION, 잠실역.STATION, 2);
            Sections sections = new Sections();
            sections.add(section);

            assertThatThrownBy(() -> sections.add(section))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("이미 존재하는 구간입니다.");
        }

        @Test
        void 두개의_역이_모두_노선에_존재하지_않으면_예외() {
            Section section = new Section(삼성역.STATION, 잠실역.STATION, 2);
            Sections sections = new Sections();
            sections.add(section);

            assertThatThrownBy(() -> sections.add(new Section(건대역.STATION, 역삼역.STATION, 2)))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("하나의 역은 반드시 노선에 존재해야합니다.");
        }

        @Nested
        class 하나의_역이_노선에_존재하고_ {

            @Nested
            class 상행역이_이미_노선에_존재할_경우_ {

                @Test
                void 해당_역의_하행역이_존재하지_않을_경우_등록한다() {
                    Section section = new Section(삼성역.STATION, 잠실역.STATION, 2);
                    Sections sections = new Sections();
                    sections.add(section);

                    sections.add(new Section(잠실역.STATION, 건대역.STATION, 1));

                    assertThat(sections.findOrderedStation())
                            .containsExactly(삼성역.STATION, 잠실역.STATION, 건대역.STATION);
                }

                @Nested
                class 해당_역의_하행역이_존재할_경우_ {

                    @ValueSource(ints = {2, 3})
                    @ParameterizedTest
                    void 기존_구간보다_거리가_크거나_같으면_예외(int distance) {
                        Section section = new Section(삼성역.STATION, 건대역.STATION, 2);
                        Sections sections = new Sections();
                        sections.add(section);

                        assertThatThrownBy(() -> sections.add(new Section(삼성역.STATION, 잠실역.STATION, distance)))
                                .isInstanceOf(SubwayIllegalArgumentException.class)
                                .hasMessage("거리는 기존 구간보다 짧아야합니다.");
                    }

                    @Test
                    void 구간을_추가한다() {
                        Section section = new Section(삼성역.STATION, 건대역.STATION, 5);
                        Sections sections = new Sections();
                        sections.add(section);

                        sections.add(new Section(삼성역.STATION, 잠실역.STATION, 2));

                        assertThat(sections.getSections())
                                .usingRecursiveComparison()
                                .ignoringFields("id")
                                .isEqualTo(List.of(new Section(삼성역.STATION, 잠실역.STATION, 2),
                                        new Section(잠실역.STATION, 건대역.STATION, 3)));
                    }
                }
            }

            @Nested
            class 하행역이_이미_노선에_존재할_경우_ {

                @Test
                void 해당_역의_상행역이_존재하지_않을_경우_등록한다() {
                    Section section = new Section(잠실역.STATION, 건대역.STATION, 3);
                    Sections sections = new Sections();
                    sections.add(section);

                    sections.add(new Section(삼성역.STATION, 잠실역.STATION, 2));

                    assertThat(sections.findOrderedStation())
                            .containsExactly(삼성역.STATION, 잠실역.STATION, 건대역.STATION);
                }

                @Nested
                class 해당_역의_상행역이_존재할_경우_ {

                    @ValueSource(ints = {2, 3})
                    @ParameterizedTest
                    void 기존_구간보다_거리가_크거나_같으면_예외(int distance) {
                        Section section = new Section(삼성역.STATION, 건대역.STATION, 2);
                        Sections sections = new Sections();
                        sections.add(section);

                        assertThatThrownBy(() -> sections.add(new Section(잠실역.STATION, 건대역.STATION, distance)))
                                .isInstanceOf(SubwayIllegalArgumentException.class)
                                .hasMessage("거리는 기존 구간보다 짧아야합니다.");
                    }

                    @Test
                    void 구간을_추가한다() {
                        Section section = new Section(삼성역.STATION, 건대역.STATION, 5);
                        Sections sections = new Sections();
                        sections.add(section);

                        sections.add(new Section(잠실역.STATION, 건대역.STATION, 3));

                        assertThat(sections.getSections())
                                .usingRecursiveComparison()
                                .ignoringCollectionOrder()
                                .ignoringFields("id")
                                .isEqualTo(List.of(new Section(삼성역.STATION, 잠실역.STATION, 2),
                                        new Section(잠실역.STATION, 건대역.STATION, 3)));
                    }
                }
            }
        }
    }

    @Nested
    class 노선에서_역_제거시_ {

        @Test
        void 노선에_구간이_없을시_예외() {
            Sections sections = new Sections();

            assertThatThrownBy(() -> sections.remove(역삼역.STATION))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("해당 역이 노선에 존재하지 않습니다.");
        }

        @Test
        void 노선에_해당_역이_없을시_예외() {
            Section section = 이호선_역삼_삼성_3.SECTION;
            Sections sections = new Sections();
            sections.add(section);

            assertThatThrownBy(() -> sections.remove(잠실역.STATION))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("해당 역이 노선에 존재하지 않습니다.");
        }

        @Test
        void 구간이_하나일_때_해당_구간을_제거한다() {
            Section section = 이호선_역삼_삼성_3.SECTION;
            Sections sections = new Sections();
            sections.add(section);

            sections.remove(역삼역.STATION);

            assertThat(sections.findOrderedStation()).isEmpty();
        }

        @Test
        void 해당_역이_종점이면_제거한다() {
            Section section1 = new Section(삼성역.STATION, 잠실역.STATION, 2);
            Section section2 = new Section(잠실역.STATION, 건대역.STATION, 3);
            Sections sections = new Sections();
            sections.add(section1);
            sections.add(section2);

            sections.remove(건대역.STATION);

            List<Section> result = sections.getSections();
            assertAll(
                    () -> {
                        assertThat(result).hasSize(1);
                        assertThat(result.get(0)).isEqualTo(section1);
                    }
            );
        }

        @Test
        void 해당_역이_중간_역일_경우_제거하고_거리를_더한다() {
            Section section1 = new Section(삼성역.STATION, 잠실역.STATION, 2);
            Section section2 = new Section(잠실역.STATION, 건대역.STATION, 3);
            Sections sections = new Sections();
            sections.add(section1);
            sections.add(section2);

            sections.remove(잠실역.STATION);

            List<Section> result = sections.getSections();
            assertAll(
                    () -> {
                        assertThat(result).hasSize(1);
                        assertThat(result.get(0))
                                .usingRecursiveComparison()
                                .ignoringFields("id")
                                .isEqualTo(new Section(삼성역.STATION, 건대역.STATION, 5));
                    }
            );
        }
    }

    @Nested
    class 노선에서_역_반환시_ {

        @Test
        void 구간이_없을시_빈_리스트를_반환한다() {
            Sections sections = new Sections();

            List<Station> stations = sections.findOrderedStation();

            assertThat(stations).isEmpty();
        }

        @Test
        void 상행에서_하행_순으로_역을_반환한다() {
            Section section1 = new Section(잠실역.STATION, 건대역.STATION, 2);
            Section section2 = new Section(삼성역.STATION, 잠실역.STATION, 2);
            Section section3 = new Section(역삼역.STATION, 삼성역.STATION, 2);
            Sections sections = new Sections();
            sections.add(section1);
            sections.add(section2);
            sections.add(section3);

            List<Station> stations = sections.findOrderedStation();

            assertThat(stations)
                    .isEqualTo(List.of(역삼역.STATION, 삼성역.STATION, 잠실역.STATION, 건대역.STATION));
        }
    }

    @Nested
    class 구간_포함_여부_검사시_ {

        @Test
        void 해당_구간을_포함하면_참() {
            // given
            Section section = 이호선_역삼_삼성_3.SECTION;
            Sections sections = new Sections(List.of(section));

            // when
            boolean contains = sections.contains(section);

            // then
            assertThat(contains).isTrue();
        }

        @Test
        void 해당_구간을_포함하지_않으면_거짓() {
            // given
            Section section = 이호선_역삼_삼성_3.SECTION;
            Sections sections = new Sections();

            // when
            boolean contains = sections.contains(section);

            // then
            assertThat(contains).isFalse();
        }
    }
}
