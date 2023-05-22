package subway.domain;

import static org.assertj.core.api.Assertions.*;
import static subway.TestSource.*;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SectionsTest {

    @Test
    void 구간_중_중복되지_않는_역을_반환한다() {
        // given
        Sections sections = new Sections(List.of(cheonhoJamsil10, jamsilJangji10));

        // when
        List<Station> distinctStations = sections.getDistinctStations();

        // then
        assertThat(distinctStations).hasSize(3);
    }

    @Test
    void 구간의_총_길이를_반환한다() {
        // given
        Sections sections = new Sections(List.of(cheonhoJamsil10, jamsilJangji10));

        // when
        int distance = sections.getTotalDistance();

        // then
        assertThat(distance).isEqualTo(20);
    }

    @Test
    void 구간_중_최고_추가_요금을_반환한다() {
        // given
        Line expensiveLine = new Line("expensiveLine", "red", 900);
        Section expensiveSection = new Section(cheonho, jamsil, expensiveLine, 10);
        Sections sections = new Sections(List.of(cheonhoJamsil10, jamsilJangji10, expensiveSection));

        // when
        int additionalCharge = sections.getMostExpensiveAdditionalCharge();

        // then
        assertThat(additionalCharge).isEqualTo(900);
    }
}
