package subway.persistence.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
class StationRepositoryTest extends RepositoryTest {

    @Test
    void 역을_저장한다() {
        // given
        final Station station = Station.from("잠실역");

        // when
        final Station actual = stationRepository.insert(station);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("잠실역");
        });
    }

    @Test
    void 역_하나를_조회한다() {
        // given
        final Station data = Station.from("잠실역");
        final Station expected = stationRepository.insert(data);

        // when
        final Station actual = stationRepository.findById(expected.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 역_하나를_삭제한다() {
        final Station data = Station.from("잠실역");
        final Station station = stationRepository.insert(data);

        assertDoesNotThrow(() -> stationRepository.deleteById(station.getId()));
    }
}
