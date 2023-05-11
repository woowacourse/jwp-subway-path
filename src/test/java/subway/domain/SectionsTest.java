package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dto.AddResult;
import subway.dto.RemoveResult;
import subway.exceptions.customexceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    private Sections sections;
    private Station firstNewStation;
    private Station secondNewStation;

    @BeforeEach
    void setUp() {
        Station sadang = new Station(1L, "사당");
        Station bangbae = new Station(2L, "방배");
        Station seocho = new Station(3L, "서초");
        Station kyodae = new Station(4L, "교대");
        Station kangnam = new Station(5L, "강남");

        sections = new Sections(new ArrayList<>(List.of(
                new Section(1L, sadang, bangbae, 10),
                new Section(2L, bangbae, seocho, 10),
                new Section(3L, seocho, kyodae, 10),
                new Section(4L, kyodae, kangnam, 10)
        )));

        firstNewStation = new Station(6L, "역삼");
        secondNewStation = new Station(7L, "선릉");
    }

    @DisplayName("처음 추가할 때는 2개의 역을 추가해야 한다.")
    @Test
    void addFirst() {
        Sections emptySections = new Sections(Collections.emptyList());
        Station sadang = new Station(1L, "사당");
        Station bangbae = new Station(2L, "방배");
        Section newSection = new Section(sadang, bangbae, 10);
        AddResult addResult = emptySections.addSection(newSection);

        assertThat(addResult.getAddSections().getSections().get(0)).isEqualTo(newSection);
        assertThat(addResult.getUpdateSections().getSections()).isEmpty();
    }

    @DisplayName("빈 노선이 아닌데 새로운 역 2개를 추가하면 예외를 던진다.")
    @Test
    void addTwoStationsInNotEmptyLine() {
        Section newSection = new Section(firstNewStation, secondNewStation, 10);
        assertThatThrownBy(() -> sections.addSection(newSection))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("상행 끝에 역을 추가한다.")
    @Test
    void addUpEndPoint() {
        Section newSection = new Section(firstNewStation, new Station(1L, "사당"), 10);
        AddResult addResult = sections.addSection(newSection);
        assertThat(addResult.getAddSections().getSections().size()).isEqualTo(1);
        assertThat(addResult.getAddSections().getSections().get(0)).isEqualTo(newSection);
        assertThat(addResult.getUpdateSections().getSections().size()).isEqualTo(0);
    }

    @DisplayName("하행 끝에 역을 추가한다.")
    @Test
    void addDownEndPoint() {
        Section newSection = new Section(new Station(5L, "강남"), firstNewStation, 10);
        AddResult addResult = sections.addSection(newSection);
        assertThat(addResult.getAddSections().getSections().size()).isEqualTo(1);
        assertThat(addResult.getAddSections().getSections().get(0)).isEqualTo(newSection);
        assertThat(addResult.getUpdateSections().getSections().size()).isEqualTo(0);
    }

    @DisplayName("상행 방향은 존재하고 하행 방향에 새로운 역을 추가한다.")
    @Test
    void addExistUpAndNotExistDown() {
        Section section = new Section(new Station(1L, "사당"), firstNewStation, 5);
        Section updatedSection = new Section(1L, firstNewStation, new Station(2L, "방배"), 5);
        AddResult addResult = sections.addSection(section);

        assertThat(addResult.getAddSections().getSections().size()).isEqualTo(1);
        assertThat(addResult.getAddSections().getSections().get(0)).isEqualTo(section);
        assertThat(addResult.getUpdateSections().getSections().size()).isEqualTo(1);
        assertThat(addResult.getUpdateSections().getSections().get(0)).isEqualTo(updatedSection);
    }

    @DisplayName("하행 방향은 존재하고 상행 방향에 새로운 역을 추가한다.")
    @Test
    void addExistDownAndNotExistUp() {
        Section section = new Section(firstNewStation, new Station(2L, "방배"), 4);
        Section updatedSection = new Section(1L, new Station(1L, "사당"), firstNewStation, 6);
        AddResult addResult = sections.addSection(section);

        assertThat(addResult.getAddSections().getSections().size()).isEqualTo(1);
        assertThat(addResult.getAddSections().getSections().get(0)).isEqualTo(section);
        assertThat(addResult.getUpdateSections().getSections().size()).isEqualTo(1);
        assertThat(addResult.getUpdateSections().getSections().get(0)).isEqualTo(updatedSection);
    }

    @DisplayName("상행 방향은 존재하고 하행 방향에 새로운 역을 추가할 때 거리가 더 멀거나 같으면 예외를 던진다.")
    @Test
    void addExistUpAndNotExistDownWithInvalidDistance() {
        Section section = new Section(new Station(1L, "사당"), firstNewStation, 10);
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("하행 방향은 존재하고 상행 방향에 새로운 역을 추가할 때 거리가 더 멀거나 같으면 예외를 던진다.")
    @Test
    void addExistDownAndNotExistUpWithInvalidDistance() {
        Section section = new Section(firstNewStation, new Station(2L, "방배"), 10);
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("노선이 비어있는 상태에서 제거하려 할 때 예외를 던진다.")
    @Test
    void removeStationWhenLineIsEmpty() {
        Sections emptySection = new Sections(Collections.emptyList());
        assertThatThrownBy(() -> emptySection.removeStation(firstNewStation))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("노선에 없는 역을 제거하려 할 때 예외를 던진다.")
    @Test
    void removeStationWhenNotExistInLine() {
        assertThatThrownBy(() -> sections.removeStation(firstNewStation))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("상행 종점에 있는 역을 제거한다.")
    @Test
    void removeUpEndPointStation() {
        RemoveResult removeResult = sections.removeStation(new Station(1L, "사당"));
        assertThat(removeResult.getRemoveIds().getIds().size()).isEqualTo(1);
        assertThat(removeResult.getRemoveIds().getIds().get(0)).isEqualTo(1L);
        assertThat(removeResult.getUpdateSections().getSections().size()).isEqualTo(0);
    }

    @DisplayName("하행 종점에 있는 역을 제거한다.")
    @Test
    void removeDownEndPointStation() {
        RemoveResult removeResult = sections.removeStation(new Station(5L, "강남"));
        assertThat(removeResult.getRemoveIds().getIds().size()).isEqualTo(1);
        assertThat(removeResult.getRemoveIds().getIds().get(0)).isEqualTo(4L);
        assertThat(removeResult.getUpdateSections().getSections().size()).isEqualTo(0);
    }

    @DisplayName("중간 역을 제거한다.")
    @Test
    void removeStationInMiddle() {
        Section expectedUpdateSection = new Section(
                1L,
                new Station(1L, "사당"),
                new Station(3L, "서초"),
                20);

        RemoveResult removeResult = sections.removeStation(new Station(2L, "방배"));
        Section actualUpdateSection = removeResult.getUpdateSections().getSections().get(0);

        assertThat(removeResult.getRemoveIds().getIds().size()).isEqualTo(1);
        assertThat(removeResult.getRemoveIds().getIds().get(0)).isEqualTo(2L);
        assertThat(removeResult.getUpdateSections().getSections().size()).isEqualTo(1);
        assertThat(actualUpdateSection).isEqualTo(expectedUpdateSection);
    }
}
