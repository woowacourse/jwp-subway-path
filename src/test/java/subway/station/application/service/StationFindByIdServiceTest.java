package subway.station.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.station.StationFixture.코다_역_id_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.adapter.station.out.FakeStationRepository;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.station.ui.dto.in.StationInfoResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 조회하는 서비스 테스트")
class StationFindByIdServiceTest {

    private FakeStationRepository fakeStationRepository;
    private StationFindByIdService stationFindByIdService;

    @BeforeEach
    void setUp() {
        fakeStationRepository = new FakeStationRepository();
        stationFindByIdService = new StationFindByIdService(fakeStationRepository);
    }

    @Test
    void 조회하면_저장소에서_조회한다() {
        // given
        fakeStationRepository.save(코다_역_id_1);

        // when
        final StationInfoResponseDto result = stationFindByIdService.findStationInfoById(코다_역_id_1.getId());

        // then
        assertThat(result).usingRecursiveComparison()
            .isEqualTo(new StationInfoResponse(코다_역_id_1.getId(), 코다_역_id_1.getName().getValue()));
    }
}
