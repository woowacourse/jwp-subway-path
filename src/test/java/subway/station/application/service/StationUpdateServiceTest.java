package subway.station.application.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.adapter.station.out.FakeStationRepository;
import subway.station.application.port.in.StationInfoUpdateRequestDto;
import subway.station.domain.Station;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 수정하는 서비스 테스트")
class StationUpdateServiceTest {

    private FakeStationRepository fakeStationRepository;
    private StationUpdateService stationUpdateService;

    @BeforeEach
    void setUp() {
        fakeStationRepository = new FakeStationRepository();
        stationUpdateService = new StationUpdateService(fakeStationRepository);
    }

    @Test
    void 수정하면_저장소에서_수정한다() {
        // given
        final String name = "강남역";
        final String newName = "강남";
        Station station = fakeStationRepository.save(new Station(name));
        station.updateName(newName);

        // when
        stationUpdateService.updateStationInfo(new StationInfoUpdateRequestDto(station.getId(), newName));

        // then
        assertTrue(fakeStationRepository.findByName(newName).isPresent());
    }
}
