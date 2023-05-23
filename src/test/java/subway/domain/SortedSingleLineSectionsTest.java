package subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.TestSource.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import subway.domain.dto.ChangesByAddition;
import subway.domain.dto.ChangesByDeletion;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SortedSingleLineSectionsTest {

    @Test
    void id로_역을_찾는다() {
        // given
        Station cheonho = new Station(1L, "천호");
        Station jamsil = new Station(2L, "잠실");
        Section cheonhoJamsil10 = new Section(cheonho, jamsil, new Line("8호선", "pink", 0), 10);

        SortedSingleLineSections sortedSingleLineSections = new SortedSingleLineSections(List.of(cheonhoJamsil10));

        // then
        assertThat(sortedSingleLineSections.findStationById(1L)).isPresent();
        assertThat(sortedSingleLineSections.findStationById(3L)).isEmpty();
    }

    @Test
    void 두_역이_포함된_구간을_찾는다() {
        // given
        // 장지 - 10 - 잠실 - 10 - 천호
        SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);
        Station upStation = jamsil;
        Station downStation = jangji;

        // then
        Section section = line8.findAnySectionWithGivenStations(jamsil, jangji);
        assertThat(section.getUpStation()).isEqualTo(upStation);
        assertThat(section.getDownStation()).isEqualTo(downStation);
    }

    @Test
    void 두_역이_포함된_구간이_없으면_예외가_발생한다() {
        // given
        // 장지 - 10 - 잠실 - 10 - 천호
        SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

        // then
        assertThatThrownBy(() -> line8.findAnySectionWithGivenStations(cheonho, jangji))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주어진 역으로 구성된 구간이 존재하지 않습니다.");
    }

    @Nested
    class getStationsInOrder_테스트 {

        @Test
        void 보유한_역을_상행_종점부터_순서대로_반환한다() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

            // when
            List<Station> stations = line8.getStationsInOrder();

            // then
            assertThat(stations).containsExactly(cheonho, jamsil, jangji);
        }

        @Test
        void 보유한_역이_없다면_empty_list를_반환한다() {
            // given
            SortedSingleLineSections sortedSingleLineSections = new SortedSingleLineSections(Collections.emptyList());

            // when
            List<Station> stations = sortedSingleLineSections.getStationsInOrder();

            // then
            assertThat(stations.size()).isZero();
        }
    }

    @Nested
    class 역_추가_성공_테스트 {

        @Test
        void 새로운_두_역을_추가한다() {
            assertDoesNotThrow(() -> new SortedSingleLineSections(List.of(new Section(cheonho, jangji, pink, 10))));
        }

        @Test
        void 상행_종점의_하행_방향으로_역을_추가한다() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

            // when
            Station mongchon = new Station(10L, "몽촌토성");
            ChangesByAddition changes = line8.findChangesWhenAdd(cheonho, mongchon, pink, 4);

            // then
            // 장지 - 10 - 잠실 - 6 - 몽촌 - 4 - 천호
            Section cheonhoMongchon4 = new Section(cheonho, mongchon, pink, 4);
            Section mongchonJamsil6 = new Section(mongchon, jamsil, pink, 6);

            assertThat(changes.getAddedSections()).containsExactlyInAnyOrder(cheonhoMongchon4, mongchonJamsil6);
            assertThat(changes.getDeletedSections()).containsExactlyInAnyOrder(cheonhoJamsil10);
        }

        @Test
        void 하행_종점의_상행_방향으로_역을_추가한다() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

            // when
            Station seokchon = new Station(10L, "석촌");
            ChangesByAddition changes = line8.findChangesWhenAdd(seokchon, jangji, pink, 9);

            // then
            // 장지 - 9 - 석촌 - 1 - 잠실 - 10 - 천호
            Section jamsilSeokchon1 = new Section(jamsil, seokchon, pink, 1);
            Section seokchonJangi9 = new Section(seokchon, jangji, pink, 9);

            assertThat(changes.getAddedSections()).containsExactlyInAnyOrder(jamsilSeokchon1, seokchonJangi9);
            assertThat(changes.getDeletedSections()).containsExactly(jamsilJangji10);
        }

        @Test
        void 상행_종점_이후에_새로운_상행_종점을_추가한다() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            SortedSingleLineSections line8 = new SortedSingleLineSections(List.of(cheonhoJamsil10, jamsilJangji10));

            // when
            Station amsa = new Station(10L, "암사");
            ChangesByAddition changes = line8.findChangesWhenAdd(amsa, cheonho, pink, 10);

            //then
            // 장지 - 10 - 잠실 - 10 - 천호 - 10 - 암사
            Section amsaCheonho10 = new Section(amsa, cheonho, pink, 10);

            assertThat(changes.getAddedSections()).containsExactly(amsaCheonho10);
            assertThat(changes.getDeletedSections()).isEmpty();
        }

        @Test
        void 하행_종점_이후에_새로운_하행_종점을_추가한다() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

            // when
            Station moran = new Station(10L, "모란");
            ChangesByAddition changes = line8.findChangesWhenAdd(jangji, moran, pink, 4);

            //then
            // 모란 - 4 - 장지 - 10 - 잠실 - 10 - 천호
            Section jangjiMoran4 = new Section(jangji, moran, pink, 4);

            assertThat(changes.getAddedSections()).containsExactly(jangjiMoran4);
            assertThat(changes.getDeletedSections()).isEmpty();
        }

        @Test
        void 중간_역의_상행_방향으로_역을_추가한다() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            SortedSingleLineSections line8 = new SortedSingleLineSections(List.of(cheonhoJamsil10, jamsilJangji10));

            // when
            Station mongchon = new Station(10L, "몽촌토성");
            ChangesByAddition changes = line8.findChangesWhenAdd(mongchon, jamsil, pink, 9);

            //then
            // 장지 - 10 - 잠실 - 9 - 몽촌 - 1 - 천호
            Section cheonhoMongchon1 = new Section(cheonho, mongchon, pink, 1);
            Section mongchonJamsil9 = new Section(mongchon, jamsil, pink, 9);

            assertThat(changes.getAddedSections()).containsExactlyInAnyOrder(cheonhoMongchon1, mongchonJamsil9);
            assertThat(changes.getDeletedSections()).containsExactly(cheonhoJamsil10);
        }

        @Test
        void 중간_역의_하행_방향으로_역을_추가한다() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

            // when
            Station seokchon = new Station(4L, "석촌");
            ChangesByAddition changes = line8.findChangesWhenAdd(jamsil, seokchon, pink, 4);

            //then
            // 장지 - 6 - 석촌 - 4 - 잠실 - 10 - 천호
            Section jamsilSeokchon4 = new Section(jamsil, seokchon, pink, 4);
            Section seokchonJangi6 = new Section(seokchon, jangji, pink, 6);

            assertThat(changes.getAddedSections()).containsExactlyInAnyOrder(jamsilSeokchon4, seokchonJangi6);
            assertThat(changes.getDeletedSections()).containsExactly(jamsilJangji10);
        }
    }

    @Nested
    class 역_추가_실패_테스트 {

        @Test
        void 입력된_두_역이_이미_같은_노선에_존재하는_경우() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

            // when & then
            assertThatThrownBy(() -> line8.findChangesWhenAdd(cheonho, jamsil, pink, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최초 등록이 아닌 경우 하나의 역은 이미 존재해야 합니다.");
        }

        @Test
        void 최초_등록이_아니며_두_역이_모두_존재하지_않는_경우() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

            // when
            Station mongchon = new Station(10L, "몽촌토성");
            Station seokchon = new Station(4L, "석촌");

            // then
            assertThatThrownBy(() -> line8.findChangesWhenAdd(mongchon, seokchon, pink, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최초 등록이 아닌 경우 하나의 역은 이미 존재해야 합니다.");
        }

        @Nested
        class 역_삭제_테스트 {

            @Test
            void 포함되지_않은_역을_삭제한다() {
                // given
                // 장지 - 10 - 잠실 - 10 - 천호
                SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

                // when
                ChangesByDeletion changes = line8.findChangesWhenDelete(gangnam);

                // then
                assertThat(changes.getAddedSections()).isEmpty();
                assertThat(changes.getDeletedSections()).isEmpty();
            }

            @Test
            void 종점을_삭제한다() {
                // given
                // 장지 - 10 - 잠실 - 10 - 천호
                SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

                // when
                ChangesByDeletion changes = line8.findChangesWhenDelete(cheonho);

                // then
                // 장지 - 10 - 잠실
                assertThat(changes.getAddedSections()).isEmpty();
                assertThat(changes.getDeletedSections()).containsExactlyInAnyOrder(jamsilJangji10);
            }

            @Test
            void 종점이_아닌_역을_삭제한다() {
                // given
                // 장지 - 10 - 잠실 - 10 - 천호
                SortedSingleLineSections line8 = new SortedSingleLineSections(line8Sections);

                // when
                ChangesByDeletion changes = line8.findChangesWhenDelete(jamsil);

                // then
                // 장지 - 20 - 천호
                Section cheonhoJangji20 = new Section(cheonho, jangji, pink, 20);
                assertThat(changes.getAddedSections()).containsExactlyInAnyOrder(cheonhoJangji20);
                assertThat(changes.getDeletedSections()).containsExactlyInAnyOrder(cheonhoJamsil10, jamsilJangji10);
            }
        }
    }
}
