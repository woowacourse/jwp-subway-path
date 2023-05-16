package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.Direction.DOWN;
import static subway.domain.Direction.UP;
import static subway.domain.LineFixture.FIXTURE_LINE_1;
import static subway.domain.SectionFixture.SECTION_END;
import static subway.domain.SectionFixture.SECTION_MIDDLE_1;
import static subway.domain.SectionFixture.SECTION_MIDDLE_2;
import static subway.domain.SectionFixture.SECTION_MIDDLE_3;
import static subway.domain.SectionFixture.SECTION_START;
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

class LineRouteTest {

    private static final LineRoute FIXTURE_LINE_ROUTE = LineRoute.of(FIXTURE_LINE_1, List.of(
            SECTION_START,
            SECTION_MIDDLE_1,
            SECTION_MIDDLE_2,
            SECTION_MIDDLE_3,
            SECTION_END
    ));

    @DisplayName("노선에 역이 존재하지 않으면 두 역을 모두 새로 등록할 수 있다")
    @Test
    void addStationsToEmpty() {
        LineRoute lineRoute = LineRoute.of(FIXTURE_LINE_1, Collections.emptyList());

        Station adding = new Station(7L, "추가역");
        lineRoute.add(FIXTURE_STATION_1, new Station(7L, "추가역"), new Distance(6), DOWN);

        assertThat(lineRoute.getOrderedStations())
                .containsExactly(FIXTURE_STATION_1, adding);

        assertThat(lineRoute.findRightSection(FIXTURE_STATION_1)
                .get()
                .getDistance()
                .getValue())
                .isEqualTo(6);
    }

    @DisplayName("기존 역 간 거리를 조정하여 새 역을 등록할 수 있다")
    @Test
    void addStationByDownDirection() {
        LineRoute lineRoute = LineRoute.of(FIXTURE_LINE_1, List.of(
                SECTION_START,
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_2,
                SECTION_MIDDLE_3,
                SECTION_END
        ));

        Station adding = new Station(7L, "추가역");
        lineRoute.add(FIXTURE_STATION_1, new Station(7L, "추가역"), new Distance(6), DOWN);

        assertThat(lineRoute.getOrderedStations())
                .containsExactly(
                        FIXTURE_STATION_1,
                        adding,
                        FIXTURE_STATION_2,
                        FIXTURE_STATION_3,
                        FIXTURE_STATION_4,
                        FIXTURE_STATION_5,
                        FIXTURE_STATION_6
                );

        assertThat(lineRoute.findLeftSection(adding).get()
                .getDistance()
                .getValue()).isEqualTo(6);
        assertThat(lineRoute.findRightSection(adding).get()
                .getDistance()
                .getValue())
                .isEqualTo(4);
    }

    @DisplayName("새 역을 기존 하행 종점 앞에 등록할 수 있다")
    @Test
    void addStationAtHead() {
        LineRoute lineRoute = LineRoute.of(FIXTURE_LINE_1, List.of(
                SECTION_START,
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_2,
                SECTION_MIDDLE_3,
                SECTION_END
        ));

        Station adding = new Station(7L, "추가역");
        lineRoute.add(FIXTURE_STATION_1, new Station(7L, "추가역"), new Distance(6), UP);

        assertThat(lineRoute.getOrderedStations())
                .containsExactly(
                        adding,
                        FIXTURE_STATION_1,
                        FIXTURE_STATION_2,
                        FIXTURE_STATION_3,
                        FIXTURE_STATION_4,
                        FIXTURE_STATION_5,
                        FIXTURE_STATION_6
                );
        assertThat(lineRoute.findRightSection(adding)
                .get()
                .getDistance()
                .getValue())
                .isEqualTo(6);
    }

    @DisplayName("새 역을 기존 상행 종점 다음에 등록할 수 있다")
    @Test
    void addStationAtTail() {
        LineRoute lineRoute = LineRoute.of(FIXTURE_LINE_1, List.of(
                SECTION_START,
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_2,
                SECTION_MIDDLE_3,
                SECTION_END
        ));

        Station adding = new Station(7L, "추가역");
        lineRoute.add(FIXTURE_STATION_6, new Station(7L, "추가역"), new Distance(6), DOWN);

        assertThat(lineRoute.getOrderedStations())
                .containsExactly(
                        FIXTURE_STATION_1,
                        FIXTURE_STATION_2,
                        FIXTURE_STATION_3,
                        FIXTURE_STATION_4,
                        FIXTURE_STATION_5,
                        FIXTURE_STATION_6,
                        adding
                );
        assertThat(lineRoute.findLeftSection(adding)
                .get()
                .getDistance()
                .getValue())
                .isEqualTo(6);
    }

    @DisplayName("기존 역 간 거리를 조정하여 역을 삭제할 수 있다")
    @Test
    void deleteStation() {
        LineRoute lineRoute = LineRoute.of(FIXTURE_LINE_1, List.of(
                SECTION_START,
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_2,
                SECTION_MIDDLE_3,
                SECTION_END
        ));

        lineRoute.delete(FIXTURE_STATION_2);

        assertThat(lineRoute.getOrderedStations())
                .containsExactly(
                        FIXTURE_STATION_1,
                        FIXTURE_STATION_3,
                        FIXTURE_STATION_4,
                        FIXTURE_STATION_5,
                        FIXTURE_STATION_6
                );
        assertThat(lineRoute.findRightSection(FIXTURE_STATION_1)
                .get()
                .getDistance()
                .getValue())
                .isEqualTo(20);
    }

    @DisplayName("하행 종점의 역을 삭제할 수 있다")
    @Test
    void deleteTailStation() {
        LineRoute lineRoute = LineRoute.of(FIXTURE_LINE_1, List.of(
                SECTION_START,
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_2,
                SECTION_MIDDLE_3,
                SECTION_END
        ));

        lineRoute.delete(FIXTURE_STATION_1);

        assertThat(lineRoute.getOrderedStations())
                .containsExactly(
                        FIXTURE_STATION_2,
                        FIXTURE_STATION_3,
                        FIXTURE_STATION_4,
                        FIXTURE_STATION_5,
                        FIXTURE_STATION_6
                );
    }

    @DisplayName("상행 종점의 역을 삭제할 수 있다")
    @Test
    void deleteHeadStation() {
        LineRoute lineRoute = LineRoute.of(FIXTURE_LINE_1, List.of(
                SECTION_START,
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_2,
                SECTION_MIDDLE_3,
                SECTION_END
        ));

        lineRoute.delete(FIXTURE_STATION_6);

        assertThat(lineRoute.getOrderedStations())
                .containsExactly(
                        FIXTURE_STATION_1,
                        FIXTURE_STATION_2,
                        FIXTURE_STATION_3,
                        FIXTURE_STATION_4,
                        FIXTURE_STATION_5
                );
    }

    @DisplayName("노선의 역이 두 개이면, 역을 삭제할 때 모두 삭제한다")
    @Test
    void deleteAllWhenTwoStationsLeft() {
        LineRoute lineRoute = LineRoute.of(FIXTURE_LINE_1, List.of(
                SECTION_START
        ));

        lineRoute.delete(FIXTURE_STATION_1);

        assertThat(lineRoute.getOrderedStations())
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
