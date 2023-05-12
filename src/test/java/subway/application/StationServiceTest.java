package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.Fixture;
import subway.dao.SubwayMapRepository;
import subway.domain.Line;
import subway.domain.SubwayMap;
import subway.dto.StationRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private SubwayMapRepository subwayMapRepository;
    @InjectMocks
    private StationService stationService;

    @Mock
    private LineService lineService;

    @Test
    @DisplayName("CreateType이 INIT일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
    void saveStationINIT() {
        // given
        final StationRequest stationRequestUp = new StationRequest("잠실역", "INIT", "2호선", true, false, null, "선릉역", 1, 1);
        final StationRequest stationRequestDown = new StationRequest("선릉역", "INIT", "2호선", false, true, "잠실역", null, 1, 1);
        final Line line = new Line(1L, "2호선", "green");


        final SubwayMap subwayMap = new SubwayMap(new HashMap<>(Collections.emptyMap()), new HashMap<>(Fixture.tempLineMap));
        when(lineService.findLineByName(line.getName())).thenReturn(line);

        // when & then
        assertDoesNotThrow(() -> stationService.saveStation(List.of(stationRequestUp, stationRequestDown)));
    }

    @Test
    @DisplayName("CreateType이 UP일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
    void saveStationUP() {
        // given
        final StationRequest stationRequestDown = new StationRequest("잠실역", "UP", "2호선", true, false, null, "A", null, 1);
        final Line line = new Line(1L, "2호선", "green");

        when(subwayMapRepository.find()).thenReturn(Fixture.subwayMap);
        when(lineService.findLineByName(line.getName())).thenReturn(line);

        // when & then
        assertDoesNotThrow(() -> stationService.saveStation(List.of(stationRequestDown)));
        verify(subwayMapRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CreateType이 DOWN일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
    void saveStationDown() {
        // given
        final StationRequest stationRequestDown = new StationRequest("잠실역", "DOWN", "2호선", false, true, "C", null, 1, null);
        final Line line = new Line(1L, "2호선", "green");

        when(subwayMapRepository.find()).thenReturn(Fixture.subwayMap);
        when(lineService.findLineByName(line.getName())).thenReturn(line);

        // when & then
        assertDoesNotThrow(() -> stationService.saveStation(List.of(stationRequestDown)));
        verify(subwayMapRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CreateType이 MID일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
    void saveStationMid() {
        // given
        final StationRequest request = new StationRequest("잠실역", "MID", "2호선", false, false, "A", "B", 1, 1);
        final Line line = new Line(1L, "2호선", "green");
        when(subwayMapRepository.find()).thenReturn(Fixture.subwayMap);
        when(lineService.findLineByName(line.getName())).thenReturn(line);

        // when & then
        assertDoesNotThrow(() -> stationService.saveStation(List.of(request)));
        verify(subwayMapRepository, times(1)).save(any());
    }
}
