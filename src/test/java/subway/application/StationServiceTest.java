package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import subway.Fixture;
import subway.dao.SubwayMapRepository;
import subway.dto.request.StationRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private SubwayMapRepository subwayMapRepository;
    @InjectMocks
    private StationService stationService;


    @Test
    @DisplayName("새로운 역을 추가한다")
    void saveStation() {
        // given
        final StationRequest stationRequest = new StationRequest("잠실역");

        // when
        when(subwayMapRepository.find()).thenReturn(Fixture.subwayMap);

        // then
        assertDoesNotThrow(() -> stationService.saveStation(stationRequest));
    }

//    @Test
//    @DisplayName("CreateType이 UP일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
//    void saveStationUP() {
//        // given
//        final LineStationRequest stationRequestTmpDown = new LineStationRequest("잠실역", "UP", "2호선", true, false, null, "A", null, 1);
//        final Line line = new Line(1L, "2호선", "green");
//
//        when(subwayMapRepository.find()).thenReturn(Fixture.subwayMap);
//        when(lineService.findLineByName(line.getName())).thenReturn(line);
//
//        // when & then
//        //assertDoesNotThrow(() -> stationService.saveStation(List.of(stationRequestTmpDown)));
//        verify(subwayMapRepository, times(1)).save(any());
//    }
//
//    @Test
//    @DisplayName("CreateType이 DOWN일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
//    void saveStationDown() {
//        // given
//        final LineStationRequest stationRequestTmpDown = new LineStationRequest("잠실역", "DOWN", "2호선", false, true, "C", null, 1, null);
//        final Line line = new Line(1L, "2호선", "green");
//
//        when(subwayMapRepository.find()).thenReturn(Fixture.subwayMap);
//        when(lineService.findLineByName(line.getName())).thenReturn(line);
//
//        // when & then
//        //assertDoesNotThrow(() -> stationService.saveStation(List.of(stationRequestTmpDown)));
//        verify(subwayMapRepository, times(1)).save(any());
//    }
//
//    @Test
//    @DisplayName("CreateType이 MID일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
//    void saveStationMid() {
//        // given
//        final LineStationRequest request = new LineStationRequest("잠실역", "MID", "2호선", false, false, "A", "B", 1, 1);
//        final Line line = new Line(1L, "2호선", "green");
//        when(subwayMapRepository.find()).thenReturn(Fixture.subwayMap);
//        when(lineService.findLineByName(line.getName())).thenReturn(line);
//
//        // when & then
//        assertDoesNotThrow(() -> stationService.saveStation(List.of(request)));
//        verify(subwayMapRepository, times(1)).save(any());
//    }
}
