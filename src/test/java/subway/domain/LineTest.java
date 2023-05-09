package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {
    private static final Station STATION_JAMSIL_NARU = new Station("잠실나루");
    private static final Station STATION_JAMSIL = new Station("잠실");
    private static final Station STATION_JAMSIL_SAENAE = new Station("잠실새내");

    @DisplayName("한 번에 두 역을 등록했을 때 그 호선에 두 개의 역만 존재한다")
    @Test
    void insertTwoStationsAndStoreTwoStations() {
        Line line = new Line("1호선", "blue");
        line.insertBoth(STATION_JAMSIL_NARU, 10, STATION_JAMSIL_SAENAE);
        List<Station> stations = line.getStations();

        assertThat(stations).hasSize(2);
    }

    @DisplayName("한 번에 두 역을 등록했을 때 두 역 사이의 거리를 저장한다")
    @Test
    void insertTwoStationsAndStoreDistanceBetweenTwoStations() {
        Line line = new Line("1호선", "blue");

        line.insertBoth(STATION_JAMSIL_NARU, 10, STATION_JAMSIL_SAENAE);
        int distance = line.getDistanceBetween(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE);

        assertThat(distance).isEqualTo(10);
    }

    @DisplayName("빈 라인에만 한 번에 두 역을 등록할 수 있다")
    @Test
    void insertTwoStationsFail() {
        Line line = new Line("1호선", "blue");
        line.insertBoth(STATION_JAMSIL_NARU, 10, STATION_JAMSIL_SAENAE);

        assertThatIllegalStateException()
                .isThrownBy(() -> line.insertBoth(STATION_JAMSIL_NARU, 10, STATION_JAMSIL_SAENAE));
    }

    @DisplayName("역의 상행에 역을 등록한다")
    @Test
    void insertStationUpper() {
        Line line = new Line("1호선", "blue");
        line.insertBoth(STATION_JAMSIL_NARU, 10, STATION_JAMSIL_SAENAE);

        line.insertUpper(STATION_JAMSIL, 6, STATION_JAMSIL_SAENAE);
    }

    @DisplayName("두 역 사이의 거리를 알 수 있다")
    @Test
    void getDistanceBetweenTwoStations() {
        Line line = new Line("1호선", "blue");

        line.insertBoth(
                STATION_JAMSIL_NARU,
                10,
                STATION_JAMSIL_SAENAE);
        line.insertUpper(STATION_JAMSIL, 6, STATION_JAMSIL_NARU);

        assertThat(line.getDistanceBetween(STATION_JAMSIL_NARU, STATION_JAMSIL)).isEqualTo(4);
    }
}
