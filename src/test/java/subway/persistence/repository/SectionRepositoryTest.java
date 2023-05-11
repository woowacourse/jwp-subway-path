package subway.persistence.repository;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.persistence.dao.LineStationDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;

@JdbcTest
@AutoConfigureTestDatabase
class SectionRepositoryTest {

    private SectionRepository sectionRepository;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired DataSource dataSource) {
        final SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource);
        final StationDao stationDao = new StationDao(jdbcTemplate, dataSource);
        final LineStationDao lineStationDao = new LineStationDao(jdbcTemplate, dataSource);

        sectionRepository = new SectionRepository(sectionDao, stationDao, lineStationDao);
    }

    @Test
    void insertTest() {
        final Line line = Line.of(1L, "1호선", "bg-red-500");

        final Station first = Station.of(1L, "1역");
        final Station second = Station.of(2L, "2역");
        final Station third = Station.of(3L, "3역");
        final Station fourth = Station.of(4L, "4역");
        final Station fifth = Station.of(5L, "5역");

        final Distance distance = Distance.from(3);

        line.initialStations(first, second, distance);
        line.addEndStation(second, third, distance);
        line.addEndStation(third, fourth, distance);
        line.addEndStation(fourth, fifth, distance);

        sectionRepository.insert(line);

        final Line anotherLine = Line.of(1L, "1호선", "bg-red-500");
        sectionRepository.findAllByLine(anotherLine);

    }

    @Test
    void findAllByLineTest() {
        sectionRepository.findAllByLine(Line.of(1L, "1호선", "bg-red-500"));
    }
}
