package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SectionsTest {

    private Sections sections;

    @BeforeEach
    void setup() {
        sections = new Sections(new ArrayList<>());
    }

    @Test
    @DisplayName("초기 구간을 추가할 때, 서로 다른 두 역을 추가한다")
    void addInitialSection() {
        Section section = new Section(new Station("강남역"), new Station("선릉역"), new Distance(10));
        assertDoesNotThrow(() -> sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10)));
        assertThat(sections.getSections()).contains(section);
    }

    @Test
    @DisplayName("초기 구간을 추가할 때, 같은 두 역을 추가하면 예외가 발생한다")
    void addInitialSectionError() {
        Section section = new Section(new Station("강남역"), new Station("강남역"), new Distance(10));
        assertThatThrownBy(() -> sections.addInitialSection(new Station("강남역"), new Station("강남역"), new Distance(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구간은 서로 다른 두 역으로 이뤄져 있어야 합니다.");
    }

    @Test
    @DisplayName("구간을 추가할 때, 기준역이 존재하지 않으면 예외가 발생한다")
    void addAdditionalSectionNoBaseStation() {
        //given
        sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10));

        assertThatThrownBy(() -> sections.addAdditionalSection(new Station("이수역"), new Station("사당역"), Direction.UP,
                new Distance(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("기준역이 존재하지 않습니다");
    }

    @Test
    @DisplayName("구간을 추가할 때, 추가하려는 역이 이미 존재하면 예외가 발생한다")
    void addAdditionalSectionExistNewStation() {
        //given
        sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10));

        assertThatThrownBy(() -> sections.addAdditionalSection(new Station("강남역"), new Station("선릉역"), Direction.UP,
                new Distance(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("추가하려는 역이 이미 존재합니다");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 20})
    @DisplayName("구간을 추가할 때, 추가하려는 역의 구간이 이미 존재하는 구간보다 길이가 길면 예외가 발생한다")
    void addAdditionalSectionExistNewLongerStation(int distance) {
        //given
        sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10));

        assertThatThrownBy(() -> sections.addAdditionalSection(new Station("강남역"), new Station("역삼역"), Direction.UP,
                new Distance(distance)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("새로운 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
    }

    @Test
    @DisplayName("구간이 정상으로 추가된다")
    void addAdditionalSection() {
        //given
        sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10));

        assertDoesNotThrow(() -> sections.addAdditionalSection(new Station("강남역"), new Station("역삼역"), Direction.UP,
                new Distance(5)));

        Section revisedSection = new Section(new Station("역삼역"), new Station("선릉역"), new Distance(5));
        Section newSection = new Section(new Station("강남역"), new Station("역삼역"), new Distance(5));
        assertThat(sections.getSections()).contains(newSection, revisedSection);
    }

    @Test
    @DisplayName("구간 내의 역을 조회하면, 상행역부터 순서대로 조회된다")
    void findAllStations() {
        //given
        sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10));
        sections.addAdditionalSection(new Station("강남역"), new Station("역삼역"), Direction.UP,
                new Distance(5));
        List<Station> stationsInOrder = List.of(new Station("강남역"), new Station("역삼역"), new Station("선릉역"));

        assertThat(sections.findAllStations()).containsExactlyElementsOf(stationsInOrder);
    }

    @Test
    @DisplayName("해당 역을 가지고 있는 구간을 조회한다.")
    void findSectionsByStation() {
        //given
        sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10));
        sections.addAdditionalSection(new Station("강남역"), new Station("역삼역"), Direction.UP,
                new Distance(5));
        Station station = new Station("역삼역");
        List<Section> sectionWithStation= List.of(new Section(new Station("강남역"), new Station("역삼역"), new Distance(5)),
                new Section(new Station("역삼역"),new Station("선릉역"), new Distance(5)));

        assertThat(sections.findSectionsByStation(station)).containsAll(sectionWithStation);
    }

    @Test
    @DisplayName("해당 역을 가지고 있는 구간이 없으면 예외가 발생한다.")
    void findSectionsByStationError() {
        //given
        sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10));
        sections.addAdditionalSection(new Station("강남역"), new Station("역삼역"), Direction.UP,
                new Distance(5));
        //when, then
        Station station = new Station("서초역");
        assertThatThrownBy(() ->sections.findSectionsByStation(station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당 역을 삭제한다.")
    void removeStation() {
        //given
        sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10));
        sections.addAdditionalSection(new Station("강남역"), new Station("역삼역"), Direction.UP,
                new Distance(5));
        //when, then
        Station station = new Station("역삼역");
        List<Section> updatedSections = List.of(new Section(new Station("강남역"), new Station("선릉역"), new Distance(10)));
        sections.removeStation(station);
        assertThat(sections.getSections()).containsAll(updatedSections);
    }

    @Test
    @DisplayName("존재하지 않는 역을 삭제하려고 하면 예외가 발한다.")
    void removeStationError() {
        //given
        sections.addInitialSection(new Station("강남역"), new Station("선릉역"), new Distance(10));
        sections.addAdditionalSection(new Station("강남역"), new Station("역삼역"), Direction.UP,
                new Distance(5));
        //when, then
        Station station = new Station("서초역");
        assertThatThrownBy(() -> sections.removeStation(station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("삭제하려는 역이 존재하지 않습니다");
    }

}
