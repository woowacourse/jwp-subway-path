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
import subway.domain.line.Station;
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
    void 역을_저장한다() {
        // given
        String stationName = "강남역";

        // when
        final Station saveStation = stationRepository.insert(stationName);
        final Optional<Station> findStation = stationRepository.findById(saveStation.getId());

        // then
        assertThat(findStation).isPresent();
        assertThat(saveStation).isEqualTo(findStation.get());
    }

    @Test
    void 이미_존재하는_역을_저장하면_저장되어_있던_역을_조회해서_반환한다() {
        // given
        final Station saveStation = stationRepository.insert("강남역");

        // when
        final Station saveExistsStation = stationRepository.insert("강남역");

        // then
        assertThat(saveExistsStation).isEqualTo(saveStation);
    }

    @Test
    void ID를_기준으로_역을_삭제한다() {
        // given
        final Station saveStation = stationRepository.insert("강남역");

        // when
        stationRepository.deleteById(saveStation.getId());
        final Optional<Station> findStation = stationRepository.findById(saveStation.getId());

        // then
        assertThat(findStation).isEmpty();
    }
}
