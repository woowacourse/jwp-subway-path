package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.exception.DomainException;

class SectionsTest {
    List<Section> sectionsList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        sectionsList.add(new Section(3L, 3L, 4L, 1L, 10));
        sectionsList.add(new Section(1L, 1L, 2L, 1L, 10));
        sectionsList.add(new Section(2L, 2L, 3L, 1L, 10));
    }

    @Test
    @DisplayName("구간을 순서대로 정렬한다.")
    void lineUpTest() {
        Sections sections = new Sections(sectionsList);
        assertAll(
                () -> assertThat(sections.findFirstStationId()).isEqualTo(1L),
                () -> assertThat(sections.findLastStationId()).isEqualTo(4L));
    }

    @Test
    @DisplayName("구간 사이에 역을 추가하는 경우 sections가 비어있으면 예외가 발생한다.")
    void validateAddingSectionThrowByNotInitialized() {
        //given
        sectionsList.clear();
        final Sections sections = new Sections(sectionsList);

        //then
        assertThatThrownBy(() -> sections.validateAddingSection(5L))
                .isInstanceOf(DomainException.class)
                .hasMessage("LINE_IS_NOT_INITIALIZED");
    }

    @Test
    @DisplayName("구간 사이에 역을 추가하는 경우 추가하려는 station이 이미 sections에 존재하면 예외가 발생한다.")
    void validateAddingSectionThrowByAlreadyExistedStation() {
        //given
        final Sections sections = new Sections(sectionsList);

        //then
        assertThatThrownBy(() -> sections.validateAddingSection(1L))
                .isInstanceOf(DomainException.class)
                .hasMessage("STATION_ALREADY_EXIST_IN_LINE");
    }

    @Test
    @DisplayName("sourceStation과 targetStation으로 이뤄진 Section을 찾는다.")
    void findSectionSuccessTest() {
        //given
        final Sections sections = new Sections(sectionsList);

        //when
        final Section result = sections.findSection(1L, 2L);

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getSourceStationId()).isEqualTo(1L),
                () -> assertThat(result.getTargetStationId()).isEqualTo(2L),
                () -> assertThat(result.getDistance()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("sourceStation과 targetStation으로 이뤄진 Section을 찾는다.")
    void findSectionFailTest() {
        //given
        final Sections sections = new Sections(sectionsList);

        //then
        assertThatThrownBy(() -> sections.findSection(1L, 3L))
                .isInstanceOf(DomainException.class)
                .hasMessage("NON_EXISTENT_SECTION");
    }

    @Test
    @DisplayName("sections에서 station을 포함한 모든 section을 반환한다")
    void findSectionsIncludeStationTest() {
        //given
        final Sections sections = new Sections(sectionsList);

        //when
        final List<Section> results = sections.findSectionsIncludeStation(2L);

        //then
        assertThat(results).hasSize(2);
    }


    @Test
    @DisplayName("sections에서 station을 포함한 모든 section을 반환한다")
    void findSectionsIncludeStationTestSingleSection() {
        //given
        final Sections sections = new Sections(sectionsList);

        //when
        final List<Section> results = sections.findSectionsIncludeStation(1L);

        //then
        assertThat(results).hasSize(1);
    }
}
