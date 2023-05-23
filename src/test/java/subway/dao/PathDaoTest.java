package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;
import subway.domain.path.Path;
import subway.domain.path.Paths;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@JdbcTest
class PathDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PathDao pathDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        this.pathDao = new PathDao(jdbcTemplate);
        this.stationDao = new StationDao(jdbcTemplate);
    }

    @DisplayName("Paths 도메인을 저장할 수 있다")
    @Test
    void save() {
        //given
        final Station station1 = stationDao.insert(new Station("서면역"));
        final Station station2 = stationDao.insert(new Station("해운대역"));
        final Path path = new Path(station1, station2, 4);
        final Paths paths = new Paths(List.of(path));

        //when
        pathDao.save(paths, 1L);

        //then
        assertThat(countRowsInTable(jdbcTemplate, "path")).isOne();
    }

    @DisplayName("해당 노선의 경로들을 가져올 수 있다")
    @Test
    void findByLineId() {
        //given
        final Station station1 = stationDao.insert(new Station("서면역"));
        final Station station2 = stationDao.insert(new Station("해운대역"));
        final Path path = new Path(station1, station2, 4);
        final Paths paths = new Paths(List.of(path));

        pathDao.save(paths, 1L);

        //when
        final Paths persisted = pathDao.findByLineId(1L);

        //then
        assertThat(persisted.getOrdered()).hasSize(1);
    }
}
