package subway.repository;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.station.Station;
import subway.integration.IntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.후추;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationRepositoryTest extends IntegrationTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    void 역을_저장한다() {
        //when
        final Station station = stationRepository.save(후추);

        //then
        assertSoftly(softly -> {
            softly.assertThat(station.getId()).isEqualTo(1L);
            softly.assertThat(station.getName()).isEqualTo("후추");
        });
    }

    @Test
    void id로_역을_찾는다() {
        //given
        final Long id = stationRepository.save(후추).getId();

        //when
        final Station station = stationRepository.findStationById(id);

        //then
        assertSoftly(softly -> {
            softly.assertThat(station.getId()).isEqualTo(1L);
            softly.assertThat(station.getName()).isEqualTo("후추");
        });
    }

    @ParameterizedTest
    @CsvSource({"후추, true", "디노, false"})
    void 포함_여부를_반환한다(final String name, final boolean expected) {
        //given
        stationRepository.save(후추);

        //when
        final boolean actual = stationRepository.contains(new Station(name));

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
