package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class LineTest {
    private static final Station STATION_JAMSIL_NARU = new Station("잠실나루");
    private static final Station STATION_JAMSIL = new Station("잠실");
    private static final Station STATION_JAMSIL_SAENAE = new Station("잠실새내");
    private static final Distance DISTANCE_10 = new Distance(10);
    private static final Distance DISTANCE_6 = new Distance(6);
    private static final Distance DISTANCE_5 = new Distance(5);

    private Line line;

    @BeforeEach
    void setUp() {
        this.line = new Line("1호선", "blue");
    }

    @DisplayName("한 번에 두 역을 등록했을 때 그 호선에는 두 개의 역만 존재한다")
    @Test
    void insertTwoStationsAndStoreTwoStations() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        List<Station> stations = line.getStations();

        assertThat(stations).hasSize(2);
    }

    @DisplayName("한 번에 두 역을 등록했을 때 두 역 사이의 거리를 저장한다")
    @Test
    void insertTwoStationsAndStoreDistanceBetweenTwoStations() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        int distance = line.getDistanceBetween(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE);

        assertThat(distance).isEqualTo(10);
    }

    @DisplayName("빈 라인에만 한 번에 두 역을 등록할 수 있다")
    @Test
    void insertTwoStationsFail() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10));
    }

    @DisplayName("라인의 상행 종점에 역을 등록한다")
    @Test
    void insertStationTop() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        line.insert(STATION_JAMSIL, STATION_JAMSIL_NARU, DISTANCE_5);

        assertSoftly(softly -> {
            softly.assertThat(line.getStations()).first().isEqualTo(STATION_JAMSIL);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_NARU)).isEqualTo(5);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_SAENAE)).isEqualTo(15);
        });
    }

    @DisplayName("라인의 상행에 역을 등록한다")
    @Test
    void insertStationUpper() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        line.insert(STATION_JAMSIL, STATION_JAMSIL_SAENAE, DISTANCE_6);

        assertSoftly(softly -> {
            softly.assertThat(line.getStations()).contains(STATION_JAMSIL);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_NARU)).isEqualTo(4);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_SAENAE)).isEqualTo(6);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE)).isEqualTo(10);
        });
    }

    @DisplayName("라인의 하행 종점에 역을 등록한다")
    @Test
    void insertStationBottom() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        line.insert(STATION_JAMSIL_SAENAE, STATION_JAMSIL, DISTANCE_5);

        assertSoftly(softly -> {
            softly.assertThat(line.getStations()).last().isEqualTo(STATION_JAMSIL);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_SAENAE)).isEqualTo(5);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_NARU)).isEqualTo(15);
        });
    }

    @DisplayName("라인의 하행에 역을 등록한다")
    @Test
    void insertStationLower() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL, DISTANCE_6);

        assertSoftly(softly -> {
            softly.assertThat(line.getStations()).contains(STATION_JAMSIL);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_NARU)).isEqualTo(6);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_SAENAE)).isEqualTo(4);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE)).isEqualTo(10);
        });
    }

    @DisplayName("잘못된 거리 정보를 가진 역을 등록하려고 하면 예외가 발생한다")
    @Test
    void throwExceptionWhenInsertWrongDistance() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_5);

        assertThatThrownBy(() -> line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL, DISTANCE_6))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 존재하는 역을 등록하려고 하면 예외가 발생한다")
    @Test
    void throwExceptionWhenInsertExistedStation() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);

        assertThatThrownBy(() -> line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_6))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행 종점을 제거한다")
    @Test
    void deleteTop() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL, DISTANCE_6);

        line.delete(STATION_JAMSIL_NARU);

        assertSoftly(softly -> {
            softly.assertThat(line.getStations()).doesNotContain(STATION_JAMSIL_NARU);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_SAENAE)).isEqualTo(4);
        });
    }

    @DisplayName("하행 종점을 제거한다")
    @Test
    void deleteBottom() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        line.insert(STATION_JAMSIL, STATION_JAMSIL_NARU, DISTANCE_6);

        line.delete(STATION_JAMSIL_SAENAE);

        assertSoftly(softly -> {
            softly.assertThat(line.getStations()).doesNotContain(STATION_JAMSIL_SAENAE);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL, STATION_JAMSIL_NARU)).isEqualTo(6);
        });
    }

    @DisplayName("중간에 있는 역을 제거한다")
    @Test
    void deleteStationInBetween() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        line.insert(STATION_JAMSIL, STATION_JAMSIL_NARU, DISTANCE_6);

        line.delete(STATION_JAMSIL);

        assertSoftly(softly -> {
            softly.assertThat(line.getStations()).doesNotContain(STATION_JAMSIL);
            softly.assertThat(line.getDistanceBetween(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE)).isEqualTo(10);
        });
    }

    @DisplayName("역이 두 개 뿐인 경우 모두 제거한다")
    @Test
    void deleteStationWhenTwoStationsLeft() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        line.delete(STATION_JAMSIL_SAENAE);

        assertThat(line.getStations()).isEmpty();
    }

    @DisplayName("존재하지 않는 역을 제거하려는 경우 예외가 발생한다")
    @Test
    void throwExceptionWhenNotExistedStation() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);

        assertThatThrownBy(() -> line.delete(STATION_JAMSIL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("두 역 사이의 거리를 알 수 있다")
    @Test
    void getDistanceBetweenTwoStations() {
        line.insert(STATION_JAMSIL_NARU, STATION_JAMSIL_SAENAE, DISTANCE_10);
        line.insert(STATION_JAMSIL, STATION_JAMSIL_SAENAE, DISTANCE_6);

        assertThat(line.getDistanceBetween(STATION_JAMSIL_NARU, STATION_JAMSIL)).isEqualTo(4);
    }
}
