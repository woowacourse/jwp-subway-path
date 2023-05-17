package subway.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;
import subway.business.domain.Direction;
import subway.business.domain.Line;
import subway.business.domain.LineRepository;
import subway.business.domain.StationRepository;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.LineStationsResponse;
import subway.business.service.dto.StationAddToLineRequest;
import subway.business.service.dto.StationDeleteRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static subway.fixtures.station.StationFixture.*;

@Sql("classpath:station_data.sql")
@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @DisplayName("노선을 생성한다.")
    @Test
    void shouldCreateLineWhenRequest() {
        when(lineRepository.create(any())).thenReturn(1L);
        when(stationRepository.findById(1L)).thenReturn(강남역);
        when(stationRepository.findById(3L)).thenReturn(잠실역);
        LineSaveRequest lineSaveRequest = new LineSaveRequest(
                "2호선",
                1L,
                3L,
                10
        );

        long id = lineService.createLine(lineSaveRequest).getId();
        assertThat(id).isEqualTo(1L);
    }

    @DisplayName("노선에 역을 추가한다.")
    @Test
    void shouldAddStationToLineWhenRequest() {
        Line line = Line.of("2호선", 강남역, 잠실역, 10);
        when(lineRepository.findById(1L)).thenReturn(line);
        when(stationRepository.findById(2L)).thenReturn(역삼역);
        when(stationRepository.findById(3L)).thenReturn(잠실역);
        doNothing().when(lineRepository).update(any());
        StationAddToLineRequest stationAddToLineRequest = new StationAddToLineRequest(
                2L,
                3L,
                "상행",
                5
        );

        lineService.addStationToLine(1L, stationAddToLineRequest);
        assertThat(line.getSections()).hasSize(2);
    }

    @DisplayName("노선에서 역을 삭제한다.")
    @Test
    void shouldDeleteStationLineWhenRequest() {
        Line line = Line.of("2호선", 강남역, 잠실역, 10);
        line.addStation(역삼역, 잠실역, Direction.UPWARD, 5);
        // 현재 노선 상태
        // 강남역(상행) - 역삼역 - 잠실역(하행)

        when(lineRepository.findById(1L)).thenReturn(line);
        doNothing().when(lineRepository).update(any());
        when(stationRepository.findById(3L)).thenReturn(잠실역);

        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(3L);
        lineService.deleteStation(1L, stationDeleteRequest);
        assertThat(line.getSections()).hasSize(1);
    }

    @DisplayName("모든 노선의 역 정보를 가져온다.")
    @Test
    void shouldReturnAllLineNameAndAllStationsOfLineWhenRequest() {
        Line line1 = Line.of("2호선", 강남역, 잠실역, 10);
        Line line2 = Line.of("3호선", 역삼역, 잠실역, 10);
        when(lineRepository.findAll()).thenReturn(List.of(line1, line2));

        List<LineStationsResponse> lineStationsResponses = lineService.findLineResponses();

        assertAll(
                () -> assertThat(lineStationsResponses.get(0).getName()).isEqualTo("2호선"),
                () -> assertThat(lineStationsResponses.get(1).getName()).isEqualTo("3호선")
        );
    }

    @DisplayName("노선의 ID를 통해 노선의 역 정보를 가져온다.")
    @Test
    void shouldReturnLineNameAndAllStationsOfLineWhenRequest() {
        Line line = Line.of("2호선", 강남역, 잠실역, 10);
        when(lineRepository.findById(1L)).thenReturn(line);

        LineStationsResponse lineStationResponse = lineService.findLineResponseById(1L);

        assertAll(
                () -> assertThat(lineStationResponse.getName()).isEqualTo("2호선"),
                () -> assertThat(lineStationResponse.getStations()).contains("강남역", "잠실역")
        );
    }
}
