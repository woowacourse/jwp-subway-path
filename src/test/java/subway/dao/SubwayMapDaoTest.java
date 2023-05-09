package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import({SubwayMapDao.class, LineDao.class, StationDao.class})
class SubwayMapDaoTest {
    @Autowired
    private SubwayMapDao subwayMapDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    @Test
    @DisplayName("노선에 역이 하나도 등록되지 않은 상황에서 최초 등록시 두 역이 동시 등록됩니다.")
    void initialize() {

    }

    @Test
    @DisplayName("노선에 역이 등록될 때 거리 정보도 함께 포함됩니다.")
    void distanceSaving() {

    }

    @Test
    @DisplayName("거리 정보는 양의 정수로 제한합니다.")
    void distanceFormat() {

    }

    @Test
    @DisplayName("A-B-C역이 등록되어 있는 노선의 어디에든 D역을 등록할 수 있습니다.")
    void savingD() {

    }

    @Test
    @DisplayName("하나의 역은 여러 노선에 등록될 수 있습니다.")
    void multipleSubwayMap() {

    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리 정보를 고려해야 합니다.")
    void distanceChange() {
        // A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
        // B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리는 양의 정수라는 비즈니스 규칙을 지켜야 합니다.")
    void distanceRule() {
        // A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
        // B-C역의 거리가 3km인 경우 B-D 거리는 3km보다 적어야 합니다.
        // B-C가 3km인데 B-D거리가 3km면 D-C거리는 0km가 되어야 하는데 거리는 양의 정수여야 하기 때문에 이 경우 등록이 불가능 해야합니다.
    }

    @Test
    @DisplayName("노선은 갈래길을 가질 수 없습니다.")
    void noForkedRoad() {
        // A-B-C 에서 B역의 다음 역으로 D역을 등록하려고 할 경우
        // A-B-C와 A-B-D로 갈래길이 만들어지는게 아니라
        // A-B-D-C로 중간에 역이 등록됩니다.
    }
}