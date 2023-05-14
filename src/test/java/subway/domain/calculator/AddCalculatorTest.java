package subway.domain.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.LineStatus;
import subway.domain.section.ContainingSections;
import subway.domain.section.Section;

import java.util.ArrayList;
import java.util.List;

import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddCalculatorTest {

    @Test
    @DisplayName("추가하려는 section에 포함된 역을 포함하고 있는 기존 section이 없는 경우, 역 2개를 모두 등록한다.")
    void initialInsertTest() {
        // given
        ContainingSections emptyContainingSections = new ContainingSections(new ArrayList<>());
        AddCalculator addCalculator = new AddCalculator(emptyContainingSections);
        Section newSection = SECTION_AFTER_CALCULATE_잠실역_TO_건대역;
        Changes expectChanges = new Changes(LINE2_ID, new ArrayList<>(), new ArrayList<>(),
                List.of(STATION_TO_INSERT_잠실역, STATION_TO_INSERT_건대역), new ArrayList<>(),
                List.of(SECTION_AFTER_CALCULATE_잠실역_TO_건대역), new ArrayList<>());

        // when
        Changes changes = addCalculator.addSection(LineStatus.INITIAL, newSection);

        // then
        assertThat(changes).isEqualTo(expectChanges);
    }

    @Test
    @DisplayName("추가하려는 구간이 상행 종점 구간이 되는 경우")
    void insertSectionUpEndCaseTest() {
        // given
        ContainingSections emptyContainingSections = new ContainingSections(List.of(SECTION_잠실역_TO_건대역));
        AddCalculator addCalculator = new AddCalculator(emptyContainingSections);
        Section newSection = SECTION_AFTER_CALCULATE_대림역_TO_잠실역;
        Changes expectChanges = new Changes(LINE2_ID, new ArrayList<>(), new ArrayList<>(),
                List.of(STATION_TO_INSERT_대림역), new ArrayList<>(),
                List.of(SECTION_AFTER_CALCULATE_대림역_TO_잠실역), new ArrayList<>());

        // when
        Changes changes = addCalculator.addSection(LineStatus.EXIST, newSection);

        // then
        assertThat(changes).isEqualTo(expectChanges);
    }

    @Test
    @DisplayName("추가하려는 구간이 하행 종점 구간이 되는 경우")
    void insertSectionDownEndCaseTest() {
        // given
        ContainingSections emptyContainingSections = new ContainingSections(List.of(SECTION_잠실역_TO_건대역));
        AddCalculator addCalculator = new AddCalculator(emptyContainingSections);
        Section newSection = SECTION_건대역_TO_성수역;
        Changes expectChanges = new Changes(LINE2_ID, new ArrayList<>(), new ArrayList<>(),
                List.of(STATION_성수역), new ArrayList<>(),
                List.of(SECTION_건대역_TO_성수역), new ArrayList<>());

        // when
        Changes changes = addCalculator.addSection(LineStatus.EXIST, newSection);

        // then
        assertThat(changes).isEqualTo(expectChanges);
    }

    @Test
    @DisplayName("추가하려는 구간이 기존 구간 사이이면서 상행역이 겹치는 경우")
    void insertSectionBetweenUpsideCaseTest() {
        // given
        ContainingSections emptyContainingSections = new ContainingSections(List.of(SECTION_잠실역_TO_건대역));
        AddCalculator addCalculator = new AddCalculator(emptyContainingSections);
        Section newSection = SECTION_AFTER_CALCULATE_잠실역_TO_강변역;
        Changes expectChanges = new Changes(LINE2_ID, new ArrayList<>(), new ArrayList<>(),
                List.of(STATION_TO_INSERT_강변역), new ArrayList<>(),
                List.of(SECTION_AFTER_CALCULATE_잠실역_TO_강변역, SECTION_AFTER_CALCULATE_강변역_TO_건대역), List.of(SECTION_잠실역_TO_건대역));

        // when
        Changes changes = addCalculator.addSection(LineStatus.EXIST, newSection);

        // then
        assertThat(changes).isEqualTo(expectChanges);
    }

    @Test
    @DisplayName("추가하려는 구간이 기존 구간 사이이면서 하행역이 겹치는 경우")
    void insertSectionBetweenDownsideCaseTest() {
        // given
        ContainingSections emptyContainingSections = new ContainingSections(List.of(SECTION_잠실역_TO_건대역));
        AddCalculator addCalculator = new AddCalculator(emptyContainingSections);
        Section newSection = SECTION_AFTER_CALCULATE_강변역_TO_건대역;
        Changes expectChanges = new Changes(LINE2_ID, new ArrayList<>(), new ArrayList<>(),
                List.of(STATION_TO_INSERT_강변역), new ArrayList<>(),
                List.of(SECTION_AFTER_CALCULATE_잠실역_TO_강변역, SECTION_AFTER_CALCULATE_강변역_TO_건대역), List.of(SECTION_잠실역_TO_건대역));

        // when
        Changes changes = addCalculator.addSection(LineStatus.EXIST, newSection);

        // then
        assertThat(changes).isEqualTo(expectChanges);
    }

    @Test
    @DisplayName("있는 노선인데 새로운 역 2개를 추가하려고 하면 예외가 발생한다.")
    void insertTestFail_noConnection() {
        // given
        ContainingSections containingSections = new ContainingSections(new ArrayList<>());
        AddCalculator addCalculator = new AddCalculator(containingSections);
        Section newSection = SECTION_AFTER_CALCULATE_건대역_TO_성수역;

        // when, then
        assertThatThrownBy(() -> addCalculator.addSection(LineStatus.EXIST, newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 등록된 역 중에 하나를 포함해야합니다.");
    }

    @Test
    @DisplayName("노선에 역을 추가함으로써 수정되는 구간의 거리는 음수가 되면 안 된다.")
    void insertTestFail_tooLong() {
        // given
        ContainingSections emptyContainingSections = new ContainingSections(List.of(SECTION_잠실역_TO_건대역));
        AddCalculator addCalculator = new AddCalculator(emptyContainingSections);
        Section newSection = SECTION_AFTER_CALCULATE_잠실역_TO_예외역;

        // when, then
        assertThatThrownBy(() -> addCalculator.addSection(LineStatus.EXIST, newSection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 사이 거리는 0km이상 100km 이하여야 합니다.");
    }
}