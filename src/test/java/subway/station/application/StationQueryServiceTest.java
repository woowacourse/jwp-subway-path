package subway.station.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.station.domain.StationFixture.코다_역_id_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.station.db.FakeStationRepository;
import subway.station.dto.response.StationInfoResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 조회하는 서비스 테스트")
class StationQueryServiceTest {

    private FakeStationRepository fakeStationRepository;
    private StationQueryService stationQueryService;

    @BeforeEach
    void setUp() {
        fakeStationRepository = new FakeStationRepository();
        stationQueryService = new StationQueryService(fakeStationRepository);
    }

    @Test
    void 조회하면_저장소에서_모두_조회한다() {
        // given
        fakeStationRepository.save(코다_역_id_1);

        // when
        stationQueryService.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(fakeStationRepository.findAll()).hasSize(1);
            softly.assertThat(fakeStationRepository.findAll()).contains(코다_역_id_1);
        });
    }

    @Test
    void 조회하면_저장소에서_조회한다() {
        // given
        fakeStationRepository.save(코다_역_id_1);

        // when
        StationInfoResponse result = stationQueryService.findStationInfoById(코다_역_id_1.getId());

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(new StationInfoResponse(코다_역_id_1.getId(), 코다_역_id_1.getName().getValue()));
    }
}
