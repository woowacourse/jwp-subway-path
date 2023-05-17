package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
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
    void 역을_전체_조회한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        stationDao.insertAll(List.of(
                new StationEntity("A", line.getId()),
                new StationEntity("B", line.getId())
        ));

        // when
        final List<StationEntity> result = stationDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(List.of(
                new StationEntity("A", line.getId()),
                new StationEntity("B", line.getId())
        ));
    }

    @Test
    void 노선id를_입력받아_역을_전체_삭제한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        stationDao.insertAll(List.of(
                new StationEntity("A", line.getId()),
                new StationEntity("B", line.getId())
        ));

        // when
        stationDao.deleteByLineId(line.getId());

        // then
        assertThat(stationDao.findAll()).hasSize(0);
    }

    @Test
    void 역을_모두_추가한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        final List<StationEntity> stations = List.of(
                new StationEntity("A", line.getId()),
                new StationEntity("B", line.getId())
        );

        // when
        stationDao.insertAll(stations);

        // then
        assertThat(stationDao.findAll()).hasSize(2);
    }

    @Test
    void 역을_추가한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        final StationEntity station = new StationEntity("B", line.getId());

        // when
        final StationEntity result = stationDao.insert(station);

        // then
        assertAll(
                () -> assertThat(result.getId()).isPositive(),
                () -> assertThat(stationDao.findAll()).hasSize(1)
        );
    }

    @Test
    void 노선_id를_받아_역을_전체_조회한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        stationDao.insertAll(List.of(
                new StationEntity("A", line.getId()),
                new StationEntity("B", line.getId())
        ));

        // when
        final List<StationEntity> result = stationDao.findByLineId(line.getId());

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(List.of(
                new StationEntity("A", line.getId()),
                new StationEntity("B", line.getId())
        ));
    }

    @Test
    void id를_받아_역을_삭제한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        final StationEntity stationEntity = stationDao.insert(new StationEntity("A", line.getId()));

        // when
        stationDao.deleteById(stationEntity.getId());

        // then
        assertThat(stationDao.findAll()).isEmpty();
    }
}
