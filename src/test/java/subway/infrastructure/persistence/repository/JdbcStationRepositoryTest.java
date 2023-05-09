package subway.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.common.RepositoryTest;
import subway.domain.Station;
import subway.domain.StationRepository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JdbcStationRepository 은(는)")
@RepositoryTest
class JdbcStationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    private final Station station = new Station("정의역");

    @Test
    void 역을_저장한다() {
        // when
        final Long id = stationRepository.save(station);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    void 역을_조회한다() {
        // given
        final Long id = stationRepository.save(station);

        // when & then
        assertThat(stationRepository.findById(id)).isPresent();
    }

    @Test
    void 역이_없는_경우() {
        // when & then
        assertThat(stationRepository.findById(1L)).isEmpty();
    }
}
