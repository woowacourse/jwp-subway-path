package subway.station.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.line.dao.StationDao;
import subway.domain.line.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private StationDao stationDao;

    @BeforeEach
    void init() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    void 역_추가_테스트() {
        //given
        StationEntity stationEntity = new StationEntity("상인역");

        //when
        StationEntity insertStationEntity = stationDao.insert(stationEntity);

        //then
        Assertions.assertThat(insertStationEntity)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(stationEntity);
    }

    @Test
    void 역_추가_실패_테스트_동일한_이름의_역이_존재할_경우() {
        //given
        StationEntity stationEntity = new StationEntity("동대구역");

        //when
        stationDao.insert(stationEntity);

        //then
        assertThatThrownBy(() -> stationDao.insert(stationEntity))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void 모든_역_조회_테스트() {
        //given
        Optional<List<StationEntity>> findStation = stationDao.findAll();

        //then
        Assertions.assertThat(findStation.get()).hasSizeGreaterThan(0);
    }

    @Test
    void 단일_역_조회_테스트() {
        //given
        StationEntity stationEntity = new StationEntity("상인역");

        //when
        StationEntity insertStationEntity = stationDao.insert(stationEntity);

        //then
        Assertions.assertThat(stationEntity).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(insertStationEntity);
    }

    @Test
    void 이름으로_역_조회_테스트() {
        //given
        StationEntity stationEntity = new StationEntity("상인역");
        //when
        stationDao.insert(stationEntity);
        Optional<StationEntity> findStation = stationDao.findByName("상인역");

        //then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> findStation.isPresent(),
                () -> Assertions.assertThat(findStation.get().getName()).isEqualTo("상인역")
        );
    }

    @Test
    void 이름으로_역_조회_실패_테스트() {
        //given
        Optional<StationEntity> station = stationDao.findByName("상인역");

        //then
        Assertions.assertThat(station).isEmpty();
    }

    @Test
    void 역_수정_테스트() {
        //given
        StationEntity stationEntity = new StationEntity("상인역");

        //when
        StationEntity insertStationEntity = stationDao.insert(stationEntity);
        StationEntity updateStationEntity = new StationEntity(insertStationEntity.getId(), "대구역");
        stationDao.update(updateStationEntity);

        //then
        Assertions.assertThat(stationDao.findById(updateStationEntity.getId()).get()).isEqualTo(updateStationEntity);
    }

    @Test
    void 역_삭제_테스트() {
        //given
        StationEntity stationEntity = new StationEntity("상인역");

        //when
        StationEntity insertStationEntity = stationDao.insert(stationEntity);
        stationDao.deleteById(insertStationEntity.getId());

        //then
        Assertions.assertThat(stationDao.findById(insertStationEntity.getId())).isEmpty();
    }
}
