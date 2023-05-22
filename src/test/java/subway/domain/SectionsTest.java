package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidSectionException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static subway.TestFeature.*;

class SectionsTest {

    @DisplayName("섞여있는 Station 리스트를 정렬해 반환한다")
    @Test
    void findStations() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));

        // when
        List<Station> sortStation = sections.findStations();

        // then
        assertThat(sortStation).containsExactly(
                STATION_인천역, STATION_방배역, STATION_봉천역,
                STATION_서울대입구, STATION_사당역, STATION_동인천역
        );
    }

    @DisplayName("하행선 종점에 역을 추가")
    @Test
    void addSectionInDownStationNode() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Section sectionToAdd = Section.of(null, STATION_낙성대역, STATION_인천역, 6);

        // when
        sections.addSection(sectionToAdd);

        // then
        assertThat(sections.findStations()).containsExactly(
                STATION_낙성대역, STATION_인천역, STATION_방배역, STATION_봉천역,
                STATION_서울대입구, STATION_사당역, STATION_동인천역
        );
    }

    @DisplayName("상행선 종점에 역을 추가")
    @Test
    void addSectionInUpStationNode() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Section sectionToAdd = Section.of(null, STATION_동인천역, STATION_낙성대역, 6);

        // when
        sections.addSection(sectionToAdd);

        // then
        assertThat(sections.findStations()).containsExactly(
                STATION_인천역, STATION_방배역, STATION_봉천역,
                STATION_서울대입구, STATION_사당역, STATION_동인천역, STATION_낙성대역
                );
    }

    @DisplayName("두 역 사이에 역을 추가 - 하행선이 존재하는 경우")
    @Test
    void addSectionBetweenStation_ExistDown() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Section sectionToAdd = Section.of(null, STATION_서울대입구, STATION_낙성대역, 3);

        // when
        sections.addSection(sectionToAdd);

        // then
        assertThat(sections.findStations()).containsExactly(
                STATION_인천역, STATION_방배역, STATION_봉천역,
                STATION_서울대입구, STATION_낙성대역, STATION_사당역, STATION_동인천역
        );
    }

    @DisplayName("두 역 사이에 역을 추가 - 상행선이 존재하는 경우")
    @Test
    void addSectionBetweenStation_ExistUp() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Section sectionToAdd = Section.of(null, STATION_낙성대역, STATION_서울대입구, 3);

        // when
        sections.addSection(sectionToAdd);

        // then
        assertThat(sections.findStations()).containsExactly(
                STATION_인천역, STATION_방배역, STATION_봉천역,
                STATION_낙성대역, STATION_서울대입구, STATION_사당역, STATION_동인천역
        );
    }

    @DisplayName("존재하지 않는 역을 추가하는 경우 예외를 반환")
    @Test
    void addSectionExceptionStation() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Section sectionToAdd = Section.of(null, STATION_교대역, STATION_낙성대역, 3);

        // then
        assertThatThrownBy(() -> sections.addSection(sectionToAdd))
                .isInstanceOf(InvalidSectionException.class);
    }

    @DisplayName("두 역 사이에 역을 추가하는 경우 거리가 기존 역 구간보다 길다면 예외를 반환")
    @Test
    void addSectionExceptionStationOverDistance() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Section sectionToAdd = Section.of(null, STATION_낙성대역, STATION_서울대입구, 10);

        // then
        assertThatThrownBy(() -> sections.addSection(sectionToAdd))
                .isInstanceOf(InvalidSectionException.class);
    }

    @DisplayName("노선에 역이 두 개만 있는 경우 하나의 역 삭제 시 두 역 모두 삭제")
    @Test
    void removeStationWhenOnlyTwoStation() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구
        ));
        Station deleteStation = STATION_서울대입구;

        // when
        sections.removeStation(deleteStation);

        // then
        assertThat(sections.findStations()).isNull();
    }

    @DisplayName("하행선 종점의 역 삭제")
    @Test
    void removeStationDownNode() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Station deleteStation = STATION_인천역;

        // when
        sections.removeStation(deleteStation);

        // then
        assertThat(sections.findStations()).containsExactly(
                STATION_방배역, STATION_봉천역,
                STATION_서울대입구, STATION_사당역, STATION_동인천역
        );
    }

    @DisplayName("상행선 종점의 역 삭제")
    @Test
    void removeStationUpNode() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Station deleteStation = STATION_동인천역;

        // when
        sections.removeStation(deleteStation);

        // then
        assertThat(sections.findStations()).containsExactly(
                STATION_인천역, STATION_방배역, STATION_봉천역,
                STATION_서울대입구, STATION_사당역
        );
    }

    @DisplayName("두 역 사이의 역 삭제")
    @Test
    void removeStationBetweenStation() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Station deleteStation = STATION_서울대입구;

        // when
        sections.removeStation(deleteStation);

        // then
        assertThat(sections.findStations()).containsExactly(
                STATION_인천역, STATION_방배역, STATION_봉천역,
                STATION_사당역, STATION_동인천역
        );
    }

    @DisplayName("존재하지 않는 역을 삭제할 경우 아무 것도 바뀌지 않는다")
    @Test
    void removeStation() {
        // given
        Sections sections = new Sections(List.of(
                SECTION_봉천_서울대입구,
                SECTION_서울대입구_사당,
                SECTION_방배_봉천,
                SECTION_인천_방배,
                SECTION_사당_동인천
        ));
        Station deleteStation = STATION_낙성대역;

        sections.removeStation(deleteStation);

        // then
        assertThat(sections.findStations()).containsExactly(
                STATION_인천역, STATION_방배역, STATION_봉천역,
                STATION_서울대입구, STATION_사당역, STATION_동인천역
        );
    }
}
