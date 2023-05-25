package subway.station.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.LineService;
import subway.line.application.dto.LineCreationDto;
import subway.line.application.dto.StationAdditionToLineDto;
import subway.line.application.dto.StationDeletionFromLineDto;
import subway.line.domain.Line;
import subway.line.domain.Lines;
import subway.line.domain.MiddleSection;
import subway.line.repository.LineRepository;
import subway.station.domain.DummyTerminalStation;
import subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.SectionFixture.DISTANCE;
import static subway.utils.StationFixture.*;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    /**
     * LINE_NUMBER_TWO
     * 선릉 --(거리: 5)--> 잠실 --(거리: 5)--> 잠실나루
     */
    @Test
    @DisplayName("노선에 새로운 역을 추가할 수 있다")
    void addStationToLine1() {
        final Station newStation = new Station(10L, "서울대입구");
        final int distanceToUpstream = 3;
        final Line line = new Line(LINE_NUMBER_TWO);
        final StationAdditionToLineDto stationAdditionToLineDto = new StationAdditionToLineDto(1L, newStation.getName(), 1L, 2L, distanceToUpstream);

        doReturn(Optional.of(line)).when(lineRepository).findLineById(line.getId());
        doReturn(newStation).when(stationService).createStationIfNotExist(newStation.getName());
        doReturn(JAMSIL_STATION).when(stationService).findStationById(stationAdditionToLineDto.getUpstreamId());
        doReturn(JAMSIL_NARU_STATION).when(stationService).findStationById(stationAdditionToLineDto.getDownstreamId());

        final LineService lineService = new LineService(lineRepository, stationService);

        lineService.addStationToLine(stationAdditionToLineDto);

        assertThat(line.getSections()).containsExactly(
                new MiddleSection(SULLEUNG_STATION, JAMSIL_STATION, DISTANCE),
                new MiddleSection(JAMSIL_STATION, newStation, distanceToUpstream),
                new MiddleSection(newStation, JAMSIL_NARU_STATION, DISTANCE - distanceToUpstream)
        );
    }

    @Test
    @DisplayName("노선 상행 종점에 새로운 역을 추가할 수 있다")
    void addStationToLine2() {
        final Station newStation = new Station(10L, "서울대입구");
        final int distanceToUpstream = 3;
        final Line line = new Line(LINE_NUMBER_TWO);
        final StationAdditionToLineDto stationAdditionToLineDto = new StationAdditionToLineDto(line.getId(), newStation.getName(), DummyTerminalStation.STATION_ID, SULLEUNG_STATION.getId(), distanceToUpstream);

        doReturn(Optional.of(line)).when(lineRepository).findLineById(line.getId());
        doReturn(newStation).when(stationService).createStationIfNotExist(newStation.getName());
        doReturn(SULLEUNG_STATION).when(stationService).findStationById(stationAdditionToLineDto.getDownstreamId());

        final LineService lineService = new LineService(lineRepository, stationService);

        lineService.addStationToLine(stationAdditionToLineDto);

        assertThat(line.getSections()).containsExactly(
                new MiddleSection(newStation, SULLEUNG_STATION, distanceToUpstream),
                new MiddleSection(SULLEUNG_STATION, JAMSIL_STATION, DISTANCE),
                new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, DISTANCE)
        );
    }

    @Test
    @DisplayName("노선 하행 종점에 새로운 역을 추가할 수 있다")
    void addStationToLine3() {
        final Station newStation = new Station(10L, "서울대입구");
        final int distanceToUpstream = 3;
        final Line line = new Line(LINE_NUMBER_TWO);
        final StationAdditionToLineDto stationAdditionToLineDto = new StationAdditionToLineDto(line.getId(), newStation.getName(), JAMSIL_NARU_STATION.getId(), DummyTerminalStation.STATION_ID, distanceToUpstream);

        doReturn(Optional.of(line)).when(lineRepository).findLineById(line.getId());
        doReturn(newStation).when(stationService).createStationIfNotExist(newStation.getName());
        doReturn(JAMSIL_NARU_STATION).when(stationService).findStationById(stationAdditionToLineDto.getUpstreamId());

        final LineService lineService = new LineService(lineRepository, stationService);

        lineService.addStationToLine(stationAdditionToLineDto);

        assertThat(line.getSections()).containsExactly(
                new MiddleSection(SULLEUNG_STATION, JAMSIL_STATION, DISTANCE),
                new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, DISTANCE),
                new MiddleSection(JAMSIL_NARU_STATION, newStation, distanceToUpstream)
        );
    }

    @Test
    @DisplayName("노선에서 상행 종점을 제거할 수 있다")
    void removeStationFromLine1() {
        final StationDeletionFromLineDto stationDeletionFromLineDto = new StationDeletionFromLineDto(1L, 1L);
        final Line line = new Line(LINE_NUMBER_TWO);

        doReturn(Optional.of(line)).when(lineRepository).findLineById(1L);
        doReturn(SULLEUNG_STATION).when(stationService).findStationById(1L);

        final LineService lineService = new LineService(lineRepository, stationService);

        lineService.deleteStationFromLine(stationDeletionFromLineDto);

        assertThat(line.getSections()).contains(
                new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, DISTANCE)
        );
    }

    @Test
    @DisplayName("노선에서 중간 역을 제거할 수 있다")
    void removeStationFromLine2() {
        final StationDeletionFromLineDto stationDeletionFromLineDto = new StationDeletionFromLineDto(1L, 1L);
        final Line line = new Line(LINE_NUMBER_TWO);

        doReturn(Optional.of(line)).when(lineRepository).findLineById(1L);
        doReturn(JAMSIL_STATION).when(stationService).findStationById(1L);

        final LineService lineService = new LineService(lineRepository, stationService);

        lineService.deleteStationFromLine(stationDeletionFromLineDto);

        assertThat(line.getSections()).contains(
                new MiddleSection(SULLEUNG_STATION, JAMSIL_NARU_STATION, DISTANCE + DISTANCE)
        );
    }

    @Test
    @DisplayName("노선에서 하행 종점을 제거할 수 있다")
    void removeStationFromLine3() {
        final StationDeletionFromLineDto stationDeletionFromLineDto = new StationDeletionFromLineDto(1L, 1L);
        final Line line = new Line(LINE_NUMBER_TWO);

        doReturn(Optional.of(line)).when(lineRepository).findLineById(1L);
        doReturn(JAMSIL_NARU_STATION).when(stationService).findStationById(1L);

        final LineService lineService = new LineService(lineRepository, stationService);

        lineService.deleteStationFromLine(stationDeletionFromLineDto);

        assertThat(line.getSections()).contains(
                new MiddleSection(SULLEUNG_STATION, JAMSIL_STATION, DISTANCE)
        );
    }

    @Test
    @DisplayName("새로운 노선을 생성할 수 있다")
    void createLine1() {
        final Station upstream = new Station(1L, "서울대입구");
        final Station downstream = new Station(2L, "잠실나루");
        final LineCreationDto lineCreationDto = new LineCreationDto("2호선", upstream.getName(), downstream.getName(), DISTANCE, 0);

        doReturn(upstream).when(stationService).createStationIfNotExist(upstream.getName());
        doReturn(downstream).when(stationService).createStationIfNotExist(downstream.getName());
        doReturn(new Lines(new ArrayList<>())).when(lineRepository).findAllLines();

        final LineService lineService = new LineService(lineRepository, stationService);

        lineService.createLine(lineCreationDto);

        final Line expectedLine = new Line(lineCreationDto.getLineName(), 0, List.of(new MiddleSection(upstream, downstream, DISTANCE)));

        verify(lineRepository, times(1)).createLine(expectedLine);
    }
}
