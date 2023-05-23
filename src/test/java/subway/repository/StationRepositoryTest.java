package subway.repository;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.StationDao;
import subway.domain.Station;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class StationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM station");
    }


    @Test
    void 저장된_역들을_가져올_수_있다() {
        // given
        stationRepository.registerStation(new Station("잠실역"));
        stationRepository.registerStation(new Station("석촌역"));
        stationRepository.registerStation(new Station("송파역"));

        // when
        final List<Station> stations = stationRepository.findStations();

        // then
        assertThat(stations).hasSize(3);
    }

}