package subway.persistence.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixtures.domain.StationFixture.JAMSIL;

@SuppressWarnings("NonAsciiCharacters")
class StationRepositoryTest extends RepositoryTest {

    @Test
    void 역을_저장한다() {

        // given, when
        final Station actual = stationRepository.insert(JAMSIL);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("잠실역");
        });
    }

    @Test
    void 모든_역을_조회한다() {
        // given
        final Station expected = stationRepository.insert(JAMSIL);

        // when
        final List<Station> actual = stationRepository.findAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0)).isEqualTo(expected);
        });
    }

    @Test
    void 역_하나를_조회한다() {
        // given
        final Station expected = stationRepository.insert(JAMSIL);

        // when
        final Station actual = stationRepository.findById(expected.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 역_하나를_삭제한다() {
        final Station station = stationRepository.insert(JAMSIL);

        assertDoesNotThrow(() -> stationRepository.deleteById(station.getId()));
    }
}
