package subway.domain.station;

import static fixtures.LineFixtures.INITIAL_Line2;
import static fixtures.StationFixtures.*;
import static fixtures.path.PathStationFixtures.ALL_INITIAL_STATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import fixtures.path.PathTransferSectionFixtures;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.section.transfer.TransferSection;
import subway.exception.StationNotFoundException;

class StationsTest {

    @Nested
    @DisplayName("상행역, 하행역 이름으로 역 방향 케이스를 반환 시에")
    class determineStationDirectionBy {

        @Test
        @DisplayName("하행역 이름이 존재하고, 상행역 이름이 존재하지 않으면 UP 케이스를 반환한다.")
        void up() {
            // given
            Stations stations = new Stations(ALL_LINE2_STATION);
            String upStationName = "없는 역";
            String downStationName = INITIAL_STATION_A.NAME;

            // when
            StationDirection stationDirection = stations.determineStationDirectionBy(upStationName, downStationName);

            // then
            Assertions.assertThat(stationDirection).isEqualTo(StationDirection.UP);
        }

        @Test
        @DisplayName("상행역 이름이 존재하고, 하행역 이름이 존재하지 않으면 DOWN 케이스를 반환한다.")
        void down() {
            // given
            Stations stations = new Stations(ALL_LINE2_STATION);
            String upStationName = INITIAL_STATION_C.NAME;
            String downStationName = "없는 역";

            // when
            StationDirection stationDirection = stations.determineStationDirectionBy(upStationName, downStationName);

            // then
            Assertions.assertThat(stationDirection).isEqualTo(StationDirection.DOWN);
        }

        @Test
        @DisplayName("상행역, 하행역 모두 존재하지 않으면 예외가 발생한다.")
        void throw_not_exist() {
            // given
            Stations stations = new Stations(ALL_LINE2_STATION);
            String upStationName = "없는 역1";
            String downStationName = "없는 역2";

            // when, then
            assertThatThrownBy(() -> stations.determineStationDirectionBy(upStationName, downStationName))
                    .isInstanceOf(StationNotFoundException.class)
                    .hasMessage("두개의 역 모두가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("상행역, 하행역 모두 존재하면 예외가 발생한다.")
        void throw_all_exist() {
            // given
            Stations stations = new Stations(ALL_LINE2_STATION);
            String upStationName = INITIAL_STATION_A.NAME;
            String downStationName = INITIAL_STATION_C.NAME;

            // when, then
            assertThatThrownBy(() -> stations.determineStationDirectionBy(upStationName, downStationName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("두개의 역이 이미 모두 존재합니다.");
        }
    }

    @Test
    @DisplayName("모두 같은 노선을 가졌을 때, 해당 노선을 반환한다.")
    void getLineWhenAllSameLine() {
        // given
        Stations stations = new Stations(ALL_LINE2_STATION);

        // when
        Line line = stations.getLineWhenAllSameLine();

        // then
        assertThat(line).usingRecursiveComparison().isEqualTo(INITIAL_Line2.FIND_LINE);
    }

    @Test
    @DisplayName("노선을 반환할 때 역들 중 하나라도 다른 노선이 있으면 예외가 발생한다.")
    void getLineWhenAllSameLine_throw_diff_line() {
        // given
        Stations stations = new Stations(ALL_INITIAL_STATION);

        // when, then
        assertThatThrownBy(() -> stations.getLineWhenAllSameLine())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("역 목록에서 가져올 노선이 여러 개가 존재합니다.");
    }

    @Test
    @DisplayName("환승 구간 리스트를 반환한다.")
    void getTransferSections() {
        // given
        Stations stations = new Stations(ALL_INITIAL_STATION);

        // when
        List<TransferSection> transferSections = stations.getTransferSections();

        // then
        assertThat(transferSections).usingRecursiveFieldByFieldElementComparator().contains(
                PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_2_TO_3_C.FIND_TRANSFER_SECTION,
                PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_2_TO_7_A.FIND_TRANSFER_SECTION,
                PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_7_TO_3_D.FIND_TRANSFER_SECTION);
    }
}
