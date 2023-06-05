package subway.station.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.adapter.station.out.FakeStationRepository;
import subway.station.application.port.in.StationCreateRequestDto;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 생성하는 서비스 테스트")
class StationCreateServiceTest {

    private FakeStationRepository fakeStationRepository;
    private StationCreateService stationCreateService;

    @BeforeEach
    void setUp() {
        fakeStationRepository = new FakeStationRepository();
        stationCreateService = new StationCreateService(fakeStationRepository);
    }

    @Test
    void 생성하면_저장소에_저장한다() {
        // given
        final String name = "강남역";

        // when
        stationCreateService.create(new StationCreateRequestDto(name));

        // then
        assertThat(fakeStationRepository.findByName(name)).isPresent();
    }
}
