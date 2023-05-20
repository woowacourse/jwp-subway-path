package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;
import subway.repository.dao.SectionDao;
import subway.repository.dao.StationDao;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationRepositoryTest {

    private StationRepository stationRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stationRepository = new StationRepository(
                new SectionDao(jdbcTemplate),
                new StationDao(jdbcTemplate)
        );
    }

    @Test
    void 역을_저장한다() {
        // given
        Station station = new Station("강남역");

        // when
        Station savedStation = stationRepository.save(station);

        // then
        assertThat(savedStation.getId()).isPositive();
    }

    @Test
    void 역을_삭제한다() {
        // given
        Station station = new Station("강남역");
        Station savedStation = stationRepository.save(station);

        // when
        stationRepository.deleteById(savedStation.getId());

        // then
        assertThat(stationRepository.findByName("강남역")).isEmpty();
    }
}
