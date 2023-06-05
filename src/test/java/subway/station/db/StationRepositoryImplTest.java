package subway.station.db;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.station.domain.StationFixture.코다_역_id_1;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.station.domain.Station;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("StationRepository 는")
@JdbcTest
class StationRepositoryImplTest {

    private final StationRepositoryImpl stationRepository;

    @Autowired
    private StationRepositoryImplTest(JdbcTemplate jdbcTemplate) {
        stationRepository = new StationRepositoryImpl(jdbcTemplate);
    }

    @Test
    void 저장을_할_수_있다() {
        // given
        final String name = "강남역";

        // when
        Station result = stationRepository.save(new Station(name));

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getName().getValue()).isEqualTo(name);
            softly.assertThat(result.getId()).isPositive();
        });
    }

    @Nested
    @DisplayName("저장된 결과를")
    class Context_SavedResult {

        private Station station;

        @BeforeEach
        void setUp() {
            station = stationRepository.save(코다_역_id_1);
        }

        @Test
        void 삭제할_수_있다() {
            // given
            // when
            stationRepository.deleteById(station.getId());
            Optional<Station> result = stationRepository.findById(station.getId());

            // then
            assertSoftly(softly -> softly.assertThat(result).isEmpty());
        }

        @Test
        void 수정할_수_있다() {
            // given
            final String updatedName = "역삼역";
            station.updateName(updatedName);

            // when
            stationRepository.update(station);
            Optional<Station> result = stationRepository.findById(station.getId());

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).isPresent();
                softly.assertThat(result.get().getName().getValue()).isEqualTo(updatedName);
            });
        }

        @Test
        void 조회할_수_있다() {
            // given
            // when
            Optional<Station> result = stationRepository.findById(station.getId());

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).isPresent();
                softly.assertThat(result.get()).isEqualTo(station);
            });
        }

        @Test
        void 이름으로_조회할_수_있다() {
            // given
            // when
            Optional<Station> result = stationRepository.findByName(station.getName().getValue());

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).isPresent();
                softly.assertThat(result.get()).isEqualTo(station);
            });
        }

        @Test
        void 전체_조회할_수_있다() {
            // given
            // when
            Iterable<Station> result = stationRepository.findAll();

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).hasSize(1);
                softly.assertThat(result).contains(station);
            });
        }
    }
}
