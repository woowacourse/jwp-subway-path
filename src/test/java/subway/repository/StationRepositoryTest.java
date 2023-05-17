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
import subway.entity.StationEntity;
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
        Optional<StationEntity> beforeSaveStation = stationRepository.findByName("역삼역");

        //when
        StationEntity station = stationRepository.findOrSaveStation("역삼역");

        //then
        Station findStation = stationRepository.findById(station.getId());
        assertThat(beforeSaveStation).isEmpty();
        assertThat(station.getId()).isEqualTo(findStation.getId());
    }

    @Test
    void 이름으로_검색한_뒤_있으면_그대로_반환한다() {
        // given
        StationEntity station = stationRepository.save(new StationEntity("역삼역"));

        // when
        StationEntity findStation = stationRepository.findOrSaveStation("역삼역");

        //then
        assertThat(station).usingRecursiveComparison()
                .isEqualTo(findStation);
    }
}
