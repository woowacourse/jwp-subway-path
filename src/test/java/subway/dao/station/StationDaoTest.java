package subway.dao.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 역_저장_테스트() {
        final StationEntity stationEntity = new StationEntity("역삼역");

        final StationEntity insertEntity = stationDao.insert(stationEntity);

        assertThat(insertEntity.getStationId()).isNotNull();
        assertThat(insertEntity.getName()).isEqualTo("역삼역");
    }

    @Test
    void 전체_조회_테스트() {
        final StationEntity stationEntity = new StationEntity("역삼역");
        stationDao.insert(stationEntity);

        final List<StationEntity> all = stationDao.findAll();

        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("역삼역");
    }

    @Test
    void 식별자_조회_테스트() {
        final StationEntity stationEntityA = new StationEntity("역삼역");
        final StationEntity stationEntityB = new StationEntity("강남역");
        final StationEntity insertA = stationDao.insert(stationEntityA);
        stationDao.insert(stationEntityB);

        final StationEntity byId = stationDao.findById(insertA.getStationId());

        assertThat(byId.getStationId()).isNotNull();
        assertThat(byId.getName()).isEqualTo("역삼역");
    }

    @Test
    void 식별자_삭제_테스트() {
        final StationEntity stationEntity = new StationEntity("역삼역");
        final StationEntity insertEntity = stationDao.insert(stationEntity);

        stationDao.deleteById(insertEntity.getStationId());

        final List<StationEntity> all = stationDao.findAll();
        assertThat(all).isEmpty();
    }
}
