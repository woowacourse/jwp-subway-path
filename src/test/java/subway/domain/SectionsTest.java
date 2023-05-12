package subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.domain.TestSource.*;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import subway.domain.dto.ChangesByAddition;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SectionsTest {

    @Nested
    class 역_추가_성공_테스트 {

        @Test
        void 새로운_두_역을_추가한다() {
            assertDoesNotThrow(() -> new Sections(List.of(new Section(cheonho, jangji, pink, 10))));
        }

        @Test
        void 상행_종점의_하행_방향으로_역을_추가한다() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            Sections line8 = line8source;

            // when
            Station mongchon = new Station(10L, "몽촌토성");
            ChangesByAddition changes = line8.getChangesWhenAdded(cheonho, mongchon, pink, 4);

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
            Sections line8 = line8source;

            // when
            Station seokchon = new Station(10L, "석촌");
            ChangesByAddition changes = line8.getChangesWhenAdded(seokchon, jangji, pink, 9);

            // then
            // 장지 - 9 - 석촌 - 1 - 잠실 - 10 - 천호
            Section jamsilSeokchon1 = new Section(jamsil, seokchon, pink, 1);
            Section seokchonJangi9 = new Section(seokchon, jangji, pink, 9);

            assertThat(changes.getAddedSections()).containsExactlyInAnyOrder(jamsilSeokchon1, seokchonJangi9);
            // assertThat(changes.getDeletedSections()).containsExactly(jamsilJangji10);
        }

        @Test
        void 상행_종점_이후에_새로운_상행_종점을_추가한다() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            Sections line8 = line8source;

            // when
            Station amsa = new Station(10L, "암사");
            ChangesByAddition changes = line8.getChangesWhenAdded(amsa, cheonho, pink, 10);

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
            Sections line8 = line8source;

            // when
            Station moran = new Station(10L, "모란");
            ChangesByAddition changes = line8.getChangesWhenAdded(jangji, moran, pink, 4);

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
            Sections line8 = line8source;

            // when
            Station mongchon = new Station(10L, "몽촌토성");
            ChangesByAddition changes = line8.getChangesWhenAdded(mongchon, jamsil, pink, 9);

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
            Sections line8 = line8source;

            // when
            Station seokchon = new Station(4L, "석촌");
            ChangesByAddition changes = line8.getChangesWhenAdded(jamsil, seokchon, pink, 4);

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
        void 입력된_두_역이_이미_모두_존재하는_경우() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            Sections line8 = line8source;

            // when & then
            assertThatThrownBy(() -> line8.getChangesWhenAdded(cheonho, jamsil, pink, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최초 등록이 아닌 경우 하나의 역은 이미 존재해야 합니다.");
        }

        @Test
        void 최초_등록이_아니며_두_역이_모두_존재하지_않는_경우() {
            // given
            // 장지 - 10 - 잠실 - 10 - 천호
            Sections line8 = line8source;

            // when
            Station mongchon = new Station(10L, "몽촌토성");
            Station seokchon = new Station(4L, "석촌");

            // then
            assertThatThrownBy(() -> line8.getChangesWhenAdded(mongchon, seokchon, pink, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최초 등록이 아닌 경우 하나의 역은 이미 존재해야 합니다.");
        }
    }

    @Nested
    class 역_삭제_결과_테스트 {

        // @Test
        // void 종점을_삭제한다() {
        //     // given
        //     // 장지 - 10 - 잠실 - 10 - 천호
        //     Sections line8 = line8source;
        //
        //     // when
        //     SectionResult sectionResult = line8.delete(cheonho);
        //
        //     // then
        //     // 장지 - 10 - 잠실
        //     assertThat(sectionResult.getAddedSections()).isEmpty();
        //     assertThat(sectionResult.getDeletedSection()).containsExactlyInAnyOrder(cheonhoJamsil10);
        //     assertThat(sectionResult.getModifiedStation()).containsExactly(cheonho);
        // }
        //
        // @Test
        // void 두_개의_호선이_교차하는_환승_역을_삭제한다() {
        //     // given
        //     //            건대
        //     //             |
        //     //            10
        //     //             |
        //     // 장지 - 10 - 잠실 - 10 - 천호
        //     //             |
        //     //            10
        //     //             |
        //     //            강남
        //     Sections line2And8 = line2And8source;
        //
        //     // when
        //     SectionResult sectionResult = line2And8.delete(jamsil);
        //
        //     // then
        //     // 장지 - 20 - 천호
        //     // 강남 - 20 - 건대
        //     Section cheonhoJangji20 = new Section(cheonho, jangji,
        //         cheonhoJamsil10.calculateCombinedDistance(jamsilJangji10.getDistance()), pink);
        //     Section kundaeGangnam20 = new Section(kundae, gangnam,
        //         kundaeJamsil10.calculateCombinedDistance(jamsilGangnam10.getDistance()), green);
        //
        //     assertThat(sectionResult.getAddedSections()).containsExactlyInAnyOrder(cheonhoJangji20, kundaeGangnam20);
        //     assertThat(sectionResult.getDeletedSection()).containsExactlyInAnyOrder(cheonhoJamsil10, jamsilJangji10,
        //         jamsilGangnam10, kundaeJamsil10);
        //     assertThat(sectionResult.getModifiedStation()).containsExactly(jamsil);
        // }
    }
}
