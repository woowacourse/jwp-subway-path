package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.domain.section.Location.END;
import static subway.domain.section.Location.MIDDLE;
import static subway.domain.section.Location.NONE;
import static subway.fixture.SectionFixture.SECTION_강남_잠실_5;
import static subway.fixture.SectionFixture.SECTION_길동_강남_5;
import static subway.fixture.SectionFixture.SECTION_길동_암사_3;
import static subway.fixture.SectionFixture.SECTION_몽촌토성_길동_2;
import static subway.fixture.SectionFixture.SECTION_몽촌토성_암사_5;
import static subway.fixture.SectionFixture.SECTION_암사_길동_10;
import static subway.fixture.SectionFixture.SECTION_잠실_길동_10;
import static subway.fixture.SectionFixture.SECTION_잠실_몽촌토성_5;
import static subway.fixture.StationFixture.STATION_강남;
import static subway.fixture.StationFixture.STATION_길동;
import static subway.fixture.StationFixture.STATION_몽촌토성;
import static subway.fixture.StationFixture.STATION_암사;
import static subway.fixture.StationFixture.STATION_잠실;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Sections;

class SectionsTest {

    private static Sections sections1;
    private static Sections sections;
    private static Sections sections3;

    @BeforeEach
    void setUp() {
        sections1 = new Sections(new ArrayList<>(List.of(SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_암사_5, SECTION_강남_잠실_5)));
        sections = new Sections(new ArrayList<>(List.of(SECTION_몽촌토성_암사_5)));
        sections3 = new Sections(new ArrayList<>());
    }

    @Test
    @DisplayName("처음으로 새로운 구간을 추가한다.")
    void add_init() {
        sections3.addSection(SECTION_강남_잠실_5);
    }

    @Test
    @DisplayName("상행 종점으로 새로운 구간을 추가한다.")
    void add_leftEnd() {
        int rawSize = sections1.getSections().size();

        sections1.addSection(SECTION_암사_길동_10);

        assertThat(sections1.getSections())
                .hasSize(rawSize + 1)
                .contains(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_암사_5, SECTION_암사_길동_10);
    }

    @Test
    @DisplayName("하행 종점으로 새로운 구간을 추가한다.")
    void add_rightEnd() {
        int rawSize = sections1.getSections().size();

        sections1.addSection(SECTION_길동_강남_5);

        assertThat(sections1.getSections())
                .hasSize(rawSize + 1)
                .contains(SECTION_길동_강남_5, SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_암사_5);
    }

    @Test
    @DisplayName("기존에 존재하던 구간과 왼쪽으로 겹치는 구간을 추가한다(ex. A-B-C 에 A-D 추가).")
    void add_innerLeft() {
        int rawSize = sections1.getSections().size();

        sections1.addSection(SECTION_몽촌토성_길동_2);

        assertThat(sections1.getSections())
                .hasSize(rawSize + 1)
                .contains(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_길동_2, SECTION_길동_암사_3);
    }

    @Test
    @DisplayName("기존에 존재하던 구간과 오른쪽으로 겹치는 구간을 추가한다(ex. A-B-C 에 D-C 추가).")
    void add_innerRight() {
        int rawSize = sections1.getSections().size();

        sections1.addSection(SECTION_길동_암사_3);

        assertThat(sections1.getSections())
                .hasSize(rawSize + 1)
                .contains(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_길동_2, SECTION_길동_암사_3);
    }

    @Test
    @DisplayName("기존 구간과 겹치는 구간을 추가할 때, 기존 구간의 거리와 같거나 크면 예외가 발생한다.")
    void add_distance() {
        assertThatThrownBy(
                () -> sections1.addSection(SECTION_잠실_길동_10)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 구간의 거리가 너무 큽니다.");
    }

    @Test
    @DisplayName("역을 삭제할 때, 해당 역이 포함된 구간들을 삭제한다. 종점이면 1개의 구간만 삭제된다.")
    void deleteSection_end() {
        int rawSize = sections1.getSections().size();

        sections1.deleteSectionByStation(STATION_강남);

        assertThat(sections1.getSections())
                .hasSize(rawSize - 1)
                .doesNotContain(SECTION_강남_잠실_5);
    }

    @Test
    @DisplayName("역을 삭제할 때, 해당 역이 포함된 구간들을 삭제한다. 중간에 위치한 역이면 2개의 구간이 삭제되고, 새로운 구간 1개가 추가된다.")
    void deleteSection_middle() {
        int rawSize = sections1.getSections().size();

        sections1.deleteSectionByStation(STATION_몽촌토성);

        assertThat(sections1.getSections())
                .hasSize(rawSize - 1)
                .doesNotContain(SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_암사_5);
    }

    @Test
    @DisplayName("역을 삭제할 때, 남은 역이 2개라면 모두 삭제한다.")
    void deleteSection_last() {
        sections.deleteSectionByStation(STATION_암사);

        assertThat(sections.getSections()).hasSize(0);
    }

    @Test
    @DisplayName("해당 역이 종점이면 END를 반환한다.")
    void checkStationLocation_end() {
        assertThat(sections1.checkStationLocation(STATION_강남)).isEqualTo(END);
    }

    @Test
    @DisplayName("해당 역이 중간에 위치하면 MIDDLE을 반환한다.")
    void checkStationLocation_middle() {
        assertThat(sections1.checkStationLocation(STATION_몽촌토성)).isEqualTo(MIDDLE);
    }

    @Test
    @DisplayName("해당 역이 존재하지 않으면 NONE을 반환한다.")
    void checkStationLocation_none() {
        assertThat(sections1.checkStationLocation(STATION_길동)).isEqualTo(NONE);
    }

    @Test
    @DisplayName("특정 역이 포함된 구간들을 조회한다.")
    void findSectionBy() {
        assertThat(sections1.findSectionBy(STATION_몽촌토성))
                .hasSize(2)
                .contains(SECTION_몽촌토성_암사_5, SECTION_잠실_몽촌토성_5);
    }

    @Test
    @DisplayName("하나의 호선에 대한 정렬된 상태의 역들을 조회한다.")
    void findAllStation() {
        assertThat(sections1.findAllStation())
                .containsExactly(STATION_강남, STATION_잠실, STATION_몽촌토성, STATION_암사);
    }
}
