package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.Fixture;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.SubwayMapRepository;
import subway.dao.entity.SectionEntity;
import subway.domain.Direction;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.CreateType;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private SubwayMapRepository subwayMapRepository;
    @InjectMocks
    private StationService stationService;

    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private LineService lineService;

    @Test
    @DisplayName("CreateType이 INIT일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
    void saveStationINIT() {
        // given
        final StationRequest stationRequestUp = new StationRequest("잠실역", CreateType.INIT, "2호선", null, "잠실나루역", null, 0);
        final StationRequest stationRequestDown = new StationRequest("잠실나루역", CreateType.INIT, "2호선", "잠실역", null, 0, null);
        final Station stationUp = new Station(stationRequestUp.getName());
        final Station stationDown = new Station(stationRequestDown.getName());
        final Station stationUpWithId = new Station(1L, stationRequestUp.getName());
        final Station stationDownWithId = new Station(2L, stationRequestDown.getName());
        final Line line = new Line("2호선", "green");

        when(lineService.findLineByName(line.getName())).thenReturn(line);
        when(stationDao.insert(stationUp)).thenReturn(stationUpWithId);
        when(stationDao.insert(stationDown)).thenReturn(stationDownWithId);

        // when
        final List<StationResponse> stationResponses = stationService.saveStation(List.of(stationRequestUp, stationRequestDown));

        // then
        assertThat(stationResponses.get(0).getId()).isEqualTo(stationUpWithId.getId());
        assertThat(stationResponses.get(1).getId()).isEqualTo(stationDownWithId.getId());
    }

    @Test
    @DisplayName("CreateType이 UP일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
    void saveStationUP() {
        // given
        final StationRequest stationRequestUp = new StationRequest("잠실역", CreateType.UP, "2호선", null, "잠실나루역", null, 1);
        final Station stationUp = new Station(stationRequestUp.getName());
        final Station stationUpWithId = new Station(3L, stationRequestUp.getName());
        final Line line = new Line(1L, "2호선", "green", 1L, 2L);
        final Station jamsilNaru = new Station(1L, "잠실나루역");

        when(stationDao.findById(1L)).thenReturn(jamsilNaru);
        when(lineService.findLineByName(line.getName())).thenReturn(line);
        when(stationDao.insert(stationUp)).thenReturn(stationUpWithId);

        // when
        final List<StationResponse> stationResponses = stationService.saveStation(List.of(stationRequestUp));

        // then
        assertThat(stationResponses.get(0).getId()).isEqualTo(stationUpWithId.getId());
    }

    @Test
    @DisplayName("CreateType이 DOWN일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
    void saveStationDown() {
        // given
        final StationRequest stationRequestDown = new StationRequest("잠실역", CreateType.DOWN, "2호선", "선릉역", null, 1, null);
        final Station stationDown = new Station(stationRequestDown.getName());
        final Station stationDownWithId = new Station(3L, stationRequestDown.getName());
        final Line line = new Line(1L, "2호선", "green", 1L, 2L);
        final Station sunlng = new Station(2L, "선릉역");

        when(stationDao.findById(2L)).thenReturn(sunlng);
        when(lineService.findLineByName(line.getName())).thenReturn(line);
        when(stationDao.insert(stationDown)).thenReturn(stationDownWithId);

        // when
        final List<StationResponse> stationResponses = stationService.saveStation(List.of(stationRequestDown));

        // then
        assertThat(stationResponses.get(0).getId()).isEqualTo(stationDownWithId.getId());
        verify(sectionDao, times(2)).insertSection(any(), anyLong());
    }

    @Test
    @DisplayName("CreateType이 MID일 때 두 개의 역을 저장하고 저장한 역의 ID를 반환한다")
    void saveStationMid() {
        // given
        final StationRequest request = new StationRequest("잠실역", CreateType.MID, "2호선", "A", "B", 1, 1);
        final Line line = new Line(1L, "2호선", "green", null, null);
        when(lineService.findLineByName(request.getLine())).thenReturn(line);
        when(subwayMapRepository.findByLineId(1L)).thenReturn(Fixture.subwayMap);
        when(stationDao.findByName(request.getPreviousStation())).thenReturn(Fixture.stationA);
        when(stationDao.findByName(request.getNextStation())).thenReturn(Fixture.stationB);

        final Station station = new Station(request.getName());
        final Station stationWithId = new Station(4L, request.getName());
        when(stationDao.insert(station)).thenReturn(stationWithId);

        final SectionEntity sectionEntityAB = new SectionEntity(1L, 1, 1L, 2L, 1L, Direction.DOWN);
        final SectionEntity sectionEntityBA = new SectionEntity(1L, 1, 2L, 1L, 1L, Direction.UP);

        when(sectionDao.findByStationIds(1L, 2L)).thenReturn(sectionEntityAB);
        when(sectionDao.findByStationIds(2L, 1L)).thenReturn(sectionEntityBA);
        doNothing().when(sectionDao).update(anyLong(), any());
        doNothing().when(sectionDao).insertSection(any(), anyLong());

        // when & then
        assertDoesNotThrow(() -> stationService.saveStation(List.of(request)));
        verify(sectionDao, times(2)).insertSection(any(), anyLong());
        verify(sectionDao, times(2)).update(anyLong(), any());
    }
}
