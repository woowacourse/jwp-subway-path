package subway.application.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.application.core.service.dto.in.IdCommand;
import subway.application.core.service.dto.in.SaveStationCommand;
import subway.application.core.service.dto.in.UpdateStationCommand;
import subway.application.core.service.dto.out.StationResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Test
    @DisplayName("역을 저장할 수 있다")
    void saveStation() {
        // given
        SaveStationCommand saveCommand = new SaveStationCommand("잠실역");

        // when
        StationResult result = stationService.saveStation(saveCommand);
        StationResult expected = new StationResult(null, "잠실역");

        // then
        assertThat(result).usingRecursiveComparison()
                .ignoringExpectedNullFields().isEqualTo(expected);
    }

    @Test
    @DisplayName("ID를 통해 역을 찾을 수 있다")
    void findStationResponseById() {
        // given
        SaveStationCommand saveCommand = new SaveStationCommand("잠실역");
        StationResult saved = stationService.saveStation(saveCommand);

        // when
        IdCommand idCommand = new IdCommand(saved.getId());
        StationResult findResult = stationService.findStationById(idCommand);

        // then
        assertThat(findResult).usingRecursiveComparison()
                .ignoringExpectedNullFields().isEqualTo(saved);
    }

    @Test
    @DisplayName("모든 역을 찾을 수 있다")
    void findAllStationResponses() {
        // given
        SaveStationCommand saveCommand = new SaveStationCommand("잠실역");
        StationResult saved = stationService.saveStation(saveCommand);

        // when
        List<StationResult> findResults = stationService.findAllStations();

        // then
        assertThat(findResults.get(0)).usingRecursiveComparison()
                .ignoringExpectedNullFields().isEqualTo(saved);
    }

    @Test
    @DisplayName("역을 업데이트 할 수 있다")
    void updateStation() {
        // given
        SaveStationCommand saveCommand = new SaveStationCommand("잠실역");
        StationResult saved = stationService.saveStation(saveCommand);

        // when
        UpdateStationCommand updateCommand = new UpdateStationCommand(saved.getId(), "방배역");
        stationService.updateStation(updateCommand);

        StationResult actual = stationService.findStationById(new IdCommand(saved.getId()));
        StationResult expected = new StationResult(saved.getId(), "방배역");

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringExpectedNullFields().isEqualTo(expected);
    }

    @Test
    @DisplayName("역을 삭제할 수 있다")
    void deleteStationById() {
        // given
        SaveStationCommand saveCommand = new SaveStationCommand("잠실역");
        StationResult saved = stationService.saveStation(saveCommand);

        // when
        IdCommand idCommand = new IdCommand(saved.getId());
        stationService.deleteStationById(idCommand);

        List<StationResult> actual = stationService.findAllStations();

        // then
        assertThat(actual).isEmpty();
    }
}
