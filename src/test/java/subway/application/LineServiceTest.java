package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.dto.LineCreationDto;
import subway.application.dto.StationAdditionToLineDto;
import subway.application.dto.StationDeletionFromLineDto;
import subway.domain.*;
import subway.exception.NameLengthException;
import subway.repository.LineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        final Station newStation = new Station("서울대입구");
        final int distanceToUpstream = 3;
        final StationAdditionToLineDto stationAdditionToLineDto = new StationAdditionToLineDto(1, newStation.getName(), JAMSIL_STATION.getName(), JAMSIL_NARU_STATION.getName(), distanceToUpstream);
        final Line line = new Line(LINE_NUMBER_TWO);

        doReturn(Optional.of(line)).when(lineRepository).findLineById(1L);
        doReturn(1L).when(stationService).createStationIfNotExist(newStation.getName());
        doReturn(JAMSIL_STATION).when(stationService).findStationByName(stationAdditionToLineDto.getUpstreamName());
        doReturn(JAMSIL_NARU_STATION).when(stationService)
                                     .findStationByName(stationAdditionToLineDto.getDownstreamName());

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
        final Station newStation = new Station("서울대입구");
        final int distanceToUpstream = 3;
        final StationAdditionToLineDto stationAdditionToLineDto = new StationAdditionToLineDto(1, newStation.getName(), DummyTerminalStation.STATION_NAME, SULLEUNG_STATION.getName(), distanceToUpstream);
        final Line line = new Line(LINE_NUMBER_TWO);

        doReturn(Optional.of(line)).when(lineRepository).findLineById(1L);
        doReturn(1L).when(stationService).createStationIfNotExist(newStation.getName());
        doReturn(SULLEUNG_STATION).when(stationService).findStationByName(stationAdditionToLineDto.getDownstreamName());

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
        final Station newStation = new Station("서울대입구");
        final int distanceToUpstream = 3;
        final StationAdditionToLineDto stationAdditionToLineDto = new StationAdditionToLineDto(1, newStation.getName(), JAMSIL_NARU_STATION.getName(), DummyTerminalStation.STATION_NAME, distanceToUpstream);
        final Line line = new Line(LINE_NUMBER_TWO);

        doReturn(Optional.of(line)).when(lineRepository).findLineById(1L);
        doReturn(1L).when(stationService).createStationIfNotExist(newStation.getName());
        doReturn(JAMSIL_NARU_STATION).when(stationService)
                                     .findStationByName(stationAdditionToLineDto.getUpstreamName());

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
        final Station upstream = new Station("서울대입구");
        final Station downstream = new Station("잠실나루");
        final LineCreationDto lineCreationDto = new LineCreationDto("2호선", upstream.getName(), downstream.getName(), DISTANCE);

        doReturn(1L).when(stationService).createStationIfNotExist(upstream.getName());
        doReturn(2L).when(stationService).createStationIfNotExist(downstream.getName());
        doReturn(new Lines(new ArrayList<>())).when(lineRepository).findAllLines();

        final LineService lineService = new LineService(lineRepository, stationService);

        lineService.createLine(lineCreationDto);

        final Line expectedLine = new Line(lineCreationDto.getLineName(), List.of(new MiddleSection(upstream, downstream, DISTANCE)));

        verify(lineRepository, times(1)).createLine(expectedLine);
    }

    @Test
    @DisplayName("상하행역이 빈문자열이면 예외를 던진다")
    void createLineFail1() {
        final Station downstream = new Station("잠실나루");
        final LineCreationDto lineCreationDto = new LineCreationDto("2호선", "", downstream.getName(), DISTANCE);

        final LineService lineService = new LineService(lineRepository, stationService);

        assertThatThrownBy(() -> lineService.createLine(lineCreationDto))
                .isInstanceOf(NameLengthException.class);
    }

    @Test
    @DisplayName("상하행역이 빈문자열이면 예외를 던진다")
    void createLineFail2() {
        final Station upstream = new Station("잠실나루");
        final LineCreationDto lineCreationDto = new LineCreationDto("2호선", upstream.getName(), "", DISTANCE);

        final LineService lineService = new LineService(lineRepository, stationService);

        assertThatThrownBy(() -> lineService.createLine(lineCreationDto))
                .isInstanceOf(NameLengthException.class);
    }
}
