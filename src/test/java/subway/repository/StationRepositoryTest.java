package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;
import subway.repository.dao.StationDao;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository = new StationRepository(new StationDao(jdbcTemplate));
    }

    @Test
    void 이름으로_검색한_뒤_없으면_생성한다() {
        // given
        Optional<Station> beforeSaveStation = stationRepository.findByName("역삼역");

        //when
        Station station = stationRepository.findOrSaveStation("역삼역");

        //then
        Station findStation = stationRepository.findById(station.getId()).get();
        assertThat(beforeSaveStation).isEmpty();
        assertThat(station).usingRecursiveComparison()
                .isEqualTo(findStation);
    }

    @Test
    void 이름으로_검색한_뒤_있으면_그대로_반환한다() {
        // given
        Station station = stationRepository.save(new Station("역삼역"));

        // when
        Station findStation = stationRepository.findOrSaveStation("역삼역");

        //then
        assertThat(station).usingRecursiveComparison()
                .isEqualTo(findStation);
    }
}
