package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.디노_조앤;
import static subway.common.fixture.DomainFixture.로운;
import static subway.common.fixture.DomainFixture.조앤;
import static subway.common.fixture.DomainFixture.조앤_로운;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.DomainFixture.후추_디노;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    @Nested
    class InsertTest {

        @Test
        void 첫_구간을_추가한다() {
            // given
            final Sections sections = new Sections(new ArrayList<>());

            // when
            final Sections insertedSections = sections.insert(후추, 디노, 7);

            // then
            assertThat(insertedSections.getSections())
                    .contains(new Section(후추, 디노, 7));
        }

        //before : 후추 - 7 - 디노 - 4 - 조앤
        //after : 후추 - 7 - 디노 - 2 - 로운 - 2 - 조앤
        @Test
        void 역을_오른쪽_사이에_추가한다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤));

            // when
            final Sections insertedSections = sections.insert(디노, 로운, 2);

            // then
            assertSoftly(softly -> {
                final List<Section> allSections = insertedSections.getSections();
                softly.assertThat(allSections).contains(후추_디노, new Section(디노, 로운, 2), new Section(로운, 조앤, 2));
                softly.assertThat(allSections).doesNotContain(디노_조앤);
            });
        }

        //before : 후추 - 7 - 디노 - 4 - 조앤
        //after : 후추 - 5 - 로운 - 2 - 디노 - 4 - 조앤
        @Test
        void 역을_왼쪽_사이에_추가한다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤));

            // when
            final Sections insertedSections = sections.insert(로운, 디노, 2);

            // then
            assertSoftly(softly -> {
                final List<Section> allSections = insertedSections.getSections();
                softly.assertThat(allSections).contains(new Section(후추, 로운, 5), new Section(로운, 디노, 2), 디노_조앤);
                softly.assertThat(allSections).doesNotContain(후추_디노);
            });
        }

        //before : 후추 - 7 - 디노 - 4 - 조앤
        //after : 후추 - 7 - 디노 - 4 - 조앤 - 2 - 로운
        @Test
        void 역을_오른쪽_끝에_추가한다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤));

            // when
            final Sections insertedSections = sections.insert(조앤, 로운, 2);

            // then
            assertThat(insertedSections.getSections()).contains(후추_디노, 디노_조앤, new Section(조앤, 로운, 2));
        }

        //before : 후추 - 7 - 디노 - 4 - 조앤
        //after : 로운 - 2 - 후추 - 7 - 디노 - 4 - 조앤
        @Test
        void 역을_왼쪽_끝에_추가한다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤));

            // when
            final Sections insertedSections = sections.insert(로운, 후추, 2);

            // then
            assertThat(insertedSections.getSections()).contains(new Section(로운, 후추, 2), 후추_디노, 디노_조앤);
        }

        // 후추 - 7 - 디노 - 4 - 조앤
        @Test
        void 호선에_존재하는_역인_경우_예외를_던진다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤));

            // expect
            assertThatThrownBy(() -> sections.insert(후추, 조앤, 2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 호선에 존재하는 역입니다.");
        }

        // 후추 - 7 - 디노
        @Test
        void 역이_다른_역들과_연결되지_않는_경우_예외를_던진다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노));

            // expect
            assertThatThrownBy(() -> sections.insert(로운, 조앤, 3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("역이 기존 호선과 연결되어야 합니다.");
        }

        // 후추 - 1 - 디노
        @Test
        void 역_사이에_추가할_수_없는_거리인_경우_예외를_던진다() {
            // given
            final Sections sections = new Sections(List.of(new Section(1L, 후추, 디노, 1)));

            // expect
            assertThatThrownBy(() -> sections.insert(후추, 조앤, 3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("기존 두 역 사이의 거리가 부족합니다.");
        }
    }

    @Nested
    class DeleteTest {

        //before : 후추 - 7 - 디노 - 4 - 조앤
        //after : 후추 - 7 - 디노
        @Test
        void 오른쪽_끝_역을_삭제한다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤));

            // when
            final Sections deletedSections = sections.delete(조앤);

            // then
            assertSoftly(softly -> {
                final List<Section> allSections = deletedSections.getSections();
                softly.assertThat(allSections).contains(후추_디노);
                softly.assertThat(allSections).doesNotContain(디노_조앤);
            });
        }

        //before : 후추 - 7 - 디노 - 4 - 조앤
        //after : 디노 - 4 - 조앤
        @Test
        void 왼쪽_끝_역을_삭제한다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤));

            // when
            final Sections deletedSections = sections.delete(후추);

            // then
            assertSoftly(softly -> {
                final List<Section> allSections = deletedSections.getSections();
                softly.assertThat(allSections).contains(디노_조앤);
                softly.assertThat(allSections).doesNotContain(후추_디노);
            });
        }

        //before : 후추 - 7 - 디노 - 4 - 조앤
        //after : 후추 - 11 - 조앤
        @Test
        void 중간_역을_삭제한다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤));

            // when
            final Sections deletedSections = sections.delete(디노);

            // then
            assertSoftly(softly -> {
                final List<Section> allSections = deletedSections.getSections();
                softly.assertThat(allSections).contains(new Section(후추, 조앤, 11));
                softly.assertThat(allSections).doesNotContain(후추_디노, 디노_조앤);
            });
        }

        //before : 후추 - 7 - 디노
        //after :
        @Test
        void 역이_두_개인_경우_역을_모두_삭제한다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노));

            // when
            final Sections deletedSections = sections.delete(디노);

            // then
            assertSoftly(softly -> {
                final List<Section> allSections = deletedSections.getSections();
                softly.assertThat(allSections).isEmpty();
                softly.assertThat(allSections).doesNotContain(후추_디노);
            });
        }

        // 후추 - 7 - 디노 - 4 - 조앤
        @Test
        void 역이_호선에_존재하지_않는_경우_예외를_던진다() {
            // given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤));

            // expect
            assertThatThrownBy(() -> sections.delete(로운))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("입력한 역이 호선에 존재하지 않습니다.");
        }
    }

    @Nested
    class OrderTest {

        @Test
        void 역을_정렬해서_반환한다() {
            //given
            final Sections sections = new Sections(List.of(후추_디노, 디노_조앤, 조앤_로운));

            //when
            final List<Station> orderedStations = sections.getOrderedStations();

            //then
            assertThat(orderedStations).containsExactly(후추, 디노, 조앤, 로운);
            System.out.println("orderedStations = " + orderedStations);
        }
    }
}
