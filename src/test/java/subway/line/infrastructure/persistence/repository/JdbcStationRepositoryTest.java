package subway.line.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.common.RepositoryTest;
import subway.line.domain.Station;
import subway.line.domain.StationRepository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JdbcStationRepository 은(는)")
@RepositoryTest
class JdbcStationRepositoryTest {

    private final Station station = new Station("정의역");

    @Autowired
    private StationRepository stationRepository;

    @Test
    void 역을_저장한다() {
        // when
        stationRepository.save(station);

        // then
        assertThat(stationRepository.findByName("정의역")).isPresent();
    }

    @Test
    void 역을_조회한다() {
        // given
        stationRepository.save(station);

        // when & then
        assertThat(stationRepository.findByName("정의역")).isPresent();
    }

    @Test
    void 역이_없는_경우() {
        // when & then
        assertThat(stationRepository.findByName("정의역")).isEmpty();
    }
}
