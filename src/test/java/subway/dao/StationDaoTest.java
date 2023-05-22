package subway.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.data.LineFixture.LINE2_ENTITY;
import static subway.data.LineFixture.LINE3_ENTITY;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @AfterEach
    void clear() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("역 정보를 저장한다.")
    void station_data_insert() {
        // givne
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);
        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());

        // when
        StationEntity result = stationDao.insert(jamsil);

        // then
        assertThat(result.getName()).isEqualTo(jamsil.getName());
        assertThat(result.getLineId()).isEqualTo(jamsil.getLineId());
    }

    @Test
    @DisplayName("초기 두 역 정보를 저장한다.")
    void init_station_data_insert() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);
        List<StationEntity> request = List.of(
                new StationEntity("잠실역", insertedLine.getId()),
                new StationEntity("선릉역", insertedLine.getId()));

        // when
        List<StationEntity> result = stationDao.insertInit(request);

        // then
        assertThat(result.get(0).getName()).isEqualTo(request.get(0).getName());
        assertThat(result.get(0).getLineId()).isEqualTo(request.get(0).getLineId());
        assertThat(result.get(1).getName()).isEqualTo(request.get(1).getName());
        assertThat(result.get(1).getLineId()).isEqualTo(request.get(1).getLineId());
    }

    @Test
    @DisplayName("역 정보를 불러온다.")
    void station_data_load() {
        // givne
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);
        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);

        // when
        Optional<StationEntity> result = stationDao.findById(insertedJamsil.getId());

        // then
        assertThat(result.get().getName()).isEqualTo(insertedJamsil.getName());
        assertThat(result.get().getLineId()).isEqualTo(insertedJamsil.getLineId());
    }

    @Test
    @DisplayName("노선 아이디와 역 이름을 통해 역 정보를 불러온다.")
    void station_data_load_by_line_id_and_name() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);
        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);

        // when
        Optional<StationEntity> result = stationDao.findByNameAndLineId(jamsil.getName(), insertedLine.getId());

        // then
        assertThat(result.get().getId()).isEqualTo(insertedJamsil.getId());
        assertThat(result.get().getName()).isEqualTo(insertedJamsil.getName());
        assertThat(result.get().getLineId()).isEqualTo(insertedJamsil.getLineId());
    }

    @Test
    @DisplayName("역 id를 통해 역을 삭제한다.")
    void delete_station_by_station_id() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);
        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);

        // when
        stationDao.deleteById(insertedJamsil.getId());
        Optional<StationEntity> result = stationDao.findById(insertedJamsil.getId());

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("여러역 id를 통해 여러역을 삭제한다.")
    void delete_stations_by_stations_ids() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);
        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity gangnam = new StationEntity("강남", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);
        StationEntity insertedGangnam = stationDao.insert(gangnam);

        // when
        stationDao.deleteBothById(List.of(insertedJamsil.getId(), insertedGangnam.getId()));
        Optional<StationEntity> result1 = stationDao.findById(insertedJamsil.getId());
        Optional<StationEntity> result2 = stationDao.findById(insertedGangnam.getId());

        // then
        assertThat(result1.isPresent()).isFalse();
        assertThat(result2.isPresent()).isFalse();
    }

    @Test
    @DisplayName("역 이름으로 역 정보를 조회한다.")
    void find_station_by_station_name() {
        // given
        LineEntity insertedLine2 = lineDao.insert(LINE2_ENTITY);
        LineEntity insertedLine3 = lineDao.insert(LINE3_ENTITY);
        StationEntity Line2Jamsil = new StationEntity("잠실", insertedLine2.getId());
        StationEntity Line3Jamsil = new StationEntity("잠실", insertedLine3.getId());
        StationEntity insertedLine2Jamsil = stationDao.insert(Line2Jamsil);
        StationEntity insertedLine3Jamsil = stationDao.insert(Line3Jamsil);


        // when
        Optional<StationEntity> result = stationDao.findByName(insertedLine2Jamsil.getName(), insertedLine2.getName());

        // then
        assertThat(result.get().getId()).isEqualTo(insertedLine2Jamsil.getId());
        assertThat(result.get().getName()).isEqualTo(insertedLine2Jamsil.getName());
    }
}
