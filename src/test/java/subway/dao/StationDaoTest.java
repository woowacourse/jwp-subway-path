package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 역을_추가한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED"));
        final StationEntity station = new StationEntity("A", line.getId());

        // when
        final StationEntity savedStation = stationDao.insert(station);

        // then
        assertThat(stationDao.findById(savedStation.getId()).get()).isEqualTo(savedStation);
    }

    @Test
    void 역을_수정한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED"));
        final StationEntity savedStation = stationDao.insert(new StationEntity("A", line.getId()));
        final StationEntity newStation = new StationEntity(savedStation.getId(), "B", line.getId());

        // when
        stationDao.update(newStation);

        // then
        assertThat(stationDao.findById(savedStation.getId()).get()).isEqualTo(newStation);
    }

    @Test
    void 역을_삭제한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED"));
        final StationEntity savedStation = stationDao.insert(new StationEntity("A", line.getId()));

        // when
        stationDao.deleteById(savedStation.getId());

        // then
        assertThat(stationDao.findAll()).hasSize(0);
    }

    @Test
    void 역을_전체_조회한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED"));
        final StationEntity savedStation1 = stationDao.insert(new StationEntity("A", line.getId()));
        final StationEntity savedStation2 = stationDao.insert(new StationEntity("B", line.getId()));

        // when
        final List<StationEntity> result = stationDao.findAll();

        // then
        assertThat(result).containsExactly(savedStation1, savedStation2);
    }

    @Test
    void 역을_id로_조회한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED"));
        final StationEntity savedStation = stationDao.insert(new StationEntity("A", line.getId()));

        // when
        Optional<StationEntity> result = stationDao.findById(savedStation.getId());

        // then
        assertThat(result.get()).isEqualTo(savedStation);
    }

    @Test
    void 노선id를_입력받아_역을_전체_삭제한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED"));
        stationDao.insert(new StationEntity("A", line.getId()));

        // when
        stationDao.deleteByLineId(line.getId());

        // then
        assertThat(stationDao.findAll()).hasSize(0);
    }

    @Test
    void 역을_모두_추가한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED"));
        final List<StationEntity> stations = List.of(
                new StationEntity("A", line.getId()),
                new StationEntity("B", line.getId())
        );

        // when
        stationDao.insertAll(stations);

        // then
        assertThat(stationDao.findAll()).hasSize(2);
    }
}
