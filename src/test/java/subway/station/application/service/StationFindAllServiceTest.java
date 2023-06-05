package subway.station.application.service;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.domain.station.StationFixture.코다_역_id_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.adapter.station.out.FakeStationRepository;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 조회하는 서비스 테스트")
class StationFindAllServiceTest {

    private FakeStationRepository fakeStationRepository;
    private StationFindAllService stationFindAllService;

    @BeforeEach
    void setUp() {
        fakeStationRepository = new FakeStationRepository();
        stationFindAllService = new StationFindAllService(fakeStationRepository);
    }

    @Test
    void 조회하면_저장소에서_모두_조회한다() {
        // given
        fakeStationRepository.save(코다_역_id_1);

        // when
        stationFindAllService.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(fakeStationRepository.findAll()).hasSize(1);
            softly.assertThat(fakeStationRepository.findAll()).contains(코다_역_id_1);
        });
    }
}
