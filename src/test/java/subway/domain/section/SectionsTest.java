package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class SectionsTest {

    Station first = Station.of(1L, "1역");
    Station second = Station.of(2L, "2역");
    Station third = Station.of(3L, "3역");

    Sections sections;

    @Nested
    class 초기_구간_등록_테스트 {

        @BeforeEach
        void setUp() {
            sections = Sections.create();
        }

        @Test
        void addSection_메소드는_아무_구간도_없는_경우_두_역을_등록한다() {
            sections.addSection(first, second, Distance.from(5), Direction.UP);

            assertThat(sections.sections().keySet()).hasSize(2);
        }
    }

    @Nested
    class 구간_등록_테스트 {

        @BeforeEach
        void setUp() {
            sections = Sections.create();
            sections.addSection(first, second, Distance.from(5), Direction.UP);
        }

        @Test
        void addSection_메소드는_구간_사이에_구간을_추가하고자_하는_경우_거리를_계산해_역을_등록한다() {
            sections.addSection(first, third, Distance.from(1), Direction.UP);

            final Map<Station, Section> sections = SectionsTest.this.sections.sections();
            final Section firstSection = sections.get(first);
            final Section secondSection = sections.get(second);
            final Section thirdSection = sections.get(third);

            assertAll(
                    () -> assertThat(firstSection.findDistanceByStation(third).distance()).isEqualTo(1),
                    () -> assertThat(firstSection.findDirectionByStation(third)).isSameAs(Direction.UP),
                    () -> assertThat(secondSection.findDistanceByStation(third).distance()).isEqualTo(4),
                    () -> assertThat(secondSection.findDirectionByStation(third)).isSameAs(Direction.DOWN),
                    () -> assertThat(thirdSection.findDistanceByStation(second).distance()).isEqualTo(4),
                    () -> assertThat(thirdSection.findDirectionByStation(second)).isEqualTo(Direction.UP),
                    () -> assertThat(thirdSection.findDistanceByStation(first).distance()).isEqualTo(1),
                    () -> assertThat(thirdSection.findDirectionByStation(first)).isEqualTo(Direction.DOWN)
            );
        }

        @Test
        void addSection_메소드는_기준_역이_종점_역이며_방향이_일치하는_경우_새로운_종점_역을_추가한다() {
            sections.addSection(first, third, Distance.from(10), Direction.DOWN);

            final Map<Station, Section> sections = SectionsTest.this.sections.sections();
            final Section firstSection = sections.get(first);
            final Section secondSection = sections.get(second);
            final Section thirdSection = sections.get(third);

            assertAll(
                    () -> assertThat(firstSection.findDistanceByStation(third).distance()).isEqualTo(10),
                    () -> assertThat(firstSection.findDirectionByStation(third)).isSameAs(Direction.DOWN),
                    () -> assertThat(firstSection.findDistanceByStation(second).distance()).isEqualTo(5),
                    () -> assertThat(firstSection.findDirectionByStation(second)).isSameAs(Direction.UP),
                    () -> assertThat(thirdSection.findDistanceByStation(first).distance()).isEqualTo(10),
                    () -> assertThat(thirdSection.findDirectionByStation(first)).isEqualTo(Direction.UP),
                    () -> assertThat(secondSection.findDistanceByStation(first).distance()).isEqualTo(5),
                    () -> assertThat(secondSection.findDirectionByStation(first)).isEqualTo(Direction.DOWN)
            );
        }

        @Test
        void addSection_메소드는_기준_역이_등록되어_있지_않은_경우_예외가_발생한다() {
            assertThatThrownBy(() -> sections.addSection(third, second, Distance.from(5), Direction.DOWN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("지정한 기준 역은 등록되어 있지 않은 역입니다.");
        }

        @Test
        void addSection_메소드는_새로_등록하는_역이_이미_등록된_경우_예외가_발생한다() {
            assertThatThrownBy(() -> sections.addSection(second, first, Distance.from(5), Direction.DOWN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 등록된 역입니다.");
        }

        @Test
        void addSection_메소드는_중간에_역을_추가하는_경우_기존_거리보다_클_경우_예와가_발생한다() {
            assertThatThrownBy(() -> sections.addSection(second, third, Distance.from(50), Direction.DOWN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록되는 구간 중간에 다른 역이 존재합니다.");
        }
    }

    @Nested
    class 구간_삭제_테스트 {

        @BeforeEach
        void setUp() {
            sections = Sections.create();
            sections.addSection(first, second, Distance.from(5), Direction.DOWN);
        }

        @Test
        void removeStation_메소드는_구간이_하나일_경우_역_하나를_구간에서_삭제하면_해당_구간_전체를_삭제한다() {
            sections.removeStation(first);

            final Map<Station, Section> actual = SectionsTest.this.sections.sections();

            assertThat(actual).isEmpty();
        }

        @Test
        void removeStation_메소드는_중간_역을_삭제하면_나머지_역을_하나로_이어준다() {
            sections.addSection(second, third, Distance.from(5), Direction.DOWN);

            sections.removeStation(second);

            final Map<Station, Section> sections = SectionsTest.this.sections.sections();
            final Section firstSection = sections.get(first);
            final Section thirdSection = sections.get(third);

            assertAll(
                    () -> assertThat(firstSection.findDistanceByStation(third).distance()).isEqualTo(10),
                    () -> assertThat(firstSection.findDirectionByStation(third)).isEqualTo(Direction.DOWN),
                    () -> assertThat(thirdSection.findDistanceByStation(first).distance()).isEqualTo(10),
                    () -> assertThat(thirdSection.findDirectionByStation(first)).isEqualTo(Direction.UP)
            );
        }

        @Test
        void removeStation_메소드는_종점_역을_삭제한다() {
            sections.addSection(second, third, Distance.from(5), Direction.DOWN);

            sections.removeStation(third);

            final Map<Station, Section> actual = sections.sections();

            assertThat(actual).hasSize(2);
        }

        @Test
        void removeStation_메소드는_등록되지_않은_역을_전달하면_예외가_발생한다() {
            assertThatThrownBy(() -> sections.removeStation(third))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 역은 노선에 등록되어 있지 않습니다.");
        }
    }

    @Nested
    class 구간_전체_정렬_조회_테스트 {

        @BeforeEach
        void setUp() {
            sections = Sections.create();
            sections.addSection(first, second, Distance.from(5), Direction.DOWN);
            sections.addSection(second, third, Distance.from(5), Direction.DOWN);
        }

        @Test
        void findStationsByOrdered_메소드는_호출하면_구간에_따라_역을_순서대로_정렬해_반환한다() {
            final List<Station> stations = sections.findStationsByOrdered();

            assertThat(stations).containsExactly(first, second, third);
        }
    }

    @Nested
    class 인접_역_전체_조회_테스트 {

        @BeforeEach
        void setUp() {
            sections = Sections.create();
            sections.addSection(first, second, Distance.from(5), Direction.DOWN);
            sections.addSection(second, third, Distance.from(5), Direction.DOWN);
        }

        @Test
        void findAllAdjustStationByStation_메소드는_역을_전달하면_해당_역과_인접한_모든_역을_반환한다() {
            final List<Station> actual = sections.findAllAdjustStationByStation(second);

            assertAll(
                    () -> assertThat(actual).hasSize(2),
                    () -> assertThat(actual).contains(first, third)
            );
        }
    }
}
