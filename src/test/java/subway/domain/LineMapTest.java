package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.vo.Direction.DOWN;
import static subway.domain.vo.Direction.UP;
import static subway.fixture.SectionFixture.SECTION_ST1_ST2;
import static subway.fixture.SectionFixture.SECTION_ST2_ST3;
import static subway.fixture.SectionFixture.SECTION_ST3_ST4;
import static subway.fixture.SectionFixture.SECTION_ST4_ST5;
import static subway.fixture.SectionFixture.SECTION_ST5_ST6;
import static subway.fixture.StationFixture.FIXTURE_STATION_1;
import static subway.fixture.StationFixture.FIXTURE_STATION_2;
import static subway.fixture.StationFixture.FIXTURE_STATION_3;
import static subway.fixture.StationFixture.FIXTURE_STATION_4;
import static subway.fixture.StationFixture.FIXTURE_STATION_5;
import static subway.fixture.StationFixture.FIXTURE_STATION_6;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.exception.IllegalDistanceArgumentException;
import subway.domain.exception.IllegalLineMapArgumentException;
import subway.domain.vo.Distance;

@DisplayName("역 개별 노선도 단위 테스트")
class LineMapTest {

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
                SECTION_ST1_ST2,
                SECTION_ST2_ST3,
                SECTION_ST3_ST4,
                SECTION_ST4_ST5,
                SECTION_ST5_ST6
        ));

        Station adding = new Station(7L, "추가역");
        lineMap.add(FIXTURE_STATION_1, new Station(7L, "추가역"), new Distance(6), DOWN);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        new Section(FIXTURE_STATION_1, adding, new Distance(6)),
                        new Section(adding, FIXTURE_STATION_2,
                                new Distance(SECTION_ST1_ST2.getDistance().getValue() - 6)),
                        SECTION_ST2_ST3,
                        SECTION_ST3_ST4,
                        SECTION_ST4_ST5,
                        SECTION_ST5_ST6
                );
    }

    @DisplayName("새 역을 기존 하행 종점 앞에 등록할 수 있다")
    @Test
    void addStationAtHead() {
        LineMap lineMap = LineMap.of(List.of(
                SECTION_ST1_ST2,
                SECTION_ST2_ST3,
                SECTION_ST3_ST4,
                SECTION_ST4_ST5,
                SECTION_ST5_ST6
        ));

        Station adding = new Station(7L, "추가역");
        lineMap.add(FIXTURE_STATION_1, new Station(7L, "추가역"), new Distance(6), UP);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        new Section(adding, FIXTURE_STATION_1, new Distance(6)),
                        SECTION_ST1_ST2,
                        SECTION_ST2_ST3,
                        SECTION_ST3_ST4,
                        SECTION_ST4_ST5,
                        SECTION_ST5_ST6
                );
    }

    @DisplayName("새 역을 기존 상행 종점 다음에 등록할 수 있다")
    @Test
    void addStationAtTail() {
        LineMap lineMap = LineMap.of(List.of(
                SECTION_ST1_ST2,
                SECTION_ST2_ST3,
                SECTION_ST3_ST4,
                SECTION_ST4_ST5,
                SECTION_ST5_ST6
        ));

        Station adding = new Station(7L, "추가역");
        lineMap.add(FIXTURE_STATION_6, new Station(7L, "추가역"), new Distance(6), DOWN);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        SECTION_ST1_ST2,
                        SECTION_ST2_ST3,
                        SECTION_ST3_ST4,
                        SECTION_ST4_ST5,
                        SECTION_ST5_ST6,
                        new Section(FIXTURE_STATION_6, adding, new Distance(6))
                );
    }

    @DisplayName("기존 역 간 거리보다 새 역 간 거리가 크거나 같으면 예외를 발생한다")
    @Test
    void addStationByDownDirectionFailInvalidDistance() {
        LineMap lineMap = LineMap.of(List.of(
                SECTION_ST1_ST2,
                SECTION_ST2_ST3,
                SECTION_ST3_ST4,
                SECTION_ST4_ST5,
                SECTION_ST5_ST6
        ));

        assertThatThrownBy(() -> lineMap.add(FIXTURE_STATION_1, new Station(7L, "추가역"),
                new Distance(SECTION_ST1_ST2.getDistance().getValue() + 1), DOWN))
                .isInstanceOf(IllegalDistanceArgumentException.class)
                .hasMessageContaining("기존 역 간 거리보다 크거나 같은 거리에 위치하는 새 역을 등록할 수 없습니다.");
    }

    @DisplayName("기준 역과 등록할 역이 동일하면 예외를 발생한다")
    @Test
    void addStationByDownDirectionFailInvalidSection() {
        LineMap lineMap = LineMap.of(List.of(
                SECTION_ST1_ST2,
                SECTION_ST2_ST3,
                SECTION_ST3_ST4,
                SECTION_ST4_ST5,
                SECTION_ST5_ST6
        ));

        assertThatThrownBy(() -> lineMap.add(FIXTURE_STATION_1, FIXTURE_STATION_1,
                new Distance(6), DOWN))
                .isInstanceOf(IllegalLineMapArgumentException.class)
                .hasMessageContaining("기준 역과 등록할 역은 동일할 수 없습니다.");
    }

    @DisplayName("기존 역 간 거리를 조정하여 역을 삭제할 수 있다")
    @Test
    void deleteStation() {
        LineMap lineMap = LineMap.of(List.of(
                SECTION_ST1_ST2,
                SECTION_ST2_ST3,
                SECTION_ST3_ST4,
                SECTION_ST4_ST5,
                SECTION_ST5_ST6
        ));

        lineMap.delete(FIXTURE_STATION_2);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        new Section(FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(
                                SECTION_ST1_ST2.getDistance().getValue()
                                        + SECTION_ST2_ST3.getDistance().getValue())),
                        SECTION_ST3_ST4,
                        SECTION_ST4_ST5,
                        SECTION_ST5_ST6
                );
    }

    @DisplayName("하행 종점의 역을 삭제할 수 있다")
    @Test
    void deleteTailStation() {
        LineMap lineMap = LineMap.of(List.of(
                SECTION_ST1_ST2,
                SECTION_ST2_ST3,
                SECTION_ST3_ST4,
                SECTION_ST4_ST5,
                SECTION_ST5_ST6
        ));

        lineMap.delete(FIXTURE_STATION_1);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        SECTION_ST2_ST3,
                        SECTION_ST3_ST4,
                        SECTION_ST4_ST5,
                        SECTION_ST5_ST6
                );
    }

    @DisplayName("상행 종점의 역을 삭제할 수 있다")
    @Test
    void deleteHeadStation() {
        LineMap lineMap = LineMap.of(List.of(
                SECTION_ST1_ST2,
                SECTION_ST2_ST3,
                SECTION_ST3_ST4,
                SECTION_ST4_ST5,
                SECTION_ST5_ST6
        ));

        lineMap.delete(FIXTURE_STATION_6);

        assertThat(lineMap.extractSections())
                .containsOnly(
                        SECTION_ST1_ST2,
                        SECTION_ST2_ST3,
                        SECTION_ST3_ST4,
                        SECTION_ST4_ST5
                );
    }

    @DisplayName("노선의 역이 두 개이면, 역을 삭제할 때 모두 삭제한다")
    @Test
    void deleteAllWhenTwoStationsLeft() {
        LineMap lineMap = LineMap.of(List.of(SECTION_ST1_ST2));

        lineMap.delete(FIXTURE_STATION_1);

        assertThat(lineMap.extractSections())
                .isEmpty();
    }

    @DisplayName("하행 기준으로 순서에 맞게 역의 목록을 반환할 수 있다")
    @Test
    void getOrderedStations() {
        LineMap lineMap = LineMap.of(List.of(
                SECTION_ST2_ST3,
                SECTION_ST1_ST2,
                SECTION_ST5_ST6,
                SECTION_ST3_ST4,
                SECTION_ST4_ST5
        ));
        assertThat(lineMap.getOrderedStations())
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
