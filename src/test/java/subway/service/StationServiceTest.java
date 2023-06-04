package subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.station.StationRequest;
import subway.dto.station.StationResponse;
import subway.service.station.StationService;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
public class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Test
    void 역_정보를_저장한다() {
        stationService.createStation(new StationRequest("잠실역"));

        final List<StationResponse> stationResponses = stationService.findAll().getStations();
        assertThat(stationResponses.size()).isEqualTo(1);
    }

    @Test
    void 역_ID로_역_정보를_조회한다() {
        final Long id = stationService.createStation(new StationRequest("잠실역"));

        final StationResponse stationResponse = stationService.findById(id);
        assertThat(stationResponse.getName()).isEqualTo("잠실역");
    }

    @Test
    void 모든_역_정보들을_조회한다() {
        stationService.createStation(new StationRequest("잠실역"));
        stationService.createStation(new StationRequest("아현역"));

        final List<StationResponse> stationResponses = stationService.findAll().getStations();
        assertThat(stationResponses.size()).isEqualTo(2);
    }

    @Test
    void 역_ID로_역_정보를_삭제한다() {
        final Long id = stationService.createStation(new StationRequest("잠실역"));

        stationService.removeStation(id);

        final List<StationResponse> stationResponses = stationService.findAll().getStations();
        assertThat(stationResponses.isEmpty()).isTrue();
    }
}
