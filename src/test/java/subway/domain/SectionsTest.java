package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    private Long lineId;

    @BeforeEach
    void setUp() {
        Station sadang = new Station(1L, "사당");
        Station bangbae = new Station(2L, "방배");
        Station seocho = new Station(3L, "서초");
        Station kyodae = new Station(4L, "교대");
        Station kangnam = new Station(5L, "강남");

        lineId = 1L;
        sections = new Sections(new ArrayList<>(List.of(
                new Section(1L, sadang.getId(), bangbae.getId(), lineId, 10),
                new Section(2L, bangbae.getId(), seocho.getId(), lineId, 10),
                new Section(3L, seocho.getId(), kyodae.getId(), lineId, 10),
                new Section(4L, kyodae.getId(), kangnam.getId(), lineId, 10)
        )));

        firstNewStation = new Station(6L, "역삼");
        secondNewStation = new Station(7L, "선릉");
    }

    @DisplayName("처음 추가할 때는 2개의 역을 추가해야 한다.")
    @Test
    void addFirst() {
        // given
        Sections emptySections = new Sections(Collections.emptyList());
        Station sadang = new Station(1L, "사당");
        Station bangbae = new Station(2L, "방배");
        Section newSection = new Section(sadang.getId(), bangbae.getId(), lineId, 10);

        // when
        emptySections.addSection(newSection);
        SectionChange sectionChange = emptySections.findSectionChange();

        // then
        assertThat(sectionChange.getNewSections().get(0)).isEqualTo(newSection);
        assertThat(sectionChange.getUpdatedSections()).isEmpty();
    }

    @DisplayName("빈 노선이 아닌데 새로운 역 2개를 추가하면 예외를 던진다.")
    @Test
    void addTwoStationsInNotEmptyLine() {
        // given
        Section newSection = new Section(firstNewStation.getId(), secondNewStation.getId(), lineId, 10);

        // when, then
        assertThatThrownBy(() -> sections.addSection(newSection))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("상행 끝에 역을 추가한다.")
    @Test
    void addUpEndPoint() {
        // given
        Station sadang = new Station(1L, "사당");
        Section newSection = new Section(firstNewStation.getId(), sadang.getId(), lineId, 10);

        // when
        sections.addSection(newSection);
        SectionChange sectionChange = sections.findSectionChange();

        // then
        assertThat(sectionChange.getNewSections().size()).isEqualTo(1);
        assertThat(sectionChange.getNewSections().get(0)).isEqualTo(newSection);
        assertThat(sectionChange.getUpdatedSections().size()).isEqualTo(0);
    }

    @DisplayName("하행 끝에 역을 추가한다.")
    @Test
    void addDownEndPoint() {
        // given
        Station kangnam = new Station(5L, "강남");
        Section newSection = new Section(kangnam.getId(), firstNewStation.getId(), lineId, 10);

        // when
        sections.addSection(newSection);
        SectionChange sectionChange = sections.findSectionChange();

        // then
        assertThat(sectionChange.getNewSections().size()).isEqualTo(1);
        assertThat(sectionChange.getNewSections().get(0)).isEqualTo(newSection);
        assertThat(sectionChange.getUpdatedSections().size()).isEqualTo(0);
    }

    @DisplayName("상행 방향은 존재하고 하행 방향에 새로운 역을 추가한다.")
    @Test
    void addExistUpAndNotExistDown() {
        // given
        Station sadang = new Station(1L, "사당");
        Station bangbae = new Station(2L, "방배");
        Section section = new Section(sadang.getId(), firstNewStation.getId(), lineId, 5);
        Section updatedSection = new Section(firstNewStation.getId(), bangbae.getId(), lineId, 5);

        // when
        sections.addSection(section);
        SectionChange sectionChange = sections.findSectionChange();

        // then
        assertThat(sectionChange.getNewSections().size()).isEqualTo(1);
        assertThat(sectionChange.getNewSections().get(0).getDistance()).isEqualTo(section.getDistance());
        assertThat(sectionChange.getUpdatedSections().size()).isEqualTo(1);
        assertThat(sectionChange.getUpdatedSections().get(0).getDistance()).isEqualTo(updatedSection.getDistance());
    }

    @DisplayName("하행 방향은 존재하고 상행 방향에 새로운 역을 추가한다.")
    @Test
    void addExistDownAndNotExistUp() {
        // given
        Station sadang = new Station(1L, "사당");
        Station bangbae = new Station(2L, "방배");
        Section section = new Section(firstNewStation.getId(), bangbae.getId(), lineId, 4);
        Section updatedSection = new Section(sadang.getId(), firstNewStation.getId(), lineId, 6);

        // when
        sections.addSection(section);
        SectionChange sectionChange = sections.findSectionChange();

        // then
        assertThat(sectionChange.getNewSections().size()).isEqualTo(1);
        assertThat(sectionChange.getNewSections().get(0).getDistance()).isEqualTo(section.getDistance());
        assertThat(sectionChange.getUpdatedSections().size()).isEqualTo(1);
        assertThat(sectionChange.getUpdatedSections().get(0).getDistance()).isEqualTo(updatedSection.getDistance());
    }

    @DisplayName("상행 방향은 존재하고 하행 방향에 새로운 역을 추가할 때 거리가 더 멀거나 같으면 예외를 던진다.")
    @Test
    void addExistUpAndNotExistDownWithInvalidDistance() {
        // given
        Station sadang = new Station(1L, "사당");
        Section section = new Section(sadang.getId(), firstNewStation.getId(), lineId, 10);

        // when, then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("하행 방향은 존재하고 상행 방향에 새로운 역을 추가할 때 거리가 더 멀거나 같으면 예외를 던진다.")
    @Test
    void addExistDownAndNotExistUpWithInvalidDistance() {
        // given
        Station bangbae = new Station(2L, "방배");
        Section section = new Section(firstNewStation.getId(), bangbae.getId(), lineId, 10);

        // when, then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("노선이 비어있는 상태에서 제거하려 할 때 예외를 던진다.")
    @Test
    void removeStationWhenLineIsEmpty() {
        // given
        Sections emptySection = new Sections(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> emptySection.removeStation(firstNewStation.getId()))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("노선에 없는 역을 제거하려 할 때 예외를 던진다.")
    @Test
    void removeStationWhenNotExistInLine() {
        // when, then
        assertThatThrownBy(() -> sections.removeStation(firstNewStation.getId()))
                .isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("상행 종점에 있는 역을 제거한다.")
    @Test
    void removeUpEndPointStation() {
        // given
        Station sadang = new Station(1L, "사당");

        // when
        sections.removeStation(sadang.getId());
        SectionChange sectionChange = sections.findSectionChange();

        // then
        assertThat(sectionChange.getDeletedSections().size()).isEqualTo(1);
        assertThat(sectionChange.getDeletedSections().get(0).getId()).isEqualTo(1L);
        assertThat(sectionChange.getUpdatedSections().size()).isEqualTo(0);
    }

    @DisplayName("하행 종점에 있는 역을 제거한다.")
    @Test
    void removeDownEndPointStation() {
        //given
        Station kangnam = new Station(5L, "강남");

        // when
        sections.removeStation(kangnam.getId());
        SectionChange sectionChange = sections.findSectionChange();

        // then
        assertThat(sectionChange.getDeletedSections().size()).isEqualTo(1);
        assertThat(sectionChange.getDeletedSections().get(0).getId()).isEqualTo(4L);
        assertThat(sectionChange.getUpdatedSections().size()).isEqualTo(0);
    }

    @DisplayName("중간 역을 제거한다.")
    @Test
    void removeStationInMiddle() {
        // given
        Station sadang = new Station(1L, "사당");
        Station seocho = new Station(3L, "서초");
        Station bangbae = new Station(2L, "방배");
        Section expectedUpdateSection = new Section(sadang.getId(), seocho.getId(), lineId, 20);

        // when
        sections.removeStation(bangbae.getId());
        SectionChange sectionChange = sections.findSectionChange();
        Section actualUpdateSection = sectionChange.getUpdatedSections().get(0);

        // then
        assertThat(sectionChange.getDeletedSections().size()).isEqualTo(1);
        assertThat(sectionChange.getDeletedSections().get(0).getId()).isEqualTo(2L);
        assertThat(sectionChange.getUpdatedSections().size()).isEqualTo(1);
        assertThat(actualUpdateSection).isEqualTo(expectedUpdateSection);
    }
}
