package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.Direction.DOWN;
import static subway.domain.Direction.UP;
import static subway.domain.SectionFixture.LINE1_SECTION_MIDDLE_ST2_ST3;
import static subway.domain.SectionFixture.LINE1_SECTION_MIDDLE_ST3_ST4;
import static subway.domain.SectionFixture.LINE1_SECTION_MIDDLE_ST4_ST5;
import static subway.domain.SectionFixture.LINE1_SECTION_ST1_ST2;
import static subway.domain.SectionFixture.LINE1_SECTION_ST5_ST6;
import static subway.domain.StationFixture.FIXTURE_STATION_1;
import static subway.domain.StationFixture.FIXTURE_STATION_2;
import static subway.domain.StationFixture.FIXTURE_STATION_3;
import static subway.domain.StationFixture.FIXTURE_STATION_4;
import static subway.domain.StationFixture.FIXTURE_STATION_5;
import static subway.domain.StationFixture.FIXTURE_STATION_6;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineMapTest {

    private static final LineMap FIXTURE_LINE_ROUTE = LineMap.of(List.of(
            LINE1_SECTION_ST1_ST2,
            LINE1_SECTION_MIDDLE_ST2_ST3,
            LINE1_SECTION_MIDDLE_ST3_ST4,
            LINE1_SECTION_MIDDLE_ST4_ST5,
            LINE1_SECTION_ST5_ST6
    ));

    @DisplayName("노선에 역이 존재하지 않으면 두 역을 모두 새로 등록할 수 있다")
    @Test
    void addStationsToEmpty() {
        LineMap lineMap = LineMap.of(Collections.emptyList());

        Station adding = new Station(7L, "추가역");
        lineMap.add(FIXTURE_STATION_1, adding, new Distance(6), DOWN);

        assertThat(lineMap.extractSections())
                .containsExactly(
                        new Section(FIXTURE_STATION_1, adding, new Distance(6))
                );
    }

    @DisplayName("기존 역 간 거리를 조정하여 새 역을 등록할 수 있다")
    @Test
    void addStationByDownDirection() {
        LineMap lineMap = LineMap.of(List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_MIDDLE_ST2_ST3,
                LINE1_SECTION_MIDDLE_ST3_ST4,
                LINE1_SECTION_MIDDLE_ST4_ST5,
                LINE1_SECTION_ST5_ST6
        ));

        Station adding = new Station(7L, "추가역");
        lineMap.add(FIXTURE_STATION_1, new Station(7L, "추가역"), new Distance(6), DOWN);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        new Section(FIXTURE_STATION_1, adding, new Distance(6)),
                        new Section(adding, FIXTURE_STATION_2, new Distance(4)),
                        LINE1_SECTION_MIDDLE_ST2_ST3,
                        LINE1_SECTION_MIDDLE_ST3_ST4,
                        LINE1_SECTION_MIDDLE_ST4_ST5,
                        LINE1_SECTION_ST5_ST6
                );
    }

    @DisplayName("새 역을 기존 하행 종점 앞에 등록할 수 있다")
    @Test
    void addStationAtHead() {
        LineMap lineMap = LineMap.of(List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_MIDDLE_ST2_ST3,
                LINE1_SECTION_MIDDLE_ST3_ST4,
                LINE1_SECTION_MIDDLE_ST4_ST5,
                LINE1_SECTION_ST5_ST6
        ));

        Station adding = new Station(7L, "추가역");
        lineMap.add(FIXTURE_STATION_1, new Station(7L, "추가역"), new Distance(6), UP);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        new Section(adding, FIXTURE_STATION_1, new Distance(6)),
                        LINE1_SECTION_ST1_ST2,
                        LINE1_SECTION_MIDDLE_ST2_ST3,
                        LINE1_SECTION_MIDDLE_ST3_ST4,
                        LINE1_SECTION_MIDDLE_ST4_ST5,
                        LINE1_SECTION_ST5_ST6
                );
    }

    @DisplayName("새 역을 기존 상행 종점 다음에 등록할 수 있다")
    @Test
    void addStationAtTail() {
        LineMap lineMap = LineMap.of(List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_MIDDLE_ST2_ST3,
                LINE1_SECTION_MIDDLE_ST3_ST4,
                LINE1_SECTION_MIDDLE_ST4_ST5,
                LINE1_SECTION_ST5_ST6
        ));

        Station adding = new Station(7L, "추가역");
        lineMap.add(FIXTURE_STATION_6, new Station(7L, "추가역"), new Distance(6), DOWN);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        LINE1_SECTION_ST1_ST2,
                        LINE1_SECTION_MIDDLE_ST2_ST3,
                        LINE1_SECTION_MIDDLE_ST3_ST4,
                        LINE1_SECTION_MIDDLE_ST4_ST5,
                        LINE1_SECTION_ST5_ST6,
                        new Section(FIXTURE_STATION_6, adding, new Distance(6))
                );
    }

    @DisplayName("기존 역 간 거리를 조정하여 역을 삭제할 수 있다")
    @Test
    void deleteStation() {
        LineMap lineMap = LineMap.of(List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_MIDDLE_ST2_ST3,
                LINE1_SECTION_MIDDLE_ST3_ST4,
                LINE1_SECTION_MIDDLE_ST4_ST5,
                LINE1_SECTION_ST5_ST6
        ));

        lineMap.delete(FIXTURE_STATION_2);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        new Section(FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(20)),
                        LINE1_SECTION_MIDDLE_ST3_ST4,
                        LINE1_SECTION_MIDDLE_ST4_ST5,
                        LINE1_SECTION_ST5_ST6
                );
    }

    @DisplayName("하행 종점의 역을 삭제할 수 있다")
    @Test
    void deleteTailStation() {
        LineMap lineMap = LineMap.of(List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_MIDDLE_ST2_ST3,
                LINE1_SECTION_MIDDLE_ST3_ST4,
                LINE1_SECTION_MIDDLE_ST4_ST5,
                LINE1_SECTION_ST5_ST6
        ));

        lineMap.delete(FIXTURE_STATION_1);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        LINE1_SECTION_MIDDLE_ST2_ST3,
                        LINE1_SECTION_MIDDLE_ST3_ST4,
                        LINE1_SECTION_MIDDLE_ST4_ST5,
                        LINE1_SECTION_ST5_ST6
                );
    }

    @DisplayName("상행 종점의 역을 삭제할 수 있다")
    @Test
    void deleteHeadStation() {
        LineMap lineMap = LineMap.of(List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_MIDDLE_ST2_ST3,
                LINE1_SECTION_MIDDLE_ST3_ST4,
                LINE1_SECTION_MIDDLE_ST4_ST5,
                LINE1_SECTION_ST5_ST6
        ));

        lineMap.delete(FIXTURE_STATION_6);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        LINE1_SECTION_ST1_ST2,
                        LINE1_SECTION_MIDDLE_ST2_ST3,
                        LINE1_SECTION_MIDDLE_ST3_ST4,
                        LINE1_SECTION_MIDDLE_ST4_ST5
                );
    }

    @DisplayName("노선의 역이 두 개이면, 역을 삭제할 때 모두 삭제한다")
    @Test
    void deleteAllWhenTwoStationsLeft() {
        LineMap lineMap = LineMap.of(List.of(
                LINE1_SECTION_ST1_ST2
        ));

        lineMap.delete(FIXTURE_STATION_1);

        assertThat(lineMap.extractSections())
                .isEmpty();
    }

    @DisplayName("하행 기준으로 순서에 맞게 역의 목록을 반환할 수 있다")
    @Test
    void getOrderedStations() {
        assertThat(FIXTURE_LINE_ROUTE.getOrderedStations())
                .containsExactly(
                        FIXTURE_STATION_1,
                        FIXTURE_STATION_2,
                        FIXTURE_STATION_3,
                        FIXTURE_STATION_4,
                        FIXTURE_STATION_5,
                        FIXTURE_STATION_6
                );
    }
}
