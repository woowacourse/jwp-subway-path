package subway.repository;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.Station;
import subway.integration.IntegrationTest;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.fixture.EntityFixture.후추_Entity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationRepositoryTest extends IntegrationTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    void 역을_저장한다() {
        //when
        final Station station = stationRepository.save(후추_Entity);

        //then
        assertSoftly(softly -> {
            softly.assertThat(station.getId()).isEqualTo(1L);
            softly.assertThat(station.getName()).isEqualTo("후추");
        });
    }

    @Test
    void id로_역을_찾는다() {
        //given
        final Long id = stationRepository.save(후추_Entity).getId();

        //when
        final Station station = stationRepository.findStationById(id);

        //then
        assertSoftly(softly -> {
            softly.assertThat(station.getId()).isEqualTo(1L);
            softly.assertThat(station.getName()).isEqualTo("후추");
        });
    }
}
