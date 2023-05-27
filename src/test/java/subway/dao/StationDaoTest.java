package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.StationEntity;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
public class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 역_ID가_존재하는지_확인한다() {
        final StationEntity stationEntity = new StationEntity("잠실역");
        final Long id = stationDao.save(stationEntity);

        final boolean stationIdExist = stationDao.isIdExist(id);

        assertThat(stationIdExist).isTrue();
    }

    @Test
    void 역_이름이_존재하는지_확인한다() {
        final StationEntity stationEntity = new StationEntity("잠실역");
        stationDao.save(stationEntity);

        final boolean stationNameExist = stationDao.isNameExist("잠실역");

        assertThat(stationNameExist).isTrue();
    }

    @Test
    void 역_정보를_저장한다() {
        final StationEntity stationEntity = new StationEntity("잠실역");

        stationDao.save(stationEntity);

        final List<StationEntity> stationEntities = stationDao.findAll();
        assertThat(stationEntities.size()).isEqualTo(1);
    }

    @Test
    void 모든_역_정보를_조회한다() {
        final StationEntity stationEntity1 = new StationEntity("잠실역");
        final StationEntity stationEntity2 = new StationEntity("아현역");
        stationDao.save(stationEntity1);
        stationDao.save(stationEntity2);

        final List<StationEntity> stationEntities = stationDao.findAll();

        assertThat(stationEntities.size()).isEqualTo(2);
    }

    @Test
    void 역_ID로_역_정보를_조회한다() {
        final StationEntity stationEntity = new StationEntity("잠실역");
        final Long id = stationDao.save(stationEntity);

        final StationEntity foundStationEntity = stationDao.findById(id);

        assertThat(stationEntity.getName()).isEqualTo(foundStationEntity.getName());
    }

    @Test
    void 역_이름으로_역_정보를_조회한다() {
        final StationEntity stationEntity = new StationEntity("잠실역");
        stationDao.save(stationEntity);

        final StationEntity foundStationEntity = stationDao.findByName("잠실역");

        assertThat(stationEntity.getName()).isEqualTo(foundStationEntity.getName());
    }

    @Test
    void 역_ID로_역_정보를_삭제한다() {
        final StationEntity stationEntity = new StationEntity("잠실역");
        final Long id = stationDao.save(stationEntity);

        stationDao.deleteById(id);

        final List<StationEntity> stationEntities = stationDao.findAll();
        assertThat(stationEntities.isEmpty()).isTrue();
    }
}
